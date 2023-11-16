import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:smart_store_flutter_starter/menuorder/map.dart';
import 'package:smart_store_flutter_starter/menuorder/shopping_cart.dart';
import 'package:smart_store_flutter_starter/util/common.dart';

class Menu extends StatefulWidget {
  const Menu({super.key});

  @override
  State<Menu> createState() => _MenuState();
}

class _MenuState extends State<Menu> {

  @override
  Widget build(BuildContext context) {

    List menuboard = List.generate(11, (index) => menuImageButton("assets/coffee${index+1}.png", context)).toList();

    return Scaffold(
      body: Container(
        alignment: Alignment.topCenter,
        margin: EdgeInsets.symmetric(vertical: 40, horizontal: 20),
        child: Column(
          children: [
            Container(
              child: Row(
                children: [
                  Text('매장과의 거리가 350m입니다.', style: textStyle20,),
                  InkWell(
                    onTap: () {
                      Navigator.push(context, MaterialPageRoute(builder: (context) => CafeMap()));
                    },
                    child: Container(
                      margin: EdgeInsets.symmetric(horizontal: 20),
                      width: 30,
                      height: 30,
                      child: Image.asset("assets/map.png", fit: BoxFit.fill,),
                    ),
                  ),
                ],
              ),
            ),
            Align(
              alignment: Alignment.topLeft,
              child: Padding(
                padding: const EdgeInsets.symmetric(vertical: 20),
                child: Text('MENU', style: textStyleRed30,),
              ),
            ),
            Expanded(
                child: GridView.count(
                  crossAxisCount: 3,
                  mainAxisSpacing: 10,
                  crossAxisSpacing: 10,
                  childAspectRatio: 1,
                  children: List.generate(menuboard.length,
                          (int index) => menuboard[index]),
                ),
            ),
          ],
        ),
      ),
      floatingActionButton: ElevatedButton(
        onPressed: (){
          Navigator.push(context, MaterialPageRoute(builder: (context)=> ShoppingCart()));
        },
        style: ElevatedButton.styleFrom(
          shape: CircleBorder(),
          backgroundColor: coffeePointRed,
        ),
        child: Container(
          padding: EdgeInsets.all(10),
          width: 60,
          height: 60,
          child: Image.asset("assets/shopping_cart.png", fit: BoxFit.contain,),
        ),
      ),
    );
  }
}
