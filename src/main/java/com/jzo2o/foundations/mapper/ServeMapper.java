package com.jzo2o.foundations.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.api.foundations.dto.response.ServeAggregationResDTO;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
public interface ServeMapper extends BaseMapper<Serve> {

    /*
     * 根据区域id查询服务列表
     */
    List<ServeResDTO> queryServeListByRegionId(@Param("regionId") Long regionId);

    /**
     * 根据区域id和状态查询服务列表
     * @param regionId
     * @param status
     * @return
     */
    List<ServeResDTO> queryServeListByRegionIdAndStatus(@Param("regionId") Long regionId, @Param("status") Integer status);

    /**
     * 根据服务项id和状态查询服务列表
     * @param serveItemId
     * @param status
     * @return
     */
    List<ServeResDTO> queryServeListByServeItemIdAndStatus(@Param("serveItemId") Long serveItemId, @Param("status") Integer status);


}
