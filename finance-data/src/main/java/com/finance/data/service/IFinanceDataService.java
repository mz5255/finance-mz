package com.finance.data.service;

import com.finance.data.entity.domian.FinanceData;

import java.util.List;

/**
 * Service接口
 * 
 * @author finance
 * @date 2025-10-06
 */
public interface IFinanceDataService 
{
    /**
     * 查询
     * 
     * @param id 主键
     * @return 
     */
    public FinanceData selectFinanceDataById(Long id);

    /**
     * 查询列表
     * 
     * @param financeData 
     * @return 集合
     */
    public List<FinanceData> selectFinanceDataList(FinanceData financeData);

    /**
     * 新增
     * 
     * @param financeData 
     * @return 结果
     */
    public int insertFinanceData(FinanceData financeData);

    /**
     * 修改
     * 
     * @param financeData 
     * @return 结果
     */
    public int updateFinanceData(FinanceData financeData);

    /**
     * 批量删除
     * 
     * @param ids 需要删除的主键集合
     * @return 结果
     */
    public int deleteFinanceDataByIds(List<String> ids);

    /**
     * 删除信息
     * 
     * @param id 主键
     * @return 结果
     */
    public int deleteFinanceDataById(String id);
}
