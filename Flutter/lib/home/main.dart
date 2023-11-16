import 'package:flutter/material.dart';
import 'package:smart_store_flutter_starter/menuorder/shopping_cart.dart';
import 'package:smart_store_flutter_starter/util/common.dart';
import 'package:smart_store_flutter_starter/dto/Order.dart';


class Main extends StatefulWidget {
  const Main({super.key});
  @override
  State<Main> createState() => _MainState();
}

class _MainState extends State<Main> {

  //테스트용 데이터
  var items = List.generate(5, (_) => Order('assets/coffee1.png', '아메리카노 외 3잔', 25000, '2023.11.15')).toList();
  var noticeItem = List.generate(3, (i) => "알림$i");

  void myOnTap(){
    Navigator.push(context, MaterialPageRoute(builder: (context)=> ShoppingCart()));
  }

  @override
  Widget build(BuildContext context) {
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
                          Text("김싸피님", style: textStyle30.apply(color: coffeeDarkBrown)),
                          Row(
                            children: [
                              Text("좋은 하루 보내세요.", style: textStyle20.apply(color: coffeeBrown)),
                              SizedBox(width: 20, height: 20, child: Image.asset('assets/smile.png'))
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
                        Text("알림판", style: textStyle30.apply(color: coffeeDarkBrown)),
                      ],
                    )
                ),
                Expanded(
                  flex: 1,
                  child: Card(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(10),
                      side: const BorderSide(width: 2, color: coffeeDarkBrown)
                    ),
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
                        Text("최근 주문", style: textStyle30.apply(color: coffeeDarkBrown)),
                      ],
                    )
                ),
                orderListRow(items, SizedBox(
                  height: 25,
                  child: Image.asset('assets/shopping_cart.png')
                ), myOnTap, height: 310)
              ],
            ),
          ),
        ),
      ),
    );
  }
}
