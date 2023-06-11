package cn.cloudcharts.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;

/**
 * @author wuque
 * @title: K8sClientTest
 * @projectName xingyun
 * @description:
 * @date 2023/6/820:46
 */
public class K8sClientTest {

    public static void main(String[] args) {
        // 定义连接集群的 Token
//        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6InRyOVBjOGRFYU96N2N6c3VHTVo3dHV6X0J5eVR2SnktdnU5cm5FWU0ycVkifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJjYWxpY28ta3ViZS1jb250cm9sbGVycy10b2tlbi05Yjc1YiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJjYWxpY28ta3ViZS1jb250cm9sbGVycyIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjE5ZTc4NGFiLWY4MGUtNGJhYS04YTFhLTUzOGYxOGIxNzk2NCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTpjYWxpY28ta3ViZS1jb250cm9sbGVycyJ9.XW6c2pjw3BnHA3VMYTKqaue_ack2vUzGpFD8B2O-JrPWpiPXAtTNM5gWlPaNbygLgxIDU5F7H6IIgFEuPfWaM_-A0bqKckGv6jUhVSTvBmGTceR76jWBD_kga97JgAJ850u8gh8Z5AaR-tGrxoAH_9OOeywEqktieXhs62Jp6whChxEndMwIG0GrCIXcKFD1-AfHhef0ZUq7VlnRFTvUumF9zJwFLK6-85tc6UWxAYvMG7WTDi-aIuW2cVhr2RI_eYO-VN2b9uUbiz_Ome08nVunbDYWzbUp4G75_GySOPEJJSyhEsFxhdEuERVZBcRtjTLtkAZtAxkXodRadNfcQg";
        String token = "";
        // 定义 Kubernetes 集群地址
        String url = "https://192.168.217.140:6443";

        // 配置客户端
        ApiClient client =
                new ClientBuilder()
                        .setBasePath(url)
                        .setVerifyingSsl(false)
                        .setAuthentication(new AccessTokenAuthentication(token))
                        .build();
        Configuration.setDefaultApiClient(client);

        try {

            CoreV1Api api = new CoreV1Api();
            V1PodList podList = api.listPodForAllNamespaces(
                    null, null, null, null, null, null, null, null, null, null);

            for (V1Pod pod : podList.getItems()) {
                System.out.println("Pod name: " + pod.getMetadata().getName());
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
