package com.worldremit.atms.controllers;

class InvalidRadiusException extends IllegalArgumentException {
  InvalidRadiusException() {
    super("Provide radius value is to high");
  }
}
