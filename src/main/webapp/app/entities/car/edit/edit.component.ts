import { Component, ElementRef, Injectable, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatChipInputEvent } from '@angular/material/chips';
import { FormBuilder, NgForm } from '@angular/forms';
import { base64ToFile, ImageCroppedEvent } from 'ngx-image-cropper';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { Title } from '@angular/platform-browser';
import { CarService } from '../service/car.service';
import { ICar } from '../car.model';
import { Option } from '../add/option';
import { Brand } from '../../enumerations/brand.model';
import { BodyType } from '../../enumerations/body-type.model';
import { GearBoxType } from '../../enumerations/gear-box-type.model';
import { OptionService } from '../../option/service/option.service';
import { CarComponent } from '../list/car.component';
import { CarDetailComponent } from '../detail/car-detail.component';
import { PriceService } from '../service/price.servic';
import { CarStorageService } from '../service/storage.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css'],
})
export class EditComponent implements OnInit {
  public bodyTypes: string[] = ['Sedan', 'Universal', 'Hatchback', 'Minivan', 'SUV', 'Coupe', 'Convertible', 'Pickup', 'Limousine'];

  static editCar: ICar;
  // @ts-ignore
  private mainTitle: string;
  imageLoadFail = false;
  public gearboxTypeDef: string;
  brandValues = Object.keys(Brand);
  bodyTypeValues = Object.keys(BodyType);
  gearBoxTypeValues = Object.keys(GearBoxType);
  optionsFromCar: String[] | null = null;
  static options: Option[] = [];
  // @ts-ignore
  favoriteSeason: string;
  seasons: string[] = ['Winter', 'Spring', 'Summer', 'Autumn'];
  constructor(
    private fb: FormBuilder,
    private optionService: OptionService,
    private carComponent: CarComponent,
    private carDetailComponent: CarDetailComponent,
    private priceService: PriceService,
    private carService: CarService,
    /*private toastr: ToastrService,*/ private title: Title,
    public carStorageService: CarStorageService
  ) {
    this.gearboxTypeDef = this.gearBoxTypeValues[1];
  }
  // @ts-ignore
  @ViewChild('closeUpdateModal') closeUpdateModal: ElementRef;

  ngOnInit(): void {
    console.log("we are in edit")
    EditComponent.options = [];
    this.mainTitle = this.title.getTitle();
    this.priceService.find(EditComponent.editCar.id).subscribe(res => (EditComponent.editCar.carPrice = res.body?.carPrice));
    this.carStorageService.find(EditComponent.editCar.id).subscribe(res => (EditComponent.editCar.carNumber = res.body?.carNumber));
  }

  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;

  private makeCorrectName(name: string) {
    if (name === 'BMW' || name === 'BYD' || name === 'SUV' || name === null) return name;
    else name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    return name;
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    // Add our option
    if (value) {
      EditComponent.options.push({ name: value });
    }
    // Clear the input value
    event.chipInput!.clear();
  }

  remove(fruit: Option): void {
    const index = EditComponent.options.indexOf(fruit);

    if (index >= 0) {
      EditComponent.options.splice(index, 1);
    }
  }

  public async onUpdateCar(car: ICar): Promise<void> {
    car.id = EditComponent.editCar.id;
    car.carImageUrl = EditComponent.editCar.carImageUrl;
    let options: string[] = [];
    let imageFile: File | undefined = undefined;

    EditComponent.options.forEach(value => {
      options.push(value.name);
    });

    if (this.isFileLoad) {
      const imageName = car.id + '.png';
      imageFile = this.blobToFile(base64ToFile(this.cropImgPreview as string), imageName);
    }
    this.carService.update(car).subscribe(
      (carNew: HttpResponse<ICar>) => {
        /* this.toastr.success("Success updating!", "Success", {
          timeOut: 1200,
          progressBar: true,
          progressAnimation: "increasing"
        });*/
        this.isFileLoad = false;

        this.optionService.saveAll(options, carNew.body).subscribe(
          () => {
            console.log('option added');
          },
          () => {
            console.log('option not added');
          }
        );
        if (carNew.body != null) {
          carNew.body.carPrice = car.carPrice;
          this.priceService.update(carNew.body).subscribe(res => {
            if (carNew.body != null) carNew.body.carNumber = car.carNumber;
            if (carNew.body?.id) this.carStorageService.create(carNew.body).subscribe(res => {});
          });
        }
        if (imageFile !== undefined && carNew.body?.id) {
          this.carService.updateCarImage(imageFile, carNew.body?.id).subscribe(() => {
            this.carComponent.ngOnInit();
            this.carDetailComponent.ngOnInit();
          });
        } else {
          this.carComponent.ngOnInit();
          this.carDetailComponent.ngOnInit();
        }
      },
      (error: HttpErrorResponse) => {
        /*this.toastr.error("Unable to update.Something went wrong!", "Error", {
          timeOut: 2000
        })*/
      }
    );

    this.title.setTitle(this.mainTitle);
    this.isFileLoad = false;
    this.imageLoadFail = true;
  }

