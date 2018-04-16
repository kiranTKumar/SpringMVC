package com.development.bo;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProductsInventory {
	//public static ThreadLocal<HashMap<Product, String>> INVENTORY_DETAILS;
    public static HashMap<Product, String> INVENTORY_DETAILS = new HashMap<>();
    Lock insertLock = new ReentrantLock();
    Lock updateLock = new ReentrantLock();
	
	/*public ProductsInventory() {
		INVENTORY_DETAILS= new ThreadLocal<HashMap<Product, String>>();
	}*/	
	public String addProductToInventory(Product inputProd){
		AtomicReference<String> keysValue= new AtomicReference<>();
		insertLock.lock();
		keysValue.set(INVENTORY_DETAILS.put(inputProd, inputProd.getProductName()
				.concat(inputProd.getProductCategoryName())
				.concat(inputProd.getPrice().toString())
				));
		insertLock.unlock();
		return keysValue.get();
	}
	
	public String fetchProductValue(Product searchProd){
		return INVENTORY_DETAILS.get(searchProd);
	}
	
	public int sizeOfCollection(){
		return INVENTORY_DETAILS.size();
	}
	
	public Optional<Entry<Product,String>> entryOfCollectionByKey(Product entryKey){
		return INVENTORY_DETAILS.entrySet().stream().filter(e ->e.getKey().equals(entryKey)).findAny();
	}

	public boolean adjustInventoryOfAProduct(String saleType, Product product) {
		boolean prodQtyupdated = false;
			if(INVENTORY_DETAILS.get(product) !=null){
				for(Product prodSetKey: INVENTORY_DETAILS.keySet()){
					//AtomicInteger currProdQty = null;
					int currProdQty = 0;
					if(prodSetKey.equals(product)){
						updateLock.lock();
						//currProdQty =new AtomicInteger(prodSetKey.getAvaiableQuntity());
						currProdQty = prodSetKey.getAvaiableQuntity();
						if(saleType.equals(SaleTypes.SOLD.name())){
							currProdQty = currProdQty-1;
						}
						else{
							currProdQty = currProdQty+1;
						}
							//prodSetKey.setAvaiableQuntity(currProdQty.decrementAndGet());
							//prodSetKey.setAvaiableQuntity(currProdQty.incrementAndGet());
						prodSetKey.setAvaiableQuntity(currProdQty);
						System.out.println(saleType+":::"+prodSetKey);
						INVENTORY_DETAILS.put(prodSetKey, prodSetKey.getProductName()
										.concat(prodSetKey.getProductCategoryName())
										.concat(prodSetKey.getPrice().toString()));
						updateLock.unlock();
						break;
					}
				}
				prodQtyupdated =true;
			}
		
		return prodQtyupdated;
	}
	
	public int fetchAvlQtyOfGivenProduct(Product product){
		int currProdQty = 0;
		if(INVENTORY_DETAILS.get(product) !=null){
			for(Product prodSetKey: INVENTORY_DETAILS.keySet()){
				if(prodSetKey.equals(product)){
					currProdQty = prodSetKey.getAvaiableQuntity();
				}
			}
		}
		return currProdQty;
	}
	
}
