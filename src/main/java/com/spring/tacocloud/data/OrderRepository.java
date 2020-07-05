package com.spring.tacocloud.data;


import com.spring.tacocloud.Order;

public interface OrderRepository {
    Order save(Order order);
}
