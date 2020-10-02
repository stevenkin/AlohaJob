package me.stevenkin.alohajob.sdk;

/**
 * 业务处理器
 */
public interface Processor {
    /**
     * 业务处理逻辑
     * @param context
     * @return
     * @throws Exception
     */
    ProcessResult process(ProcessContext context) throws Exception;
}
