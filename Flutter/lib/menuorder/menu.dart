import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store/menuorder/map.dart';
import 'package:smart_store/menuorder/shopping_cart.dart';
import 'package:smart_store/service/ProductService.dart';
import 'package:smart_store/util/common.dart';

import '../dto/OrderDetailitem.dart';
import '../dto/Product.dart';
import '../start/page_router.dart';

class Menu extends StatefulWidget {
  const Menu({super.key});

  @override
  State<Menu> createState() => _MenuState();
}

class _MenuState extends State<Menu> {
  List<Product> menulist = [];
  var productservice = ProductService();

  // 초기 메뉴판 위젯 구성
  Widget board = Container();

  List<OrderDetailitem> shoppingOrder = [];

  void addOrder(OrderDetailitem orderdetail) {
    setState(() {
      shoppingOrder.add(orderdetail);
    });
  }

  @override
  Widget build(BuildContext context) {
    // 서버에서 메뉴 정보 가져와서 다시 이미지 구성함
    productservice.getproductmenu().then((value) {
      menulist = value;
      setState(() {
        board = GridView.count(
          crossAxisCount: 3,
          mainAxisSpacing: 10,
          crossAxisSpacing: 10,
          childAspectRatio: 1,
          children: List.generate(
              menulist.length,
              (int index) =>
                  menuImageButton(menulist[index], context, addOrder)),
        );
      });
    }).catchError((e) {
      board = GridView.count(
        crossAxisCount: 3,
        mainAxisSpacing: 10,
        crossAxisSpacing: 10,
        childAspectRatio: 1,
        children:
            List.generate(12, (int index) => roundImage("assets/logo.png")),
      );
    });

    return Scaffold(
      body: Container(
        alignment: Alignment.topCenter,
        margin: EdgeInsets.symmetric(vertical: 40, horizontal: 20),
        child: Column(
          children: [
            Container(
              child: Row(
                children: [
                  Text(
                    '매장과의 거리가 350m입니다.',
                    style: textStyle20,
                  ),
                  InkWell(
                    onTap: () {
                      Navigator.push(context,
                          MaterialPageRoute(builder: (context) => CafeMap()));
                    },
                    child: Container(
                      margin: EdgeInsets.symmetric(horizontal: 20),
                      width: 30,
                      height: 30,
                      child: Image.asset(
                        "assets/map.png",
                        fit: BoxFit.fill,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            Align(
              alignment: Alignment.topLeft,
              child: Padding(
                padding: const EdgeInsets.symmetric(vertical: 20),
                child: Text(
                  'MENU',
                  style: textStyleRed30,
                ),
              ),
            ),
            Expanded(
              child: board,
            ),
          ],
        ),
      ),
      floatingActionButton: ElevatedButton(
        onPressed: () async {
          var answer = await Navigator.push(
              context,
              MaterialPageRoute(
                  builder: (context) => ShoppingCart(
                        neworder: shoppingOrder,
                      )));
          if (answer == 'OK') {
            setState(() {
              shoppingOrder.clear();
              Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => PageRouter()));
            });
          }
        },
        style: ElevatedButton.styleFrom(
          shape: CircleBorder(),
          backgroundColor: coffeePointRed,
        ),
        child: Container(
          padding: EdgeInsets.all(10),
          width: 60,
          height: 60,
          child: Image.asset(
            "assets/shopping_cart.png",
            fit: BoxFit.contain,
          ),
        ),
      ),
    );
  }
}
