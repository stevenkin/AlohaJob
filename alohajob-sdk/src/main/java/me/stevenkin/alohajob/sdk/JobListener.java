package me.stevenkin.alohajob.sdk;

import me.stevenkin.alohajob.common.dto.JobInstanceDto;

public interface JobListener {
    void onFinish(JobInstanceDto instanceDto, ProcessResult result);
}
