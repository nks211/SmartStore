import 'package:flutter/material.dart';
import 'package:smart_store_flutter_starter/dto/order_detail.dart';
import 'package:smart_store_flutter_starter/util/common.dart';

class OrderDetailPage extends StatelessWidget {

  final List<OrderDetail> items;

  const OrderDetailPage(this.items, {super.key});

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
                      child: Text("주문상세", style: textStyle30.apply(color: coffeePointRed)),
                    ),
                  ],
                ),
                Padding(
                    padding: const EdgeInsets.only(top: 15, bottom: 15),
                    child: Row(
                      children: [
                        Expanded(

                          child: Text(
                              "주문완료\n2021.05.01\n12500원", style: textStyle20.apply(color: coffeeDarkBrown, fontFamily: 'eland_choice_b')),
                        ),
                      ],
                    )
                ),

                Expanded(
                  child: SizedBox(
                      height: 150,
                      child : ListView.builder(
                        itemCount: items.length,
                        itemBuilder: (BuildContext context, int position){
                          return orderedItem(items[position]);
                        },
                      )
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}


