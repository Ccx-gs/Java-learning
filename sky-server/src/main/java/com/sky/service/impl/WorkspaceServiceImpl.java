package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    // 引入订单和用户的 Mapper
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {

        // 准备一个 Map 用于往 Mapper 传参数
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);

        //1. 查询总订单数
        Integer totalOrderCount = orderMapper.countByMap(map);

        //2. 查询有效订单数
        map.put("status", Orders.COMPLETED); // 追加条件：已完成
        Integer validOrderCount = orderMapper.countByMap(map);

        //3. 查询营业额
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null ? 0.0 : turnover;

        //4. 查询新增用户数
        Map userMap = new HashMap();
        userMap.put("begin", begin);
        userMap.put("end", end);
        Integer newUsers = userMapper.countByMap(userMap);

        //5. 计算：订单完成率 和 平均客单价
        Double orderCompletionRate = 0.0;
        Double unitPrice = 0.0;
        if (totalOrderCount != 0) {
            // 注意这里要把 int 转成 double 再除，不然 1/2 会变成 0
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        if (validOrderCount != 0) {
            unitPrice = turnover / validOrderCount;
        }

        //6. 封装并返回
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }
    /**
     * 查询订单管理数据 (统计今天的各种状态订单数)
     * @return
     */
    @Override
    public OrderOverViewVO getOrderOverView() {
        Map map = new HashMap();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("end", LocalDateTime.now().with(LocalTime.MAX));

        // 1. 待接单数量 (状态 2)
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.countByMap(map);

        // 2. 待派送数量 (状态 3)
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);

        // 3. 已完成数量 (状态 5)
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);

        // 4. 已取消数量 (状态 6)
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);

        // 5. 全部订单数量 (清空状态条件)
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 查询菜品总览
     * @return
     */
    @Override
    public DishOverViewVO getDishOverView() {
        Map map = new HashMap();
        // 起售中 (状态 1)
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        // 停售中 (状态 0)
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询套餐总览
     * @return
     */
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        Map map = new HashMap();
        // 起售中 (状态 1)
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        // 停售中 (状态 0)
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
