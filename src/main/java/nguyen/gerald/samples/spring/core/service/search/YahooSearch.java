package nguyen.gerald.samples.spring.core.service.search;

import org.springframework.stereotype.Service;

@Service
public class YahooSearch implements SearchService {
    @Override
    public String[] search(String query) {
        return new String[]{ "Yahoo sample result" };
    }
}
