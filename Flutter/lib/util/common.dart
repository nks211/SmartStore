import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:fluttertoast/fluttertoast.dart';
import '../dto/OrderDetailitem.dart';
import '../dto/Orderitem.dart';
import '../dto/Product.dart';
import '../menuorder/menudetail.dart';

// Color 정의
const Color coffeeBrown = Color(0xff907f60);
const Color coffeeBackground = Color(0xFFF0E3DB);
const Color coffeePointRed = Color(0xff9d0200);
const Color coffeeDarkBrown = Color(0xff493319);
const Color menuBackground = Color(0xffeee1da);

// TextStyle 정의
const TextStyle textLogin = TextStyle(fontFamily: 'cafe24', fontSize: 50.0);
const TextStyle textStyle15 =
    TextStyle(fontFamily: 'Montserrat', fontSize: 15.0);
const TextStyle textStyle15Bold = TextStyle(
    fontFamily: 'Montserrat', fontSize: 15.0, fontWeight: FontWeight.bold);
const TextStyle textStyle20 =
    TextStyle(fontFamily: 'Montserrat', fontSize: 20.0);
const TextStyle textStyle20Bold = TextStyle(
    fontFamily: 'Montserrat', fontSize: 20.0, fontWeight: FontWeight.bold);
const TextStyle textStyle20White =
    TextStyle(fontFamily: 'Montserrat', fontSize: 20.0, color: Colors.white);
const TextStyle textStyle25 =
    TextStyle(fontFamily: 'Montserrat', fontSize: 25.0);
const TextStyle textStyle20B = TextStyle(
    fontFamily: 'Montserrat', fontSize: 20.0, fontWeight: FontWeight.bold);
const TextStyle textStyle30 =
    TextStyle(fontFamily: 'eland_choice_b', fontSize: 30.0);
const TextStyle textStyle50 =
    TextStyle(fontFamily: 'Montserrat', fontSize: 50.0);
const TextStyle textStyleRed30 = TextStyle(
    fontFamily: 'Montserrat', fontSize: 30.0, color: Color(0xff9d0200));
const TextStyle textOrder = TextStyle(
    fontFamily: 'eland_choice_b', fontSize: 20, color: coffeeDarkBrown);

//http통신 주소값
const BaseUrl = "http://192.168.0.11:9987/";

//서버 내 이미지 저장 경로값. (ex. C:\Temp\imgs\menu)
const imagepath = "imgs/menu/";

//알림판에 표시되는 목록 데이터
List<String> noticeItem = [];

// 네이버 계정 로그인 판별용 상수값
enum Platform {
  naver,
  none,
}

Platform platform = Platform.none;

int distance = 350;

//fcm 메시지 전달 위한 고유 토큰값
String token = 'fbP2TvrRQYCMKV4gziMXB5:APA91bFhdWEFA0qFtlMagAtbOcE3EMgOJBSXUCHTrnVaI0Q3LKBFEQBFaSGqygjAHGWACtnPuTdP4Z4Eg1Y3WWfxtvo-fN11lz7vEx80IZNEQyblGqY7eqWABpy-Vj4xg1J-Qj4CKwnJ';

// toast message
void showToast(String msg) {
  Fluttertoast.showToast(
      msg: msg,
      toastLength: Toast.LENGTH_SHORT,
      gravity: ToastGravity.BOTTOM,
      timeInSecForIosWeb: 1,
      textColor: Colors.white,
      fontSize: 16.0);
}

//알림판 구성 및 항목 구성
Widget notice(String content, Function function) {
  return Row(
    children: [
      Expanded(flex: 1, child: Text(content, style: textStyle15)),
      IconButton(
          onPressed: () { function(content); },
          icon: const Icon(Icons.cancel, color: coffeeBrown))
    ],
  );
}

Widget noticeScroll(List<String> notices, Function function) {
  return ListView.builder(
    padding: EdgeInsets.zero,
    itemCount: notices.length,
    itemBuilder: (BuildContext context, int position) {
      return notice(notices[position], function);
    },
  );
}

// fcm 메시지 수신하여 알림창 띄우는 함수
void firebasemessage() async {
  FirebaseMessaging.onMessage.listen((RemoteMessage message) async {
    RemoteNotification? notification = message.notification;

    if (notification != null) {
      FlutterLocalNotificationsPlugin().show(
        notification.hashCode,
        notification.title,
        notification.body,
        const NotificationDetails(
          android: AndroidNotificationDetails(
            'high_importance_channel',
            'high_importance_notification',
            importance: Importance.max,
          ),
        ),
      );
    }
  });
}

//이미지 위젯
Widget roundImage(String src, {double height = 150, double width = 150}) {
  return ClipRRect(
    borderRadius: BorderRadius.circular(30),
    child: Container(
      height: height,
      width: width,
      color: coffeeBackground,
      child: Image.network(src),
    ),
  );
}

