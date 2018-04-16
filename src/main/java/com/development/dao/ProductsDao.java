package com.development.dao;

import com.development.bo.Product;

public interface ProductsDao {

	String addProductToInventory(Product saveProduct);

	String findTheProductInInventory(Product searchProduct);
	
	boolean updateTheProductsAvailableCount(String saleType, Product  product);

}
