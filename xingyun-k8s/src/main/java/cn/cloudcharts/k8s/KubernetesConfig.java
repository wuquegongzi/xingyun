package cn.cloudcharts.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuque
 * @title: KubernetesConfig
 * @projectName xingyun
 * @description:
 * @date 2023/6/1111:23
 */
@Configuration
public class KubernetesConfig {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesConfig.class);

    private Map<String, ApiClient> clients = new ConcurrentHashMap<>();

    /**
     * 根据配置初始化
     */
    @PostConstruct
    public void init() {
        //TODO db 获取
        List<String> clientNames = new ArrayList<>();
        for (String clientName : clientNames) {
            createK8sApiClient(clientName,"","",false,false,false);
        }
    }

    private ApiClient createK8sApiClient(String clientName,
                                    String url,
                                    String token,
                                    boolean isVerifyingSsl,
                                    boolean isDebugging,
                                    boolean isDefault) {
        ApiClient client =
                new ClientBuilder()
                            .setBasePath(url)
                        .setVerifyingSsl(isVerifyingSsl)
                            .setAuthentication(new AccessTokenAuthentication(token))
                        .build().setDebugging(isDebugging);
        if(isDefault){
            io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);
        }
        logger.info("Creating Kubernetes client for client name: {}", clientName);
        clients.put(clientName, client);
        return client;
    }

    @PreDestroy
    public void cleanup() {
        for (ApiClient client : clients.values()) {
//          todo
        }
    }

    public ApiClient getApiClient(String clientName){
        if(clients.containsKey(clientName)){
            return clients.get(clientName);
        }

        //TODO 获取 配置信息
       return createK8sApiClient(clientName,"","",false,false,false);
    }



}
