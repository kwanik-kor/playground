package com.gani.springai.service.structuredoutput;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class StructuredOutputServiceTest {
    @Autowired
    private StructuredOutputService structuredOutputService;

    @DisplayName("ListOutputConverter는 LLM 출력을 List<String>으로 변환한다.")
    @Nested
    class ListOutputConverterTest {

        @Test
        void listOutputConverterLowLevel() {
            List<String> result = structuredOutputService.listOutputConverterLowLevel("서울");
            assertEquals(5, result.size());
        }

        @Test
        void listOutputConverterHighLevel() {
            List<String> result = structuredOutputService.listOutputConverterHighLevel("서울");
            assertEquals(5, result.size());
        }
    }

    @DisplayName("BeanOutputConverter는 LLM 출력을 정의한 객체의 형태로 변환한다.")
    @Nested
    class BeanOutputConverterTest {

        @Test
        void beanOutputConverterLowLevel() {
            final String city = "서울";
            Hotel hotel = structuredOutputService.beanOutputConverterLowLevel(city);
            assertEquals(city, hotel.getCity());
            assertEquals(5, hotel.getNames().size());
        }

        @Test
        void beanOutputConverterHighLevel() {
            final String city = "서울";
            Hotel hotel = structuredOutputService.beanOutputConverterHighLevel(city);
            assertEquals(city, hotel.getCity());
            assertEquals(5, hotel.getNames().size());
        }
    }

    @DisplayName("ParameterizedTypeReference를 이용해 List<T>로 반환할 수 있다.")
    @Nested
    class GenericBeanOutputConverterTest {

        @Test
        void genericBeanOutputConverterHighLevel() {
            final List<String> cities = List.of("서울", "대구", "부산");
            List<Hotel> hotels = structuredOutputService.genericBeanOutputConverterHighLevel(String.join(",", cities));

            for (Hotel hotel : hotels) {
                assertTrue(cities.contains(hotel.getCity()));
                assertEquals(3, hotel.getNames().size());
            }
        }
    }

    @DisplayName("Map<String, Object> 형태로 LLM 응답을 변환할 수 있다.")
    @Nested
    class MapOutputConverterTest {

        @Test
        void mapOutputConverterHighLevel() throws IOException {
            Map<String, Object> hotelInfo = structuredOutputService.mapOutputConverterHighLevel("그랜드 워커힐 서울");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

            for (Map.Entry<String, Object> entry : hotelInfo.entrySet()) {
                bw.write("# key: " + entry.getKey() + "\n");
                bw.write("- value: " + entry.getValue() + "\n");
            }
            bw.flush();
            bw.close();
        }
    }

    @DisplayName("SystemMessage를 함께 사용하여 구조화된 출력을 강화할 수 있다.")
    @Nested
    class WithSystemMessageTest {

        @Test
        void withSystemMessage() {
            final String reviewTxt = "귀멸의 칼날 무한성편 진짜 대존잼. 하쿠지 지켜주지 못해 미안해";
            final ReviewClassification review = structuredOutputService.classifyReview(reviewTxt);

            assertEquals(reviewTxt, review.getReview());
            assertEquals(ReviewClassification.Sentiment.POSITIVE, review.getSentiment());
        }
    }
}