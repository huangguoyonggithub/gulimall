package com.hgy.product.service.impl;

import com.hgy.common.to.SkuReductionTo;
import com.hgy.common.to.SpuBoundsTo;
import com.hgy.common.utils.R;
import com.hgy.product.dao.SpuInfoDescDao;
import com.hgy.product.entity.*;
import com.hgy.product.feign.CouponFeignService;
import com.hgy.product.service.*;
import com.hgy.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.Query;

import com.hgy.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * //TODO 高级部分完善
     * 保存发布商品功能
     * @param vo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {

        //1、保存spu基本信息,pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,infoEntity); //拷贝数据
        infoEntity.setCreateTime(new Date()); //给spuInfoEntity设置vo中没有的数据
        infoEntity.setUpdateTime(new Date()); //给spuInfoEntity设置vo中没有的数据
        this.saveBaceSpuInfo(infoEntity); //保存数据

        //2、保存spu的描述图片信息,pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity); //保存数据

        //3、保存spu图片集,pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(infoEntity.getId(),images); //保存数据

        //4、保存spu的规格参数(基本属性),pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map((attr) -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(infoEntity.getId());
            productAttrValueEntity.setAttrId(attr.getAttrId());
            AttrEntity byId = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(byId.getAttrName());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttrValue(collect); //保存数据

        //5、保存spu的积分信息，要跨库gulimall_sms-->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds,spuBoundsTo);
        spuBoundsTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundsTo); //保存数据
        if (r.getCode() != 0){
            log.error("远程保存spu积分信息失败");
        }

        //6、保存当前spu对应的所有sku信息
        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() != 0){
            skus.forEach(item -> {  //sku的基本信息自动生成的id要和sku的图片信息，销售属性信息等等关联，所以使用forEach遍历
                //6.1）、sku的基本信息，pms_sku_info
                //获取sku里的默认图片（就一个默认的图片）
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1){ //是默认图片
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity); //保存数据

                //sku的图片信息、销售属性信息等等都需要先保存sku的基本信息，因为它们要关联sku的基本信息，所以先获取自动生成的skuId
                Long skuId = skuInfoEntity.getSkuId();

                //6.2）、sku的图片信息,pms_sku_images
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter((entity) -> {
                    //过滤sku图片有值才保存,返回true是想要，false就是剔除
                    return StringUtils.hasLength(entity.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntities); //保存数据

                //6.3）、sku的销售属性信息，pms_sku_sale_attr_value
                List<Attr> attrs = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(attr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities); //保存数据

                //6.4）、sku的优惠、满减信息，要跨库（就是在不同的数据库中）,gulimall_sms-->sms_sku_ladder、sms_sku_full_reduction、sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0  || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo); //保存数据
                    if (r1.getCode() != 0){
                        log.error("远程保存sku的优惠、满减信息失败");
                    }
                }
            });
        }
    }

    /**
     * 保存发布商品功能-->保存spu基本信息,pms_spu_info
     * @param spuInfoEntity
     */
    @Override
    public void saveBaceSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    /**
     * spu列表查询以及检索
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByConition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> wrapper= new QueryWrapper<>();

        /**
         * 查询条件
         * status:
         * key:
         * brandId: 0
         * catelogId: 0
         */

        String key = (String) params.get("key");
        if (StringUtils.hasLength(key)){
            wrapper.and((obj) -> {
                obj.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String) params.get("status");
        if (StringUtils.hasLength(status)){
            wrapper.and((obj) -> {
                obj.eq("publish_status",status);
            });
        }
        String brandId = (String) params.get("brandId");
        if (StringUtils.hasLength(brandId) && !"0".equalsIgnoreCase(brandId)){
            wrapper.and((obj) -> {
                obj.eq("brand_id",brandId);
            });
        }
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.hasLength(catelogId) && !"0".equalsIgnoreCase(catelogId)){
            wrapper.and((obj) -> {
                obj.eq("catalog_id",catelogId);
            });
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }
}