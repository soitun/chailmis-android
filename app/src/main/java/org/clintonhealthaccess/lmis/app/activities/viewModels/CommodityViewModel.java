package org.clintonhealthaccess.lmis.app.activities.viewmodels;

import org.clintonhealthaccess.lmis.app.models.Commodity;
import org.clintonhealthaccess.lmis.app.models.OrderReason;

import java.io.Serializable;
import java.util.Date;

public class CommodityViewModel extends BaseCommodityViewModel implements Serializable {
    private int quantityEntered;

    private int expectedOrderQuantity;
    private Integer orderReasonPosition;
    private Date orderPeriodStartDate, orderPeriodEndDate;
    private int unexpectedReasonPosition;
    private OrderReason reasonForOrder;
    private OrderReason reasonForUnexpectedOrderQuantity;

    public CommodityViewModel(Commodity commodity) {
        super(commodity);
    }

    public CommodityViewModel(Commodity commodity, int quantityEntered) {
        this(commodity);
        this.quantityEntered = quantityEntered;
    }

    public int getQuantityEntered() {
        return quantityEntered;
    }

    public void setQuantityEntered(int quantityEntered) {
        this.quantityEntered = quantityEntered;
    }


    public boolean quantityIsUnexpected() {
        return (quantityEntered > (1.1 * this.expectedOrderQuantity));
    }

    public int getExpectedOrderQuantity() {
        return expectedOrderQuantity;
    }

    public void setExpectedOrderQuantity(int expectedOrderQuantity) {
        this.expectedOrderQuantity = expectedOrderQuantity;
    }

    public Date getOrderPeriodEndDate() {
        return orderPeriodEndDate;
    }

    public void setOrderPeriodEndDate(Date orderPeriodEndDate) {
        this.orderPeriodEndDate = orderPeriodEndDate;
    }

    public Date getOrderPeriodStartDate() {
        return orderPeriodStartDate;
    }

    public void setOrderPeriodStartDate(Date orderPeriodStartDate) {
        this.orderPeriodStartDate = orderPeriodStartDate;
    }

    public Integer getOrderReasonPosition() {
        return orderReasonPosition;
    }

    public void setOrderReasonPosition(Integer orderReasonPosition) {
        this.orderReasonPosition = orderReasonPosition;
    }

    public int getUnexpectedReasonPosition() {
        return unexpectedReasonPosition;
    }

    public void setUnexpectedReasonPosition(int unexpectedReasonPosition) {
        this.unexpectedReasonPosition = unexpectedReasonPosition;
    }

    public OrderReason getReasonForOrder() {
        return reasonForOrder;
    }

    public void setReasonForOrder(OrderReason reasonForOrder) {
        this.reasonForOrder = reasonForOrder;
    }

    public OrderReason getReasonForUnexpectedOrderQuantity() {
        return reasonForUnexpectedOrderQuantity;
    }

    public void setReasonForUnexpectedOrderQuantity(OrderReason reasonForUnexpectedOrderQuantity) {
        this.reasonForUnexpectedOrderQuantity = reasonForUnexpectedOrderQuantity;
    }

    public boolean isValidAsOrderItem() {
        return orderPeriodEndDate != null && orderPeriodStartDate != null && quantityEntered > 0;
    }
}
