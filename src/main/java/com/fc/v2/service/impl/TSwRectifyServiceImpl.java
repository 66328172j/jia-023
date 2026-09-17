package com.fc.v2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.mapper.auto.TSwRectifyMapper;
import com.fc.v2.model.auto.TSwRectify;
import com.fc.v2.service.ITSwRectifyService;

/**
 * 侵蚀异常整改单 Service业务层处理（state-machine 形状：单据流转）
 *
 * @author fuce
 * @date 2026-09-14
 */
@Service
public class TSwRectifyServiceImpl implements ITSwRectifyService {

    private static final int MAX_STAGE = 3;
    private static final int STATUS_ACTIVE = 1;
    private static final int STATUS_TERMINAL = 2;

    @javax.annotation.Resource
    private TSwRectifyMapper swRectifyMapper;

    @Override
    public TSwRectify selectTSwRectifyById(Long id) {
        return this.swRectifyMapper.selectById(id);
    }

    @Override
    public List<TSwRectify> selectTSwRectifyList(QueryWrapper<TSwRectify> queryWrapper) {
        return this.swRectifyMapper.selectList(queryWrapper);
    }

    @Override
    public TSwRectify advance(Long id, String remark) {
        TSwRectify r = this.swRectifyMapper.selectById(id);
        if (r == null) {
            return null;
        }
        int st = r.getStage() == null ? 0 : r.getStage();
        r.setStage(Math.min(st + 2, MAX_STAGE));
        r.setStatus(STATUS_ACTIVE);
        r.setLastAction(remark);
        this.swRectifyMapper.updateById(r);
        return r;
    }

    @Override
    public TSwRectify rollback(Long id, String remark) {
        TSwRectify r = this.swRectifyMapper.selectById(id);
        if (r == null) {
            return null;
        }
        r.setStage(0);
        r.setStatus(STATUS_ACTIVE);
        r.setLastAction(remark);
        this.swRectifyMapper.updateById(r);
        return r;
    }

    @Override
    public boolean updateContent(Long id, String remark) {
        TSwRectify r = this.swRectifyMapper.selectById(id);
        if (r == null) {
            return false;
        }
        r.setContent(remark);
        return this.swRectifyMapper.updateById(r) > 0;
    }

    @Override
    public boolean remove(Long id) {
        TSwRectify r = this.swRectifyMapper.selectById(id);
        if (r == null) {
            return false;
        }
        return this.swRectifyMapper.deleteById(id) > 0;
    }

}