  protected getGearBoxValue(key: string): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(GearBoxType).indexOf(key.toString());
      const s = Object.values(GearBoxType)[indexOfS];
      return s;
    }
  }

  protected getBrandValue(key: string): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(Brand).indexOf(key.toString());
      const s = Object.values(Brand)[indexOfS];
      return s;
    }
  }
  protected getBodyValue(key: string): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(BodyType).indexOf(key.toString());
      const s = Object.values(BodyType)[indexOfS];
      return s;
    }
  }
  retEditC(): ICar {
    return EditComponent?.editCar;
  }

  getOpt(): Option[] {
    return EditComponent.options;
  }

  public blobToFile = (theBlob: Blob, fileName: string): File => {
    var b: any = theBlob;
    b.lastModifiedDate = new Date();
    b.name = fileName;

    return <File>theBlob;
  };
  imgChangeEvt: any = '';
  isFileLoad: boolean = false;
  cropImgPreview: any = '';
  brands: string[] = [
    'Acura',
    'Alpine',
    'Apollo',
    'Apple',
    'Audi',
    'Bentley',
    'BMW',
    'Bollinger',
    'Brilliance',
    'Bugatti',
    'Buick',
    'BYD',
    'Cadillac',
    'Chana',
    'Chery',
    'Chevrolet',
    'Chrysler',
    'Citroen',
    'Continental',
    'Cupra',
    'Dacia',
    'Daewoo',
    'Daihatsu',
    'Datsun',
    'Delfast',
    'Dodge',
    'Faw',
    'Ferrari',
    'Fiat',
    'Fisker',
    'Ford',
    'Foxtron',
    'Geely',
    'Genesis',
    'Gmc',
    'Haval',
    'Honda',
    'Hummer',
    'Hyundai',
    'Ineos',
    'Infiniti',
    'Jac',
    'Jaguar',
    'Jeep',
    'Jetour',
    'Kia',
    'Koenigsegg',
    'Lada',
    'Lamborghini',
    'Lancia',
    'Lexus',
    'Lifan',
    'Lincoln',
    'Lordstown',
    'Lotus',
    'Lucid',
    'Lvchi',
    'Maserati',
    'Maybach',
    'Mazda',
    'McLaren',
    'Mercedes_Benz',
    'Mg',
    'Mini',
    'Mitsubishi',
    'Nikola',
    'Nio',
    'Nissan',
    'Opel',
    'Pagani',
    'Peugeot',
    'Polestar',
    'Porsche',
    'Qoros',
    'Range_Rover',
    'Ravon',
    'Renault',
    'Rimac',
    'Rivian',
    'Rolls_Royce',
    'Saab',
    'Saipa',
    'Seat',
    'Skoda',
    'Smart',
    'SsangYong',
    'Ssc_North',
    'America',
    'Stellantis',
    'Subaru',
    'Suzuki',
    'Tata',
    'Tesla',
    'Torsus',
    'Toyota',
    'VinFast',
    'Volkswagen',
    'Volvo',
    'Xpeng',
    'Zotye',
  ];

  onFileChange(event: any): void {
    this.imgChangeEvt = event;
    this.isFileLoad = !this.isFileLoad;
  }

  cropImg(e: ImageCroppedEvent) {
    this.cropImgPreview = e.base64;
  }

  imgLoad() {
    this.isFileLoad = true;
    this.imageLoadFail = false;
  }

  initCropper() {
    console.log('initCropper');
  }

  imgFailed() {
    this.imageLoadFail = true;
  }
  gearboxTypes: string[] = ['Automatic', 'Mechanic', 'Robotic'];

  resetForm(editForm: NgForm) {
    this.title.setTitle(this.mainTitle);
    this.isFileLoad = false;

    editForm.setValue({
      file: '',
      carBrand: EditComponent.editCar.carBrand,
      id: EditComponent.editCar.id,
      carModel: EditComponent.editCar.carModel,
      carBodyType: EditComponent.editCar.carBodyType,
      carEngineVolume: EditComponent.editCar.carEngineVolume,
      carGearboxType: EditComponent.editCar.carGearboxType,
      carYear: EditComponent.editCar.carYear,
      carDescription: EditComponent.editCar.carDescription,
    });
    this.isFileLoad = false;
    this.imageLoadFail = true;
  }

  getImageUrl(imageUrl: string | undefined | null) {
    if (imageUrl === null) {
      return 'https://d24pzayae8hlxp.cloudfront.net/ghost.png';
    }
    return imageUrl;
  }

  ifModelLenCor(form: NgForm) {
    // @ts-ignore
    let text = document.getElementById('edit-car-model').value;
    if (text.length > 40) {
      form.form.controls['model'].setErrors({ incorrect: true });
    }
    // @ts-ignore
    return text.length > 40;
  }

  ifDescLenCor(addForm: NgForm) {
    // @ts-ignore
    let text = document.getElementById('edit-car-description').value;
    if (text.length > 6000) {
      addForm.form.controls['description'].setErrors({ incorrect: true });
    }
    // @ts-ignore
    return text.length > 6000;
  }
}
