package nguyen.gerald.samples.spring.core.api;

import nguyen.gerald.samples.spring.core.service.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    public String[] search(@RequestParam("query") String query) {
        return searchService.search(query);
    }
}
