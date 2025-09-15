package com.gani.springai.service.structuredoutput;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StructuredOutputService {
    private final ChatClient chatClient;

    public StructuredOutputService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    /**
     * List<String>으로 변환하는 ListOutputConverter
     * @param city 도시 이름
     * @return 도시의 호텔 목록
     */
    public List<String> listOutputConverterLowLevel(String city) {
        final ListOutputConverter converter = new ListOutputConverter();
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("{city}에서 유명한 호텔 목록 5개를 출력하세요. {format}")
                .build();

        Prompt prompt = promptTemplate.create(
                Map.of("city", city, "format", converter.getFormat()));

        String commaSeperatedString = chatClient.prompt(prompt)
                .call()
                .content();

        return converter.convert(commaSeperatedString);
    }

    /**
     * List<String>으로 변환하는 ListOutputConverter
     * @param city 도시 이름
     * @return 도시의 호텔 목록
     */
    public List<String> listOutputConverterHighLevel(String city) {
        return chatClient.prompt()
                .user("%s에서 유명한 호텔 목록 5개를 출력하세요.".formatted(city))
                .call()
                .entity(new ListOutputConverter());
    }

    /**
     * T 객체로 변환하고자 한다면, BeanOutputConverter<T> 사용 가능
     * @param city 도시명
     * @return 호텔 목록
     */
    public Hotel beanOutputConverterLowLevel(String city) {
       final BeanOutputConverter<Hotel> converter = new BeanOutputConverter<>(Hotel.class);

        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("{city}에서 유명한 호텔 목록 5개를 출력하세요. {format}")
                .build();

        Prompt prompt = promptTemplate.create(
                Map.of("city", city, "format", converter.getFormat()));

        return converter.convert(chatClient.prompt(prompt)
                .call()
                .content());
    }

    /**
     * T 객체로 변환하고자 한다면, BeanOutputConverter<T> 사용 가능
     * @param city 도시명
     * @return 호텔 목록
     */
    public Hotel beanOutputConverterHighLevel(String city) {
        return chatClient.prompt()
                .user("%s에서 유명한 호텔 목록 5개를 출력하세요.".formatted(city))
                .call()
                .entity(new BeanOutputConverter<>(Hotel.class));
    }

    /**
     * ParameterizedTypeReference를 이용해 List<T> 반환 역시 가능함
     * @param cities 도시목록
     * @return 도시별 호텔 목록
     */
    public List<Hotel> genericBeanOutputConverterHighLevel(String cities) {
        return chatClient.prompt()
                .user("""
                    다음 도시들에서 유명한 호텔 3개를 출력하세요.
                    %s
                """.formatted(cities))
                .call()
                .entity(new ParameterizedTypeReference<List<Hotel>>() {});
    }

    /**
     * Map<String, Object> 객체로 변환할 수 있음 <br>
     * 비정규화된 정보를 수신하기 위한 용도로 생각할 수 있지만 Nullable 하더라도 필요로 하는 객체의 형태에 맞춰서 응답을 받는 것이 보다 나을지도...
     * @param hotel
     * @reutrn 호텔에 대한 정보
     */
    public Map<String, Object> mapOutputConverterHighLevel(String hotel) {
        return chatClient.prompt()
                .user("""
                    호텔 %s에 대해 정보를 알려주세요
                """.formatted(hotel))
                .call()
                .entity(new MapOutputConverter());
    }

    /**
     * entity()에 사용자 지침에 대한 출력 포맷을 지정할 수 있지만, 시스템 메시지에서 1차 지침으로 주고,
     * entity에 구체적인 타입 정보를 제공해서 정확한 JSON 타입을 반환하게 한다면 구조화된 출력을 강화할 수 있음
     * @param review 영화 리뷰
     * @return 리뷰 정보
     */
    public ReviewClassification classifyReview(String review) {
        return chatClient.prompt()
                .system("""
                    영화 리뷰를 [POSITIVE, NEUTRAL, NEGATIVE] 중에서 하나로 분류하고,
                    유효한 JSON을 반환하세요.
                """)
                .user(review)
                .options(ChatOptions.builder().temperature(0.0).build())
                .call()
                .entity(ReviewClassification.class);
    }

}
