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
@Slf4j
@Api(tags = "商户相关接口")


public class ShopController {

    public static final String KEY_SHOP_STATUS = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺的营业状态
     * @param status 0-营业中，1-打烊中
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
public Result setStatus (@PathVariable Integer status){
    log.info("设置店铺的营业状态为：{}",status == 1 ? "营业中":"打烊中");
    redisTemplate.opsForValue().set(KEY_SHOP_STATUS,status);
    return Result.success();
}

@GetMapping("/status")
@ApiOperation("获取店铺的营业状态")
public Result<Integer> getStatus() {
    Integer status = (Integer) redisTemplate.opsForValue().get(KEY_SHOP_STATUS);
    log.info("店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
    return Result.success(status);
}
}
