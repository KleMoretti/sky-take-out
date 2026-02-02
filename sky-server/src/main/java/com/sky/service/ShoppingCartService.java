package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface ShoppingCartService {

    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车列表
     * @return
     */
    List<ShoppingCart> addShoppingCart();

    void cleanShoppingCart();
}
