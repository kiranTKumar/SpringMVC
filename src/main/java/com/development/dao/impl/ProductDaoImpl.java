package com.development.dao.impl;

import com.development.bo.Product;
import com.development.bo.ProductsInventory;
import com.development.dao.ProductsDao;

public class ProductDaoImpl implements ProductsDao {
	
	ProductsInventory prodInventory;
	
	public void setProdInventory(ProductsInventory prodInventory) {
		this.prodInventory = prodInventory;
	}

	@Override
	public String addProductToInventory(Product inputProduct) {
		return prodInventory.addProductToInventory(inputProduct);
	}

	@Override
	public String findTheProductInInventory(Product searchProd) {
		return prodInventory.fetchProductValue(searchProd);
	}

	@Override
	/*
	 *return if it is sold we will decrement count and return true else increment and return true
	 *saleType is a String which can be sold or return 
	 *
	 */
	public boolean updateTheProductsAvailableCount(String saleType, Product product) {
		return prodInventory.adjustInventoryOfAProduct(saleType, product);
	}
	
	
}
