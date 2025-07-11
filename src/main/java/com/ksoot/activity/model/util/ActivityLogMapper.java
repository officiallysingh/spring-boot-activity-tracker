package com.ksoot.activity.model.util;

import com.ksoot.activity.model.ActivityLog;
import com.ksoot.activity.model.ActivityLogVM;
import java.util.List;
import java.util.function.Function;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActivityLogMapper {

  ActivityLogMapper INSTANCE = Mappers.getMapper(ActivityLogMapper.class);

  Function<List<ActivityLog>, List<ActivityLogVM>> ACTIVITY_LOGS_PAGE_TRANSFORMER =
      revisions -> revisions.stream().map(ActivityLogMapper.INSTANCE::toActivityLogVM).toList();

  ActivityLogVM toActivityLogVM(final ActivityLog activityLog);
}
