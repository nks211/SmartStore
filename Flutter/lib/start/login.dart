import 'package:flutter/material.dart';
import 'package:smart_store_flutter_starter/start/page_router.dart';
import 'package:smart_store_flutter_starter/util/common.dart';

import 'join.dart';
import 'package:smart_store_flutter_starter/menuorder/menu.dart';

class Login extends StatefulWidget {
  @override
  _Login createState() => _Login();
}

class _Login extends State<Login> {



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SingleChildScrollView(
        child: Container(
          alignment: Alignment.center,
          child: ListView(
            shrinkWrap: true,
            children: [
              SizedBox(
                height: 120,
              ),
              Container(
                alignment: Alignment.center,
                margin: EdgeInsets.all(20),
                height: 200,
                child: Image.asset("assets/logo.png"),
              ),
              Align(
                alignment: Alignment.center,
                child: Text(
                  'Login', style: textLogin,
                ),
              ),
              SizedBox(
                height: 30,
              ),
              Container(
                padding: EdgeInsets.symmetric(horizontal: 30, vertical: 10),
                child: TextField(
                  decoration: InputDecoration(
                    hintText: 'ID',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                      borderSide: BorderSide(width: 1, color: coffeeBrown),
                    ),
                  ),
                ),
              ),
              Container(
                padding: EdgeInsets.symmetric(horizontal: 30, vertical: 10),
                child: TextField(
                  decoration: InputDecoration(
                    hintText: 'PW',
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                      borderSide: BorderSide(width: 1, color: coffeeBrown),
                    ),
                  ),
                ),
              ),
              Container(
                padding: EdgeInsets.symmetric(vertical: 20, horizontal: 30),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    ElevatedButton(
                        onPressed: (){
                          Navigator.push(context, MaterialPageRoute(builder: (context) => PageRouter()));
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
                            child: Text('Login', style: TextStyle(color: Colors.white),))
                    ),
                    ElevatedButton(
                        onPressed: (){
                          Navigator.push(context, MaterialPageRoute(builder: (context) => Join()));
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
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
