package com.example.spring_boot2.config.coverter;

import com.example.spring_boot2.controller.SortType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.util.ParsingUtils;

public class SortTypeFormatter implements Converter<String, SortType> {

    /**
     * @apiNote camelCase 를 snake_case 로 변환합니다.
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return
     */
    @Override
    public SortType convert(String source) {
        return SortType.valueOf(ParsingUtils.reconcatenateCamelCase(source, "_").toUpperCase());
    }
}
