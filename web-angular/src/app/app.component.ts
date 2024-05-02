import { Component, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ExampleController, HelloWorld } from '@monorepo-api';
import { Observable } from 'rxjs';
import { AsyncPipe, JsonPipe } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, AsyncPipe, JsonPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  helloWorld$!: Observable<HelloWorld>;

  constructor(private exampleController: ExampleController) {}

  ngOnInit(): void {
    this.helloWorld$ = this.exampleController.helloWorld();
  }
}
