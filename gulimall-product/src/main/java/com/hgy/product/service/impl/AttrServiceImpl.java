package com.hgy.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hgy.common.constant.ProductConstant;
import com.hgy.product.dao.AttrAttrgroupRelationDao;
import com.hgy.product.dao.AttrGroupDao;
import com.hgy.product.dao.CategoryDao;
import com.hgy.product.entity.*;
import com.hgy.product.service.CategoryService;
import com.hgy.product.vo.AttrGroupRelationVo;
import com.hgy.product.vo.AttrRespVo;
import com.hgy.product.vo.AttrVo;
import com.hgy.product.vo.BaseAttrs;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.Query;

import com.hgy.product.dao.AttrDao;
import com.hgy.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional //事务
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        //attrEntity.setAttrName(attr.getAttrName());
        //把vo数据一个个封装到po里，很麻烦所以我们用复制（将页面封装来的值attr(vo)封装到attrEntity(po)）
        BeanUtils.copyProperties(attr,attrEntity);
        //1、保存基本数据
        this.save(attrEntity);
        //2、保存关联关系(属性分组的id和属性id)
        if(attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null){  //表示是一个基本属性（不是就不要添加）
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }

    /**
     * 规格参数的查询和销售属性的查询
     * @param params
     * @param catelogId
     * @param attrType
     * @return
     */
    @Transactional //事务
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("attr_type","base".equalsIgnoreCase(attrType)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()); //数据库查询条件

        if (catelogId != 0){
            //有分类id,封装数据库查询条件
            wrapper.eq("catelog_id",catelogId);
        }
        //获取浏览器输入的查询提交,如果输入了就封装数据库查询条件，没有就默认查询全部
        String key = (String) params.get("key");
        if (StringUtils.hasText(key)){
            wrapper.and((obj) -> {  //使用箭头函数，表示下面的数据是一体的，完成一个就行
                obj.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        //进行分页查询
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        //进一步封装成想要的数据（不要使用多表连接查询，数据量太庞大）
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((attrEntity) -> {
            //1、把attrEntity的所有基本数据拷贝到attrRespVo中
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            //2、设置分类和分组的名字
            //如果是销售属性不需要添加分组
            if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
                //1)、获取分组的名字
                // 通过attr_id获取属性和属性分组的关系数据
                AttrAttrgroupRelationEntity attrId = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrId != null && attrId.getAttrGroupId() != null) {
                    //通过属性和属性分组的关系数据里属性分组id获取属性分组的数据
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            //2）、获取分类的名字
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            return attrRespVo;
        }).collect(Collectors.toList());

        //pageUtils返回的数据使用进一步封装好后的结果集
        pageUtils.setList(respVos);

        return pageUtils;
    }

    /**
     * 规格参数的修改时回写和销售属性的修改时回写
     * @param attrId
     * @return
     */
    @Transactional
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        //封装返回数据
        AttrRespVo respVo = new AttrRespVo();
        //使用逆向生成的方法获取attrEntity（属性的基本数据）
        AttrEntity attrEntity = this.getById(attrId);
        //拷贝attrEntity数据到respVo中
        BeanUtils.copyProperties(attrEntity,respVo);

        //respVo放入分组id和分类的完整路径
        //1、设置分组信息
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //获取属性和属性分组关系数据
            AttrAttrgroupRelationEntity attrgroupRelation = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attrgroupRelation != null){
                //分组id
                respVo.setAttrGroupId(attrgroupRelation.getAttrGroupId());
                //获取属性分组数据
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupRelation.getAttrGroupId());
                if (attrGroupEntity != null){
                    //分组名字
                    respVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        //2、设置分类信息
        //获取分类id
        Long catelogId = attrEntity.getCatelogId();
        //获取分类完整路径
        Long[] categoryPath = categoryService.findCategoryPath(catelogId);
        respVo.setCatelogPath(categoryPath);

        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null){
            respVo.setCatelogName(categoryEntity.getName());
        }

        return respVo;
    }

    /**
     * 规格参数的修改和销售属性的修改
     * @param attr
     */
    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        //修改基本数据
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);

        //1、修改分组关联
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            //设置AttrGroupId和AttrId
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());

            Long attrId = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if (attrId > 0){  //属性和属性分组有关系才修改
                //更新数据
                attrAttrgroupRelationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            }else {  //属性和属性分组没有关系就新增
                attrAttrgroupRelationDao.insert(relationEntity);
            }
        }
    }

    /**
     * 根据分组Id查找所有关联的基本属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        //收集attrId的List集合
        List<Long> attrIds = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        if(attrIds == null || attrIds.size() == 0){
            return null;
        }

        //根据attrId的List集合查询所有的属性信息
        List<AttrEntity> Attrentities = this.listByIds(attrIds);
        return Attrentities;
    }

    /**
     * 删除属性与分组的关联关系
     * @param vos
     */
    @Override
    public void  deleteRelation(AttrGroupRelationVo[] vos) {
        //封装数据
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        //批量删除关联关系
        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }

    /**
     * 获取分组没有关联的所有属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Transactional
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1、当前分组只能关联自己属性的分类里面的所有属性
        //获取当前分组的信息
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        //获取当前分类的ID
        Long catelogId = attrGroupEntity.getCatelogId();

        //2、当前分组只能关联别的分组没有引用的属性
        //2.1)、当前分类下的其他分组(ne排除当前分组)
        List<AttrGroupEntity> group = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        //获取所有其他分组的id
        List<Long> collect = group.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        //2.2）、这些分组关联的属性
        List<AttrAttrgroupRelationEntity> groupId = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", collect));
        //获取所有其他分组关联的所有属性的id
        List<Long> attrIds = groupId.stream().map((irem) -> {
            return irem.getAttrId();
        }).collect(Collectors.toList());

        //2.3）、从当前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds != null && attrIds.size() > 0){
            wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if (StringUtils.hasLength(key)){
            wrapper.and((obj) -> {
                obj.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }
}