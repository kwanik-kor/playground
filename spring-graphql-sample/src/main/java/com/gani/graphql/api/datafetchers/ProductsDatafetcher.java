package com.gani.graphql.api.datafetchers;

import com.gani.graphql.api.generated.DgsConstants;
import com.gani.graphql.api.generated.types.Product;
import com.gani.graphql.api.generated.types.ProductCriteria;
import com.gani.graphql.api.services.ProductService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@DgsComponent
public class ProductsDatafetcher {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final ProductService service;

    public ProductsDatafetcher(ProductService service) {
        this.service = service;
    }

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.Products
    )
    public List<Product> getProducts(@InputArgument("filter") ProductCriteria criteria) {
        return service.getProducts(criteria);
    }
}
