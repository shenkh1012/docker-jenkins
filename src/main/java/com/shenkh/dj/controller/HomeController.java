package com.shenkh.dj.controller;

import com.shenkh.dj.ApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
  private ApplicationInfo applicationInfo;

  @Autowired
  public HomeController(ApplicationInfo applicationInfo) {
    this.applicationInfo = applicationInfo;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity home() {
    return applicationInfo();
  }

  @GetMapping(value = "application-info", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity applicationInfo() {
    return ResponseEntity.ok(this.applicationInfo);
  }
}
