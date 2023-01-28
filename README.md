# sample-spring-core

## Multiple Beans sharing the same name

Branch: `git checkout dependency/multiple-beans`

```
2023-01-28T21:58:39.153+08:00  WARN 12532 --- [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'searchController': Unsatisfied dependency expressed through field 'searchService': No qualifying bean of type 'nguyen.gerald.samples.spring.core.service.search.SearchService' available: expected single matching bean but found 2: bingSearch,googleSearch
2023-01-28T21:58:39.157+08:00  INFO 12532 --- [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2023-01-28T21:58:39.176+08:00  INFO 12532 --- [           main] .s.b.a.l.ConditionEvaluationReportLogger : 

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2023-01-28T21:58:39.219+08:00 ERROR 12532 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description:

Field searchService in nguyen.gerald.samples.spring.core.api.SearchController required a single bean, but 2 were found:
	- bingSearch: defined in file [C:\Users\huy_n\workspace\samples\spring\core\target\classes\nguyen\gerald\samples\spring\core\service\search\BingSearch.class]
	- googleSearch: defined in file [C:\Users\huy_n\workspace\samples\spring\core\target\classes\nguyen\gerald\samples\spring\core\service\search\GoogleSearch.class]


Action:

Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed

```


### @Primary solution

Branch: `git checkout dependency/multiple-beans-solution-primary`

```
Index: src/main/java/nguyen/gerald/samples/spring/core/service/search/GoogleSearch.java
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/src/main/java/nguyen/gerald/samples/spring/core/service/search/GoogleSearch.java b/src/main/java/nguyen/gerald/samples/spring/core/service/search/GoogleSearch.java
--- a/src/main/java/nguyen/gerald/samples/spring/core/service/search/GoogleSearch.java	(revision 1dec78b11d9aa88a032b64871bf42a8cbdab5c93)
+++ b/src/main/java/nguyen/gerald/samples/spring/core/service/search/GoogleSearch.java	(date 1674915907606)
@@ -1,7 +1,9 @@
 package nguyen.gerald.samples.spring.core.service.search;
 
+import org.springframework.context.annotation.Primary;
 import org.springframework.stereotype.Service;
 
+@Primary
 @Service
 public class GoogleSearch implements SearchService {
     @Override

```

### Qualifier Solution

Branch: `git checkout dependency/multiple-beans-solution-qualifier`

```
Index: src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java b/src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java
--- a/src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java	(revision bd5d95525ddbe69ecb7061fe6dde628d2388feb5)
+++ b/src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java	(revision f3c258f9dde64ed7f06888341beb224d5ae6ffef)
@@ -2,6 +2,7 @@
 
 import nguyen.gerald.samples.spring.core.service.search.SearchService;
 import org.springframework.beans.factory.annotation.Autowired;
+import org.springframework.beans.factory.annotation.Qualifier;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestParam;
@@ -11,6 +12,7 @@
 @RequestMapping("/search/")
 public class SearchController {
     @Autowired
+    @Qualifier("bingSearch")
     private SearchService searchService;
 
     @GetMapping

```

### Accepting multiple beans

Branch: `git checkout dependency/multiple-beans-accepting-multiple`

```
Index: src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java b/src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java
--- a/src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java	(revision d58209653cc89fd17a2978f0d5dceffd5a1a3af6)
+++ b/src/main/java/nguyen/gerald/samples/spring/core/api/SearchController.java	(revision 215efb3f29f03912c0a83c7bb3307240617a5799)
@@ -11,10 +11,10 @@
 @RequestMapping("/search/")
 public class SearchController {
     @Autowired
-    private SearchService searchService;
+    private SearchService[] searchServices;
 
     @GetMapping
     public String[] search(@RequestParam("query") String query) {
-        return searchService.search(query);
+        return searchServices[0].search(query);
     }
 }

```

### External configuration

Branch: `git checkout dependency/multiple-beans-configuration`

First attempt:

```
Index: src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java b/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java
new file mode 100644
--- /dev/null	(revision 80527ef1d1e02cd2280cc21ee3f3275e29555eb1)
+++ b/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java	(revision 80527ef1d1e02cd2280cc21ee3f3275e29555eb1)
@@ -0,0 +1,22 @@
+package nguyen.gerald.samples.spring.core.config;
+
+import nguyen.gerald.samples.spring.core.service.search.BingSearch;
+import nguyen.gerald.samples.spring.core.service.search.GoogleSearch;
+import nguyen.gerald.samples.spring.core.service.search.SearchService;
+import org.springframework.beans.factory.annotation.Autowired;
+import org.springframework.context.annotation.Bean;
+import org.springframework.context.annotation.Configuration;
+
+@Configuration
+public class SearchConfig {
+    @Autowired
+    private GoogleSearch googleSearch;
+
+    @Autowired
+    private BingSearch bingSearch;
+
+    @Bean
+    public SearchService searchService() {
+        return googleSearch;
+    }
+}

```

