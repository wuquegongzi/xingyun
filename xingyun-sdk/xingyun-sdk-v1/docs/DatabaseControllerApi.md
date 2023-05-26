# DatabaseControllerApi

All URIs are relative to *http://192.168.217.140:8088*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**add**](DatabaseControllerApi.md#add) | **POST** /database/add |  |
| [**callList**](DatabaseControllerApi.md#callList) | **POST** /database/list |  |
| [**getOneById**](DatabaseControllerApi.md#getOneById) | **POST** /database/getOneById |  |
| [**saveOrUpdate**](DatabaseControllerApi.md#saveOrUpdate) | **PUT** /database |  |
| [**testConnect**](DatabaseControllerApi.md#testConnect) | **POST** /database/testConnect |  |


<a id="add"></a>
# **add**
> R add(database)



### Example
```java
// Import classes:
import cn.cloudcharts.xingyun.client.ApiClient;
import cn.cloudcharts.xingyun.client.ApiException;
import cn.cloudcharts.xingyun.client.Configuration;
import cn.cloudcharts.xingyun.client.models.*;
import cn.cloudcharts.xingyun.client.api.DatabaseControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://192.168.217.140:8088");

    DatabaseControllerApi apiInstance = new DatabaseControllerApi(defaultClient);
    Database database = new Database(); // Database | 
    try {
      R result = apiInstance.add(database);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabaseControllerApi#add");
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
| **database** | [**Database**](Database.md)|  | |

### Return type

[**R**](R.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a id="callList"></a>
# **callList**
> RPageInfoObject callList(dataBaseRequest)



### Example
```java
// Import classes:
import cn.cloudcharts.xingyun.client.ApiClient;
import cn.cloudcharts.xingyun.client.ApiException;
import cn.cloudcharts.xingyun.client.Configuration;
import cn.cloudcharts.xingyun.client.models.*;
import cn.cloudcharts.xingyun.client.api.DatabaseControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://192.168.217.140:8088");

    DatabaseControllerApi apiInstance = new DatabaseControllerApi(defaultClient);
    DataBaseRequest dataBaseRequest = new DataBaseRequest(); // DataBaseRequest | 
    try {
      RPageInfoObject result = apiInstance.callList(dataBaseRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabaseControllerApi#callList");
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
| **dataBaseRequest** | [**DataBaseRequest**](DataBaseRequest.md)|  | |

### Return type

[**RPageInfoObject**](RPageInfoObject.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a id="getOneById"></a>
# **getOneById**
> RDatabase getOneById(id)



### Example
```java
// Import classes:
import cn.cloudcharts.xingyun.client.ApiClient;
import cn.cloudcharts.xingyun.client.ApiException;
import cn.cloudcharts.xingyun.client.Configuration;
import cn.cloudcharts.xingyun.client.models.*;
import cn.cloudcharts.xingyun.client.api.DatabaseControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://192.168.217.140:8088");

    DatabaseControllerApi apiInstance = new DatabaseControllerApi(defaultClient);
    String id = "id_example"; // String | 
    try {
      RDatabase result = apiInstance.getOneById(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabaseControllerApi#getOneById");
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
| **id** | **String**|  | |

### Return type

[**RDatabase**](RDatabase.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a id="saveOrUpdate"></a>
# **saveOrUpdate**
> RVoid saveOrUpdate(database)



### Example
```java
// Import classes:
import cn.cloudcharts.xingyun.client.ApiClient;
import cn.cloudcharts.xingyun.client.ApiException;
import cn.cloudcharts.xingyun.client.Configuration;
import cn.cloudcharts.xingyun.client.models.*;
import cn.cloudcharts.xingyun.client.api.DatabaseControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://192.168.217.140:8088");

    DatabaseControllerApi apiInstance = new DatabaseControllerApi(defaultClient);
    Database database = new Database(); // Database | 
    try {
      RVoid result = apiInstance.saveOrUpdate(database);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabaseControllerApi#saveOrUpdate");
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
| **database** | [**Database**](Database.md)|  | |

### Return type

[**RVoid**](RVoid.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

<a id="testConnect"></a>
# **testConnect**
> R testConnect(database)



### Example
```java
// Import classes:
import cn.cloudcharts.xingyun.client.ApiClient;
import cn.cloudcharts.xingyun.client.ApiException;
import cn.cloudcharts.xingyun.client.Configuration;
import cn.cloudcharts.xingyun.client.models.*;
import cn.cloudcharts.xingyun.client.api.DatabaseControllerApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://192.168.217.140:8088");

    DatabaseControllerApi apiInstance = new DatabaseControllerApi(defaultClient);
    Database database = new Database(); // Database | 
    try {
      R result = apiInstance.testConnect(database);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabaseControllerApi#testConnect");
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
| **database** | [**Database**](Database.md)|  | |

### Return type

[**R**](R.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

