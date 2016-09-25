package com.hbhongfei.hfcable.pojo;

import java.io.Serializable;
import java.util.Date;


/**
 * 订单表
 */

public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String orderNumber;	//订单编号
	public int tag; //是否购买   1表示未购买，2表示购买
	public int shipOrNot;//是否发货 1表示发货 2表示未发货
	public ShoppingCart shoppingCart; //购物车
	public Double money;//订单总金额

	public int deleteOrNot;//是否删除 0表示未删除，1表示已经删除



	//yxy
	public int completeOrNot;//此订单是否完成，0表示未完成，1表示已完成
	public Date orderTime;//下单时间
	public Date cancleTime;//取消订单时间
	public int cancleOrNot;//是否取消订单，0表示未取消，1表示已经取消
	public ShoppingAddress shoppingAddress;// 收获地址
	public Logistics logistics;//物流信息
	public Order() {
		super();
	}
	public Order(String orderNumber, int tag, int shipOrNot,
			ShoppingCart shoppingCart,Double money) {
		super();
		this.orderNumber = orderNumber;
		this.tag = tag;
		this.shipOrNot = shipOrNot;
		this.shoppingCart = shoppingCart;
		this.money=money;
	}





	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	


	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}
	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}
	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
	//是否发货
	public int getShipOrNot() {
		return shipOrNot;
	}
	public void setShipOrNot(int shipOrNot) {
		this.shipOrNot = shipOrNot;
	}
	
	//订单金额
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", orderNumber=" + orderNumber + ", tag="
				+ tag + ", shipOrNot=" + shipOrNot + ", shoppingCart="
				+ shoppingCart + ", money=" + money + "]";
	}
	
	


	
	
	
}
