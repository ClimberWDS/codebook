package com.wds.codebook.sharding.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wds.codebook.common.core.model.BaseEntity;
import com.wds.codebook.common.core.util.GenerateUtils;
import com.wds.codebook.common.core.util.JacksonUtils;
import com.wds.codebook.order.api.dto.goods.resp.GoodsSkuDto;
import com.wds.codebook.order.api.dto.order.resp.OrderBaseDto;
import com.wds.codebook.order.api.dto.order.resp.OrderDetailDto;
import com.wds.codebook.order.api.enums.GoodsEnum;
import com.wds.codebook.order.api.enums.OrderEnum;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("od_order")
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {

    /**
     * 订单编码
     */
    private String orderCode;

    /**
     * 用户uid
     */
    private Integer uid;

    /**
     * 支付币种
     */
    private String payCoin;

    /**
     * 支付币种类型
     */
    private Integer payCoinType;

    /**
     * 总价
     */
    private BigDecimal totalAmount;

    /**
     * 应付总价
     */
    private BigDecimal amount;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * cid 多个以英文逗号","分隔
     */
    private String cid;

    /**
     * 收货方式 0-邮寄 1自提
     */
    private Integer deliveryType;

    /**
     * 激活时间
     */
    private Long activeTime;

    /**
     * 付款时间
     */
    private Long payTime;

    /**
     * 完成时间
     */
    private Long completeTime;

    /**
     * 订单类型 同商品类型
     */
    private Integer type;

    /**
     * 订单状态 0-待付款 1-已付款(邮寄-待发货，自提-待领取) 2-已完成 3-已取消
     *
     * @see OrderEnum.OrderStatusEnum
     */
    private Integer status;

    /**
     * 物流/自提记录状态 同物流记录表/自提记录表status
     */
    private Integer recordStatus;

    /**
     * 附加内容
     */
    private String additionalJson;

    @TableField(exist = false)
    private long qty;

    /**
     * 查询订单详情
     *
     * @param orderDetailList
     * @return
     */
    public OrderBaseDto convert(List<OrderDetail> orderDetailList, Map<Long, List<GoodsSkuDto>> goodsMap) {
        OrderBaseDto orderBaseDto = new OrderBaseDto();
        BeanUtils.copyProperties(this, orderBaseDto);
        if (CollectionUtils.isNotEmpty(orderDetailList)) {
            List<OrderDetailDto> details = orderDetailList.stream().map(orderDetail -> {
                GoodsSkuDto goodsSkuDto = null;
                if (MapUtils.isNotEmpty(goodsMap)) {
                    List<GoodsSkuDto> goodsList = goodsMap.get(orderDetail.getGoodsId());
                    goodsSkuDto = CollectionUtils.isEmpty(goodsList) ? null : goodsList.get(0);
                }
                return orderDetail.convert(goodsSkuDto);
            }).collect(Collectors.toList());
            //todo @wds 联调日志待删除
            log.debug("查询订单详情：{}", JacksonUtils.toJson(details));
            orderBaseDto.setDetails(details);
        }
        return orderBaseDto;
    }

    public OrderBaseDto convert(List<OrderDetail> orderDetailList) {
        return convert(orderDetailList, null);
    }

    public Order(Integer accountType) {
        this.orderCode = GenerateUtils.genOrderCode(accountType, OrderEnum.OrderTypeEnum.SIM_CARD.code);
        this.status = OrderEnum.OrderStatusEnum.NO_PAY.code;
        this.recordStatus = OrderEnum.OrderExpressStatusEnum.NO_PAY.code;
    }


    /**
     * 是否需要支付
     * 调用账户服务
     */
    public boolean needPay() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 是否为自提订单
     *
     * @return
     */
    public boolean isSelfPickup() {
        return OrderEnum.DeliveryTypeEnum.SELF_PICKUP.code.equals(deliveryType);
    }

    /**
     * 是否为发货订单
     *
     * @return
     */
    public boolean isExpress() {
        return OrderEnum.DeliveryTypeEnum.EXPRESS.code.equals(deliveryType);
    }

    /**
     * 订单支付 状态变更
     *
     * @return
     */
    public Order pay() {
        this.status = OrderEnum.OrderStatusEnum.PAID.code;
        this.recordStatus = isExpress() ? OrderEnum.OrderExpressStatusEnum.UN_SEND.code
                : OrderEnum.OrderSelfPickipStatusEnum.UN_RECEIVED.getCode();
        this.payTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 订单完成 状态变更
     *
     * @return
     */
    public Order complete(String cid) {
        this.cid = cid;
        this.status = OrderEnum.OrderStatusEnum.COMPLETE.code;
        this.recordStatus = isExpress() ? OrderEnum.OrderExpressStatusEnum.SENT.code
                : OrderEnum.OrderSelfPickipStatusEnum.RECEIVED.getCode();
        this.completeTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 订单取消 状态变更
     *
     * @return
     */
    public Order cancel() {
        this.status = OrderEnum.OrderStatusEnum.CANCEL.code;
        this.recordStatus = isExpress() ? OrderEnum.OrderExpressStatusEnum.CANCEL.code
                : OrderEnum.OrderSelfPickipStatusEnum.CANCEL.getCode();
        return this;
    }

    /**
     * 订单自动取消 状态变更
     *
     * @return
     */
    public Order autoCancel(String remark) {
        this.status = OrderEnum.OrderStatusEnum.CANCEL.code;
        this.recordStatus = isExpress() ? OrderEnum.OrderExpressStatusEnum.CANCEL.code
                : OrderEnum.OrderSelfPickipStatusEnum.CANCEL.getCode();
        this.additionalJson = StringUtils.isBlank(remark) ? "订单过期,自动取消" : remark;
        return this;
    }

    /**
     * 订单确认 状态变更
     *
     * @return
     */
    public Order confirm() {
        this.status = OrderEnum.OrderStatusEnum.COMPLETE.code;
        this.recordStatus = isExpress() ? OrderEnum.OrderExpressStatusEnum.CONFIRM.code
                : OrderEnum.OrderSelfPickipStatusEnum.RECEIVED.getCode();
        this.completeTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 订单确认 状态变更
     *
     * @return
     */
    public Order autoConfirm(String remark) {
        this.status = OrderEnum.OrderStatusEnum.COMPLETE.code;
        this.recordStatus = isExpress() ? OrderEnum.OrderExpressStatusEnum.CONFIRM.code
                : OrderEnum.OrderSelfPickipStatusEnum.RECEIVED.getCode();
        this.completeTime = System.currentTimeMillis();
        this.additionalJson = StringUtils.isBlank(remark) ? "自动确认订单" : remark;
        return this;
    }

    /**
     * 订单允许取消
     *
     * @return
     */
    public boolean allowCancel() {
        if (status.equals(OrderEnum.OrderStatusEnum.NO_PAY.code)) {
            return true;
        } else if (status.equals(OrderEnum.OrderStatusEnum.PAID.code)) {
            if (type.equals(GoodsEnum.GoodsTypeEnum.USAGE.getCode())) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
