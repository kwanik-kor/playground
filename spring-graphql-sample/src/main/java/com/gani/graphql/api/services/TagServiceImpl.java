package com.gani.graphql.api.services;

import com.gani.graphql.api.generated.types.Product;
import com.gani.graphql.api.generated.types.Tag;
import com.gani.graphql.api.generated.types.TagInput;
import com.gani.graphql.api.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {
    private final Repository repository;

    public TagServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Map<String, List<Tag>> getTags(List<String> productIds) {
        return Map.of();
    }

    @Override
    public Product addTags(String productId, List<TagInput> tags) {
        return null;
    }
}
