package com.development.bo;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.development.dao.impl.ProductDaoImpl;
//we have all implementations available so no need to mock this
//if we have are suppose to create new method to update product which is not available then we can 
//the new method
public class ProductsDaoTest {
	
	
	ProductDaoImpl 	prodDao;


	ProductsInventory prodInventory = new ProductsInventory();

	//@Before method was the issue and @Runwith was missing.
	
	@Before
	public void setup() {
		prodInventory = new ProductsInventory();
		prodDao = new ProductDaoImpl();
		prodDao.setProdInventory(prodInventory);
	}
	
	//@Test
	public void test1AddGivenValidProductToInvetory(){
		System.out.println(prodInventory.sizeOfCollection());
		Product inputProduct = new Product(1L,"first product","firstCategory",10.00f, 10);
		String actualFoundProduct = prodDao.addProductToInventory(inputProduct);
		String expectedProduct = null;
		System.out.println(prodInventory.fetchProductValue(inputProduct));
		//assertEquals(expectedProduct, actualFoundProduct);
	}
	
	//@Test
	public void test2AddExistingProductToInvetoryWithChangeInPrice(){
		System.out.println(prodInventory.sizeOfCollection());
		Product inputProduct = new Product(1L,"first product","firstCategory",20.00f, 20);
 		String expectedProduct = null;//"first product".concat("firstCategory").concat(10.00f+"");
		String actualFoundProduct = prodDao.addProductToInventory(inputProduct);
		System.out.println(prodInventory.fetchProductValue(inputProduct));
		//assertEquals(expectedProduct, actualFoundProduct);
		System.out.println(prodInventory.sizeOfCollection());
	}
	
	//@Test
	public void test3AddExistingProductPriceButDifferentProduct(){
		System.out.println(prodInventory.sizeOfCollection());
		Product inputProduct = new Product(2L,"second product","firstCategory",20.00f, 5);
 		String expectedProduct = null;//"first product".concat("firstCategory").concat(10.00f+"");
		String actualFoundProduct = prodDao.addProductToInventory(inputProduct);
		System.out.println(prodInventory.fetchProductValue(inputProduct));
		//assertEquals(expectedProduct, actualFoundProduct);
		System.out.println(prodInventory.sizeOfCollection());
	}
	
	@Ignore
	@Test
	public void test4UpdateProductAvailbilityCountIfSoldforExistingProductId(){
		//setup test data
		/*when(prodInventory.adjustInventoryOfAProduct(SaleTypes.SOLD.name(),1L)).
		thenReturn(true);*/
		
		boolean expected =true;
		//run test/ create test statement
		Product inputProduct = new Product(2L,"second product","firstCategory",20.00f, 0);
		boolean actual = prodDao.updateTheProductsAvailableCount(SaleTypes.SOLD.name(),inputProduct);
		//here actual is calculated from when statement.
		assertEquals(expected, actual);
		int expectedAvlQty = 4;
		int actualAvlQty = prodInventory.fetchAvlQtyOfGivenProduct(inputProduct);
		assertEquals(expectedAvlQty, actualAvlQty);
	}
	
