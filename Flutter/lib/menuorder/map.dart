import 'dart:async';

import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:smart_store/util/common.dart';

class CafeMap extends StatefulWidget {
  const CafeMap({super.key});

  @override
  State<CafeMap> createState() => _MapState();
}

class _MapState extends State<CafeMap> {

  // Completer<GoogleMapController> _completer = Completer();
  // static final CameraPosition initposition = CameraPosition(
  //     target: LatLng(36.108888,128.4113603),
  //     zoom: 15,
  // );

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView(
        shrinkWrap: true,
        children: [
          Container(
            padding: EdgeInsets.all(30),
              child: Text('Map', style: textStyleRed30,)
          ),
          InkWell(
              onTap: () {
                showDialog(
                  barrierDismissible: true,
                    context: context,
                    builder: (context) {
                      return AlertDialog(
                        title: Text('싸피벅스', style: textStyle15,),
                        contentPadding: EdgeInsets.all(20),
                        content: Container(
                          height: 350,
                          child: Column(
                            children: [
                              Container(
                                height: 200,
                                  child: Image.asset("assets/logo.png", fit: BoxFit.fill,)
                              ),
                              Align(
                                  alignment: Alignment.centerLeft,
                                  child: Column(
                                    children: [
                                      Text('주문가능시간', style: textStyle30),
                                      Container(
                                        padding: EdgeInsets.all(30),
                                        child: Text('주중  07:00~20:30\n주말  09:00~22:00', style: textStyle20,),
                                      )
                                    ],
                                  ),
                              ),
                            ],
                          ),
                        ),
                        actions: [
                          TextButton(
                              onPressed: (){
                                Navigator.pop(context);
                              },
                              child: Text('길찾기', style: textStyle15,)
                          ),
                          TextButton(
                              onPressed: (){
                                Navigator.pop(context);
                              },
                              child: Text('전화걸기', style: textStyle15,)
                          ),
                        ],
                      );
                    }
                );
              },
              child: Image.asset("assets/map_real.png", fit: BoxFit.fill,)
          ),
        ],
      ),
    );
  }
}
