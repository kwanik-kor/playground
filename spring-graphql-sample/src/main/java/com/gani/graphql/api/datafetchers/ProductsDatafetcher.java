package com.gani.graphql.api.datafetchers;

import com.gani.graphql.api.dataloaders.TagsDataloaderWithContext;
import com.gani.graphql.api.generated.DgsConstants;
import com.gani.graphql.api.generated.types.Product;
import com.gani.graphql.api.generated.types.ProductCriteria;
import com.gani.graphql.api.services.ProductService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.InputArgument;
import org.dataloader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.metrics.StartupStep.Tags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @DgsData(parentType = DgsConstants.PRODUCT.TYPE_NAME, field = DgsConstants.PRODUCT.Tags)
    public CompletableFuture<List<Tags>> tags(DgsDataFetchingEnvironment env) {
        DataLoader<String, List<Tags>> tagsDataLoader = env.getDataLoader(TagsDataloaderWithContext.class);
        Product product = env.getSource();
        return tagsDataLoader.load(product.getId());
    }
}
