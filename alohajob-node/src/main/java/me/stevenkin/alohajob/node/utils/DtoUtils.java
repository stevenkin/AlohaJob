package me.stevenkin.alohajob.node.utils;

import me.stevenkin.alohajob.common.dto.JobInstanceResultDto;
import me.stevenkin.alohajob.sdk.ProcessResult;
import me.stevenkin.alohajob.sdk.ProcessResultType;

import static me.stevenkin.alohajob.common.enums.JobInstanceStatus.SUCCESS;
import static me.stevenkin.alohajob.common.enums.JobInstanceStatus.of;

public class DtoUtils {
    public static ProcessResult toProcessResult(JobInstanceResultDto resultDto) {
        if (resultDto == null)
            return null;
        return new ProcessResult(of(resultDto.getStatus()) == SUCCESS ? ProcessResultType.SUCCESS : ProcessResultType.FAIL, resultDto.getMsg());
    }
}
