package com.jzo2o.foundations.service;

import java.util.List;

import com.jzo2o.foundations.model.dto.request.ServeTypeListDto;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;

public interface HomeService {

    /**
     * 根据区域id获取服务图标信息
     *
     * @param regionId 区域id
     * @return 服务图标列表
     */
    List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId);

    /**
     * 根据区域id查询服务类型
     * @param regionId
     * @return
     */
    List<ServeTypeListDto> queryServeTypeByRegionIdCache(Long regionId);

    /**
     * 根据区域id查询热门服务
     * @param regionId
     * @return
     */
    List<ServeAggregationSimpleResDTO> queryHotServeListByRegionId(Long regionId);

    /**
     * 根据服务id查询服务详情
     * @param id
     * @return
     */
    List<ServeAggregationSimpleResDTO> queryServeDetail(Long id);


}
