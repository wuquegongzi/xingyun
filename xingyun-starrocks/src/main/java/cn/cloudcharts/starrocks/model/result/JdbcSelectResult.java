package cn.cloudcharts.starrocks.model.result;

import cn.cloudcharts.core.domain.R;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author wuque
 * @title: JdbcSelectResult
 * @projectName xingyun
 * @description:
 * @date 2023/5/2219:17
 */
@Data
public class JdbcSelectResult extends R {

    private List<String> columns;
    private List<LinkedHashMap<String, Object>> rowData;
    private Integer total;
    private Integer page;
    private Integer limit;

    public static JdbcSelectResult buildResult() {
        JdbcSelectResult result = new JdbcSelectResult();
        result.setTimestamp(DateUtil.current());
        return result;
    }

    private static final String STATUS = "status";
    private static final List<String> STATUS_COLUMN =
            new ArrayList<String>() {

                {
                    add("status");
                }
            };

    public void setStatusList(List<Object> statusList) {
        this.setColumns(STATUS_COLUMN);
        List<LinkedHashMap<String, Object>> dataList = new ArrayList<>();
        for (Object item : statusList) {
            LinkedHashMap map = new LinkedHashMap<String, Object>();
            map.put(STATUS, item);
            dataList.add(map);
        }
        this.setRowData(dataList);
        this.setTotal(statusList.size());
    }

    public void error(String error) {
        this.setExecuteTime(DateUtil.current());
        this.setCode(FAIL);
        this.setMsg(error);
    }

    public void success() {
        this.setExecuteTime(DateUtil.current());
        this.setCode(SUCCESS);
    }
}
