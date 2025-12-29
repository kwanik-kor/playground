package com.gani.graphql.api.repository;

import com.gani.graphql.api.generated.types.Product;

import java.util.List;

public interface Repository {

    Product getProduct(String id);

    List<Product> getProducts();

}
