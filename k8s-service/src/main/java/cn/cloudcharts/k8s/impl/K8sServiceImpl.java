package cn.cloudcharts.k8s.impl;

import cn.cloudcharts.k8s.K8sService;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author wuque
 * @title: K8sServiceImpl
 * @projectName xingyun
 * @description:
 * @date 2023/5/419:18
 */
@Service
public class K8sServiceImpl implements K8sService {

    private CoreV1Api api;

    @PostConstruct
    public void init() throws IOException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
        api = new CoreV1Api(client);
    }

    public V1Pod getPod(String namespace, String name) throws ApiException {
        return api.readNamespacedPod(name, namespace, null);
    }

    public void deletePod(String namespace, String name) throws ApiException {
        api.deleteNamespacedPod(name, namespace, null, null, null, null, null, null);
    }



}
