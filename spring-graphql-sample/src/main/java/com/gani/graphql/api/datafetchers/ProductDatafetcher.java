package com.gani.graphql.api.datafetchers;

import com.gani.graphql.api.generated.DgsConstants;
import com.gani.graphql.api.generated.DgsConstants.QUERY;
import com.gani.graphql.api.generated.types.Product;
import com.gani.graphql.api.services.ProductService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.apache.logging.log4j.util.Strings;

/**
 * 데이터베이스나 써드-파티 API/문서 저장소와 같은 영구 저장소에서 정보 검색
 */
@DgsComponent
public class ProductDatafetcher {
    private final ProductService productService;

    public ProductDatafetcher(ProductService productService) {
        this.productService = productService;
    }

    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = QUERY.Product)
    public Product getProduct(@InputArgument("id") String id) {
        if (Strings.isBlank(id)) {
            new RuntimeException("유효하지 않은 제품 아이디입니다.");
        }
        return productService.getProduct(id);
    }


}
