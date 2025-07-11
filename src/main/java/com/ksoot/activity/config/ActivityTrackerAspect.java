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
    Object[] args = joinPoint.getArgs();
    // Optionally add method args to metadata

    ActivityLog activityLog = ActivityLog.start(activity, author, description);
    activityLogRepository.save(activityLog);

    Object result = joinPoint.proceed();

    activityLogRepository.save(activityLog);

    return result;
  }
}
