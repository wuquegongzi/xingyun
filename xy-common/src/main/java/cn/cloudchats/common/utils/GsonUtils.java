package cn.cloudchats.common.utils;


import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;


/**
 * GSON封装工具类
 * 解决int类型在map中被转换成double类型问题
 * @author wuque
 * @date 12:12 2021-08-31
 **/
public class GsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(GsonUtils.class);

    //不用创建对象,直接使用Gson.就可以调用方法
    private static Gson gson = null;

    public static Type gsonMapTypeToken = new TypeToken<HashMap<String, Object>>(){}.getType();

    static {
        if (gson == null) {
            //gson = new Gson();
            //当使用GsonBuilder方式时属性为空的时候输出来的json字符串是有键值key的,显示形式是"key":null，而直接new出来的就没有"key":null的
            gson = new GsonBuilder()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            gson = setTypeAdapter(gson);
        }
    }

    //无参的私有构造方法
    private GsonUtils() {
    }

    public static Gson setTypeAdapter(Gson gson) {

        try {
            Field factories = Gson.class.getDeclaredField("factories");
            factories.setAccessible(true);
            Object o = factories.get(gson);
            Class<?>[] declaredClasses = Collections.class.getDeclaredClasses();
            for (Class c : declaredClasses) {
                if ("java.util.Collections$UnmodifiableList".equals(c.getName())) {
                    Field listField = c.getDeclaredField("list");
                    listField.setAccessible(true);
                    List<TypeAdapterFactory> list = (List<TypeAdapterFactory>) listField.get(o);
                    int i = list.indexOf(ObjectTypeAdapter.FACTORY);
                    list.set(i, new TypeAdapterFactory() {
                        @Override
                        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                            if (type.getRawType() == Object.class) {
                                return new TypeAdapter() {
                                    @Override
                                    public Object read(JsonReader in) throws IOException {
                                        JsonToken token = in.peek();
                                        //判断字符串的实际类型
                                        switch (token) {
                                            case BEGIN_ARRAY:
                                                List<Object> list = new ArrayList<>();
                                                in.beginArray();
                                                while (in.hasNext()) {
                                                    list.add(read(in));
                                                }
                                                in.endArray();
                                                return list;

                                            case BEGIN_OBJECT:
                                                Map<String, Object> map = new LinkedTreeMap<>();
                                                in.beginObject();
                                                while (in.hasNext()) {
                                                    map.put(in.nextName(), read(in));
                                                }
                                                in.endObject();
                                                return map;
                                            case STRING:
                                                return in.nextString();
                                            case NUMBER:
                                                String s = in.nextString();
                                                if (s.contains(".")) {
                                                    return Double.valueOf(s);
                                                } else {
                                                    try {
                                                        return Integer.valueOf(s);
                                                    } catch (Exception e) {
                                                        return Long.valueOf(s);
                                                    }
                                                }
                                            case BOOLEAN:
                                                return in.nextBoolean();
                                            case NULL:
                                                in.nextNull();
                                                return null;
                                            default:
                                                throw new IllegalStateException();
                                        }
                                    }

                                    @Override
                                    public void write(JsonWriter out, Object value) throws IOException {
                                        if (value == null) {
                                            out.nullValue();
                                            return;
                                        }
                                        //noinspection unchecked
                                        TypeAdapter<Object> typeAdapter = gson.getAdapter((Class<Object>) value.getClass());
                                        if (typeAdapter instanceof ObjectTypeAdapter) {
                                            out.beginObject();
                                            out.endObject();
                                            return;
                                        }
                                        typeAdapter.write(out, value);
                                    }
                                };
                            }
                            return null;
                        }
                    });
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return gson;
    }

    /**
     * 将对象转成json格式
     *
     * @param object
     * @return String
     */
    public static String gsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 将json转成特定的cls的对象
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T gsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            //传入json对象和对象类型,将json转成对象
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * json字符串转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
   /* public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            //根据泛型返回解析指定的类型,TypeToken<List<T>>{}.getType()获取返回类型
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }
*/

    /**
     * json字符串转成list
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        List<T> mList = new ArrayList<T>();

        JsonArray array = JsonParser.parseString(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }

        return mList;
    }

    /**
     * json字符串转成list
     *
     * @param json
     * @param typeOfT
     * @return
     */
    public static <T> List<T> jsonToList(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }


    /**
     * json字符串转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,gsonMapTypeToken);
        }
        return list;
    }

    /**
     * json字符串转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> gsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, gsonMapTypeToken);
        }
        return map;
    }
}
