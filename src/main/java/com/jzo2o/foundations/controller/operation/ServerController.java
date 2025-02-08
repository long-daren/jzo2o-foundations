package com.jzo2o.foundations.controller.operation;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author: yjy
 * @date: 2020/7/21
 * @describe: 区域服务管理相关接口
 */
@RestController("operationServerController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端-区域服务管理相关接口")
public class ServerController {

    @Resource
    private IServeService serveService;

    @GetMapping("/page")
    @ApiOperation("区域服务分页查询")
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO){
        return serveService.page(servePageQueryReqDTO);
    }

    @PostMapping("/batch")
    @ApiOperation("添加区域服务")
    public void add(@RequestBody List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        serveService.batchAdd(serveUpsertReqDTOList);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改区域服务价格")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
        @ApiImplicitParam(name = "price", value = "价格", required = true, dataTypeClass = BigDecimal.class)
    })
    public void update(@PathVariable("id") Long id,@RequestParam("price") BigDecimal price) {
        serveService.update(id, price);
    }
    
    @PutMapping("/onSale/{id}")
    @ApiOperation("区域服务上架")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void onSale(@PathVariable("id") Long id) {
        serveService.onSale(id);
    }

    @PutMapping("/offSale/{id}")
    @ApiOperation("区域服务下架")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void offSale(@PathVariable("id") Long id) {
        serveService.offSale(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除区域服务")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void delete(@PathVariable("id") Long id) {
        serveService.delete(id);
    }

    @PutMapping("/onHot/{id}")
    @ApiOperation("区域服务设置热门")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void onHot(@PathVariable("id") Long id) {
        serveService.onHot(id);
    }

    @PutMapping("/offHot/{id}")
    @ApiOperation("区域服务取消热门")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
    })
    public void offHot(@PathVariable("id") Long id) {
        serveService.offHot(id);
    }
}
