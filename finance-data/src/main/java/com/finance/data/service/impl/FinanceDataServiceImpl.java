package com.finance.data.service.impl;

import com.finance.data.entity.domian.FinanceData;
import com.finance.data.mapper.FinanceDataMapper;
import com.finance.data.service.IFinanceDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service业务层处理
 * 
 * @author finance
 * @date 2025-10-06
 */
@Service
public class FinanceDataServiceImpl implements IFinanceDataService
{
    @Resource
    private FinanceDataMapper financeDataMapper;

    /**
     * 查询
     * 
     * @param id 
     * @return
     */
    @Override
    public FinanceData selectFinanceDataById(Long id)
    {
        return financeDataMapper.selectById(id);
    }

    /**
     * 查询列表
     * 
     * @param financeData 
     * @return 
     */
    @Override
    public List<FinanceData> selectFinanceDataList(FinanceData financeData)
    {
        return financeDataMapper.selectFinanceDataList(financeData);
    }

    /**
     * 新增
     * 
     * @param financeData 
     * @return 结果
     */
    @Override
    public int insertFinanceData(FinanceData financeData)
    {
        financeData.buildSave();
        return financeDataMapper.insert(financeData);
    }

    /**
     * 修改
     * 
     * @param financeData 
     * @return 结果
     */
    @Override
    public int updateFinanceData(FinanceData financeData)
    {
        financeData.buildUpdate();
        return financeDataMapper.updateById(financeData);
    }

    /**
     * 批量删除
     * 
     * @param ids 需要删除的主键
     * @return 结果
     */
    @Override
    public int deleteFinanceDataByIds(List<String> ids)
    {
        return financeDataMapper.deleteBatchIds(ids);
    }

    /**
     * 删除信息
     * 
     * @param id 主键
     * @return 结果
     */
    @Override
    public int deleteFinanceDataById(String id)
    {
        return financeDataMapper.deleteById(id);
    }
}
