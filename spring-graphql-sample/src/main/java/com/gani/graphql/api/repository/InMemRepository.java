package com.gani.graphql.api.repository;

import com.gani.graphql.api.generated.types.Product;
import com.gani.graphql.api.generated.types.Tag;
import net.datafaker.Faker;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@org.springframework.stereotype.Repository
public class InMemRepository implements Repository {
    private static final Map<String, Product> productEntities = new ConcurrentHashMap<>();
    private static final Map<String, Tag> tagEntities = new ConcurrentHashMap<>();
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public InMemRepository() {
        Faker faker = new Faker();
        IntStream.range(0, faker.number().numberBetween(20, 50)).forEach(num -> {
            String tag = faker.book().genre();
            tagEntities.putIfAbsent(tag,
                    Tag.newBuilder().id(UUID.randomUUID().toString()).name(tag).build());
        });

        IntStream.range(0, faker.number().numberBetween(4, 20)).forEach(number -> {
            String id = String.format("a1s2d3f4-%d", number);
            String title = faker.book().title();
            List<Tag> tags = tagEntities.entrySet().stream()
                    .filter(t -> t.getKey().startsWith(faker.book().genre().substring(0, 1)))
                    .map(Entry::getValue).collect(toList());
            if (tags.isEmpty()) {
                tags.add(tagEntities.entrySet().stream().findAny().get().getValue());
            }
            Product product = Product.newBuilder().id(id).name(title)
                    .description(faker.lorem().sentence()).count(faker.number().numberBetween(10, 100))
                    .price(BigDecimal.valueOf(faker.number().randomDigitNotZero()))
                    .imageUrl(String.format("/images/%s.jpeg", title.replace(" ", "")))
                    .tags(tags).build();
            productEntities.put(id, product);
        });

    }

    @Override
    public Product getProduct(String id) {
        if (Strings.isBlank(id)) {
            throw new RuntimeException("Invalid Product ID.");
        }
        Product product = productEntities.get(id);
        if (Objects.isNull(product)) {
            throw new RuntimeException("Product not found.");
        }
        return product;
    }

    @Override
    public List<Product> getProducts() {
        return new ArrayList<>(productEntities.values());
    }
}
