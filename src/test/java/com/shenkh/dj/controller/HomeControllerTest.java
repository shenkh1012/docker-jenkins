package com.shenkh.dj.controller;

import com.shenkh.dj.ApplicationInfo;
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
  @Autowired
  private HomeController controller;

  @Autowired
  private ApplicationInfo applicationInfo;

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
    assertEquals(applicationInfo.getName(), jsonObject.get("name"));
    assertEquals(applicationInfo.getDescription(), jsonObject.get("description"));
    assertEquals(applicationInfo.getVersion(), jsonObject.get("version"));
  }
}
