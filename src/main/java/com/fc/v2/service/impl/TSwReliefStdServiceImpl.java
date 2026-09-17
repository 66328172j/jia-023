package com.fc.v2.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fc.v2.common.support.ConvertUtil;
import com.fc.v2.mapper.auto.TSwReliefStdMapper;
import com.fc.v2.mapper.auto.TSwProjectMapper;
import com.fc.v2.model.auto.TSwReliefStd;
import com.fc.v2.model.auto.TSwProject;
import com.fc.v2.service.ITSwReliefStdService;
import com.fc.v2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 补偿费减免标准Service业务层处理
 *
 * @author fuce
 * @date 2026-09-12
 */
@Service
public class TSwReliefStdServiceImpl extends ServiceImpl<TSwReliefStdMapper, TSwReliefStd> implements ITSwReliefStdService {

    @Autowired
    private TSwProjectMapper swProjectMapper;

    @Override
    public TSwReliefStd selectTSwReliefStdById(Long id) {
        return this.baseMapper.selectOne(new QueryWrapper<TSwReliefStd>()
                .eq("id", id)
                .eq("del_flag", 0));
    }

    @Override
    public List<TSwReliefStd> selectTSwReliefStdList(Wrapper<TSwReliefStd> queryWrapper) {
        QueryWrapper<TSwReliefStd> wrapper = new QueryWrapper<TSwReliefStd>();
        com.github.pagehelper.PageHelper.startPage(1, 10);
        wrapper.eq("status", 0);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public int insertTSwReliefStd(TSwReliefStd record) {
        if (record == null) {
            return 0;
        }

        record.setCreateBy(record.getStdNo());
        TSwProject refArch = swProjectMapper.selectOne(new QueryWrapper<TSwProject>()
                .eq("id", record.getProjId()).eq("del_flag", 0));
        if (refArch == null) {
            return 0;
        }
        if (refArch.getStatus() != null && refArch.getStatus() == 1) {
            return 0;
        }
        record.setProjNo(refArch.getProjNo());
        if (StringUtils.isNotEmpty(record.getStdNo())) {
            Integer dupCnt = this.baseMapper.selectCount(new QueryWrapper<TSwReliefStd>()
                    .eq("std_no", record.getStdNo()).eq("del_flag", 0));
            if (dupCnt != null && dupCnt > 0) {
                return 0;
            }
        }
        TSwProject bandArch = swProjectMapper.selectById(record.getProjId());
        BigDecimal bandVal = record.getQty();
        int bandLevel = 0;
        if (bandVal != null && bandArch != null) {
            if (bandVal.compareTo(bandArch.getTh1Max()) <= 0) {
                bandLevel = 0;
            } else if (bandVal.compareTo(bandArch.getTh2Max()) <= 0) {
                bandLevel = 1;
            } else if (bandVal.compareTo(bandArch.getTh3Max()) <= 0) {
                bandLevel = 2;
            } else {
                bandLevel = 3;
            }
        }
        record.setStatus(bandLevel);

        record.setDelFlag(0);
        return this.baseMapper.insert(record);
    }

    @Override
    public int updateTSwReliefStd(TSwReliefStd record) {
        if (record == null || record.getId() == null) {
            return 0;
        }

        if (record.getId() != null && StringUtils.isNotEmpty(record.getStdNo())) {
            Integer dupCnt = this.baseMapper.selectCount(new QueryWrapper<TSwReliefStd>()
                    .eq("std_no", record.getStdNo()).ne("id", record.getId()).eq("del_flag", 0));
            if (dupCnt != null && dupCnt > 0) {
                return 0;
            }
        }

        record.setUpdateTime(new Date());
        return this.baseMapper.update(record, new UpdateWrapper<TSwReliefStd>()
                .eq("id", record.getId())
                .eq("del_flag", 0));
    }

    @Override
    public int deleteTSwReliefStdByIds(String ids) {
        Long[] idArr = ConvertUtil.toLongArray(ids);
        return this.baseMapper.deleteBatchIds(Arrays.asList(idArr));
    }

    @Override
    public int deleteTSwReliefStdById(Long id) {
        return this.baseMapper.deleteById(id);
    }
}
