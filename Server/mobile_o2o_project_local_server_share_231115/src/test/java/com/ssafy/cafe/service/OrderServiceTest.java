package com.ssafy.cafe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.dto.OrderDetail;
import com.ssafy.cafe.model.service.FCMService;


class OrderServiceTest extends AbstractServiceTest {
	
	@Autowired
	FCMService fcmservice;
	

    @Test
    void sendMessage() throws IOException{
    	String token = "fv2kNDQxQ6CagVDq3_jeLG:APA91bGtwSFP4XLnb_kzQsYY1Uld8Y2wTk_zD-n2Hxu7XQyNXFgcx_1HeLkcJGjyz-2ePrLfISvCMMJ9lYOZ0zdIVde-cTcHx-KMzylwbd5pu7stAnXk_cY1RjF08XbkeRrbMRejQyUA";
    	fcmservice.sendMessageTo(token, "testtitle", "testbody");
    }

//    @Test
//    @org.junit.jupiter.api.Order(1)
//    void makeOrderTest() {
//        Order order = new Order(user.getId(), "table_test", new Date(), 'N');
//        List<OrderDetail> details = new ArrayList<>();
//        details.add(new OrderDetail(1, 1));
//        details.add(new OrderDetail(2, 2));
//        details.add(new OrderDetail(2, 2));
//        order.setDetails(details);
//        
//        orderService.makeOrder(order);
//    }
//    
//    static Order last;
//    @Test
//    @org.junit.jupiter.api.Order(2)
//    void getOrderByUserTest() {
//        List<Order> orders = orderService.getOrdreByUser(user.getId());
//        System.out.println(last);
//        last = orders.get(0);
//        assertEquals(last.getOrderTable(), "table_test");
//        
//        orders = orderService.getOrdreByUser("id 02");
//        assertEquals(orders.size(), 0);
//    }
//    
//    
//    @Test
//    @org.junit.jupiter.api.Order(3)
//    void getOrderWithDetailsTest() {
//        Order order = orderService.getOrderWithDetails(last.getId());
//        assertEquals(order.getUserId(), last.getUserId());
//        assertEquals(order.getOrderTable(), last.getOrderTable());
//        List<OrderDetail> details = order.getDetails();
//        assertEquals(details.size(), 3);
//        assertEquals(details.get(0).getQuantity(), 2);
//        last = order;
//    }
//
//
//    
//    @Test
//    @org.junit.jupiter.api.Order(4)
//    void updateOrderTest() {
//        assertEquals(last.getCompleted(), 'N');
//        last.setCompleted('Y');
//        orderService.updateOrder(last);
//        Order order = orderService.getOrderWithDetails(last.getId());
//        assertEquals(order.getCompleted(), 'Y');
//    }
//    
//    @Test
//    @org.junit.jupiter.api.Order(5)
//    void deleteOrderTest() {
//        System.out.println("last: "+last);
//        List<OrderDetail> details = last.getDetails();
//        System.out.println("삭제 대상 목록: "+details);
//        for(OrderDetail detail: details) {
//            dDao.delete(detail.getId());
//        }
//        dDao.delete(last.getId());
//    }

}
