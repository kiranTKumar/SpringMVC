package com.development.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProductDTO implements Serializable, Cloneable{
	private static final long serialVersionUID = -5719078645678019580L;

	private Long productCode;
	private String productName;
	private String productCategoryName;
	private Float price;
	private Integer avaiableQuntity;
	
	public ProductDTO() {
	}

	public ProductDTO(Long productCode, String productName, String productCategoryName, Float price, Integer avaiableQuntity) {
		this.productCode = productCode;
		this.productName = productName;
		this.productCategoryName = productCategoryName;
		this.price = price;
		this.avaiableQuntity = avaiableQuntity;
	}

	public Long getProductCode() {
		return productCode;
	}

	public void setProductId(Long productId) {
		this.productCode = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getAvaiableQuntity() {
		return avaiableQuntity;
	}

	public void setAvaiableQuntity(Integer avaiableQuntity) {
		this.avaiableQuntity = avaiableQuntity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPrice());
	}

	@Override
	public boolean equals(Object obj) {
		ProductDTO comparingProduct = (ProductDTO) obj;
		if(comparingProduct==null)
			return false;
		if(this == comparingProduct)
			return false;
		return getProductCode().equals(comparingProduct.getProductCode())
				&& getProductName().equals(comparingProduct.getProductName())
				&& getProductCategoryName().equals(comparingProduct.getProductCategoryName());
	}

	@Override
	public String toString() {
		return "Product [productCode=" + productCode + ", productName=" + productName + ", ProductCategoryName="
				+ productCategoryName + ", price=" + price + ", avaiableQuntity=" + avaiableQuntity + "]";
	}
	
	@Override
	public ProductDTO clone() throws CloneNotSupportedException {
		return (ProductDTO) super.clone();
	}

}
