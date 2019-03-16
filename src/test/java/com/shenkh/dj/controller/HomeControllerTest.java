package com.shenkh.dj.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("Home Controller tests")
public class HomeControllerTest {
  private static final String APPLICATION_NAME = "docker-jenkins";
  private static final String APPLICATION_DESCRIPTION = "Test CI/CD with jenkins";
  private static final String APPLICATION_VERSION = "0.0.1-SNAPSHOT";

  @Autowired
  private HomeController controller;

  @Test
  void testHome() {
    validateHomeAndApplicationInfoResponse(controller.home());
  }

  @Test
  void testApplicationInfo() {
    validateHomeAndApplicationInfoResponse(controller.applicationInfo());
  }

  private void validateHomeAndApplicationInfoResponse(ResponseEntity<?> responseEntity) {
    Object responseBody = responseEntity.getBody();
    assertNotNull(responseBody);

    JSONObject jsonObject = new JSONObject(responseEntity.getBody());
    assertEquals(APPLICATION_NAME, jsonObject.get("name"));
    assertEquals(APPLICATION_DESCRIPTION, jsonObject.get("description"));
    assertEquals(APPLICATION_VERSION, jsonObject.get("version"));
  }
}
