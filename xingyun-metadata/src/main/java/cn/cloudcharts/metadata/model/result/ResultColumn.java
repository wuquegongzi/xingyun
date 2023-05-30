package cn.cloudcharts.metadata.model.result;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
public class ResultColumn implements Serializable {

    private static final long serialVersionUID = 6438514547501611599L;

    private String name;
    private String type;
    private String comment;
    private boolean keyFlag;
    private boolean autoIncrement;
    private String defaultValue;
    private boolean isNullable;
    private String columnFamily;
    private Integer position;
    private Integer length;
    private Integer precision;
    private Integer scale;
    private String characterSet;
    private String collation;

}
