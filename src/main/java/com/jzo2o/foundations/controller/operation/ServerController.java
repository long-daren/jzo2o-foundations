package com.jzo2o.foundations.controller.operation;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;

import io.swagger.annotations.Api;
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
}
