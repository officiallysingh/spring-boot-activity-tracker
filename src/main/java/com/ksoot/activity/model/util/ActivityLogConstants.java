package com.ksoot.activity.model.util;

import java.util.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ActivityLogConstants {

  public static final String SLASH = "/";

  public static final Locale SYSTEM_LOCALE = Locale.getDefault();

  public static final String API = SLASH + "api";

  public static final String V1 = SLASH + "v1";

  public static final String V2 = SLASH + "v2";

  public static final int DEFAULT_PAGE_SIZE = 16;

  public static final String ACTIVITY_LOGS_COLLECTION_NAME = "activity_logs";
}
