package com.ssafy.cafe.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.cafe.model.dto.Order;
import com.ssafy.cafe.model.service.FCMService;
import com.ssafy.cafe.model.service.OrderService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/order")
@CrossOrigin("*")
public class OrderRestController {
    @Autowired
    private OrderService oService;
    
    @Autowired
    private FCMService fService;
    
    @PostMapping
    @ApiOperation(value="order 객체를 저장하고 추가된 Order의 id를 반환한다.", 
    		notes = "<pre>아래 형태로 입력하면 주문이 입력된다. \r\n"
    				+ "'id 02' 고객이 1번 상품(아메리카노)를 2개 주문함. \r\n"
    				+"{\r\n"
    				+ "  \"completed\": \"N\",\r\n"
    				+ "  \"details\": [\r\n"
    				+ "    {\r\n"
    				+ "      \"productId\": 1,\r\n"
    				+ "      \"quantity\": 2\r\n"
    				+ "    }\r\n"
    				+ "  ],\r\n"
    				+ "  \"orderTable\": \"웹주문\",\r\n"
    				+ "  \"userId\": \"id 02\"\r\n"
    				+ "} "
    				+ "</pre>", response = Integer.class )
    public Integer makeOrder(@RequestBody Order order) throws IOException {
        oService.makeOrder(order);
        String token = "fv2kNDQxQ6CagVDq3_jeLG:APA91bGtwSFP4XLnb_kzQsYY1Uld8Y2wTk_zD-n2Hxu7XQyNXFgcx_1HeLkcJGjyz-2ePrLfISvCMMJ9lYOZ0zdIVde-cTcHx-KMzylwbd5pu7stAnXk_cY1RjF08XbkeRrbMRejQyUA";
//        String token = "d7lriZ0fRVyR2hvSqYK-XV:APA91bH-auF2EhcIaiGEZaAcx9X4SPN-vjMeFpO33qs-QiwSJnb8OSvRdseaPtIlL2F3J7bO8I9zC_9QdSw9RGQGDhjBKyQwA-7ZJm1vZwLutzpL06Dwja5M3AGkH2QJwU87i7E7J-Au";
        fService.sendMessageTo(token, "makeOrder", "body");
        return order.getId();
    }
    
    @GetMapping("/{orderId}")
    @ApiOperation(value="{orderId}에 해당하는 주문의 상세 내역을 목록 형태로 반환한다."
            + "이 정보는 사용자 정보 화면의 주문 내역 조회에서 사용된다."
    		, response = List.class)
    public List<Map> getOrderDetail(@PathVariable Integer orderId) {
        return oService.selectOrderTotalInfo(orderId);
    }
    
    @GetMapping("/byUser")
    @ApiOperation(value="{id}에 해당하는 사용자의 최근 1개월간 주문 내역을 반환한다."
            + "반환 정보는 1차 주문번호 내림차순, 2차 주문 상세 내림차순으로 정렬된다.", 
            notes = "6단계에서 사용됨.", response = List.class)
    public List<Map<String, Object>> getLastMonthOrder(String id) {
        return oService.getLastMonthOrder(id);
    }    
    
    @GetMapping("/byUserIn6Months")
    @ApiOperation(value="{id}에 해당하는 사용자의 최근 6개월간 주문 내역을 반환한다."
            + "반환 정보는 1차 주문번호 내림차순, 2차 주문 상세 내림차순으로 정렬된다.", 
            notes = "6단계에서 사용됨.", response = List.class)
    public List<Map<String, Object>> getLast6MonthOrder(String id) {
        return oService.getLast6MonthOrder(id);
    }   
    
    @GetMapping("/allOrdersByResult")
    @ApiOperation(value="{result}가 Y이면 완료된 주문을, N이면 완료되지 않은 주문들을 반환한다.", response = List.class)
    public List<Map<String, Object>> getAllCompletedOrder(String result){
    	return oService.getAllCompletedOrder(result);
    }
    
    @PutMapping
    @ApiOperation(value="주문을 완료한다. completed = Y가 된다", response = Boolean.class)
    public Boolean completeOrder(@RequestBody Order order) throws IOException {
    	oService.updateOrder(order);
    	String token = "fv2kNDQxQ6CagVDq3_jeLG:APA91bGtwSFP4XLnb_kzQsYY1Uld8Y2wTk_zD-n2Hxu7XQyNXFgcx_1HeLkcJGjyz-2ePrLfISvCMMJ9lYOZ0zdIVde-cTcHx-KMzylwbd5pu7stAnXk_cY1RjF08XbkeRrbMRejQyUA";
//    	String token = "d7lriZ0fRVyR2hvSqYK-XV:APA91bH-auF2EhcIaiGEZaAcx9X4SPN-vjMeFpO33qs-QiwSJnb8OSvRdseaPtIlL2F3J7bO8I9zC_9QdSw9RGQGDhjBKyQwA-7ZJm1vZwLutzpL06Dwja5M3AGkH2QJwU87i7E7J-Au";
        fService.sendMessageTo(token, "makeOrder", "body");
    	return true;
    }
}
