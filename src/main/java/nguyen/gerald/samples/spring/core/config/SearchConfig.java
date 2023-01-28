package nguyen.gerald.samples.spring.core.config;

import nguyen.gerald.samples.spring.core.service.search.BingSearch;
import nguyen.gerald.samples.spring.core.service.search.GoogleSearch;
import nguyen.gerald.samples.spring.core.service.search.SearchService;
import nguyen.gerald.samples.spring.core.service.search.YahooSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfig {
    enum SearchProvider { GOOGLE, BING, YAHOO }
    @Autowired
    private GoogleSearch googleSearch;

    @Autowired
    private BingSearch bingSearch;

    @Autowired
    private YahooSearch yahooSearch;

    @Bean
    public SearchService searchService(@Value("${search.provider:BING}") SearchProvider provider) {
        return switch (provider) {
            case BING -> bingSearch;
            case YAHOO -> yahooSearch;
            default -> googleSearch;
        };
    }
}
