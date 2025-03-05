package com.jzo2o.foundations.controller.consumer;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jzo2o.foundations.model.dto.request.ServeTypeListDto;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.service.HomeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController("consumerServeController")
@RequestMapping("/customer/serve")
@Api(tags = "用户端 - 首页服务查询接口")
public class FirstPageServeController {

    @Resource
    private HomeService homeService;
    @GetMapping("/firstPageServeList")
    @ApiOperation("首页服务列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeCategoryResDTO> serveCategory(@RequestParam("regionId") Long regionId) {
        return homeService.queryServeIconCategoryByRegionIdCache(regionId);
    }


    @GetMapping("/serveTypeList")
    @ApiOperation("服务类型列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeTypeListDto> serveTypeList(@RequestParam("regionId") Long regionId) {
        List<ServeTypeListDto> serveTypeList = homeService.queryServeTypeByRegionIdCache(regionId);
        return serveTypeList;
    }

    @GetMapping("/hotServeList")
    @ApiOperation("热门服务列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeAggregationSimpleResDTO> hotServeList(@RequestParam("regionId") Long regionId) {
        List<ServeAggregationSimpleResDTO> serveCategoryResDTOS = homeService.queryHotServeListByRegionId(regionId);
        return serveCategoryResDTOS;
    }


    @GetMapping("/{id}")
    @ApiOperation("服务详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeAggregationSimpleResDTO> serveDetail(@PathVariable("id") Long id) {
        List<ServeAggregationSimpleResDTO> serveCategoryResDTOS = homeService.queryServeDetail(id);
        return serveCategoryResDTOS;
    }



}
