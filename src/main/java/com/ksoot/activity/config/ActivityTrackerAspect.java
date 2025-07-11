package com.ksoot.activity.config;

import com.ksoot.activity.adapter.repository.ActivityLogRepository;
import com.ksoot.activity.model.ActivityLog;
import com.ksoot.activity.model.AuthorProvider;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@RequiredArgsConstructor
public class ActivityTrackerAspect {

  private ActivityLogRepository activityLogRepository;

  private AuthorProvider authorProvider;

  @Around("@annotation(TrackActivity)")
  public Object logActivity(ProceedingJoinPoint joinPoint, TrackActivity trackActivity)
      throws Throwable {
    final ActivityLog.Activity activity = trackActivity.action();
    final String description = trackActivity.description();
    final String author = this.authorProvider.getAuthor();

    //        Map<String, String> metadata =
    // Arrays.stream(trackActivity.metadata()).map(String::trim)
    //                .filter(s -> !s.isEmpty())
    //                .reduce(new HashMap<>(), (map, s) -> {
    //                    String[] parts = s.split("=");
    //                    map.put(parts[0], parts[1]);
    //                    return map;
    //                });
    final Object[] args = joinPoint.getArgs();
    // Optionally add method args to metadata

    final ActivityLog activityLog = ActivityLog.start(activity, author, description);
    this.activityLogRepository.save(activityLog);

    final Object result = joinPoint.proceed();

    this.activityLogRepository.save(activityLog);

    return result;
  }
}
