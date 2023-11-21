import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store/mypage/user_info.dart';
import 'package:smart_store/service/OrderService.dart';
import 'package:smart_store/service/UserService.dart';
import 'package:smart_store/util/common.dart';

import 'package:smart_store/home/main.dart';
import 'package:smart_store/menuorder/menu.dart';

import '../dto/Grade.dart';
import '../dto/Orderitem.dart';
import '../dto/User.dart';

class PageRouter extends StatefulWidget {
  const PageRouter({super.key});
  @override
  State<PageRouter> createState() => _PageRouterState();
}

class _PageRouterState extends State<PageRouter> {
  int _selected = 0;
  var userservice = UserService();
  var orderservice = OrderService();
  List userinfo = [];
  List pages = [
    Main(
      user: User.init(),
      orderdata: [],
    ),
    Menu(),
    UserInfo(user: User.init(), orderdata: [], usergrade: Grade.init())
  ];

  // 최초 로딩 및 페이지 이동 시 사용자 정보 갱신
  void getUserInfo() {
    setState(() {
      Future<SharedPreferences> preferences = SharedPreferences.getInstance();
      preferences.then((value) {
        String? id = value.getString('id');
        String? pass = value.getString('pass');
        if (id != null && pass != null) {
          User loginuser = User(id, pass, '');
          userservice.userInfo(loginuser).then((_) {
            var grade = Grade.fromJson(_['grade']);
            List<Orderitem> orders = [];
            for (var data in _['order'] as List) {
              orders.add(Orderitem.fromJson(data));
            }
            var name = User.fromJson(_['user']);
            setState(() {
              userinfo = [name, orders, grade];
              pages = [
                Main(
                  user: name,
                  orderdata: orders,
                ),
                Menu(),
                UserInfo(user: name, orderdata: orders, usergrade: grade),
              ];
            });
          });
        }
      });
    });
  }

  @override
  void initState() {
    setState(() {
      getUserInfo();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: pages[_selected],
      ),
      bottomNavigationBar: BottomNavigationBar(
        backgroundColor: coffeeBrown,
        items: [
          BottomNavigationBarItem(
            icon: Image.asset(
              "assets/home.png",
              width: 30,
              color: _selected == 0 ? Colors.white : Colors.white60,
            ),
            label: "Home",
          ),
          BottomNavigationBarItem(
            icon: Image.asset(
              "assets/shopping_list.png",
              width: 30,
              color: _selected == 1 ? Colors.white : Colors.white60,
            ),
            label: "Order",
          ),
          BottomNavigationBarItem(
            icon: Image.asset(
              "assets/user.png",
              width: 30,
              color: _selected == 2 ? Colors.white : Colors.white60,
            ),
            label: "Mypage",
          ),
        ],
        currentIndex: _selected,
        selectedItemColor: Colors.white,
        onTap: (index) {
          setState(() {
            getUserInfo();
            _selected = index;
          });
        },
      ),
    );
  }
}
