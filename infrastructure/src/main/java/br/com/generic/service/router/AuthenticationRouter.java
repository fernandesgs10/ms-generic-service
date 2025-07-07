package br.com.generic.service.router;

import br.com.generic.service.exchange.AuthenticationExchange;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthenticationRouter {

    private final AuthenticationExchange accountPayExchange;

    public static final String ROUTE_LOGIN = "direct:login";
    public static final String ROUTE_REFRESH_TOKEN = "direct:refreshToken";


    public RouteBuilder getRoute(String route) {
        switch (route) {
            case ROUTE_LOGIN:
                return login();
            case ROUTE_REFRESH_TOKEN:
                return refreshToken();
            default:
                throw new IllegalArgumentException("Invalid route: " + route);
        }
    }

    public RouteBuilder login() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_LOGIN)
                        .bean(accountPayExchange, "login");
            }
        };
    }



    public RouteBuilder refreshToken() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_REFRESH_TOKEN)
                        .bean(accountPayExchange, "refreshToken");
            }
        };
    }


}
