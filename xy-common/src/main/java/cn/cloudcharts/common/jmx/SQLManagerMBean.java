/**
 * Copyright (c) 2012,USTC E-BUSINESS TECHNOLOGY CO.LTD All Rights Reserved.
 */

package cn.cloudcharts.common.jmx;

import javax.management.JMException;
import javax.management.openmbean.TabularData;

/**
 * @author libinsong1204@gmail.com
 * @date 2012-6-19 上午9:50:37
 */
public interface SQLManagerMBean {
	TabularData getUriList() throws JMException;

    String findSQL(String id);

    String resetSql(String id, String type, String sql);

    long getSQLCount();
}
