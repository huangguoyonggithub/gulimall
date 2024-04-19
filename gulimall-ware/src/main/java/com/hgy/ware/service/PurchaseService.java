package com.hgy.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.ware.entity.PurchaseEntity;
import com.hgy.ware.vo.MergeVo;
import com.hgy.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:38:52
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);
}

