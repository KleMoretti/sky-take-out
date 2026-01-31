package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "商铺相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String KEY="SHOP_STATUS";
    /**
     * 设置商铺状态
     * @param status 商铺状态 1-营业中 0-打烊中
     * @return 无
     */
    @PutMapping("/{status}")
    @ApiOperation("修改商铺状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置商铺状态:{}", status==1?"营业中":"打烊中");
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }

    /**
     * 获取商铺状态
     * @return 商铺状态 1-营业中 0-打烊中
     */
    @GetMapping("/status")
    @ApiOperation("获取商铺状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺的营业状态为: {}",status==1?"营业中":"打烊中");
        return Result.success(status);
    }
}
