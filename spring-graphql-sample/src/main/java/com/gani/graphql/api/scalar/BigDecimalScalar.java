package com.gani.graphql.api.scalar;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import graphql.schema.idl.RuntimeWiring;

import static graphql.scalars.ExtendedScalars.GraphQLBigDecimal;

/**
 * dgs-extended-scalars 라이브러리를 통한 스칼라 정의
 */
@DgsComponent
public class BigDecimalScalar {

    @DgsRuntimeWiring
    public RuntimeWiring.Builder addScalar(RuntimeWiring.Builder builder) {
        return builder.scalar(GraphQLBigDecimal);
    }
}
