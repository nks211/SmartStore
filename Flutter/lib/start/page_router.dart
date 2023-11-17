import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store_flutter_starter/mypage/user_info.dart';
import 'package:smart_store_flutter_starter/util/common.dart';

import 'package:smart_store_flutter_starter/home/main.dart';
import 'package:smart_store_flutter_starter/menuorder/menu.dart';

import '../dto/Grade.dart';

class PageRouter extends StatefulWidget {
  List userdata = [];
  PageRouter(this.userdata, {Key? key}) : super(key: key);

  @override
  State<PageRouter> createState() => _PageRouterState();
}

class _PageRouterState extends State<PageRouter> {

  int _selected = 0;

  @override
  Widget build(BuildContext context) {

    List pages = [
      Main(user: widget.userdata[0], orderdata: widget.userdata[1],),
      Menu(),
      UserInfo(user: widget.userdata[0], orderdata: widget.userdata[1], usergrade: widget.userdata[2],)
    ];

    return Scaffold(
      body: SafeArea(
        child: pages[_selected],
      ),
      bottomNavigationBar: BottomNavigationBar(
        backgroundColor: coffeeBrown,
        items: [
          BottomNavigationBarItem(
              icon: Image.asset("assets/home.png", width: 30, color: _selected == 0? Colors.white : Colors.white60,),
              label: "Home",
          ),
          BottomNavigationBarItem(
            icon: Image.asset("assets/shopping_list.png", width: 30, color: _selected == 1? Colors.white : Colors.white60,),
            label: "Order",
          ),
          BottomNavigationBarItem(
            icon: Image.asset("assets/user.png", width: 30, color: _selected == 2? Colors.white : Colors.white60,),
            label: "Mypage",
          ),
        ],
        currentIndex: _selected,
        selectedItemColor: Colors.white,
        onTap: (index) { setState(() {
          _selected = index;
        }); },
      ),
    );
  }
}
