import 'package:flutter/material.dart';
import 'package:smart_store/dto/OrderDetailitem.dart';
import 'package:smart_store/menuorder/shopping_cart.dart';
import 'package:smart_store/service/OrderService.dart';
import 'package:smart_store/service/UserService.dart';
import 'package:smart_store/util/common.dart';

import '../dto/Orderitem.dart';
import '../dto/User.dart';
import '../start/page_router.dart';

class Main extends StatefulWidget {
  final User user;
  final List<Orderitem> orderdata;

  Main({required this.user, required this.orderdata});

  @override
  State<Main> createState() => _MainState();
}

class _MainState extends State<Main> {
  //테스트용 데이터
  var noticeItem = List.generate(3, (i) => "알림$i");

  var userservice = UserService();
  var orderservice = OrderService();
  Widget orderlist = Container(); //주문 내역 위젯 초기화

  void reorder(Orderitem item) async {
    var answer = await Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => ShoppingCart(
                  neworder: item.details,
                )));
    if (answer == 'OK') {
      setState(() {
        Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => PageRouter()));
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    // 주문 id별 상세내역 추가한 후 리스트로 반환
    for (var order in widget.orderdata) {
      orderservice.getOrderDetails(order.id).then((value) {
        order.setDetails(value);
        setState(() {
          orderlist = orderListRow(
              widget.orderdata,
              SizedBox(
                  height: 25, child: Image.asset('assets/shopping_cart.png')),
              reorder,
              height: 310);
        });
      });
    }

    return Scaffold(
      body: SafeArea(
        child: Center(
          child: Padding(
            padding: const EdgeInsets.all(20),
            child: Column(
              children: [
                Row(
                  children: [
                    Expanded(
                      flex: 1,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text("${widget.user.name}님",
                              style: textStyle30.apply(color: coffeeDarkBrown)),
                          Row(
                            children: [
                              Text("좋은 하루 보내세요.",
                                  style: textStyle20.apply(color: coffeeBrown)),
                              SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: Image.asset('assets/smile.png'))
                            ],
                          )
                        ],
                      ),
                    ),
                  ],
                ),
                Padding(
                    padding: const EdgeInsets.only(top: 20, bottom: 10),
                    child: Row(
                      children: [
                        Text("알림판",
                            style: textStyle30.apply(color: coffeeDarkBrown)),
                      ],
                    )),
                Expanded(
                  flex: 1,
                  child: Card(
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10),
                        side:
                            const BorderSide(width: 2, color: coffeeDarkBrown)),
                    child: Container(
                      padding: const EdgeInsets.only(left: 20, right: 20),
                      child: noticeScroll(noticeItem),
                    ),
                  ),
                ),
                Padding(
                    padding: const EdgeInsets.only(top: 15, bottom: 15),
                    child: Row(
                      children: [
                        Text("최근 주문",
                            style: textStyle30.apply(color: coffeeDarkBrown)),
                      ],
                    )),
                orderlist,
              ],
            ),
          ),
        ),
      ),
    );
  }
}
