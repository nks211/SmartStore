import 'package:flutter/material.dart';
import 'package:smart_store_flutter_starter/util/common.dart';
import 'package:smart_store_flutter_starter/dto/order_detail.dart';

//주문 옵션 토클 버튼
Widget toggleBtn(String content, bool isSelected, Function onPressed1 ){
  return Container(
    padding: const EdgeInsets.only(top: 10, bottom: 10),
    width: 100,
    child: ElevatedButton(
      onPressed: (){onPressed1();},
      style: ElevatedButton.styleFrom(
          backgroundColor: isSelected ? coffeeBackground:Colors.white,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(5),
            side: const BorderSide(width: 1)
          )
      ),
      child: Text(content, style: textStyle15Bold.apply(color: coffeeDarkBrown)),
    ),
  );
}

class ShoppingCart extends StatefulWidget{
  @override
  State<StatefulWidget> createState() => _ShoppingCart();
}

class _ShoppingCart extends State<ShoppingCart> {
  var items = testDetails;
  var inShop = false;

  void toggle(){
    setState(() {
      inShop = !inShop;
    });
  }

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
                      child: Text("장바구니", style: textStyle30.apply(color: coffeePointRed)),
                    )
                  ),
                  toggleBtn("매장", inShop, toggle),
                  toggleBtn("T-Out", !inShop,toggle)
                ],
              ),
              Expanded(
                flex: 1,
                child: Padding(
                  padding: const EdgeInsets.all(10),
                  child: SizedBox(
                      height: 150,
                      child : ListView.builder(
                        itemCount: items.length,
                        itemBuilder: (BuildContext context, int position){
                          return orderedItem(items[position], btnVisible: true);
                        },
                      )
                  ),
                ),
              ),
              const Padding(
                padding: EdgeInsets.only(left: 8, right: 8),
                child: Row(
                  children: [
                    Expanded(flex: 1, child: Text("총 3개")),
                    Text("12500원")
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
                        onPressed: (){},
                        style: OutlinedButton.styleFrom(
                            backgroundColor: coffeePointRed,
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(0),
                            )
                        ),
                        child: Padding(
                            padding: const EdgeInsets.all(10),
                            child: Text("주문하기", style: textStyle20.apply(color: Colors.white))
                        )
                      )
                    )
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