	@Ignore
	@Test
	public void test5ZZZZMultiThreadingOrdersQtyChange(){
		
		test1AddGivenValidProductToInvetory();
		test2AddExistingProductToInvetoryWithChangeInPrice();
		test3AddExistingProductPriceButDifferentProduct();
	
		List<Future<?>> futuresList = new ArrayList<>();
		
		ExecutorService exServ = Executors.newFixedThreadPool(10);
		Product product = new Product(1L, "first product","firstCategory",20.00f, 20);//1-->20
		futuresList.add(exServ.submit(new ProductCheck(product, SaleTypes.SOLD.name(), prodDao)));//1-->19
		Product product1 = new Product(1L, "first product","firstCategory",20.00f, 20);//1-->19
		futuresList.add(exServ.submit(new ProductCheck(product1, SaleTypes.RETURN.name(), prodDao)));//1-->20
		Product product2 = new Product(2L,"second product","firstCategory",20.00f, 0);//2-->5
		futuresList.add(exServ.submit(new ProductCheck(product2, SaleTypes.SOLD.name(), prodDao)));//2-->4
		Product product3 = new Product(2L,"second product","firstCategory",20.00f, 0);//2-->4
		futuresList.add(exServ.submit(new ProductCheck(product3, SaleTypes.RETURN.name(), prodDao)));//2-->5
		Product product4 = new Product(1L, "first product","firstCategory",20.00f, 20);//1-->20
		futuresList.add(exServ.submit(new ProductCheck(product4, SaleTypes.RETURN.name(), prodDao)));//1-->19
		Product product5 = new Product(2L,"second product","firstCategory",20.00f, 0);//2-->5
		futuresList.add(exServ.submit(new ProductCheck(product5, SaleTypes.RETURN.name(), prodDao)));//2-->6
		Product product6 = new Product(1L, "first product","firstCategory",10.00f, 20);//1-->10
		futuresList.add(exServ.submit(new ProductCheck(product6, SaleTypes.SOLD.name(), prodDao)));//1-->9
		Product product7 = new Product(1L, "first product","firstCategory",10.00f, 20);//-->9
		futuresList.add(exServ.submit(new ProductCheck(product7, SaleTypes.RETURN.name(), prodDao)));//1-->10
		Product product8 = new Product(2L,"second product","firstCategory",20.00f, 0);//2-->6
		futuresList.add(exServ.submit(new ProductCheck(product8, SaleTypes.SOLD.name(), prodDao)));//2-->5
		Product product9 = new Product(1L, "first product","firstCategory",10.00f, 20);//1-->10
		futuresList.add(exServ.submit(new ProductCheck(product9, SaleTypes.SOLD.name(), prodDao)));//1-->9
		
		for(Future f : futuresList){
			System.out.println(f.isDone());
		}
		
		exServ.shutdown();
		
		int expectedAvlQtyProd1_20 = 19;
		int actualAvlQtyProd1_20 = prodInventory.fetchAvlQtyOfGivenProduct(product);
		assertEquals(expectedAvlQtyProd1_20, actualAvlQtyProd1_20);
		
		int expectedAvlQtyProd1_10 = 9;
		int actualAvlQtyProd1_10 = prodInventory.fetchAvlQtyOfGivenProduct(product9);
		assertEquals(expectedAvlQtyProd1_10, actualAvlQtyProd1_10);

		int expectedAvlQtyProd2_20 = 5;
		int actualAvlQtyProd2_20 = prodInventory.fetchAvlQtyOfGivenProduct(product9);
		assertEquals(expectedAvlQtyProd2_20, actualAvlQtyProd2_20);
	}

