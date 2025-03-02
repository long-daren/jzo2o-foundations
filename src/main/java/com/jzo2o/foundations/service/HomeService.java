package com.jzo2o.foundations.service;

import java.util.List;

import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;

public interface HomeService {

    /**
     * 根据区域id获取服务图标信息
     *
     * @param regionId 区域id
     * @return 服务图标列表
     */
    List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId);
}
