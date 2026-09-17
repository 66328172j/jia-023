package com.fc.v2.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.common.log.Log;
import com.fc.v2.model.auto.TSwReliefStd;
import com.fc.v2.service.ITSwReliefStdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 补偿费减免标准 Controller
 *
 * @author fuce
 * @date 2026-09-12
 */
@Api(value = "补偿费减免标准")
@Controller
@RequestMapping("/SwReliefStdController")
public class SwReliefStdController extends BaseController {

    private final String prefix = "admin/swReliefStd";

    @Autowired
    private ITSwReliefStdService swReliefStdService;

    @ApiOperation(value = "分页跳转", notes = "分页跳转")
    @GetMapping("/view")
    @RequiresPermissions("sw:swReliefStd:view")
    public String view(ModelMap model) {
        return prefix + "/list";
    }

    @Log(title = "补偿费减免标准集合查询", action = "list")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/list")
    @RequiresPermissions("sw:swReliefStd:list")
    @ResponseBody
    public ResultTable list(TSwReliefStd record) {
        QueryWrapper<TSwReliefStd> queryWrapper = new QueryWrapper<TSwReliefStd>();
        startPage();
        com.github.pagehelper.PageInfo<TSwReliefStd> page =
                new com.github.pagehelper.PageInfo<TSwReliefStd>(swReliefStdService.selectTSwReliefStdList(queryWrapper));
        return pageTable(page.getList(), page.getTotal());
    }

    @Log(title = "补偿费减免标准新增", action = "add")
    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    @RequiresPermissions("sw:swReliefStd:add")
    @ResponseBody
    public AjaxResult add(TSwReliefStd record) {
        return toAjax(swReliefStdService.insertTSwReliefStd(record));
    }

    @Log(title = "补偿费减免标准修改", action = "edit")
    @ApiOperation(value = "修改保存", notes = "修改保存")
    @PostMapping("/edit")
    @RequiresPermissions("sw:swReliefStd:edit")
    @ResponseBody
    public AjaxResult editSave(TSwReliefStd record) {
        return toAjax(swReliefStdService.updateTSwReliefStd(record));
    }

    @Log(title = "补偿费减免标准删除", action = "remove")
    @ApiOperation(value = "删除", notes = "删除")
    @DeleteMapping("/remove")
    @RequiresPermissions("sw:swReliefStd:remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(swReliefStdService.deleteTSwReliefStdByIds(ids));
    }
}
