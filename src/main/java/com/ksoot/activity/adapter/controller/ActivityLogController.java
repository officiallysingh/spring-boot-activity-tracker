package com.ksoot.activity.adapter.controller;

import static com.ksoot.activity.model.util.ActivityLogConstants.DEFAULT_PAGE_SIZE;
import static com.ksoot.activity.model.util.ActivityLogMapper.ACTIVITY_LOGS_PAGE_TRANSFORMER;

import com.ksoot.activity.adapter.repository.ActivityLogRepository;
import com.ksoot.activity.model.ActivityLog;
import com.ksoot.activity.model.ActivityLogVM;
import com.ksoot.activity.model.util.PaginatedResource;
import com.ksoot.activity.model.util.PaginatedResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/activity-logs")
@Tag(name = "Activity Logs", description = "query APIs")
@RequiredArgsConstructor
public class ActivityLogController {

  private final ActivityLogRepository activityLogRepository;

  @GetMapping
  @Operation(operationId = "get-activity-logs", summary = "Gets a page of Activity Logs")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description =
                "Activity Logs page returned successfully. Returns an empty page if no records found"),
        @ApiResponse(responseCode = "500", description = "Internal Server error")
      })
  public PaginatedResource<ActivityLogVM> getActivityLogs(
      @Parameter(description = "Name of the user who performed the activity. E.g. <b>SYSTEM</b>")
          @RequestParam(required = false)
          final String author,
      @Parameter(description = "Activity Name.") @RequestParam(required = false)
          final ActivityLog.Activity activity,
      @Parameter(description = "Activity Status.") @RequestParam(required = false)
          final ActivityLog.Status status,
      @Parameter(description = "From Datetime, Inclusive. E.g. <b>2023-12-20T13:57:13+05:30</b>")
          @RequestParam(required = false)
          final OffsetDateTime fromDateTime,
      @Parameter(description = "Till Datetime, Inclusive. E.g. <b>2023-12-22T13:57:13+05:30</b>")
          @RequestParam(required = false)
          final OffsetDateTime tillDateTime,
      @Parameter(
              description =
                  "Phrases to look for in <b>description</b>, <b>metadata</b> or <b>error message</b>.")
          @RequestParam(required = false)
          final List<String> phrases,
      @ParameterObject @PageableDefault(size = DEFAULT_PAGE_SIZE) final Pageable pageRequest) {

    Page<ActivityLog> activityLogsPage =
        this.activityLogRepository.getActivityLogs(
            author, activity, status, fromDateTime, tillDateTime, phrases, pageRequest);
    return PaginatedResourceAssembler.assemble(activityLogsPage, ACTIVITY_LOGS_PAGE_TRANSFORMER);
  }
}
