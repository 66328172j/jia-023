package com.fc.v2.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fc.v2.model.auto.TSwReliefStd;

import java.util.List;

/**
 * 补偿费减免标准 Service接口
 *
 * @author fuce
 * @date 2026-09-12
 */
public interface ITSwReliefStdService {

    /** 按主键查询 */
    TSwReliefStd selectTSwReliefStdById(Long id);

    /** 按条件查询列表（分页由调用方统一处理） */
    List<TSwReliefStd> selectTSwReliefStdList(Wrapper<TSwReliefStd> queryWrapper);

    /** 新增 */
    int insertTSwReliefStd(TSwReliefStd record);

    /** 修改 */
    int updateTSwReliefStd(TSwReliefStd record);

    /** 批量删除 */
    int deleteTSwReliefStdByIds(String ids);

    /** 按主键删除 */
    int deleteTSwReliefStdById(Long id);
}
