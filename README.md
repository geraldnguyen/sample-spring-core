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

