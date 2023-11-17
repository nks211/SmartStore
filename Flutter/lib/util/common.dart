import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import '../dto/Order.dart';
import '../dto/Product.dart';
import '../dto/order_detail.dart';
import '../menuorder/menudetail.dart';
import '../menuorder/shopping_cart.dart';

String userName = "김싸피";

// Color 정의
const Color coffeeBrown = Color(0xff907f60);
const Color coffeeBackground = Color(0xFFF0E3DB);
const Color coffeePointRed = Color(0xff9d0200);
const Color coffeeDarkBrown = Color(0xff493319);
const Color menuBackground = Color(0xffeee1da);

// TextStyle 정의
const TextStyle textLogin =  TextStyle(fontFamily: 'cafe24', fontSize: 50.0);
const TextStyle textStyle15 = TextStyle(fontFamily: 'Montserrat', fontSize: 15.0);
const TextStyle textStyle15Bold = TextStyle(fontFamily: 'Montserrat', fontSize: 15.0, fontWeight: FontWeight.bold);
const TextStyle textStyle20 = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0);
const TextStyle textStyle20Bold = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0, fontWeight: FontWeight.bold);
const TextStyle textStyle20White = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0, color: Colors.white);
const TextStyle textStyle25 = TextStyle(fontFamily: 'Montserrat', fontSize: 25.0);
const TextStyle textStyle20B = TextStyle(fontFamily: 'Montserrat', fontSize: 20.0, fontWeight: FontWeight.bold);
const TextStyle textStyle30 = TextStyle(fontFamily: 'eland_choice_b', fontSize: 30.0);
const TextStyle textStyle50 = TextStyle(fontFamily: 'Montserrat', fontSize: 50.0);
const TextStyle textStyleRed30 = TextStyle(fontFamily: 'Montserrat', fontSize: 30.0, color: Color(0xff9d0200));
const TextStyle textOrder = TextStyle(fontFamily: 'eland_choice_b', fontSize: 20, color: coffeeDarkBrown);

//test용 데이터
List<OrderDetail> testDetails =[
  OrderDetail('assets/coffee1.png', '아메리카노', 2500, 2),
  OrderDetail('assets/coffee3.png', '카페라떼', 3000, 3)
];
List comment = ['nice', 'cool', 'happy', 'happy'];

//http통신 주소값
const BaseUrl = "http://192.168.33.119:9987/";

// toast message
void showToast(String msg){
  Fluttertoast.showToast(
      msg: msg,
      toastLength: Toast.LENGTH_SHORT,
      gravity: ToastGravity.BOTTOM,
      timeInSecForIosWeb: 1,
      textColor: Colors.white,
      fontSize: 16.0
  );
}

//알림판 구성 및 항목 구성
Widget notice(String content){
  return Row(
    children: [
      Expanded(flex: 1, child: Text(content, style: textStyle15)),
      IconButton(onPressed: (){}, icon:const Icon(Icons.cancel, color: coffeeBrown))
    ],
  );
}

Widget noticeScroll(List<String> notices){
  return ListView.builder(
    padding: EdgeInsets.zero,
    itemCount: notices.length,
    itemBuilder: (BuildContext context, int position){
      return notice(notices[position]);
    },
  );
}


//이미지 위젯
Widget roundImage(String src, {double height = 150, double width = 150}){
  return ClipRRect(
    borderRadius: BorderRadius.circular(30),
    child: Container(
      height: height,
      width: width,
      color: coffeeBackground,
      child: Image.asset(src),
    ),
  );
}

//메뉴판 이미지 위젯 (roundImage 활용)
Widget menuImageButton(Product item, BuildContext context) {
  return InkWell(
      onTap: (){
        Navigator.push(context, MaterialPageRoute(builder: (context) => MenuDetail(menuitem: item,)));
      },
      child: roundImage('assets/${item.img}'),
  );
}


//장바구니 주문 목록 구성 위젯
Widget orderedItem(OrderDetail item, {bool btnVisible = false}){
  return Padding(
    padding: const EdgeInsets.only(top: 10, bottom: 10),
    child: Stack(
      children: [
        Row(
          children: [
            roundImage(item.imgSrc!, width: 100, height: 100),
            const SizedBox(width: 20),
            Expanded(child: Column(
              children: [
                Row(
                  children: [
                    Expanded(
                      child: Text(item.name!, style: textStyle20.apply(color: coffeeDarkBrown)),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(right: 40),
                      child: Text("${item.quantity}잔", style: textStyle20.apply(color: coffeeDarkBrown)),
                    )
                  ],
                ),
                const SizedBox(height: 10),
                Row(
                  children: [
                    Expanded(
                      child: Text("${item.price}원", style: textStyle20.apply(color: coffeeDarkBrown)),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(right: 30),
                      child: Text("${item.totalPrice}원", style: textStyle15.apply(color: coffeeDarkBrown)),
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
              child: IconButton(onPressed: (){}, icon:const Icon(Icons.cancel, color: coffeeBrown))
          ),
        )

      ],
    ),
  );
}


//주문 관련 위젯 (최근 주문, 주문 내역 목록에서 사용)
Card orderCard(Order order, Widget widget, Function onTap1){
  return Card(
    shape: RoundedRectangleBorder(
      borderRadius: BorderRadius.circular(10),
      side: const BorderSide(width: 2, color: coffeeDarkBrown),
    ),
    child: Padding(
      padding: const EdgeInsets.only(top: 10, left: 20, right: 20),
      child: InkWell(
        onTap: (){
          onTap1();
        },
        child: Column(
          children: [
            roundImage(order.imgSrc!),
            const SizedBox(height: 20),
            Text(order.orderDetails!, style: textOrder),
            Text('${order.totalPrice}원', style: textOrder),
            Text(order.date!, style: textOrder),
            const SizedBox(height: 2),
            widget
          ],
        ),
      ),
    ),
  );
}


// 최근 주문 및 주문 내역에 표시될 위젯
Widget orderScroll(List<Order> orderList, Widget widget, Function onTab){
  return ListView.builder(
    scrollDirection: Axis.horizontal,
    itemCount: orderList.length,
    itemBuilder: (BuildContext context, int position){
      return orderCard(orderList[position], widget, onTab);
    },
  );
}

Widget orderListRow(List<Order> items, Widget widget, Function onTab, {double height = 350}){
  return Row(
    children: [
      Expanded(
          child: SizedBox(height: height, child: orderScroll(items, widget, onTab))
      )
    ],
  );
}


//stamp 관련 데이터 및 함수 모음
List<int> requiredStamps = [10, 15, 20, 25, 0];
List levelName = ['씨앗', '꽃', '열매', '커피콩', '나무'];

List<int> calculateStampLevel(int stamps){
  int cur = stamps;
  bool check = false;
  int level = 4;
  int subLevel = 1;

  for(int lv=0;lv<4;lv++){
    if(!check){
      for(int j=1;j<=5;j++){
        cur -= requiredStamps[lv];
        if(cur<=0){
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