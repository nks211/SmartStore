import 'package:flutter/material.dart';
import 'package:smart_store_flutter_starter/mypage/order_detail_page.dart';
import 'package:smart_store_flutter_starter/dto/Order.dart';
import 'package:smart_store_flutter_starter/util/common.dart';

class UserInfo extends StatefulWidget {
  @override
  _UserInfo createState() => _UserInfo();
}

class _UserInfo extends State<UserInfo> {

  //test용 데이터
  var items = List.generate(5, (_) => Order('assets/coffee1.png', '아메리카노 외 3잔', 25000, '2023.11.15')).toList();

  var curLv = 2;
  var curSubLv = 2;
  var curExp = 1;
  var curMxExp = 10;
  var percent = 0.0;

  @override
  void initState() {
    super.initState();

    var stamps = 12;

    var lvInfo = calculateStampLevel(stamps);
    curLv = lvInfo[0];
    curSubLv = lvInfo[1];
    curMxExp = requiredStamps[curLv];
    curExp = curMxExp+lvInfo[2];
    percent = (curExp/curMxExp);
  }

  void myOnTap(){
    Navigator.push(context, MaterialPageRoute(builder: (context)=> OrderDetailPage(testDetails)));
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Center(
          child: Padding(
            padding: const EdgeInsets.all(10),
            child: Column(
              children: [
                Row(
                  children: [
                    SizedBox(
                      height: 60,
                      child: Image.asset("assets/user.png"),
                    ),

                    Expanded(
                      flex: 1,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text("김싸피님", style: textStyle30.apply(color: coffeeDarkBrown)),
                          Text("안녕하세요.", style: textStyle20.apply(color: coffeeBrown))
                        ],
                      ),
                    ),
                    IconButton(
                        onPressed: (){ Navigator.pop(context); },
                        iconSize: 50,
                        icon: Image.asset('assets/logout.png'))
                  ],
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Row(
                    children: [
                      SizedBox(height: 30, child: Image.asset('assets/seeds.png')),
                      Text("${levelName[curLv]} $curSubLv단계", style: textStyle20Bold.apply(color: coffeeDarkBrown)),
                      Expanded(
                        flex: 1,
                        child: LinearProgressIndicator(
                          value: percent,
                          backgroundColor: Colors.grey,
                          valueColor: const AlwaysStoppedAnimation<Color>(coffeePointRed),
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
                      Text("다음 레벨까지 ${curMxExp-curExp}잔 남았습니다.", style: textStyle15Bold.apply(color: Colors.grey))
                    ],
                  ),
                ),
                const SizedBox(height: 5),
                Image.asset('assets/space.png'),
                Padding(
                  padding: const EdgeInsets.only(top: 30, bottom: 10),
                  child: Row(
                    children: [
                      Text("주문내역", style: textStyle30.apply(color: coffeeDarkBrown)),
                    ],
                  )
                ),
                orderListRow(items, OutlinedButton(
                  onPressed: (){},
                  style: OutlinedButton.styleFrom(
                    backgroundColor: coffeeBackground,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(20),
                    )
                  ),
                  child: const Padding(
                    padding: EdgeInsets.all(10),
                    child: Text("픽업 완료", style: textStyle20)
                  )
                ), myOnTap, height: 320)
              ],
            ),
          ),
        ),
      ),
    );
  }
}
