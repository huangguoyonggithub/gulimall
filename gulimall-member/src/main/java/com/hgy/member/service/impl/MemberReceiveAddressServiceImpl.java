package com.hgy.member.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.Query;

import com.hgy.member.dao.MemberReceiveAddressDao;
import com.hgy.member.entity.MemberReceiveAddressEntity;
import com.hgy.member.service.MemberReceiveAddressService;


@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }

}