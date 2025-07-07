package br.com.generic.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionRouterEnum {

    ROUTE_PUSH_MONEY("direct:sacarDinheiro"),

    ROUTE_LIST_TRANSACTION_BY_DATA("direct:listarTransacaoPorData"),

    ROUTE_LIST_TRANSACTION_PULL_MONEY_BY_NUM_VALUE("direct:listTransacaoDepositarDinheiroPorNumValor"),

    ROUTE_LIST_TRANSACTION_PUSH_MONEY_BY_NUM_VALUE("direct:listTransacaoSaqueDinheiroPorNumValor"),

    ROUTE_PULL_MONEY("direct:depositarDinheiro");

    private final String name;



}
