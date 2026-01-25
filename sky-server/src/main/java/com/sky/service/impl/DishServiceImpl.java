package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //菜品表插入数据
        dishMapper.insert(dish);
        //获取菜品id
        Long dishId = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            //遍历口味集合,为每个口味设置菜品id
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //口味表插入n条数据,批量插入
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 分页查询菜品信息
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        // 校验菜品状态
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 校验菜品是否关联套餐
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 批量删除菜品
        // for (Long id : ids) {
        //dishMapper.deletebyId(id);
        // 批量删除菜品对应的口味数据
        //dishFlavorMapper.deleteByDishId(id);
        //}
        dishMapper.deletebyIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);

    }

    /**
     * 根据id查询菜品及口味信息
     *
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = dishMapper.getById(id);
        //查询菜品对应的口味信息
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

        //封装数据到DishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品信息，同时更新对应的口味数据
     *
     * @param dishDTO
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //更新菜品表基本信息
        dishMapper.update(dish);

        //删除原有的口味数据，为新增的口味数据做准备
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //新增口味数据
        if (flavors != null && flavors.size() > 0) {
            //遍历口味集合,为每个口味设置菜品id
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            //口味表插入n条数据,批量插入
            dishFlavorMapper.insertBatch(flavors);
        }
    }

}