2nd Attempt:

```
Index: src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java b/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java
--- a/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java	(revision caf322dc9df4e6bb0517666dc13f593fab269105)
+++ b/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java	(revision b4bb0bf045d4aaefaea45751eed43a54d4dbfd7e)
@@ -16,6 +16,11 @@
     @Autowired
     private BingSearch bingSearch;
 
+    @Bean
+    public SearchService searchService(@Value("${google-search.enabled:false}") boolean enableGoogleSearch) {
+        return enableGoogleSearch ? googleSearch : bingSearch;
+    }
+
 //    Not enough configurability
 //    @Bean
 //    public SearchService searchService() {
Index: src/main/resources/application.properties
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>ISO-8859-1
===================================================================
diff --git a/src/main/resources/application.properties b/src/main/resources/application.properties
--- a/src/main/resources/application.properties	(revision caf322dc9df4e6bb0517666dc13f593fab269105)
+++ b/src/main/resources/application.properties	(revision b4bb0bf045d4aaefaea45751eed43a54d4dbfd7e)
@@ -1,1 +1,2 @@
 
+google-search.enabled=true
\ No newline at end of file


```

### Advanced external configuration: feature switching

Branch: `git checkout dependency/multiple-beans-feature-switch`

```
Index: src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java b/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java
--- a/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java	(revision 5736cf335e9a5d6e247283c24bd801fb6e5497a3)
+++ b/src/main/java/nguyen/gerald/samples/spring/core/config/SearchConfig.java	(revision 19636b56bc8f529311cb3ef881c5bfb1f535a340)
@@ -3,6 +3,7 @@
 import nguyen.gerald.samples.spring.core.service.search.BingSearch;
 import nguyen.gerald.samples.spring.core.service.search.GoogleSearch;
 import nguyen.gerald.samples.spring.core.service.search.SearchService;
+import nguyen.gerald.samples.spring.core.service.search.YahooSearch;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.context.annotation.Bean;
@@ -10,20 +11,22 @@
 
 @Configuration
 public class SearchConfig {
+    enum SearchProvider { GOOGLE, BING, YAHOO }
     @Autowired
     private GoogleSearch googleSearch;
 
     @Autowired
     private BingSearch bingSearch;
 
+    @Autowired
+    private YahooSearch yahooSearch;
+
     @Bean
-    public SearchService searchService(@Value("${google-search.enabled:false}") boolean enableGoogleSearch) {
-        return enableGoogleSearch ? googleSearch : bingSearch;
+    public SearchService searchService(@Value("${search.provider:BING}") SearchProvider provider) {
+        return switch (provider) {
+            case BING -> bingSearch;
+            case YAHOO -> yahooSearch;
+            default -> googleSearch;
+        };
     }
-
-//    Not enough configurability
-//    @Bean
-//    public SearchService searchService() {
-//        return googleSearch;
-//    }
 }
Index: src/main/java/nguyen/gerald/samples/spring/core/service/search/YahooSearch.java
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>UTF-8
===================================================================
diff --git a/src/main/java/nguyen/gerald/samples/spring/core/service/search/YahooSearch.java b/src/main/java/nguyen/gerald/samples/spring/core/service/search/YahooSearch.java
new file mode 100644
--- /dev/null	(revision 19636b56bc8f529311cb3ef881c5bfb1f535a340)
+++ b/src/main/java/nguyen/gerald/samples/spring/core/service/search/YahooSearch.java	(revision 19636b56bc8f529311cb3ef881c5bfb1f535a340)
@@ -0,0 +1,11 @@
+package nguyen.gerald.samples.spring.core.service.search;
+
+import org.springframework.stereotype.Service;
+
+@Service
+public class YahooSearch implements SearchService {
+    @Override
+    public String[] search(String query) {
+        return new String[]{ "Yahoo sample result" };
+    }
+}
Index: src/main/resources/application.properties
IDEA additional info:
Subsystem: com.intellij.openapi.diff.impl.patch.CharsetEP
<+>ISO-8859-1
===================================================================
diff --git a/src/main/resources/application.properties b/src/main/resources/application.properties
--- a/src/main/resources/application.properties	(revision 5736cf335e9a5d6e247283c24bd801fb6e5497a3)
+++ b/src/main/resources/application.properties	(revision 19636b56bc8f529311cb3ef881c5bfb1f535a340)
@@ -1,2 +1,2 @@
 
-google-search.enabled=true
\ No newline at end of file
+search.provider=YAHOO
\ No newline at end of file


```

