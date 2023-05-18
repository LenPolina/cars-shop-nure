import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';

import { CarService } from '../service/car.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatChipInputEvent } from '@angular/material/chips';
import { Option } from './option';
import { base64ToFile, ImageCroppedEvent } from 'ngx-image-cropper';
import { Title } from '@angular/platform-browser';
import { ICar } from '../car.model';
import { Brand } from '../../enumerations/brand.model';
import { BodyType } from '../../enumerations/body-type.model';
import { GearBoxType } from '../../enumerations/gear-box-type.model';
import { CarComponent } from '../list/car.component';
import { Router } from '@angular/router';
import { OptionService } from '../../option/service/option.service';
import { PriceService } from '../service/price.servic';
import { StorageService } from 'ngx-webstorage/lib/core/interfaces/storageService';
import { CarStorageService } from '../service/storage.service';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css'],
})
export class AddComponent implements OnInit {
  brandValues = Object.keys(Brand);
  bodyTypeValues = Object.keys(BodyType);
  gearBoxTypeValues = Object.keys(GearBoxType);
  public bodyTypes: string[] = ['Sedan', 'Universal', 'Hatchback', 'Minivan', 'SUV', 'Coupe', 'Convertible', 'Pickup', 'Limousine'];
  public cars: ICar[];
  public imageLoadFail: boolean = false;
  // @ts-ignore
  mainTitle: string;
  // @ts-ignore
  @ViewChild('closeAddModal') closeAddModal: ElementRef;
  // @ts-ignore
  @ViewChild('addForm') addForm: NgForm;
  public isImageLoad: boolean = false;
  public gearboxTypeDef: string;
  public brandDef = this.brandValues[0];
  public bodyTypeDef: string;
  public modelDef: string;
  public descDef: string;
  priceDef: number;
  public yearDef: number;
  public engineVolDef: number;

  constructor(
    private carService: CarService,
    private storageService: CarStorageService,
    private optionService: OptionService,
    private priceService: PriceService,
    /*private toastr: ToastrService,*/ public title: Title,
    private router: Router,
    private c: CarComponent
  ) {
    this.cars = [];
    this.gearboxTypeDef = this.gearBoxTypeValues[0];
    this.bodyTypeDef = this.bodyTypes[0];
    this.modelDef = 'X5';
    this.descDef = 'lane keeping, adaptive cruise, panorama, projection, leather seats, heating, memory, massage and much more';
    this.yearDef = 2021;
    this.priceDef = 7000;
    this.engineVolDef = 3.5;
  }

  ngOnInit() {
    this.mainTitle = this.title.getTitle();
  }

  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  options: Option[] = [{ name: 'Keyless access system' }, { name: 'Leather interior' }, { name: 'Start-stop system' }];

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value) {
      this.options.push({ name: value });
    }

    event.chipInput!.clear();
  }

  remove(fruit: Option): void {
    const index = this.options.indexOf(fruit);

    if (index >= 0) {
      this.options.splice(index, 1);
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
  protected getGearBoxValue(key: string): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(GearBoxType).indexOf(key.toString());
      const s = Object.values(GearBoxType)[indexOfS];
      return s;
    }
  }
  public async onAddCar(addForm: NgForm): Promise<void> {
    const opt: string[] = [];
    this.options.forEach(value => {
      opt.push(value.name);
    });

    let car = addForm.value;

    let imageFile: File | undefined = undefined;

    if (this.isImageLoad) {
      let id = 1;
      const imageName = id + '.png';
      imageFile = this.blobToFile(base64ToFile(this.cropImgPreview as string), imageName);
    }
    this.closeAddModal.nativeElement.click();
    console.log(car);
    this.carService.create(car).subscribe(
      carNew => {
        /* this.toastr.success("Success adding!", "Success", {
        timeOut: 2000,
        progressBar: true,
        progressAnimation: "increasing"
      });*/
        this.optionService.saveAll(opt, carNew.body).subscribe(
          () => {},
          () => {
            console.log('option not added');
          }
        );

        if (carNew.body !== null) {
          carNew.body.carPrice = car.carPrice;
          this.priceService.create(carNew.body).subscribe(res => {
            if (carNew.body !== null) carNew.body.carNumber = car.carNumber;
            if (carNew.body?.id)
              this.storageService.create(carNew.body).subscribe(res => {
                if (imageFile !== undefined && carNew.body?.id) {
                  this.carService.updateCarImage(imageFile, carNew.body?.id).subscribe(() => this.c.ngOnInit());
                } else this.c.ngOnInit();
                console.log('car new' + carNew.body);
              });
          });
        }
      },
      (error: HttpErrorResponse) => {
        /* this.toastr.error("Unable to save.Something went wrong!", "Error", {
        timeOut: 2000
      });*/
      }
    );
  }

  setTitle() {
    this.title.setTitle(this.mainTitle);
  }

  private makeCorrectName(name: string) {
    if (name === 'BMW' || name === 'BYD' || name === 'SUV' || name === null) return name;
    else name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    return name;
  }

  public blobToFile = (theBlob: Blob, fileName: string): File => {
    var b: any = theBlob;
    b.lastModifiedDate = new Date();
    b.name = fileName;

    return <File>theBlob;
  };
  imgChangeEvt: any = '';
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
  gearboxTypes: string[] = ['Automatic', 'Mechanic', 'Robotic'];
  numberDef: number = 1;

  onFileChange(event: any): void {
    this.imgChangeEvt = event;
  }

  cropImg(e: ImageCroppedEvent) {
    this.cropImgPreview = e.base64;
    console.log(e);
  }

  imgLoad() {
    this.isImageLoad = true;
    this.imageLoadFail = false;
  }

  initCropper() {
    console.log('initCropper');
  }

  imgFailed() {
    this.isImageLoad = false;
    this.imageLoadFail = true;
  }

  resetForm(addForm: NgForm) {
    this.setTitle();
    addForm.reset();
    addForm.setValue({
      imageUrl: '',
      carBrand: 'ACURA',
      carModel: 'X5',
      carBodyType: '',
      carEngineVolume: '',
      carGearboxType: 'AUTOMATIC',
      carYear: 2021,
      carPrice: 2000,
      carNumber: 2,
      carDescription: '',
    });
    this.cropImgPreview = null;
    this.isImageLoad = false;
    this.imageLoadFail = true;
    console.log('isImageLoad = false;');
    this.imgChangeEvt = '';
    this.options = [{ name: 'Keyless access system' }, { name: 'Leather interior' }, { name: 'Start-stop system' }];
  }

  ifModelLenCor(form: NgForm) {
    // @ts-ignore
    let text = document.getElementById('carModel').value;
    if (text.length > 40) {
      form.form.controls['model'].setErrors({ incorrect: true });
    }
    // @ts-ignore
    return text.length > 40;
  }

  ifDescLenCor(addForm: NgForm) {
    // @ts-ignore
    let text = document.getElementById('carDescription').value;
    if (text.length > 6000) {
      addForm.form.controls['description'].setErrors({ incorrect: true });
    }
    // @ts-ignore
    return text.length > 6000;
  }
}
