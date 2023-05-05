package cn.cloudcharts;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XingyunApplicationTests {

    @Test
    void contextLoads(){

        // 定义连接集群的 Token
        String token = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ii1rMzBsaXF1TXVPeTFFVmJGNGZPY1EwTmlVeDFLTVZ0UFlwYUZWWFFVaDgifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJkZWZhdWx0LXRva2VuLWJueHg3Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImRlZmF1bHQiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJhMzhkNzE3ZS1mMDQ5LTRhYTQtOTczOS05OWI4MWUyNTI1MjQiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZS1zeXN0ZW06ZGVmYXVsdCJ9.vo7IPlMIgfeFJ7QFlxqDOrqhgGDl3n9KVo1ToMGWmzr5i3Rw5oP5aqc0NfSFp1bC6VQfc0JIQflyd5kjfcID9UdOUdX3O__Br89rznDTERn-33S5oJ1RPEGSkhNN0XamHSwOfAh5Ou0t2U_vT3qe8udPtFjinwNIJHZsCHP9PuiVynpRFX8irb-GtWzjSDI54BkpYCx9ZMExmibTTq2je16YnUMbhn6_DHbkvfEQwYzO4q6eUvzZaA7E7u4G1ufDdI1kBxvtUXSNM-FY-QjuL9S8vG3nKQ8ENvm5TVutB5gInvCRl3A9NUl8zYsqaePCbBCi6Y_m9E8bSOownBcVRw";
        // 定义 Kubernetes 集群地址
        String url = "https://192.168.216.190:6443";

        // 配置客户端
//        ApiClient apiClient = Config.fromToken(url, token, false);

        ApiClient client =
                new ClientBuilder().setBasePath(url)
                        .setVerifyingSsl(false)
                        .setAuthentication(new AccessTokenAuthentication(token))
                        .build();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1PodList list = null;
        try {
            list = api.listPodForAllNamespaces(
                    true, "", "", "",
                    10, "", "", "",
                    5, true);

            for (V1Pod item : list.getItems()) {
                System.out.println(item.getMetadata().getName());
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

}
