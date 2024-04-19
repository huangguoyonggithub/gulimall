package com.hgy.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.member.entity.MemberCollectSpuEntity;

import java.util.Map;

/**
 * 会员收藏的商品
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:27:52
 */
public interface MemberCollectSpuService extends IService<MemberCollectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

