# ExecuteControllerApi

All URIs are relative to *http://192.168.217.140:8088*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**executeSql**](ExecuteControllerApi.md#executeSql) | **POST** /executeSql |  |


<a id="executeSql"></a>
# **executeSql**
> RJdbcSelectResult executeSql(sqlDTO)



### Example
```java
// Import classes:
import cn.cloudcharts.xingyun.client.ApiClient;
import cn.cloudcharts.xingyun.client.ApiException;
import cn.cloudcharts.xingyun.client.Configuration;
import cn.cloudcharts.xingyun.client.models.*;
import cn.cloudcharts.xingyun.client.api.ExecuteControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://192.168.217.140:8088");

    ExecuteControllerApi apiInstance = new ExecuteControllerApi(defaultClient);
    SqlDTO sqlDTO = new SqlDTO(); // SqlDTO | 
    try {
      RJdbcSelectResult result = apiInstance.executeSql(sqlDTO);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ExecuteControllerApi#executeSql");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **sqlDTO** | [**SqlDTO**](SqlDTO.md)|  | |

### Return type

[**RJdbcSelectResult**](RJdbcSelectResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

