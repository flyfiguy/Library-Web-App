package com.springbootlibrary.config;

import com.springbootlibrary.entity.Book;
import com.springbootlibrary.entity.History;
import com.springbootlibrary.entity.Message;
import com.springbootlibrary.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
    //Will allow us to properly make requests to our front end React application.
    //Will put this in a CORS mapping
    private String theAllowedOrigins = "https://localhost:3000";

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        //Methods we are disabling
        //Service will return 405 Method Not Allowed
        HttpMethod[] theUnsupportedActions = {
                HttpMethod.POST,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
                HttpMethod.PUT
        };

        //Expose all the ids for the classes, so we are going to use the primary key for functionality on the front-end
        //so we know exactly what book we are using. Hibernate automatically hides the id's (primary key).
        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);
        config.exposeIdsFor(History.class);
        config.exposeIdsFor(Message.class);

        //HTTP methods we do not want exposed. Disable them.
        disableHttpMethods(Book.class, config, theUnsupportedActions);
        disableHttpMethods(Review.class, config, theUnsupportedActions);
        disableHttpMethods(History.class, config, theUnsupportedActions);
        disableHttpMethods(Message.class, config, theUnsupportedActions);

        /*Configure CORS Mapping
         * https://www.baeldung.com/spring-cors
         * In any modern browser, Cross-Origin Resource Sharing (CORS) is a relevant specification with the emergence of HTML5 and JS clients that consume data via REST APIs.
         * Often, the host that serves the JS (e.g. example.com) is different from the host that serves the data (e.g. api.example.com). In such a case, CORS enables cross-domain communication.
         * Spring provides first-class support for CORS, offering an easy and powerful way of configuring it in any Spring or Spring Boot web application.*/
        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        //Apply to Repo for the class passed in...
        config.getExposureConfiguration().
                forDomainType(theClass).
                //Single item to disable
                withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions)).
                //Collection to disable
                withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }
}

