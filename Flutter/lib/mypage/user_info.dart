import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store/mypage/order_detail_page.dart';
import 'package:smart_store/mypage/user_detail_page.dart';
import 'package:smart_store/util/common.dart';

import '../dto/Grade.dart';
import '../dto/OrderDetailitem.dart';
import '../dto/Orderitem.dart';
import '../dto/User.dart';
import '../service/OrderService.dart';
import '../start/login.dart';

class UserInfo extends StatefulWidget {
  final User user;
  final List<Orderitem> orderdata;
  final Grade usergrade;

  UserInfo(
      {required this.user, required this.orderdata, required this.usergrade});

  @override
  _UserInfo createState() => _UserInfo();
}

class _UserInfo extends State<UserInfo> {

  var curLv = 0;
  var curSubLv = 1;
  var curExp = 1;
  var curMxExp = 10;
  var curLvImg = 'assets/seeds.png';
  var percent = 0.0;

  // 사용자 스탬프 개수 및 회원 등급 정보 초기화
  @override
  void initState() {
    var stamps = widget.user.stamps;

    var lvInfo = calculateStampLevel(stamps);
    curLv = lvInfo[0];
    curSubLv = lvInfo[1];
    curLvImg = levelimg[curLv];
    curMxExp = requiredStamps[curLv];
    curExp = curMxExp + lvInfo[2];
    percent = (curExp / curMxExp);
  }

  void myOrderDetails(Orderitem item) {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => OrderDetailPage(
                  item: item,
                )));
  }

  var orderservice = OrderService();
  Widget orderlist = Container(); //주문 내역 위젯 초기화

  TextEditingController controller = TextEditingController();

  @override
  Widget build(BuildContext context) {

    var path = platform == Platform.naver? 'assets/naverlogo.png'
        : 'assets/user.png';

    // 주문 id별 상세내역 추가한 후 리스트로 반환
    for (var order in widget.orderdata) {
      orderservice.getOrderDetails(order.id).then((value) {
        order.setDetails(value);
        setState(() {
          orderlist = orderListRow(
              widget.orderdata,
              OutlinedButton(
                  onPressed: () {},
                  style: OutlinedButton.styleFrom(
                      backgroundColor: coffeeBackground,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20),
                      )),
                  child: const Padding(
                      padding: EdgeInsets.all(10),
                      child: Text("픽업 완료", style: textStyle20))),
              myOrderDetails,
              height: 340);
        });
      });
    }

    return Scaffold(
      body: SingleChildScrollView(
        child: SafeArea(
          child: Center(
            child: Padding(
              padding: const EdgeInsets.all(10),
              child: Column(
                children: [
                  Row(
                    children: [
                      InkWell(
                        onTap: () {
                          showDialog(
                              context: context,
                              builder: (context) {
                                return AlertDialog(
                                  title: Text('회원 정보 확인을 위해 비밀번호를 입력하세요'),
                                  contentPadding: EdgeInsets.all(30),
                                  content: Container(
                                    height: 50,
                                    child: TextField(
                                      controller: controller,
                                      obscureText: true,
                                    ),
                                  ),
                                  actions: [
                                    TextButton(
                                      onPressed: () {
                                        Future<SharedPreferences> preference = SharedPreferences.getInstance();
                                        preference.then((value) {
                                          if (value.getString('pass') == controller.text) {
                                            controller.text = '';
                                            Navigator.pop(context);
                                            Navigator.push(context, MaterialPageRoute(builder: (context) => UserDetailPage(name: widget.user.name,)));
                                          }
                                          else {
                                            showToast('비밀번호가 일치하지 않습니다.');
                                          }
                                        });
                                      },
                                      child: Text('확인'),
                                    ),
                                  ],
                                );
                              }
                          );
                        },
                        child: SizedBox(
                          height: 60,
                          child: Image.asset(path),
                        ),
                      ),
                      Expanded(
                        flex: 1,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text("${widget.user.name}님",
                                style: textStyle30.apply(color: coffeeDarkBrown)),
                            Text("안녕하세요.",
                                style: textStyle20.apply(color: coffeeBrown))
                          ],
                        ),
                      ),
                      IconButton(
                          onPressed: () {
                            Future<SharedPreferences> preferences =
                            SharedPreferences.getInstance();
                            preferences.then((value) {
                              value.remove('id');
                              value.remove('pass');
                              value.setStringList('notice${widget.user.id}', noticeItem);
                              platform = Platform.none;
                              noticeItem = [];
                            });
                            Navigator.pushReplacement(context,
                                MaterialPageRoute(builder: (context) => Login()));
                          },
                          iconSize: 50,
                          icon: Image.asset(
                            'assets/logout.png',
                            width: 50,
                          ))
                    ],
                  ),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Row(
                      children: [
                        SizedBox(
                            height: 30, child: Image.asset('assets/seeds.png')),
                        Text("${levelName[curLv]} $curSubLv단계",
                            style: textStyle20Bold.apply(color: coffeeDarkBrown)),
                        Expanded(
                          flex: 1,
                          child: LinearProgressIndicator(
                            value: percent,
                            backgroundColor: Colors.grey,
                            valueColor: const AlwaysStoppedAnimation<Color>(
                                coffeePointRed),
                            minHeight: 6.0,
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(left: 3),
                          child: Text("$curExp/$curMxExp"),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left: 5),
                    child: Row(
                      children: [
                        Text("다음 레벨까지 ${curMxExp - curExp}잔 남았습니다.",
                            style: textStyle15Bold.apply(color: Colors.grey))
                      ],
                    ),
                  ),
                  const SizedBox(height: 5),
                  Image.asset('assets/space.png'),
                  Padding(
                      padding: const EdgeInsets.only(top: 30, bottom: 10),
                      child: Row(
                        children: [
                          Text("주문내역",
                              style: textStyle30.apply(color: coffeeDarkBrown)),
                        ],
                      )),
                  orderlist,
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
