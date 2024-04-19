package com.hgy.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.member.entity.MemberStatisticsInfoEntity;

import java.util.Map;

/**
 * 会员统计信息
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:27:52
 */
public interface MemberStatisticsInfoService extends IService<MemberStatisticsInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

