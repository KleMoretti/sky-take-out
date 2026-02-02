package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态条件查询购物车列表
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);


    @Update("update shopping_cart set number =#{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 新增购物车记录
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(user_id,dish_id,setmeal_id,dish_flavor,image,amount,number,create_time) " +
            "values(#{userId},#{dishId},#{setmealId},#{dishFlavor},#{image},#{amount},#{number},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车记录
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);
}
