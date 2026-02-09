package com.gani.graphql.api.services;

import com.gani.graphql.api.generated.types.Product;
import com.gani.graphql.api.generated.types.Tag;
import com.gani.graphql.api.generated.types.TagInput;

import java.util.List;
import java.util.Map;

public interface TagService {

    Map<String, List<Tag>> getTags(List<String> productIds);

    Product addTags(String productId, List<TagInput> tags);

}
