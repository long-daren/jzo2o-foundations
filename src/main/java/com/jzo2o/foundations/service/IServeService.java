package com.jzo2o.foundations.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
public interface IServeService extends IService<Serve> {

    /**
     * 分页查询服务列表
     * @param servePageQueryReqDTO 查询条件
     * @return 分页结果
     */
    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);

    /**
     * 批量添加区域服务
     * @param serveUpsertReqDTOList
     */
    void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    /**
     * 修改区域服务价格
     * @param id
     * @param price
     * @return
     */
    Serve update(Long id, BigDecimal price);

    /**
     * 上架区域服务
     * @param id
     * @return
     */
    Serve onSale(Long id);

    /**
     * 下架区域服务
     * @param id
     * @return
     */
    Serve offSale(Long id);

    /**
     * 删除区域服务
     * @param id
     */
    void delete(Long id);

    /**
     * 区域服务设置热门
     * @param id
     * @return
     */
    Serve onHot(Long id);

    /**
     * 区域服务取消热门
     * @param id
     * @return
     */
    Serve offHot(Long id);
}