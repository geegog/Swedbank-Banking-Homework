package com.swedbank.common.application.config;

import com.swedbank.common.application.Dto.MoneyDTO;
import com.swedbank.common.application.customizer.ModelMapperCustomizer;
import com.swedbank.common.domian.Money;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Currency;
import java.util.List;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(List<ModelMapperCustomizer> customizers) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<Money, MoneyDTO> moneyToDto = ctx -> {
            Money src = ctx.getSource();
            if (src == null) return null;
            return new MoneyDTO(src.getAmount(), src.getCurrency().getCurrencyCode());
        };

        Converter<MoneyDTO, Money> dtoToMoney = ctx -> {
            MoneyDTO src = ctx.getSource();
            if (src == null) return null;
            return Money.of(src.getAmount(), Currency.getInstance(src.getCurrency()));
        };

        modelMapper.addConverter(moneyToDto);
        modelMapper.addConverter(dtoToMoney);

        customizers.forEach(customizer -> customizer.customize(modelMapper));

        return modelMapper;
    }
}
