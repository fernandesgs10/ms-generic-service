package br.com.generic.service.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class SortConverterUtil {

    public static List<Sort.Order> getOrdersFromString(String sortBy) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sortBy != null && !sortBy.isBlank()) {
            String[] parts = sortBy.split(",");
            String property = parts[0].trim();
            Sort.Direction direction = Sort.Direction.ASC;
            if (parts.length > 1) {
                try {
                    direction = Sort.Direction.fromString(parts[1].trim());
                } catch (IllegalArgumentException e) {
                    log.warn("Direção inválida '{}', usando ASC", parts[1].trim());
                }
            }
            orders.add(new Sort.Order(direction, property));
        }

        if (orders.isEmpty()) {
            orders.add(Sort.Order.asc("id"));
        }

        return orders;
    }

}
