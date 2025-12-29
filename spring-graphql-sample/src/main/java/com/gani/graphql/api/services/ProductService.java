package com.gani.graphql.api.services;

import com.gani.graphql.api.generated.types.Product;
import com.gani.graphql.api.generated.types.ProductCriteria;
import org.reactivestreams.Publisher;

import java.util.List;

public interface ProductService {
    Product getProduct(String id);

    List<Product> getProducts(ProductCriteria criteria);

    Product addQuantity(String productId, int qty);

    Publisher<Product> getProductPublisher();
}
