package com.jzo2o.foundations.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.mapper.ServeTypeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.dto.request.ServeTypeListDto;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;
import com.jzo2o.foundations.service.HomeService;
import com.jzo2o.foundations.service.IRegionService;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {


    @Resource
    private ServeMapper serveMapper;

    @Resource
    private IRegionService regionService;

    @Resource
    private ServeTypeMapper serveTypeMapper;
    /**
     * 根据区域id获取服务图标信息
     *
     * @param regionId 区域id
     * @return 服务图标列表
     */
    @Caching(
        cacheable = {
            @Cacheable(value = RedisConstants.CacheName.SERVE_ICON,key = "#regionId",unless = "#result.size() != 0",
                cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
            @Cacheable(value = RedisConstants.CacheName.SERVE_ICON,key = "#regionId",unless = "#result.size() == 0",
                cacheManager = RedisConstants.CacheManager.FOREVER)
        }
    )
    @Override
    public List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId) {
        //1.校验当前城市是否为启用状态
        Region region = regionService.getById(regionId);
        if (ObjectUtil.isNull(region) || ObjectUtil.equal(FoundationStatusEnum.DISABLE.getStatus(), region.getActiveStatus())) {
            return Collections.emptyList();
        }
        //2.根据城市编码查询所有的服务图标
        List<ServeCategoryResDTO> list = serveMapper.findServeIconCategoryByRegionId(regionId);
        if (ObjectUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        //3.服务类型取前两个，每个类型下服务项取前4个
        int endIndex = list.size() >= 2 ? 2 : list.size();
        List<ServeCategoryResDTO> serveCategoryResDTOS = new ArrayList<>(list.subList(0, endIndex));
        serveCategoryResDTOS.forEach(serveCategoryResDTO -> {
            List<ServeSimpleResDTO> serveResDTOList = serveCategoryResDTO.getServeResDTOList();
            int endIndex2 = serveResDTOList.size() >= 4 ? 4 : serveResDTOList.size();
            List<ServeSimpleResDTO> serveSimpleResDTOS = new ArrayList<>(serveResDTOList.subList(0, endIndex2));
            serveCategoryResDTO.setServeResDTOList(serveSimpleResDTOS);
        });
        return serveCategoryResDTOS;
    }

    /**
     * 根据区域id查询服务类型
     *
     * @param regionId
     * @return
     */
    @Caching(
        cacheable = {
            //result为null时,属于缓存穿透情况，缓存时间30分钟
            @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
            //result不为null时,永久缓存
            @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
        }
    )
    @Override
    public List<ServeTypeListDto> queryServeTypeByRegionIdCache(Long regionId) {
        //1.校验当前城市是否为启用状态
        Region region = regionService.getById(regionId);
        if (ObjectUtil.isNull(region) || ObjectUtil.equal(FoundationStatusEnum.DISABLE.getStatus(), region.getActiveStatus())) {
            return Collections.emptyList();
        }

        List<ServeTypeListDto> list = serveTypeMapper.findServeTypeByRegionId(regionId);
        if (ObjectUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    /**
     * 根据区域id查询热门服务
     *
     * @param regionId
     * @return
     */
    @Caching(
        cacheable = {
            @Cacheable(value = RedisConstants.CacheName.HOT_SERVE,key = "#regionId",unless = "#result.size() != 0",
                cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
            @Cacheable(value = RedisConstants.CacheName.HOT_SERVE,key = "#regionId",unless = "#result.size() == 0",
                cacheManager = RedisConstants.CacheManager.FOREVER)
        }
    )
    @Override
    public List<ServeAggregationSimpleResDTO> queryHotServeListByRegionId(Long regionId) {
        //1.校验当前城市是否为启用状态
        Region region = regionService.getById(regionId);
        if (ObjectUtil.isNull(region) || ObjectUtil.equal(FoundationStatusEnum.DISABLE.getStatus(), region.getActiveStatus())) {
            return Collections.emptyList();
        }
        //2.根据城市编码查询所有的热门服务
        List<ServeAggregationSimpleResDTO> serveAggregationSimpleResDTOS = serveMapper.queryHotServeListByRegionId(regionId);
        if (ObjectUtil.isEmpty(serveAggregationSimpleResDTOS)) {
            return Collections.emptyList();
        }
        return serveAggregationSimpleResDTOS;
    }

    /**
     * 根据服务id查询服务详情
     *
     * @param id
     * @return
     */
    @Caching(
        cacheable = {
            @Cacheable(value = RedisConstants.CacheName.SERVE,key = "#id",unless = "#result.size() != 0",
                cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
            @Cacheable(value = RedisConstants.CacheName.SERVE,key = "#id",unless = "#result.size() == 0",
                cacheManager = RedisConstants.CacheManager.FOREVER)
        }
    )
    @Override
    public List<ServeAggregationSimpleResDTO> queryServeDetail(Long id) {
        List<ServeAggregationSimpleResDTO> serveAggregationSimpleResDTOS = serveMapper.queryServeDetailById(id);
        return serveAggregationSimpleResDTOS;
    }
}
