package com.jzo2o.foundations.handler;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.service.HomeService;
import com.jzo2o.foundations.service.IRegionService;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SpringCacheSyncHandler {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private IRegionService regionService;
    @Resource
    private HomeService homeService;

    @XxlJob(value = "activeRegionCacheSync")
    public void activeRegionCacheSync() {
        log.info(">>>>>>>>开始进行缓存同步，更新已启用区域");
        String key = RedisConstants.CacheName.JZ_CACHE + "::ACTIVE_REGIONS";
        redisTemplate.delete(key);
        List<RegionSimpleResDTO> regionSimpleResDTOS = regionService.queryActiveRegionListCache();
        regionSimpleResDTOS.forEach(regionSimpleResDTO -> {
            //删除该区域下的首页服务列表
            String key1 = RedisConstants.CacheName.SERVE_ICON + "::" + regionSimpleResDTO.getId();
            redisTemplate.delete(key1);
            homeService.queryServeIconCategoryByRegionIdCache(regionSimpleResDTO.getId());
            //删除该区域下的服务类型列表缓存
            String key2 = RedisConstants.CacheName.SERVE_TYPE + "::" + regionSimpleResDTO.getId();
            redisTemplate.delete(key2);
            homeService.queryServeTypeByRegionIdCache(regionSimpleResDTO.getId());
            //删除该区域下的热门服务列表缓存
            String hot_serve_key = RedisConstants.CacheName.HOT_SERVE + "::" + regionSimpleResDTO.getId();
            redisTemplate.delete(hot_serve_key);
            homeService.queryHotServeListByRegionId(regionSimpleResDTO.getId());
        });
        log.info(">>>>>>>>更新已启用区域完成");
    }
}
