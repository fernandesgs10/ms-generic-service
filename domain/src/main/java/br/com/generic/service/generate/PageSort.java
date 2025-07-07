package br.com.generic.service.generate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class PageSort {

    public static void main(String args[]) {
        List<String> list = new ArrayList<>();
        list.add("nome");
        //list.add("desc");

        var test = getOrders(list);
        List<Sort.Order> list1 = new ArrayList<>();
        test.forEach(test2 -> {
            if(!test2.toString().equalsIgnoreCase("desc: ASC")) {
                list1.add(test2);
            }
        });

        System.out.println(list1);

    }

    public static List<Sort.Order> getSortFormat(List<Sort.Order> list) {
        List<Sort.Order> list1 = new ArrayList<>();
        list.forEach(test2 -> {
            if(!test2.toString().equalsIgnoreCase("desc: ASC")) {
                list1.add(test2);
            }
        });

        return list;
    }

    public static StringBuilder formatSort(List<String> properties) {
        StringBuilder str = new StringBuilder();
        if(properties != null) {
            final int[] count = {0};
            properties.forEach(sortBys -> {
                count[0]++;
                str.append("sortBy=" + sortBys);
                if (count.length > 0)
                    str.append("&");
            });
        }

        return str;
    }

    public static List<Sort.Order> getOrders(List<String> properties) {
        Sort sort = Sort.by(new Sort.Order[0]);

        List<Sort.Order> sortOrders = new ArrayList<>();
        boolean isPagination = false;

        if (properties != null) {
            for (int i=0; i<=properties.size();i++) {
                if (properties.size() > 1) {
                    isPagination = true;
                    try {
                        if (properties.get(i).trim().equalsIgnoreCase("desc")) {
                            sortOrders.add(Sort.by(properties.get(i - 1)).descending().getOrderFor(properties.get(i - 1)));
                    } if (properties.get(i).trim().equalsIgnoreCase("asc")) {
                        sortOrders.add(Sort.by(properties.get(i - 1)).ascending().getOrderFor(properties.get(i - 1)));
                    }
                    }catch (Exception ex) {
                        return sortOrders;
                    }
                }
            }

            if(!isPagination) {
                for (String property : properties) {
                    String[] split = property.split(",");
                    if (split.length > 1 && split[1].trim().equalsIgnoreCase("desc")) {
                        System.out.println();
                        sortOrders.add(Sort.by(split[0]).descending().getOrderFor(split[0]));
                    } else {
                        sortOrders.add(Sort.by(split[0]).ascending().getOrderFor(split[0]));
                    }
                }
            }
        }

        return sortOrders;


    }

    public static Pageable ajustarSort(Pageable pageable, Map<String, String> orderMap) {

        List<Sort.Order> orders = pageable.getSort().stream().map(order -> criarOrder(order, orderMap)).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
    }

    private static Sort.Order criarOrder(Sort.Order order, Map<String, String> orderMap) {

        String propriedade = order.getProperty();

        if (orderMap.containsKey(propriedade)) {

            if (StringUtils.isBlank(orderMap.get(propriedade))) {
                return null;
            }

            propriedade = orderMap.get(propriedade);
        }

        return new Sort.Order(order.getDirection(), propriedade);
    }
}
