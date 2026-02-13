package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询套餐选项
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 根据菜品id查询包含该菜品的起售套餐数量
     * @param dishId
     * @return
     */
    @Select("select count(*) from setmeal_dish sd left join setmeal s on sd.setmeal_id = s.id where sd.dish_id = #{dishId} and s.status = 1")
    Integer countByDishIdAndStatus(Long dishId);

    /**
     * 批量插入套餐菜品关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