	public void testLoopMehtod() throws InterruptedException{
		test1AddGivenValidProductToInvetory();
		test2AddExistingProductToInvetoryWithChangeInPrice();
		test3AddExistingProductPriceButDifferentProduct();
		
		for(int i=0;i<20;i++){
			test6MultiThreadingOrdersQtyChange();
			System.out.println("------------------------------End------------------------------");
		}
	}
	
	
	@Test
	public void test6MultiThreadingOrdersQtyChange() throws InterruptedException{
		
		test1AddGivenValidProductToInvetory();
		test2AddExistingProductToInvetoryWithChangeInPrice();
		test3AddExistingProductPriceButDifferentProduct();
	
		Product product = new Product(1L, "first product","firstCategory",20.00f, 20);//1_20-->20--B
		Thread t1 = new Thread(new ProductCheck(product, SaleTypes.SOLD.name(), prodDao));//1_20-->19--A
		Product product1 = new Product(1L, "first product","firstCategory",20.00f, 20);//1_20-->19--B
		Thread t2 = new Thread(new ProductCheck(product1, SaleTypes.RETURN.name(), prodDao));//1_20-->20--A
		Product product2 = new Product(2L,"second product","firstCategory",20.00f, 0);//2_20-->5--B
		Thread t3 = new Thread(new ProductCheck(product2, SaleTypes.SOLD.name(), prodDao));//2_20-->4--A
		Product product3 = new Product(2L,"second product","firstCategory",20.00f, 0);//2_20-->4--B
		Thread t4 = new Thread(new ProductCheck(product3, SaleTypes.RETURN.name(), prodDao));//2_20-->5--A
		Product product4 = new Product(1L, "first product","firstCategory",10.00f, 20);//1_10-->10--B
		Thread t5 = new Thread(new ProductCheck(product4, SaleTypes.SOLD.name(), prodDao));//1_10-->9--A
		Product product5 = new Product(1L, "first product","firstCategory",10.00f, 20);//1_10-->9--B
		Thread t6 = new Thread(new ProductCheck(product5, SaleTypes.RETURN.name(), prodDao));//1_10-->10--A
		Product product6 = new Product(2L,"second product","firstCategory",20.00f, 0);//2_20-->5--B
		Thread t7 = new Thread(new ProductCheck(product6, SaleTypes.SOLD.name(), prodDao));//2_20-->4--A
		Product product7 = new Product(2L,"second product","firstCategory",20.00f, 0);//2_20-->4--B
		Thread t8 = new Thread(new ProductCheck(product7, SaleTypes.RETURN.name(), prodDao));//2_20-->5--A
		Product product8 = new Product(1L, "first product","firstCategory",10.00f, 20);//1_10-->10--B
		Thread t9 = new Thread(new ProductCheck(product8, SaleTypes.SOLD.name(), prodDao));//1_10-->9--A
		Product product9 = new Product(1L, "first product","firstCategory",10.00f, 20);//1_10-->9--B
		Thread t10 = new Thread(new ProductCheck(product9, SaleTypes.RETURN.name(), prodDao));//1_10-->10--A
		
		t1.start();
//		System.out.println(t1.isAlive());
		t2.start();
//		System.out.println(t2.isAlive());
		t2.join();
//		System.out.println(t2.isAlive());
		t3.start();
//		System.out.println(t3.isAlive());
		t3.join();
//		System.out.println(t3.isAlive());
		t4.start();
//		System.out.println(t4.isAlive());
		t4.join();
//		System.out.println(t4.isAlive());
		t5.start();
//		System.out.println(t5.isAlive());
		t5.join();
//		System.out.println(t5.isAlive());
		t6.start();
//		System.out.println(t6.isAlive());
		t6.join();
//		System.out.println(t6.isAlive());
		t7.start();
//		System.out.println(t7.isAlive());
		t7.join();
//		System.out.println(t7.isAlive());
		t8.start();
//		System.out.println(t8.isAlive());
		t8.join();
//		System.out.println(t8.isAlive());
		t9.start();
//		System.out.println(t9.isAlive());
		t9.join();
//		System.out.println(t9.isAlive());
		t10.start();
//		System.out.println(t10.isAlive());
		t10.join();
//		System.out.println(t10.isAlive());
		
		int expectedAvlQtyProd1_20 = 20;
		int actualAvlQtyProd1_20 = prodInventory.fetchAvlQtyOfGivenProduct(product);
//		System.out.println("expectedAvlQtyProd1_20::"+expectedAvlQtyProd1_20+":::actualAvlQtyProd1_20:::"+actualAvlQtyProd1_20);
		assertEquals(expectedAvlQtyProd1_20, actualAvlQtyProd1_20);
		
		int expectedAvlQtyProd1_10 = 10;
		int actualAvlQtyProd1_10 = prodInventory.fetchAvlQtyOfGivenProduct(product9);
//		System.out.println("expectedAvlQtyProd1_10::"+expectedAvlQtyProd1_10+":::actualAvlQtyProd1_10:::"+actualAvlQtyProd1_10);
		assertEquals(expectedAvlQtyProd1_10, actualAvlQtyProd1_10);

		int expectedAvlQtyProd2_20 = 5;
		int actualAvlQtyProd2_20 = prodInventory.fetchAvlQtyOfGivenProduct(product3);
//		System.out.println("expectedAvlQtyProd2_20::"+expectedAvlQtyProd2_20+":::actualAvlQtyProd2_20:::"+actualAvlQtyProd2_20);
		assertEquals(expectedAvlQtyProd2_20, actualAvlQtyProd2_20);
	}
}
