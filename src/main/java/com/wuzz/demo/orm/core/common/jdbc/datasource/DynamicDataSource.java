package com.wuzz.demo.orm.core.common.jdbc.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @description:动态数据源
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/25 15:55
 * @since 1.0
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {  


    //entry的目的，主要是用来给每个数据源打个标记
	private DynamicDataSourceEntry dataSourceEntry;  
    
    @Override  
    protected Object determineCurrentLookupKey() {
        return this.dataSourceEntry.get();  
    }  
  
    public void setDataSourceEntry(DynamicDataSourceEntry dataSourceEntry) {  
        this.dataSourceEntry = dataSourceEntry;
    }
    
    public DynamicDataSourceEntry getDataSourceEntry(){
    		return this.dataSourceEntry;
    }
    
}
