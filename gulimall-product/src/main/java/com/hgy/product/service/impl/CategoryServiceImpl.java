package com.hgy.product.service.impl;

import com.hgy.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.Query;

import com.hgy.product.dao.CategoryDao;
import com.hgy.product.entity.CategoryEntity;
import com.hgy.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

//    @Autowired
//    CategoryDao categoryDao;  //基础ServiceImpl，CategoryDao就是基础ServiceImpl里的baseMapper，所以不注入直接使用baseMapper

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查出所有分类以及子分类，以树形解构组装起来
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构
        //2.1）、找到所有的一级分类
        List<CategoryEntity> lever1Menus = entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0) //获取所有一级分类
                .map(menu -> {
                    menu.setChildren(getChildrens(menu,entities)); //获取当前菜单的子菜单
                    return menu;
                })
                .sorted((menu1,menu2) -> {
                    return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());  //排序
                })
                .collect(Collectors.toList());

        return lever1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1、检查当前删除菜单，是否被别的地方引用

        //逻辑删除（就是隐藏和显示）
        baseMapper.deleteBatchIds(asList);
    }

    //[2,25,225]
    @Override
    public Long[] findCategoryPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();

        List<Long> parentPath = findParentPath(catelogId, paths);

        //翻转数据，将parentPath[225,25,2]变成[2,25,225]
        Collections.reverse(parentPath);

        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级连更新关联的所有数据
     * @param category
     */
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        //更新自己的数据
        this.updateById(category);
        //更新关联表的数据
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    //递归获取所有paths [225,25,2]
    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        //1、收集当前节点ID
        paths.add(catelogId);
        //2、查找当前节点ID的父节点数据
        CategoryEntity byId = this.getById(catelogId);
        //3、如果当前节点ID的父ID不为0，就添加父ID
        if (byId.getParentCid() != 0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    /**
     * 递归查找所有菜单的子菜单
     * @param root 当前菜单
     * @param all 所有菜单
     * @return
     */
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){

        List<CategoryEntity> children = all.stream()
                .filter(categoryEntity -> {
                    return categoryEntity.getParentCid() == root.getCatId(); //获取当前菜单的子菜单
                })
                .map(categoryEntity -> {
                    //1、找到子菜单（递归查找）
                    categoryEntity.setChildren(getChildrens(categoryEntity,all));
                    return categoryEntity;
                })
                .sorted((menu1,menu2) -> {
                    //2、菜单的排序
                    return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort()); //排序
                })
                .collect(Collectors.toList());

        return children;
    }

}