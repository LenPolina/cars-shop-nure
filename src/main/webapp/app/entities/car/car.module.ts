import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CarComponent } from './list/car.component';
import { CarDetailComponent } from './detail/car-detail.component';
import { CarUpdateComponent } from './update/car-update.component';
import { CarDeleteDialogComponent } from './delete/car-delete-dialog.component';
import { CarRoutingModule } from './route/car-routing.module';
import { FloatingButtonComponent } from './floating-button/floating-button.component';
import { AddComponent } from './add/add.component';
import { MatChipsModule } from '@angular/material/chips';
import { ImageCropperModule } from 'ngx-image-cropper';
import { MatRadioModule } from '@angular/material/radio';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { EditComponent } from './edit/edit.component';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@NgModule({
  imports: [
    SharedModule,
    MatRadioModule,
    MatSlideToggleModule,
    CarRoutingModule,
    FormsModule,
    MatFormFieldModule,
    MatChipsModule,
    ImageCropperModule,
    MatRadioModule,
    MatIconModule,
    MatTabsModule,
  ],
  declarations: [
    CarComponent,
    EditComponent,
    CarDetailComponent,
    AddComponent,
    CarUpdateComponent,
    CarDeleteDialogComponent,
    FloatingButtonComponent,
  ],
})
export class CarModule {}
