package com.driver;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository= new OrderRepository();

    void addOrder(Order order){
        orderRepository.addOrder(order);
    }
    void addPartner(String partnerId){
        DeliveryPartner partner= new DeliveryPartner(partnerId);
        orderRepository.addPartner(partner);
    }
    void addOrderPartnerPair(String orderId, String partnerId){
        orderRepository.addOrderPartnerPair(orderId,partnerId);
    }
    Order getOrderById(String orderId){
        return orderRepository.getOrderById(orderId);
    }
    DeliveryPartner getPartnerById(String partnerId){
        return orderRepository.getPartnerById(partnerId);
    }
    Integer getOrderCountByPartnerId(String partnerId){
        return orderRepository.getOrderCountByPartnerId(partnerId);
    }
    List<String> getOrdersByPartnerId(String partnerId){
        return orderRepository.getOrdersByPartnerId(partnerId);
    }
    List<String> getAllOrders(){
        return orderRepository.getAllOrders();
    }
    Integer getCountOfUnassignedOrders(){
        return orderRepository.getCountOfUnassignedOrders();
    }
    Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int deliveryTime= Integer.parseInt(time.substring(0,2))*60+Integer.parseInt(time.substring(3,5));
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(deliveryTime, partnerId);
    }
    String getLastDeliveryTimeByPartnerId(String partnerId){
        int deliveryTime= orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
        String hours= Integer.toString(deliveryTime/60);
        if(hours.length()==1)
            hours='0'+hours;
        String mins= Integer.toString(deliveryTime%60);
        if(mins.length()==1)
            mins='0'+mins;
        String time= hours+':'+mins;
        return time;
    }
    void deletePartnerById(String partnerId){
        orderRepository.deletePartnerById(partnerId);
    }
    void deleteOrderById(String orderId){
        orderRepository.deleteOrderById(orderId);
    }
}