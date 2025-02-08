package com.jzo2o.foundations.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.foundations.enums.FoundationHotEnum;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;

import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {

    @Resource
    private ServeItemMapper serveItemMapper;

    @Resource
    private RegionMapper regionMapper;

    /**
     * 分页查询
     *
     * @param servePageQueryReqDTO 查询条件
     * @return 分页结果
     */
    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO) {
        //通过baseMapper调用queryServeListByRegionId方法
        PageResult<ServeResDTO> serveResDTOPageResult = PageHelperUtils.selectPage(servePageQueryReqDTO,
            () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));
        return serveResDTOPageResult;
    }

    /**
     * 批量添加区域服务
     *
     * @param serveUpsertReqDTOList
     */
    @Override
    public void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOList) {
            // 校验服务项是否存在或者服务项是否启用
            Long serveItemId = serveUpsertReqDTO.getServeItemId();
            ServeItem serveItem = serveItemMapper.selectById(serveItemId);
            if (ObjectUtils.isNull(serveItem)) {
                throw new ForbiddenOperationException("服务项不存在");
            }
            if(serveItem.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
                throw new ForbiddenOperationException("服务项未启用不允许添加");
            }

            // 校验服务项是否已存在
            Integer count = lambdaQuery().eq(Serve::getServeItemId, serveUpsertReqDTO.getServeItemId())
                .eq(Serve::getRegionId, serveUpsertReqDTO.getRegionId()).count();
            if (count > 0) {
                throw new ForbiddenOperationException("服务项已存在");
            }

            //插入数据
            Serve serve = BeanUtils.toBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serve.getRegionId());
            String cityCode = region.getCityCode();
            serve.setCityCode(cityCode);
            baseMapper.insert(serve);
        }
    }

    /**
     * 修改区域服务价格
     *
     * @param id
     * @param price
     * @return
     */
    @Override
    public Serve update(Long id, BigDecimal price) {
        boolean update = lambdaUpdate().eq(Serve::getId, id).set(Serve::getPrice, price).update();
        if (!update) {
            throw new CommonException("修改服务价格失败");
        }
        return baseMapper.selectById(id);
    }

    /**
     * 上架区域服务
     *
     * @param id
     * @return
     */
    @Override
    public Serve onSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        //查询区域服务状态
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        if ( !(serve.getSaleStatus() == FoundationStatusEnum.DISABLE.getStatus() || serve.getSaleStatus() == FoundationStatusEnum.INIT.getStatus()) ) {
            throw new ForbiddenOperationException("草稿或下架状态方可上架");
        }

        //查询服务项状态
        ServeItem serveItem = serveItemMapper.selectById(serve.getServeItemId());
        if(ObjectUtil.isNull(serveItem)){
            throw new ForbiddenOperationException("所属服务项不存在");
        }
        if (!(FoundationStatusEnum.ENABLE.getStatus()==serveItem.getActiveStatus())) {
            throw new ForbiddenOperationException("服务项为启用状态方可上架");
        }

        boolean update = lambdaUpdate().eq(Serve::getId, id).set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus()).update();
        if(!update){
            throw new CommonException("启动服务失败");
        }
        return baseMapper.selectById(id);
    }

    /**
     * 下架区域服务
     *
     * @param id
     * @return
     */
    @Override
    public Serve offSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        if(serve.getSaleStatus() == FoundationStatusEnum.INIT.getStatus() || serve.getSaleStatus() == FoundationStatusEnum.DISABLE.getStatus()){
            throw new ForbiddenOperationException("服务已下架");
        }
        boolean update = lambdaUpdate().eq(Serve::getId, id).set(Serve::getSaleStatus, FoundationStatusEnum.DISABLE.getStatus()).update();
        if(!update){
            throw new CommonException("下架服务失败");
        }
        return baseMapper.selectById(id);
    }

    /**
     * 删除区域服务
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        if(serve.getSaleStatus() == FoundationStatusEnum.ENABLE.getStatus()){
            throw new ForbiddenOperationException("服务已上架，无法删除");
        }
        baseMapper.deleteById(id);
    }

    /**
     * 区域服务设置热门
     *
     * @param id
     * @return
     */
    @Override
    public Serve onHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        if(serve.getSaleStatus() == FoundationStatusEnum.DISABLE.getStatus() || serve.getSaleStatus() == FoundationStatusEnum.INIT.getStatus()){
            throw new ForbiddenOperationException("服务已下架或者服务为草稿状态，无法设置热门");
        }
        boolean update = lambdaUpdate().eq(Serve::getId, id).set(Serve::getIsHot, FoundationHotEnum.ON_HOT.getHot()).update();
        if(!update){
            throw new CommonException("设置热门失败");
        }
        return baseMapper.selectById(id);
    }

    /**
     * 区域服务取消热门
     *
     * @param id
     * @return
     */
    @Override
    public Serve offHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtil.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在");
        }
        if(serve.getSaleStatus() == FoundationStatusEnum.DISABLE.getStatus() || serve.getSaleStatus() == FoundationStatusEnum.INIT.getStatus()){
            throw new ForbiddenOperationException("服务已下架或者服务为草稿状态，无法取消热门");
        }
        boolean update = lambdaUpdate().eq(Serve::getId, id).set(Serve::getIsHot, FoundationHotEnum.OFF_HOT.getHot()).update();
        if(!update){
            throw new CommonException("取消热门失败");
        }
        return baseMapper.selectById(id);
    }

}