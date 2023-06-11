package cn.cloudcharts.k8s.impl;

import cn.cloudcharts.k8s.K8sService;
import cn.cloudcharts.k8s.KubernetesConfig;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class K8sServiceImpl implements K8sService {

    @Autowired
    KubernetesConfig kubernetesConfig;

    public ApiClient getApiClient(String clientName) {
        return kubernetesConfig.getApiClient(clientName);
    }

    public V1Pod getPods(String clientName,String namespace, String name) throws ApiException {
        ApiClient apiClient = kubernetesConfig.getApiClient(clientName);
        CoreV1Api api = new CoreV1Api(apiClient);
        return api.readNamespacedPod(name, namespace, null);
    }

    public void deletePod(String clientName,String namespace, String name) throws ApiException {
        ApiClient apiClient = kubernetesConfig.getApiClient(clientName);
        CoreV1Api api = new CoreV1Api(apiClient);
        api.deleteNamespacedPod(name, namespace, null, null, null, null, null, null);
    }



}
