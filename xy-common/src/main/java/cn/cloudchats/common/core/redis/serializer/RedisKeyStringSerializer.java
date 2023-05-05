package cn.cloudchats.common.core.redis.serializer;

import cn.cloudchats.common.utils.StringUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import java.nio.charset.Charset;

/**
 * @author wuque
 * @title: RedisKeySerializer
 * @projectName datac-commons-lang
 * @description: 自定义redis序列化
 * @date 2022/9/1411:48
 */
public class RedisKeyStringSerializer implements RedisSerializer<String> {

    private final Charset charset;
    private String profiles;

    public RedisKeyStringSerializer()
    {
        this(Charset.forName("UTF8"));
    }

    public RedisKeyStringSerializer(String profiles)
    {
        this(Charset.forName("UTF8"));
        this.profiles = profiles;
    }

    public RedisKeyStringSerializer(Charset charset)
    {
        Assert.notNull(charset, "字符集不允许为NULL");
        this.charset = charset;
    }


    @Override
    public byte[] serialize(String s) throws SerializationException {
        if (StringUtils.isNotEmpty(profiles))
        {
            return new StringBuilder(profiles).append(":").append(s).toString().getBytes(charset);
        }
        return s.getBytes(charset);
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return (bytes == null ? null : new String(bytes, charset));
    }
}
