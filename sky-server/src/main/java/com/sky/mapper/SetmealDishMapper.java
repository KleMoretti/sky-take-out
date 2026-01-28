package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品ids查询对应的套餐ids
     * @param dishIds
     * @return
     */

    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入套餐菜品关联数据
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    void deleteBySetmealIds(List<Long> ids);

    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long id);

}
