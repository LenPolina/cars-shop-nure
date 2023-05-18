import { Component, HostListener, OnInit } from '@angular/core';

@Component({
  selector: 'app-floating-button',
  template: '<button  id="btn-to-car"><ng-content></ng-content></button>',
  styles: [
    `
      button {
        border-radius: 3rem;
        position: fixed;
        width: 70px;
        height: 70px;
        padding: 0.375rem 0.75rem;
        bottom: 20px;
        z-index: 2;
        transition: all 0.5s ease;
        right: 30px;
        color: #ffffff;
        background: rgba(252, 88, 24, 0.89);
        border: none;
        text-align: center;
        box-shadow: 2px 2px 3px #888888;
        font-size: 2rem;
        line-height: 1.5;
        cursor: pointer;
        justify-content: center;
        margin-bottom: 20px;
        margin-right: 10px;
      }

      button:active,
      button:focus {
        color: #ffffff;
        background: rgba(255, 71, 0, 0.89);
      }

      button:hover {
        color: #ffffff;
        background: rgba(255, 71, 0, 0.89);
        box-shadow: 3px 3px 4px #5b5b5b;
      }
    `,
  ],
})
export class FloatingButtonComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  @HostListener('window:scroll', []) onWindowScroll() {
    let scrollHeight = document.documentElement.scrollHeight;
    let clientHeight = document.documentElement.clientHeight;
    let height = scrollHeight - clientHeight;
    let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
    if (height - scrollTop <= 100) {
      let s = document.getElementById('btn-to-car');
      // @ts-ignore
      s.style.fontSize = '5px !important';
      let r = -100 + (height - scrollTop);
      // @ts-ignore
      s.style.transform = 'translate3d(0px,' + r + 'px, 0px)';
    } else {
      let s = document.getElementById('btn-to-car');
      // @ts-ignore
      s.style.transform = 'translate3d(0px, 20px, 0px)';
    }
  }
}
