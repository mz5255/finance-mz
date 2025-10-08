package com.finance.data.entity.domian;

import com.finance.common.annotation.Excel;
import com.finance.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 运输财务数据对象
 *
 * @author finance
 * @date 2025-10-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FinanceData extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    @Excel(name = "唯一标识")
    private String id;

    /** 发货日期 */
    @Excel(name = "发货日期")
    private Date shippingDate;

    /** 发货方 */
    @Excel(name = "发货方")
    private String senderName;

    /** 发货方联系电话 */
    @Excel(name = "发货方联系电话")
    private String senderPhone;

    /** 装货地址 */
    @Excel(name = "装货地址")
    private String loadingAddress;

    /** 货物名称 */
    @Excel(name = "货物名称")
    private String cargoName;

    /** 吨位数 */
    @Excel(name = "吨位数")
    private BigDecimal weight;

    /** 支数 */
    @Excel(name = "支数")
    private Integer packagesCount;

    /** 件数 */
    @Excel(name = "件数")
    private Integer itemsCount;

    /** 卸货地址 */
    @Excel(name = "卸货地址")
    private String unloadingAddress;

    /** 卸货联系人姓名 */
    @Excel(name = "卸货联系人姓名")
    private String unloadingContactName;

    /** 卸货联系人电话 */
    @Excel(name = "卸货联系人电话")
    private String unloadingContactPhone;

    /** 司机姓名 */
    @Excel(name = "司机姓名")
    private String driverName;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String licensePlate;

    /** 司机联系电话 */
    @Excel(name = "司机联系电话")
    private String driverPhone;

    /** 中转地 */
    @Excel(name = "中转地")
    private String transferPoint;

    /** 中转费
     * 支出
     * */
    @Excel(name = "中转费")
    private BigDecimal transferFee;

    /** 信息费
     *  利润
     * */
    @Excel(name = "信息费")
    private BigDecimal infoFee;

    /** 发货信息费
     * 支出
     * */
    @Excel(name = "发货信息费")
    private BigDecimal shippingInfoFee;

    /** 开税
     * 支出
     * */
    @Excel(name = "开税")
    private BigDecimal tax;

    /** 到付
     * 支出
     * */
    @Excel(name = "到付")
    private BigDecimal collectOnDelivery;

    /** 返款
     * 支出
     * */
    @Excel(name = "返款")
    private BigDecimal refund;

    /** 管理费
     * 利润
     * */
    @Excel(name = "管理费")
    private BigDecimal managementFee;

    /** 吊车
     * 支出
     * */
    @Excel(name = "吊车")
    private BigDecimal craneFee;

    /** 高速口
     * 支出
     * */
    @Excel(name = "高速口")
    private BigDecimal highwayFee;

    /** 提货车费用
     * 支出
     * */
    @Excel(name = "提货车费用")
    private BigDecimal pickupFee;

    /** 油/气 的费用
     * 支出
     * */
    @Excel(name = "油/气 费用")
    private BigDecimal fuel;

    /** 车杂费
     * 支出
     * */
    @Excel(name = "车杂费")
    private BigDecimal carMiscFee;

    /** 车单价
     *
     * */
    @Excel(name = "车单价")
    private BigDecimal carUnitPrice;

    /** 车总价
     *  吨位*车单价 - 管理费-信息费-高速口-吊车-到付   =车总价
     * */
    @Excel(name = "车总价")
    private BigDecimal carTotalPrice;

    /** 接价单
     *  钢材元/吨
     * */
    @Excel(name = "接价单")
    private BigDecimal price;

    /** 接价总
     * 接价单*吨位
     * */
    @Excel(name = "接价总")
    private BigDecimal priceTotal;

    /** 结运费联系人 */
    @Excel(name = "结运费联系人")
    private String shipmentFeeContactName;

    /** 利
     * 接价总-车总价
     * */
    @Excel(name = "利润")
    private BigDecimal profit;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;



    public void buildSave(){
        this.calculatePrice();
        this.setIsDelete(0);
    }

    public void buildUpdate(){
        this.calculatePrice();
    }



    /**
     * 计算利润
     */
    public void calculatePrice(){
        // 计算车总价：吨位 * 车单价 - 管理费 - 信息费 - 高速口 - 吊车 - 到付
        this.carTotalPrice = this.weight.multiply(this.carUnitPrice)
                .subtract(this.managementFee)
                .subtract(this.infoFee)
                .subtract(this.highwayFee)
                .subtract(this.craneFee)
                .subtract(this.collectOnDelivery);
        // 计算接价总： 接价单*吨位
        this.priceTotal = this.weight.multiply(this.price);
        // 利润： 接价总-车总价 - 中转费 - 发货信息费 - 开税 - 车杂费 - 提货车费用 - 油/气 的费用
        this.profit = this.priceTotal.subtract(this.carTotalPrice)
                .subtract(this.transferFee)
                .subtract(this.shippingInfoFee)
                .subtract(this.tax)
                .subtract(this.carMiscFee)
                .subtract(this.pickupFee)
                .subtract(this.fuel);
    }
}
