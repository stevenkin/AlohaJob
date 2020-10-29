package me.stevenkin.alohajob.node.core;

import me.stevenkin.alohajob.common.dto.*;
import me.stevenkin.alohajob.common.request.JobInstanceFinishReq;

import me.stevenkin.alohajob.common.request.JobInstanceNewReq;
import me.stevenkin.alohajob.common.response.Response;
import me.stevenkin.alohajob.common.utils.Retry;
import me.stevenkin.alohajob.node.service.OnlineService;
import me.stevenkin.alohajob.sdk.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;

@Component
public class SchedulerServerClient {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OnlineService onlineService;

    /**
     * 获取用户信息
     * @param id
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public UserDto getUser(Long id) throws Exception {
        String get_user_url = "http://%s/admin/getUser?id=%d";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(get_user_url, iterator.next(), id);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<UserDto>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

    /**
     * 获取应用信息
     * @param id
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public AppDto getApp(Long id) throws Exception {
        String get_app_url = "http://%s/admin/getApp?id=%d";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(get_app_url, iterator.next(), id);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<AppDto>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

    /**
     * 获取job信息
     * @param id
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public JobDto getJob(Long id) throws Exception {
        String get_job_url = "http://%s/admin/getJob?id=%d";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(get_job_url, iterator.next(), id);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<JobDto>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

    /**
     * 获取job的一次触发信息
     * @param id
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public JobTriggerDto getJobTrigger(String id) throws Exception {
        String get_job_trigger_url = "http://%s/job/getJobTrigger?id=%s";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(get_job_trigger_url, iterator.next(), id);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<JobTriggerDto>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

    /**
     * 获取job的一次触发的根实例信息
     * @param triggerId
     * @return 可能有多个根实例
     * @throws Exception 当服务皆不可用时抛出
     */
    public List<JobInstanceDto> getRootJobInstance(String triggerId) throws Exception {
        String get_root_job_trigger_url = "http://%s/job/getRootJobTrigger?id=%s";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(get_root_job_trigger_url, iterator.next(), triggerId);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<List<JobInstanceDto>>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

    /**
     * 获取job实例
     * @param parentInstanceId 产生这个job实例的父实例id
     * @param subInstanceName 这个job实例的名字
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public JobInstanceDto getJobInstance(String parentInstanceId, String subInstanceName) throws Exception {
        String get_job_instance_url = "http://%s/job/getJobInstanceByName?id=%sname=%s";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(get_job_instance_url, iterator.next(), parentInstanceId, subInstanceName);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<JobInstanceDto>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

    /**
     * 获取job实例
     * @param instanceId 这个实例的id
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public JobInstanceDto getJobInstance(String instanceId) throws Exception {
        String get_job_instance_url = "http://%s/job/getJobInstance?id=%s";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(get_job_instance_url, iterator.next(), instanceId);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<JobInstanceDto>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

    /**
     * 获取一个job实例的执行结果
     * @param instanceId 这个job实例的id
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public JobInstanceResultDto getJobInstanceResult(String instanceId) throws Exception {
        String get_job_instance_result_url = "http://%s/job/getJobInstanceResult?id=%s";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(get_job_instance_result_url, iterator.next(), instanceId);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<JobInstanceResultDto>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

    /**
     * 用于执行器拉取分配给他的job实例
     * @param triggerId job的一次执行id
     * @param address 执行器的地址
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public JobInstanceDto pullJobInstance(String triggerId, String address) throws Exception {
        String pull_job_instance_url = "http://%s/job/pullJobInstance?id=%s&address=%s";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(pull_job_instance_url, iterator.next(), triggerId, address);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<JobInstanceDto>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

    /**
     * 用于执行器执行完成一个job实例时调用
     * @param instanceId job实例id
     * @param result job实例的执行结果
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public boolean finishJobInstance(String instanceId, ProcessResult result) throws Exception {
        String finish_job_instance_url = "http://%s/job/finishJobInstance";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        Response response = Retry.execute(() -> {
            String url = String.format(finish_job_instance_url, iterator.next());
            return restTemplate
                    .exchange(url, HttpMethod.POST,
                            new HttpEntity<>(new JobInstanceFinishReq(instanceId, result.getType().getCode(), result.getMsg())),
                            Response.class)
                    .getBody();
        }, iterator::hasNext, 1000);
        return response.isSuccess();
    }

    /**
     * 取消一个job实例的执行
     * @param instanceId
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public boolean cancelInstance(String instanceId) throws Exception {
        String cancel_job_instance_url = "http://%s/job/cancelInstance?id=%s";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        Response response = Retry.execute(() -> {
            String url = String.format(cancel_job_instance_url, iterator.next(), instanceId);
            return restTemplate
                    .exchange(url, HttpMethod.GET,
                            null,
                            Response.class)
                    .getBody();
        }, iterator::hasNext, 1000);
        return response.isSuccess();
    }

    /**
     * 设置一个job实例完成并且job实例执行过程中产生的异步job实例也都完成了
     * @param instanceId
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public boolean callbackCompleteInstance(String instanceId) throws Exception {
        String calback_complete_job_instance_url = "http://%s/job/callbackComplete?id=%s";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        Response response = Retry.execute(() -> {
            String url = String.format(calback_complete_job_instance_url, iterator.next(), instanceId);
            return restTemplate
                    .exchange(url, HttpMethod.GET,
                            null,
                            Response.class)
                    .getBody();
        }, iterator::hasNext, 1000);
        return response.isSuccess();
    }

    /**
     * 检查job实例是否存在
     * @param instanceId job实例id
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public boolean checkJobInstanceIsExist(String instanceId) throws Exception {
        return getJobInstance(instanceId) != null;
    }

    /**
     * 检查job实例是否存在
     * @param parentInstanceId 产生这个job实例的父实例id
     * @param subInstanceName 这个job实例的名字
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public boolean checkJobInstanceIsExist(String parentInstanceId, String subInstanceName) throws Exception {
        return getJobInstance(parentInstanceId, subInstanceName) != null;
    }

    /**
     * 用于job实例的执行过程中创建异步job实例
     * @param triggerId job的一次执行id
     * @param parentInstanceId 当前job实例id
     * @param subInstanceName 要产生的异步job实例的名字，要保证这个名字在当前job实例中是惟一的
     * @param instanceParam 要产生的异步job实例的参数
     * @return
     * @throws Exception 当服务皆不可用时抛出
     */
    public String newJobInstance(String triggerId, String parentInstanceId, String subInstanceName, String instanceParam) throws Exception {
        String new_job_instance_url = "http://%s/job/newJobInstance";
        Iterator<String> iterator = onlineService.getServerAddress().iterator();
        return Retry.execute(() -> {
            String url = String.format(new_job_instance_url, iterator.next());
            return restTemplate
                    .exchange(url, HttpMethod.POST,
                            new HttpEntity<>(new JobInstanceNewReq(triggerId, parentInstanceId, subInstanceName, instanceParam)),
                            new ParameterizedTypeReference<Response<String>>() {
                            })
                    .getBody().getData();
        }, iterator::hasNext, 1000);
    }

}
