package com.ksoot.activity.model;

import java.time.OffsetDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogVM {

  protected String id;

  private String author;

  private String activity;

  private String description;

  private OffsetDateTime timestamp;

  private ActivityLog.Status status;

  private String errorMessage;

  private Map<String, String> metadata;
}
