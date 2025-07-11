package com.ksoot.activity.config;

import com.ksoot.activity.adapter.controller.ActivityLogController;
import com.ksoot.activity.adapter.repository.ActivityLogRepository;
import com.ksoot.activity.model.AuthorProvider;
import com.ksoot.activity.model.SpringSecurityAuthorProvider;
import com.ksoot.activity.model.SystemAuthorProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;

@AutoConfiguration
public class ActivityTrackerAutoConfiguration {

  @Bean
  ActivityTrackerAspect activityTrackerAspect() {
    return new ActivityTrackerAspect();
  }

  @Bean
  ActivityLogController activityLogController(final ActivityLogRepository activityLogRepository) {
    return new ActivityLogController(activityLogRepository);
  }

  @ConditionalOnMissingBean(AuthorProvider.class)
  static class AuthorProviderConfiguration {

    // Uncomment if you want to use just a hardcoded author name always.
    //  @Bean
    //  AuthorProvider authorProvider() {
    //    return new MockAuthorProvider();
    //  }

    @ConditionalOnMissingClass("org.springframework.security.core.context.SecurityContextHolder")
    @Bean
    AuthorProvider systemAuthorProvider(
        @Value("#{systemProperties['user.name']}") String systemUserName) {
      return new SystemAuthorProvider(systemUserName);
    }

    // Uncomment if you have spring-boot-starter-security dependency
    @ConditionalOnClass(SecurityContextHolder.class)
    @Bean
    AuthorProvider authorProvider() {
      return new SpringSecurityAuthorProvider();
    }
  }
}