//메뉴판 이미지 위젯 (roundImage 활용)
Widget menuImageButton(Product item, BuildContext context, Function function) {
  return InkWell(
    onTap: () async {
      OrderDetailitem newitem = await Navigator.push(
          context,
          MaterialPageRoute(
              builder: (context) => MenuDetail(
                    menuitem: item,
                  )));
      function(newitem);
    },
    child: roundImage(BaseUrl + imagepath + item.img),
  );
}

//장바구니 및 최근 주문 목록 구성 위젯
Widget orderedItem(OrderDetailitem item, Function function,
    {bool btnVisible = false}) {
  return Padding(
    padding: const EdgeInsets.only(top: 10, bottom: 10),
    child: Stack(
      children: [
        Row(
          children: [
            roundImage(BaseUrl + imagepath + item.img, width: 100, height: 100),
            const SizedBox(width: 20),
            Expanded(
                child: Column(
              children: [
                Row(
                  children: [
                    Expanded(
                      child: Text(item.name,
                          style: textStyle20.apply(color: coffeeDarkBrown)),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(right: 40),
                      child: Text("${item.quantity}잔",
                          style: textStyle20.apply(color: coffeeDarkBrown)),
                    )
                  ],
                ),
                const SizedBox(height: 10),
                Row(
                  children: [
                    Expanded(
                      child: Text("${item.unitprice}원",
                          style: textStyle20.apply(color: coffeeDarkBrown)),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(right: 30),
                      child: Text("${item.totalprice}원",
                          style: textStyle15.apply(color: coffeeDarkBrown)),
                    )
                  ],
                )
              ],
            ))
          ],
        ),
        Visibility(
          visible: btnVisible,
          child: Container(
              alignment: Alignment.topRight,
              child: IconButton(
                  onPressed: btnVisible
                      ? () {
                          function(item);
                        }
                      : () {},
                  icon: Icon(Icons.cancel, color: coffeeBrown))),
        )
      ],
    ),
  );
}

//주문 내역 관련 함수
int totalquantity(List<OrderDetailitem> items) {
  int result = 0;
  for (var detail in items) {
    result += detail.quantity;
  }
  return result;
}

int totalprice(List<OrderDetailitem> items) {
  int result = 0;
  for (var detail in items) {
    result += detail.totalprice;
  }
  return result;
}

//주문 관련 위젯 (최근 주문, 주문 내역 목록에서 사용)
Card orderCard(Orderitem item, Widget widget, Function onTap1) {
  return Card(
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(10),
      side: const BorderSide(width: 2, color: coffeeDarkBrown),
    ),
    child: Padding(
      padding: const EdgeInsets.only(top: 10, left: 20, right: 20),
      child: InkWell(
        onTap: () {
          onTap1(item);
        },
        child: Column(
          children: [
            roundImage(BaseUrl + imagepath + item.details[0].img),
            const SizedBox(height: 20),
            Text('${item.details[0].name} 외 ${totalquantity(item.details)}잔',
                style: textOrder),
            Text('${totalprice(item.details)}원', style: textOrder),
            Text(item.orderTime.substring(0, 10), style: textOrder),
            const SizedBox(height: 2),
            widget
          ],
        ),
      ),
    ),
  );
}

// 최근 주문 및 주문 내역에 표시될 위젯
Widget orderScroll(List<Orderitem> orderList, Widget widget, Function onTab) {
  return ListView.builder(
    scrollDirection: Axis.horizontal,
    itemCount: orderList.length,
    itemBuilder: (BuildContext context, int position) {
      return orderCard(orderList[position], widget, onTab);
    },
  );
}

Widget orderListRow(List<Orderitem> items, Widget widget, Function onTab,
    {double height = 350}) {
  return Row(
    children: [
      Expanded(
          child: SizedBox(
              height: height, child: orderScroll(items, widget, onTab)))
    ],
  );
}

//stamp 관련 데이터 및 함수 모음
List<int> requiredStamps = [10, 15, 20, 25, 0];
List levelName = ['씨앗', '꽃', '열매', '커피콩', '나무'];
List<String> levelimg = [
  'assets/seeds.png',
  'assets/flower.png',
  'assets/coffee_fruit.png',
  'assets/coffee_beans.png',
  'assets/coffee_tree.png'
];

List<int> calculateStampLevel(int stamps) {
  int cur = stamps;
  bool check = false;
  int level = 4;
  int subLevel = 1;

  for (int lv = 0; lv < 4; lv++) {
    if (!check) {
      for (int j = 1; j <= 5; j++) {
        cur -= requiredStamps[lv];
        if (cur <= 0) {
          check = true;
          subLevel = j;
          level = lv;
          break;
        }
      }
    }
  }

  return [level, subLevel, cur];
}
