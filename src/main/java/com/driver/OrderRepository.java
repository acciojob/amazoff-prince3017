package com.driver;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private HashMap<String,Order> orderDb= new HashMap<>();
    private HashMap<String,DeliveryPartner> deliveryPartnerDb= new HashMap<>();
    private HashMap<String,List<String>> orderDeliveryPartnerDb= new HashMap<>();
    private HashMap<String,Order> unassignedOrderDb= new HashMap<>();

    void addOrder(Order order){
        String id= order.getId();
        orderDb.put(id,order);
        unassignedOrderDb.put(id,order);
    }
    void addPartner(DeliveryPartner partner){
        String id= partner.getId();
        deliveryPartnerDb.put(id,partner);
    }
    void addOrderPartnerPair(String orderId, String partnerId){
        if(orderDb.containsKey(orderId) && deliveryPartnerDb.containsKey(partnerId))
        {
            //Adding to deliveryPartnerDb
            List<String> orders= new ArrayList<>();
            if(orderDeliveryPartnerDb.containsKey(partnerId))
            {
                orders= orderDeliveryPartnerDb.get(partnerId);
            }
            orders.add(orderId);
            orderDeliveryPartnerDb.put(partnerId,orders);

            //Incrementing noOfOrders of delivery partner
            DeliveryPartner partner= deliveryPartnerDb.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders()+1);

            //Removing order from unassigned orders db
            if(unassignedOrderDb.containsKey(orderId))
                unassignedOrderDb.remove(orderId);
        }
    }
    Order getOrderById(String orderId){
        Order order= orderDb.get(orderId);
        return order;
    }
    DeliveryPartner getPartnerById(String partnerId){
        DeliveryPartner partner= deliveryPartnerDb.get(partnerId);
        return partner;
    }
    Integer getOrderCountByPartnerId(String partnerId){
        List<String> orders= new ArrayList<>();
        if(orderDeliveryPartnerDb.containsKey(partnerId))
        {
            orders= orderDeliveryPartnerDb.get(partnerId);
        }
        return orders.size();
    }
    List<String> getOrdersByPartnerId(String partnerId){
        List<String> orders= new ArrayList<>();
        if(orderDeliveryPartnerDb.containsKey(partnerId))
        {
            orders= orderDeliveryPartnerDb.get(partnerId);
        }
        return orders;
    }
    List<String> getAllOrders(){
        List<String> orders= new ArrayList<>();
        for(Order order: orderDb.values())
        {
            orders.add(order.getId());
        }
        return orders;
    }
    Integer getCountOfUnassignedOrders(){
        return unassignedOrderDb.size();
    }
    Integer getOrdersLeftAfterGivenTimeByPartnerId(Integer time, String partnerId) {
        List<String> orders= new ArrayList<>();
        int count=0;
        if(orderDeliveryPartnerDb.containsKey(partnerId))
        {
            orders= orderDeliveryPartnerDb.get(partnerId);
            for(int i=0; i<orders.size(); i++)
            {
                Order order= orderDb.get(orders.get(i));
                if(order.getDeliveryTime()>time)
                    count++;
            }
        }
        return count;
    }
    Integer getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orders= new ArrayList<>();
        int maxTime= 0;
        if(orderDeliveryPartnerDb.containsKey(partnerId))
        {
            orders= orderDeliveryPartnerDb.get(partnerId);
            for(int i=0; i<orders.size(); i++)
            {
                Order order= orderDb.get(orders.get(i));
                if(order.getDeliveryTime()>maxTime)
                {
                    maxTime= order.getDeliveryTime();
                }
            }
        }
        return maxTime;
    }
    void deletePartnerById(String partnerId){
        List<String> orders= new ArrayList<>();
        //adding all orders of delivery partners in unassigned orders db
        if(orderDeliveryPartnerDb.containsKey(partnerId))
        {
            orders= orderDeliveryPartnerDb.get(partnerId);
            for(int i=0; i<orders.size(); i++)
            {
                Order order= orderDb.get(orders.get(i));
                String id= order.getId();
                unassignedOrderDb.put(id,order);
            }
        }
        //removing delivery partner from order delivery partner pair db
        orderDeliveryPartnerDb.remove(partnerId);

        //removing delivery partner from delivery partner db
        deliveryPartnerDb.remove(partnerId);
    }
    void deleteOrderById(String orderId){
        //Deleting order from orderdb
        if(orderDb.containsKey(orderId))
            orderDb.remove(orderId);

        //Removing order from delivery partner order pair
        for(String partnerId: orderDeliveryPartnerDb.keySet())
        {
            List<String> orders= orderDeliveryPartnerDb.get(partnerId);
            if(orders.contains(orderId))
            {
                orders.remove(orderId);
                return;
            }
        }

        //if order is not assigned then removing from unassigned db
        if(unassignedOrderDb.containsKey(orderId))
        {
            unassignedOrderDb.remove(orderId);
        }
    }
}