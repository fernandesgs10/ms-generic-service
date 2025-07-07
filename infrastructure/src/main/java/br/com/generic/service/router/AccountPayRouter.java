package br.com.generic.service.router;

import br.com.generic.service.exchange.AccountPayExchange;
import lombok.AllArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccountPayRouter {

    private final AccountPayExchange accountPayExchange;

    public static final String ROUTE_LOGIN = "direct:login";
    public static final String ROUTE_SAVE = "direct:save";
    public static final String ROUTE_UPDATE = "direct:update";
    public static final String ROUTE_FIND_ALL = "direct:findAll";

    public static final String ROUTE_FIND_DUE_DATE_DESCRIPTION = "direct:findByDueDateAndDescription";
    public static final String ROUTE_REMOVE = "direct:remove";
    public static final String ROUTE_FIND_USER_BY_ID = "direct:findUserById";
    public static final String ROUTE_REFRESH_TOKEN = "direct:refreshToken";
    public static final String ROUTE_VALIDATE_TOKEN = "direct:validateToken";
    public static final String ROUTE_SEND_MESSAGE_TOKEN = "direct:sendMessageToken";
    public static final String ROUTE_FIND_SUM_AMOUNT = "direct:findSumAmountByPaymentDate";

    public static final String ROUTE_UPLOAD_CSV = "direct:uploadCsv";




    public RouteBuilder getRoute(String route) {
        switch (route) {
            case ROUTE_LOGIN:
                return login();
            case ROUTE_SAVE:
                return save();
            case ROUTE_UPDATE:
                return update();
            case ROUTE_FIND_ALL:
                return findAll();
            case ROUTE_REMOVE:
                return remove();
            case ROUTE_FIND_USER_BY_ID:
                return findUserById();
            case ROUTE_REFRESH_TOKEN:
                return refreshToken();
            case ROUTE_VALIDATE_TOKEN:
                return validateToken();
            case ROUTE_SEND_MESSAGE_TOKEN:
                return sendMessageToken();
            case ROUTE_FIND_DUE_DATE_DESCRIPTION:
                return findByDueDateAndDescription();
            case ROUTE_FIND_SUM_AMOUNT:
                return findSumAmountByPaymentDate();
            case ROUTE_UPLOAD_CSV:
                return uploadCsv();
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

    public RouteBuilder save() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_SAVE)
                        .bean(accountPayExchange, "save");
            }
        };
    }

    public RouteBuilder update() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_UPDATE)
                        .bean(accountPayExchange, "update");
            }
        };
    }

    public RouteBuilder findAll() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_FIND_ALL)
                        .bean(accountPayExchange, "findAll");
            }
        };
    }

    public RouteBuilder findByDueDateAndDescription() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_FIND_DUE_DATE_DESCRIPTION)
                        .bean(accountPayExchange, "findByDueDateAndDescription");
            }
        };
    }

    public RouteBuilder remove() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_REMOVE)
                        .bean(accountPayExchange, "remove");
            }
        };
    }

    public RouteBuilder findUserById() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_FIND_USER_BY_ID)
                        .bean(accountPayExchange, "findUserById");
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

    public RouteBuilder validateToken() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_VALIDATE_TOKEN)
                        .bean(accountPayExchange, "validateToken");
            }
        };
    }

    public RouteBuilder sendMessageToken() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_SEND_MESSAGE_TOKEN)
                        .bean(accountPayExchange, "sendMessageToken");
            }
        };
    }

    public RouteBuilder findSumAmountByPaymentDate() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_FIND_SUM_AMOUNT)
                        .bean(accountPayExchange, "findSumAmountByPaymentDate");
            }
        };
    }

    public RouteBuilder uploadCsv() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from(ROUTE_UPLOAD_CSV)
                        .bean(accountPayExchange, "uploadCsv");
            }
        };
    }
}
