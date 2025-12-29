package com.gani.graphql.api.scalar;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import graphql.schema.idl.RuntimeWiring;

import static graphql.scalars.ExtendedScalars.DateTime;

/**
 * graphql.schema의 Coercing 구현을 통한 스칼라 정의
 */
@DgsComponent
public class DateTimeScalar {

    @DgsRuntimeWiring
    public RuntimeWiring.Builder addScalar(RuntimeWiring.Builder builder) {
        return builder.scalar(DateTime);
    }

}
