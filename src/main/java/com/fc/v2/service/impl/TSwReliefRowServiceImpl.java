package com.fc.v2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.mapper.auto.TSwReliefRowMapper;
import com.fc.v2.model.auto.TSwReliefRow;
import com.fc.v2.service.ITSwReliefRowService;

/**
 * 补偿费减免明细 Service业务层处理（batch-process 形状：整批提交）
 *
 * @author fuce
 * @date 2026-09-14
 */
@Service
public class TSwReliefRowServiceImpl implements ITSwReliefRowService {

    private static final int MAX_ROWS = 500;
    private static final int STATUS_OK = 1;
    private static final int STATUS_FAIL = 2;

    @javax.annotation.Resource
    private TSwReliefRowMapper swReliefRowMapper;

    @Override
    public TSwReliefRow selectTSwReliefRowById(Long id) {
        return this.swReliefRowMapper.selectById(id);
    }

    @Override
    public int submitBatch(String batchNo, List<TSwReliefRow> rows) {
        String no = rows.get(0).getBatchNo();
        java.util.List<TSwReliefRow> errors = new java.util.ArrayList<TSwReliefRow>();
        int seq = 0;
        for (TSwReliefRow r : rows) {
            if (r.getItemCode() == null || r.getItemCode().trim().isEmpty()
                    || r.getQty() == null
                    || r.getQty().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                seq++;
                r.setRowNo(Integer.valueOf(seq));
                r.setBatchNo(no);
                r.setStatus(STATUS_FAIL);
                this.swReliefRowMapper.insert(r);
                errors.add(r);
            }
        }
        if (!errors.isEmpty()) {
            return 0;
        }
        int ok = 0;
        for (TSwReliefRow r : rows) {
            r.setBatchNo(no);
            r.setStatus(STATUS_OK);
            this.swReliefRowMapper.insert(r);
            ok++;
        }
        return ok;
    }

    @Override
    public List<TSwReliefRow> listErrors(String batchNo) {
        return this.swReliefRowMapper.selectList(new QueryWrapper<TSwReliefRow>()
                .eq("batch_no", batchNo).eq("status", STATUS_FAIL));
    }
}
