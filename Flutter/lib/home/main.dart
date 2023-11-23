import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:smart_store/dto/OrderDetailitem.dart';
import 'package:smart_store/menuorder/shopping_cart.dart';
import 'package:smart_store/service/NoteService.dart';
import 'package:smart_store/service/OrderService.dart';
import 'package:smart_store/service/UserService.dart';
import 'package:smart_store/util/common.dart';

import '../dto/Note.dart';
import '../dto/Orderitem.dart';
import '../dto/User.dart';
import '../start/page_router.dart';

class Main extends StatefulWidget {
  final User user;
  final List<Orderitem> orderdata;

  Main({required this.user, required this.orderdata});

  @override
  State<Main> createState() => _MainState();
}

class _MainState extends State<Main> {

  var userservice = UserService();
  var orderservice = OrderService();
  var noteservice = NoteService();
  Widget orderlist = Container(); //주문 내역 위젯 초기화

  TextEditingController titlecontroller = TextEditingController();
  TextEditingController contentcontroller = TextEditingController();
  TextEditingController receivercontroller = TextEditingController();

  void reorder(Orderitem item) async {
    var answer = await Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => ShoppingCart(
                  neworder: item.details,
                )));
    if (answer == 'OK') {
      setState(() {
        Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => PageRouter()));
      });
    }
  }

  void addnotification(String notice) {
    setState(() {
      noticeItem.add(notice);
    });
  }

  // 알림판 내 알림 닫는 함수
  void closenotification(String notice) {
    setState(() {
      noticeItem.remove(notice);
    });
  }

  @override
  Widget build(BuildContext context) {

    // 알림 목록 최신 순으로 가져오기
    Future<SharedPreferences> preferences = SharedPreferences.getInstance();
    preferences.then((value) {
      if (value.getStringList('notice${widget.user.id}') != null) {
        noticeItem = List.from(value.getStringList('notice${widget.user.id}')!.reversed);
        value.remove('notice${widget.user.id}');
      }
    });

    // 주문 id별 상세내역 추가한 후 리스트로 반환
    for (var order in widget.orderdata) {
      orderservice.getOrderDetails(order.id).then((value) {
        order.setDetails(value);
        setState(() {
          orderlist = orderListRow(
              widget.orderdata,
              SizedBox(
                  height: 25, child: Image.asset('assets/shopping_cart.png')),
              reorder,
              height: 310);
        });
      });
    }

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
                          Text("${widget.user.name}님",
                              style: textStyle30.apply(color: coffeeDarkBrown)),
                          Row(
                            children: [
                              Text("좋은 하루 보내세요.",
                                  style: textStyle20.apply(color: coffeeBrown)),
                              SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: Image.asset('assets/smile.png'))
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
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text("알림판",
                            style: textStyle30.apply(color: coffeeDarkBrown)),
                        ElevatedButton(
                            onPressed: () {
                              showDialog(context: context,
                                  builder: (context) {
                                    return AlertDialog(
                                      title: Text('쪽지를 보낼 사람에게 하고 싶은 말을 적어보세요 (제목, 내용, 보낼 사람)', style: textStyle15,),
                                      contentPadding: EdgeInsets.all(20),
                                      content: Container(
                                        alignment: Alignment.center,
                                        width: 100,
                                        height: 200,
                                        color: Color(0xffeeeeee),
                                        child: ListView(
                                          shrinkWrap: true,
                                          children: [
                                            Text('제목', style: textStyle15,),
                                            Container(
                                              margin: EdgeInsets.symmetric(horizontal: 10, vertical: 20),
                                              child: TextField(
                                                controller: titlecontroller,
                                              ),
                                            ),
                                            Text('내용', style: textStyle15,),
                                            Container(
                                              margin: EdgeInsets.symmetric(horizontal: 10, vertical: 20),
                                              child: TextField(
                                                controller: contentcontroller,
                                              ),
                                            ),
                                            Text('보낼 사람의 아이디', style: textStyle15,),
                                            Container(
                                              margin: EdgeInsets.symmetric(horizontal: 10, vertical: 20),
                                              child: TextField(
                                                controller: receivercontroller,
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                      actions: [
                                        TextButton(onPressed: () {
                                          String title = titlecontroller.text;
                                          String content = contentcontroller.text;
                                          String receiverid = receivercontroller.text;
                                          Future<SharedPreferences> preferences = SharedPreferences.getInstance();
                                          preferences.then((value) {
                                            if (value.getString('id') != null) {
                                              String senderid = value.getString('id')!;
                                              Note newnote = Note(title, content, senderid, receiverid);
                                              noteservice.sendNote(newnote).then((_) {
                                                if (_ == 'true') {
                                                  showToast('쪽지 보내기에 성공하였습니다.');
                                                  titlecontroller.text = '';
                                                  contentcontroller.text = '';
                                                  receivercontroller.text = '';
                                                }
                                              }).catchError((e) {
                                                showToast('쪽지 보내기에 실패하였습니다.');
                                              });
                                              Navigator.pop(context);
                                            }
                                          });
                                        }, child: Text('쪽지 보내기', style: textStyle15Bold.apply(color: Colors.black),)),
                                      ],
                                    );
                                  }
                              );
                            },
                            style: ElevatedButton.styleFrom(
                                backgroundColor: coffeePointRed,
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(20),
                                )
                            ),
                            child: Container(
                                width: 100, height: 40,
                                padding: EdgeInsets.all(10),
                                child: Text("쪽지 보내기", style: textStyle15.apply(color: Colors.white))),
                        ),
                      ],
                    )),
                Expanded(
                  flex: 1,
                  child: Card(
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10),
                        side:
                            const BorderSide(width: 2, color: coffeeDarkBrown)),
                    child: Container(
                      padding: const EdgeInsets.only(left: 20, right: 20),
                      child: noticeScroll(noticeItem, closenotification),
                    ),
                  ),
                ),
                Padding(
                    padding: const EdgeInsets.only(top: 15, bottom: 15),
                    child: Row(
                      children: [
                        Text("최근 주문",
                            style: textStyle30.apply(color: coffeeDarkBrown)),
                      ],
                    )),
                orderlist,
              ],
            ),
          ),
        ),
      ),
    );
  }
}
