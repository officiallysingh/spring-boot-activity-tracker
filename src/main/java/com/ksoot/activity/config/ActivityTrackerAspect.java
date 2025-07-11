package com.ksoot.activity.config;

import com.ksoot.activity.adapter.repository.ActivityLogRepository;
import com.ksoot.activity.model.ActivityLog;
import com.ksoot.activity.model.AuthorProvider;
import com.ksoot.activity.model.TrackActivity;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@RequiredArgsConstructor
public class ActivityTrackerAspect {

  private ActivityLogRepository activityLogRepository;

  private AuthorProvider authorProvider;

  //  @Around("@annotation(com.ksoot.activity.model.TrackActivity)")
  @Around("execution (@com.ksoot.activity.model.TrackActivity * *.*(..))")
  public Object logActivity(final ProceedingJoinPoint joinPoint)
      throws Throwable {
//    final String activity = trackActivity.activity();
//    final String description = trackActivity.description();
//    final String author = this.authorProvider.getAuthor();
//
//    final ActivityLog activityLog = ActivityLog.start(activity, author, description);
//    this.activityLogRepository.save(activityLog);

    final Object result = joinPoint.proceed();

//    this.activityLogRepository.save(activityLog);

    return result;
  }
}
