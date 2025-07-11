package com.ksoot.activity.config;

import com.ksoot.activity.adapter.repository.ActivityLogRepository;
import com.ksoot.activity.model.ActivityLog;
import com.ksoot.activity.model.AuthorProvider;
import com.ksoot.activity.model.Tag;
import com.ksoot.activity.model.TrackActivity;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
@RequiredArgsConstructor
public class ActivityTrackerAspect {

  private static final String METHOD_NAME = "methodName";
  private static final String CLASS_NAME = "className";

  private final ActivityLogRepository activityLogRepository;

  private final AuthorProvider authorProvider;

  @Around("execution (@com.ksoot.activity.model.TrackActivity * *.*(..))")
  public Object logActivity(final ProceedingJoinPoint pjp) throws Throwable {
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    TrackActivity trackActivity = method.getAnnotation(TrackActivity.class);
    if (trackActivity == null) {
      method = pjp.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
      trackActivity = method.getAnnotation(TrackActivity.class);
    }

    final String activity = trackActivity.activity();
    final String description = trackActivity.description();
    final List<String> tags = Arrays.stream(trackActivity.tags()).map(Tag::value).toList();
    final String author = this.authorProvider.getAuthor();

    final String methodName = method.getName();
    final String className = pjp.getTarget().getClass().getSimpleName();
    final Map<String, String> metadata = new LinkedHashMap<>();
    metadata.put(METHOD_NAME, methodName);
    metadata.put(CLASS_NAME, className);

    final ActivityLog activityLog =
        ActivityLog.start(activity, author, description, tags, metadata);

    this.activityLogRepository.save(activityLog);

    Object result;
    try {
      result = pjp.proceed();
    } catch (final Throwable throwable) {
      final String errorMessage = ExceptionUtils.getStackTrace(throwable);
      activityLog.fail(errorMessage);
      this.activityLogRepository.save(activityLog);
      throw throwable;
    }

    activityLog.complete();
    this.activityLogRepository.save(activityLog);

    return result;
  }
}
