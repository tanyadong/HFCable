package com.hbhongfei.hfcable.pojo;

/**
 * 颜色、规格属性
 * shitilei
 */
public class SkuItme {
	private String id;//id
	private String skuSpecifications;//规格
	private String skuColor;//颜色
	private int skuStock;//库存
	private String skuIamgeUrl;//图片路径
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	public String getSkuSpecifications() {
		return skuSpecifications;
	}

	public void setSkuSpecifications(String skuSpecifications) {
		this.skuSpecifications = skuSpecifications;
	}

	public String getSkuColor() {
		return skuColor;
	}
	public void setSkuColor(String skuColor) {
		this.skuColor = skuColor;
	}
	public int getSkuStock() {
		return skuStock;
	}
	public void setSkuStock(int skuStock) {
		this.skuStock = skuStock;
	}
	public String getSkuIamgeUrl() {
		return skuIamgeUrl;
	}
	public void setSkuIamgeUrl(String skuIamgeUrl) {
		this.skuIamgeUrl = skuIamgeUrl;
	}
	
	
	
	
	
	

}
