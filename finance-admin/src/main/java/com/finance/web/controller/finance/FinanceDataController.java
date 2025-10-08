package com.finance.web.controller.finance;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.finance.data.entity.domian.FinanceData;
import com.finance.data.service.IFinanceDataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.finance.common.annotation.Log;
import com.finance.common.core.controller.BaseController;
import com.finance.common.core.domain.AjaxResult;
import com.finance.common.enums.BusinessType;
import com.finance.common.utils.poi.ExcelUtil;
import com.finance.common.core.page.TableDataInfo;

/**
 * Controller
 * 
 * @author finance
 * @date 2025-10-06
 */
@RestController
@RequestMapping("/finance/data")
public class FinanceDataController extends BaseController
{
    @Resource
    private IFinanceDataService financeDataService;

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('finance:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinanceData financeData)
    {
        startPage();
        List<FinanceData> list = financeDataService.selectFinanceDataList(financeData);
        return getDataTable(list);
    }

    /**
     * 导出列表
     */
    @PreAuthorize("@ss.hasPermi('finance:data:export')")
    @Log(title = "", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinanceData financeData)
    {
        List<FinanceData> list = financeDataService.selectFinanceDataList(financeData);
        ExcelUtil<FinanceData> util = new ExcelUtil<FinanceData>(FinanceData.class);
        util.exportExcel(response, list, "数据");
    }

    /**
     * 获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('finance:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(financeDataService.selectFinanceDataById(id));
    }

    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('finance:data:add')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FinanceData financeData)
    {
        return toAjax(financeDataService.insertFinanceData(financeData));
    }

    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('finance:data:edit')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FinanceData financeData)
    {
        return toAjax(financeDataService.updateFinanceData(financeData));
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('finance:data:remove')")
    @Log(title = "删除", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable List<String> ids)
    {
        return toAjax(financeDataService.deleteFinanceDataByIds(ids));
    }
}
