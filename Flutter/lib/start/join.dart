import 'package:flutter/material.dart';
import 'package:smart_store_flutter_starter/util/common.dart';

class Join extends StatefulWidget {
  const Join({super.key});

  @override
  State<Join> createState() => _JoinState();
}

class _JoinState extends State<Join> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SingleChildScrollView(
        child: Column(
          children: [
            SizedBox(
              height: 100,
            ),
            Container(
              alignment: Alignment.center,
              margin: EdgeInsets.all(20),
              height: 200,
              child: Image.asset("assets/logo.png"),
            ),
            Text(
              'Join', style: textLogin,
            ),
            SizedBox(
              height: 30,
            ),
            Container(
              padding: EdgeInsets.symmetric(horizontal: 30, vertical: 10),
              child: Row(
                children: [
                  Container(
                    width: 250,
                    margin: EdgeInsets.symmetric(horizontal: 10),
                    child: TextField(
                      decoration: InputDecoration(
                        hintText: '아이디를 입력하세요.',
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.all(Radius.circular(10)),
                          borderSide: BorderSide(width: 1, color: coffeeBrown),
                        ),
                      ),
                    ),
                  ),
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                      ),
                      backgroundColor: coffeeBrown,
                      padding: EdgeInsets.all(5),
                    ),
                      onPressed: () {},
                      child: Container(
                        margin: EdgeInsets.symmetric(horizontal: 5),
                        width: 50,
                        height: 50,
                        child: Image.asset("assets/check.png", fit: BoxFit.fill,),
                      ),
                  ),
                ],
              ),
            ),
            Container(
              margin: EdgeInsets.symmetric(horizontal: 40, vertical: 10),
              child: TextField(
                decoration: InputDecoration(
                  hintText: '비밀번호를 입력하세요.',
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.all(Radius.circular(10)),
                    borderSide: BorderSide(width: 1, color: coffeeBrown),
                  ),
                ),
              ),
            ),
            Container(
              margin: EdgeInsets.symmetric(horizontal: 40, vertical: 10),
              child: TextField(
                decoration: InputDecoration(
                  hintText: '별명을 입력하세요.',
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.all(Radius.circular(10)),
                    borderSide: BorderSide(width: 1, color: coffeeBrown),
                  ),
                ),
              ),
            ),
            Container(
              padding: EdgeInsets.symmetric(vertical: 20),
              child: ElevatedButton(
                  onPressed: (){
                    Navigator.pop(context);
                  },
                  style: ElevatedButton.styleFrom(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                    ),
                    backgroundColor: coffeeBrown,
                  ),
                  child: Container(
                      alignment: Alignment.center,
                      width: 120,
                      height: 50,
                      child: Text('Join', style: TextStyle(color: Colors.white),))
              ),
            ),
          ],
        ),
      ),
    );
  }
}
