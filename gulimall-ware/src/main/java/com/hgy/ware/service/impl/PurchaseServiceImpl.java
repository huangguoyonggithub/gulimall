package com.hgy.ware.service.impl;

import com.hgy.common.constant.WareConstant;
import com.hgy.ware.entity.PurchaseDetailEntity;
import com.hgy.ware.service.PurchaseDetailService;
import com.hgy.ware.service.WareSkuService;
import com.hgy.ware.vo.MergeVo;
import com.hgy.ware.vo.PurchaseDoneVo;
import com.hgy.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.Query;

import com.hgy.ware.dao.PurchaseDao;
import com.hgy.ware.entity.PurchaseEntity;
import com.hgy.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查询未领取的采购单
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    /**
     * 合并采购需求
     * @param mergeVo
     */
    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null){  //如果不存在采购单，先创建一个新的采购单，再把采购需求加上去
            //新建一个采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);

            purchaseId = purchaseEntity.getId();
        }

        //TODO 确认采购需求项的状态是0、1才可以合并

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map((item) -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());
        //把采购需求合并带采购单上
        purchaseDetailService.updateBatchById(collect);
        //修改采购单的时间
        PurchaseEntity newPurchaseEntity = new PurchaseEntity();
        newPurchaseEntity.setId(purchaseId);
        newPurchaseEntity.setUpdateTime(new Date());
        this.updateById(newPurchaseEntity);
    }

    /**
     * 领取采购单
     * @param ids 采购单id
     */
    @Override
    public void received(List<Long> ids) {
        //1、确认当前采购单是新建或者已分配状态（确认是自己的采购单等等功能没写）
        List<PurchaseEntity> collect = ids.stream().map((id) -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter((item) -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map((item) -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //2、改变采购单的状态
        this.updateBatchById(collect);

        //3、改变采购需求项的状态
        collect.forEach((item) -> {
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailPurchaseId(item.getId());
            List<PurchaseDetailEntity> purchaseDetailEntityList = entities.stream().map((entitie) -> {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(entitie.getId());
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return detailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(purchaseDetailEntityList);
        });
    }

    /**
     * 完成采购
     * @param doneVo
     */
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {
        //1、改变采购项状态
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        if (items != null){
            for (PurchaseItemDoneVo item : items) {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                    flag = false;
                    detailEntity.setStatus(item.getStatus());
                }else {
                    detailEntity.setStatus(item.getStatus());
                    //3、将采购的采购项入库
                    PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                    wareSkuService.addStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());
                }
                detailEntity.setId(item.getItemId());
                updates.add(detailEntity);
            }
        }
        //批量更新状态
        purchaseDetailService.updateBatchById(updates);

        //2、改变采购单状态
        Long id = doneVo.getId();
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        //更新状态
        this.updateById(purchaseEntity);
    }

}