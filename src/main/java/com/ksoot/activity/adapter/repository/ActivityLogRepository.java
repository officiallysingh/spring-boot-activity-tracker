package com.ksoot.activity.adapter.repository;

import static com.ksoot.activity.model.util.ActivityLogConstants.ACTIVITY_LOGS_COLLECTION_NAME;

import com.ksoot.activity.model.ActivityLog;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ActivityLogRepository {

  private final MongoOperations mongoOperations;

  public Page<ActivityLog> getActivityLogs(
      final String author,
      final ActivityLog.Activity activity,
      final ActivityLog.Status status,
      final OffsetDateTime fromDateTime,
      final OffsetDateTime tillDateTime,
      final List<String> phrases,
      final Pageable pageRequest) {

    Query query = new Query();

    if (CollectionUtils.isNotEmpty(phrases)) {
      TextCriteria criteria = TextCriteria.forDefaultLanguage();
      criteria.matchingAny(phrases.toArray(new String[phrases.size()]));
      query = TextQuery.queryText(criteria).sortByScore();
    }

    if (Objects.nonNull(status)) {
      query.addCriteria(Criteria.where("status").is(status));
    }
    if (Objects.nonNull(activity)) {
      query.addCriteria(Criteria.where("activity").is(activity));
    }
    if (StringUtils.isNotBlank(author)) {
      query.addCriteria(Criteria.where("author").is(author));
    }

    if (Objects.nonNull(fromDateTime) && Objects.nonNull(tillDateTime)) {
      query.addCriteria(
          Criteria.where("timestamp")
              .gte(fromDateTime)
              .andOperator(Criteria.where("timestamp").lte(tillDateTime)));
    } else if (Objects.nonNull(fromDateTime)) {
      query.addCriteria(Criteria.where("timestamp").gte(fromDateTime));
    } else if (Objects.nonNull(tillDateTime)) {
      query.addCriteria(Criteria.where("timestamp").lte(tillDateTime));
    }
    final long totalRecords = this.mongoOperations.count(query, ACTIVITY_LOGS_COLLECTION_NAME);
    if (totalRecords == 0) {
      return Page.empty();
    } else {
      final Pageable pageable =
          totalRecords <= pageRequest.getPageSize()
              ? PageRequest.of(0, pageRequest.getPageSize(), pageRequest.getSort())
              : pageRequest;
      final List<ActivityLog> feeMovementRecords =
          this.mongoOperations.find(query.with(pageable), ActivityLog.class);
      return new PageImpl<>(feeMovementRecords, pageable, totalRecords);
    }
  }
}
