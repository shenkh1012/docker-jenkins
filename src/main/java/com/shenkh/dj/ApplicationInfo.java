package com.shenkh.dj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInfo {
  @Value("docker-jenkins")
  private String name;
  @Value("Test CI/CD with jenkins")
  private String description;
  @Value("0.0.1-SNAPSHOT")
  private String version;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getVersion() {
    return version;
  }
}
