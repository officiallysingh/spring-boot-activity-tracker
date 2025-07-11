package com.ksoot.activity.config;

import com.ksoot.activity.model.ActivityLog;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackActivity {

  ActivityLog.Activity action();

  String description() default "";

  Tag[] tags() default {};

  boolean skip() default false;
}
