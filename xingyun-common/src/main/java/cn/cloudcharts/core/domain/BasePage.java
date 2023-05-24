package cn.cloudcharts.core.domain;

/**
 * @author wuque
 * @title: BasePage
 * @projectName xingyun
 * @description:
 * @date 2023/5/1719:20
 */
public class BasePage {

    private int pageNum;
    private int pageSize;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        pageNum = pageNum > 0 ? pageNum : 1;
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        pageSize = pageSize > 0 ? pageSize : 10;
        this.pageSize = pageSize;
    }
}
