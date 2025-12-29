package com.gani.graphql.api.services;

import com.gani.graphql.api.generated.types.Product;
import com.gani.graphql.api.generated.types.ProductCriteria;
import com.gani.graphql.api.generated.types.TagInput;
import com.gani.graphql.api.repository.Repository;
import org.apache.logging.log4j.util.Strings;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Service
public class ProductServiceImpl implements ProductService {
    private final Repository repository;

    public ProductServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Product getProduct(String id) {
        return repository.getProduct(id);
    }

    @Override
    public List<Product> getProducts(ProductCriteria criteria) {
        List<Predicate<Product>> predicates = new ArrayList<>(2);
        if (!Objects.isNull(criteria)) {
            if (Strings.isNotBlank(criteria.getName())) {
                Predicate<Product> namePredicate = p -> p.getName().contains(criteria.getName());
                predicates.add(namePredicate);
            }
            if (!Objects.isNull(criteria.getTags()) && !criteria.getTags().isEmpty()) {
                List<String> tags = criteria.getTags().stream().map(TagInput::getName).toList();
                Predicate<Product> tagsPredicate = p ->
                        p.getTags().stream().anyMatch(t -> tags.contains(t.getName()));
                predicates.add(tagsPredicate);
            }
        }
        if (predicates.isEmpty()) {
            return repository.getProducts();
        }
        return repository.getProducts().stream()
                .filter(p -> predicates.stream().allMatch(pre -> pre.test(p))).collect(toList());
    }

    @Override
    public Product addQuantity(String productId, int qty) {
        return null;
    }

    @Override
    public Publisher<Product> getProductPublisher() {
        return null;
    }
}
