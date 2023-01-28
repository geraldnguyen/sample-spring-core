package nguyen.gerald.samples.spring.core.config;

import nguyen.gerald.samples.spring.core.service.search.BingSearch;
import nguyen.gerald.samples.spring.core.service.search.GoogleSearch;
import nguyen.gerald.samples.spring.core.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfig {
    @Autowired
    private GoogleSearch googleSearch;

    @Autowired
    private BingSearch bingSearch;

    @Bean
    public SearchService searchService() {
        return googleSearch;
    }
}
