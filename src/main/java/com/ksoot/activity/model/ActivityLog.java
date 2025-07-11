package com.ksoot.activity.model;

import static com.ksoot.activity.model.util.ActivityLogConstants.ACTIVITY_LOGS_COLLECTION_NAME;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@PersistenceCreator))
@Document(collection = ACTIVITY_LOGS_COLLECTION_NAME)
@TypeAlias("activity_log")
public class ActivityLog {

  @Id protected String id;

  @Version protected Long version;

  @NotEmpty @Indexed private String author;

  @NotNull @Indexed private String activity;

  @TextIndexed(weight = 1)
  private String description;

  @NotEmpty private OffsetDateTime timestamp;

  @NotNull @Indexed private Status status;

  @TextIndexed(weight = 2)
  private List<String> tags;

  @TextIndexed(weight = 3)
  private String errorMessage;

  private Map<String, String> metadata;

  public static ActivityLog start(
      final String activity, final String author, final String description) {
    return start(activity, author, description, null);
  }

  public static ActivityLog start(
      final String activity, final String author, final String description, List<String> tags) {
    ActivityLog activityLog = new ActivityLog();

    activityLog.activity = activity;
    activityLog.author = author;
    activityLog.description = description;
    activityLog.timestamp = OffsetDateTime.now();
    activityLog.status = Status.STARTED;
    activityLog.tags = tags;

    return activityLog;
  }

  public void complete() {
    this.status = Status.COMPLETED;
  }

  public void complete(final Map<String, String> metadata) {
    this.status = Status.COMPLETED;
    this.metadata = metadata;
  }

  public void fail(final String errorMessage) {
    this.status = Status.FAILED;
    this.errorMessage = errorMessage;
  }

  public void fail(final String errorMessage, final Map<String, String> metadata) {
    this.status = Status.FAILED;
    this.errorMessage = errorMessage;
    this.metadata = metadata;
  }

  public enum Status {
    STARTED,
    COMPLETED,
    FAILED,
    UNKNOWN
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    ActivityLog that = (ActivityLog) o;

    return new org.apache.commons.lang3.builder.EqualsBuilder().append(id, that.id).isEquals();
  }

  @Override
  public int hashCode() {
    return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37).append(id).toHashCode();
  }
}
