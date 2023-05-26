# HelloControllerApi

All URIs are relative to *http://192.168.217.140:8088*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**home**](HelloControllerApi.md#home) | **GET** / |  |


<a id="home"></a>
# **home**
> String home()



### Example
```java
// Import classes:
import cn.cloudcharts.xingyun.client.ApiClient;
import cn.cloudcharts.xingyun.client.ApiException;
import cn.cloudcharts.xingyun.client.Configuration;
import cn.cloudcharts.xingyun.client.models.*;
import cn.cloudcharts.xingyun.client.api.HelloControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://192.168.217.140:8088");

    HelloControllerApi apiInstance = new HelloControllerApi(defaultClient);
    try {
      String result = apiInstance.home();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling HelloControllerApi#home");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

