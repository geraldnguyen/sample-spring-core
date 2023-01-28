package nguyen.gerald.samples.spring.core.service.search;

import org.springframework.stereotype.Service;

@Service
public class BingSearch implements SearchService {
    @Override
    public String[] search(String query) {
        return new String[]{ "Bing sample result" };
    }
}
