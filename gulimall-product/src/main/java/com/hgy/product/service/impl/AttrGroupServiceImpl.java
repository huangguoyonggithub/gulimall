package com.hgy.product.service.impl;

import com.hgy.product.entity.AttrEntity;
import com.hgy.product.service.AttrService;
import com.hgy.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.Query;

import com.hgy.product.dao.AttrGroupDao;
import com.hgy.product.entity.AttrGroupEntity;
import com.hgy.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        //select * from pms_attr_group where catelog_id = ? and (attr_group_id=key or attr_group_name like %key%)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (StringUtils.hasLength(key)){
            wrapper.and((obj) -> {
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }

        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }else {
            wrapper.eq("catelog_id",catelogId);
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper);

            return new PageUtils(page);
        }
    }

    /**
     * 根据分类id查出所有分组以及这些组里面的属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //1、通过catelogId三级分类id查出当前分类下的所有属性分组
        List<AttrGroupEntity> groupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        //2、通过所有属性分组查出它们关联的所有属性
        List<AttrGroupWithAttrsVo> collect = groupEntities.stream().map((item) -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(item,attrGroupWithAttrsVo);
            List<AttrEntity> attrs = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(attrs);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());

        return collect;
    }


}