package com.pazeto.comercio.vo;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Comparator;

public class Product implements Serializable {

	@Exclude
	String id;
	String name;
	String description;

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Exclude
	public String getFullname() {
		return this.name + this.description;
	}

    public static class ProductNameComparator implements Comparator<Product> {

		public int compare(Product product1, Product product2) {
			String productName1 = product1.getName().toUpperCase();
			String productName2 = product2.getName().toUpperCase();

			return productName1.compareTo(productName2);
		}
    }
}
