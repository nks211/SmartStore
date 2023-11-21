import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store/service/OrderService.dart';
import 'package:smart_store/util/common.dart';

import '../dto/OrderDetail.dart';
import '../dto/OrderDetailitem.dart';
import '../dto/Orderitem.dart';
import '../start/page_router.dart';

//주문 옵션 토클 버튼
Widget toggleBtn(String content, bool isSelected, Function onPressed1) {
  return Container(
    padding: const EdgeInsets.only(top: 10, bottom: 10),
    width: 100,
    child: ElevatedButton(
      onPressed: () {
        onPressed1();
      },
      style: ElevatedButton.styleFrom(
          backgroundColor: isSelected ? coffeeBackground : Colors.white,
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(5),
              side: const BorderSide(width: 1))),
      child:
          Text(content, style: textStyle15Bold.apply(color: coffeeDarkBrown)),
    ),
  );
}

// 주문 상세 목록 생성 및 삭제 기능 구현
Widget shoppinglist(List<OrderDetailitem> items, Function function) {
  return Expanded(
    child: Padding(
      padding: EdgeInsets.all(10),
      child: SizedBox(
          height: 150,
          child: ListView.builder(
            itemCount: items.length,
            itemBuilder: (BuildContext context, int position) {
              return orderedItem(items[position], function, btnVisible: true);
            },
          )),
    ),
  );
}

class ShoppingCart extends StatefulWidget {
  List<OrderDetailitem> neworder;

  ShoppingCart({required this.neworder});

  @override
  State<StatefulWidget> createState() => _ShoppingCart();
}

class _ShoppingCart extends State<ShoppingCart> {
  var inShop = false;

  void toggle() {
    setState(() {
      inShop = !inShop;
    });
  }

  void remove(OrderDetailitem item) {
    setState(() {
      widget.neworder.remove(item);
    });
  }

  var orderservice = OrderService();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Center(
          child: Column(
            children: [
              Row(
                children: [
                  Expanded(
                      child: Padding(
                    padding: const EdgeInsets.all(10),
                    child: Text("장바구니",
                        style: textStyle30.apply(color: coffeePointRed)),
                  )),
                  toggleBtn("매장", inShop, toggle),
                  toggleBtn("T-Out", !inShop, toggle),
                ],
              ),
              shoppinglist(widget.neworder, remove),
              Padding(
                padding: EdgeInsets.only(left: 8, right: 8),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text('총 ${totalquantity(widget.neworder)}개'),
                    Text("${totalprice(widget.neworder)}원"),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.only(bottom: 10),
                child: Row(
                  children: [
                    Expanded(
                        flex: 1,
                        child: OutlinedButton(
                            onPressed: () {
                              var completed = 'N';
                              var ordertable = '웹주문';
                              var realtime =
                                  DateTime.fromMillisecondsSinceEpoch(
                                      DateTime.now().millisecond + 32400000);
                              var ordertime =
                                  DateFormat('yyyy-MM-ddThh:mm:ss.000+00:00')
                                      .format(realtime);
                              Future<SharedPreferences> preferences =
                                  SharedPreferences.getInstance();
                              preferences.then((value) {
                                if (value.getString('id') != null) {
                                  var userid = value.getString('id')!;
                                  Orderitem newitem = Orderitem(0, userid,
                                      ordertable, ordertime, completed);
                                  newitem.setDetails(widget.neworder);
                                  newitem.jsonDetails();
                                  print(jsonEncode(newitem.toJson()));
                                  orderservice.makeOrder(newitem).then((value) {
                                    if (value != '') {
                                      showToast('주문이 완료되었습니다.');
                                      Navigator.pop(context, 'OK');
                                    } else {
                                      showToast('주문 오류');
                                    }
                                  });
                                }
                              });
                            },
                            style: OutlinedButton.styleFrom(
                                backgroundColor: coffeePointRed,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(0),
                                )),
                            child: Padding(
                                padding: const EdgeInsets.all(10),
                                child: Text("주문하기",
                                    style: textStyle20.apply(
                                        color: Colors.white)))))
                  ],
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
