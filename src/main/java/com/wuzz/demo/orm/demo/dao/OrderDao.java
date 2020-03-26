package com.wuzz.demo.orm.demo.dao;

import com.wuzz.demo.orm.core.common.jdbc.datasource.DynamicDataSource;
import com.wuzz.demo.orm.demo.entity.Order;
import com.wuzz.demo.orm.framework.BaseDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;


@Repository
public class OrderDao extends BaseDaoSupport<Order, Long> {

    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    private SimpleDateFormat fullDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DynamicDataSource dataSource;

    @Override
    protected String getPKColumn() {
        return "id";
    }

    @Resource(name = "dynamicDataSource")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = (DynamicDataSource) dataSource;
        this.setDataSourceReadOnly(dataSource);
        this.setDataSourceWrite(dataSource);
    }

    /**
     * @throws Exception
     */
    public boolean insertOne(Order order) throws Exception {
        //约定优于配置
        Date date = null;
        if (order.getCreateTime() == null) {
            date = new Date();
            order.setCreateTime(date.getTime());
        } else {
            date = new Date(order.getCreateTime());
        }
        Integer dbRouter = Integer.valueOf(yearFormat.format(date));
        int i = dbRouter % 2 + 1;
        System.out.println("自动分配到【DB_ORM" + i + "】数据源");
        this.dataSource.getDataSourceEntry().set(i);

        order.setCreateTimeFmt(fullDataFormat.format(date));

        Long orderId = super.insertAndReturnId(order);
        order.setId(orderId);
        return orderId > 0;
    }


}
