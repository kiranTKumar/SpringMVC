package com.development.bo;

import java.util.concurrent.atomic.AtomicReference;

import com.development.dao.impl.ProductDaoImpl;

class ProductCheck implements Runnable{
	private AtomicReference<Product> product= new AtomicReference<>();
	private AtomicReference<String> saleType= new AtomicReference<>();
	
	ProductDaoImpl prodDao;
	
	
	public ProductCheck(Product product, String saleType, ProductDaoImpl prodDao) {
		this.product.set(product);
		this.saleType.set(saleType);
		this.prodDao = prodDao;
	}
	
	@Override
	public void run() {
		prodDao.updateTheProductsAvailableCount(saleType.get(), product.get());
	}
}