import com.development.bo.Product;

public class ApplicationJava {

	public static void main(String[] args) throws CloneNotSupportedException {
		Product inputProduct = new Product(1L,"first product","firstCategory",20.00f, 20);
 		Product clonedProduct = (Product) inputProduct.clone();
 		System.out.println(clonedProduct);
	}
	
}
