import 'dart:async';

import 'package:flutter/material.dart';
import 'package:location/location.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:smart_store/util/common.dart';

class CafeMap extends StatefulWidget {
  const CafeMap({super.key});

  @override
  State<CafeMap> createState() => _MapState();
}

class _MapState extends State<CafeMap> {

  // 초기 구글맵 위치 설정
  Completer<GoogleMapController> _completer = Completer();
  static final CameraPosition initposition = CameraPosition(
    target: LatLng(37.5518911, 126.9917937),
    zoom: 15,
  );

  // 위치 권한 확인 후 승인받으면 현재 위치로 이동
  void checkPermission() async {
    PermissionStatus _checkpermission;
    _checkpermission = await Location().hasPermission();
    if (_checkpermission == PermissionStatus.denied) {
      _checkpermission = await Location().requestPermission();
      if (_checkpermission == PermissionStatus.granted) {
        moveCamera();
      }
    } else {
      moveCamera();
    }
  }

  void moveCamera() async {
    LocationData _location = await Location().getLocation();
    final CameraPosition StoreShop = CameraPosition(
      target: LatLng(_location.latitude!, _location.longitude!),
      zoom: 14,
    );
    final GoogleMapController controller = await _completer.future;
    controller.animateCamera(CameraUpdate.newCameraPosition(StoreShop));
  }

  // 마커 클릭 시 가게 정보 표시
  void mapinfo(BuildContext context) {
    showDialog(
        barrierDismissible: true,
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text(
              '싸피벅스',
              style: textStyle15,
            ),
            contentPadding: EdgeInsets.all(20),
            content: Container(
              height: 400,
              child: Column(
                children: [
                  Container(
                      height: 200,
                      child: Image.asset(
                        "assets/logo.png",
                        fit: BoxFit.fill,
                      )),
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Column(
                      children: [
                        Text('주문가능시간', style: textStyle30),
                        Container(
                          padding: EdgeInsets.all(30),
                          child: Text(
                            '주중  07:00~20:30\n주말  09:00~22:00',
                            style: textStyle20,
                          ),
                        )
                      ],
                    ),
                  ),
                ],
              ),
            ),
            actions: [
              TextButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: Text(
                    '길찾기',
                    style: textStyle15,
                  )),
              TextButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: Text(
                    '전화걸기',
                    style: textStyle15,
                  )),
            ],
          );
        });
  }

  @override
  void initState() {
    checkPermission();
  }

  @override
  Widget build(BuildContext context) {

    List<Marker> markers = [
      Marker(
        position: LatLng(36.10611, 128.4256),
        markerId: MarkerId("1"),
        onTap: () { mapinfo(context); },
      ),
    ];

    return Scaffold(
      body: Stack(
        children: [
          GoogleMap(
            initialCameraPosition: initposition,
            onMapCreated: (GoogleMapController controller) {
              _completer.complete(controller);
            },
            mapType: MapType.normal,
            markers: markers.toSet(),
          ),
          Container(
              color: Colors.white,
              padding: EdgeInsets.all(20),
              margin: EdgeInsets.symmetric(horizontal: 30, vertical: 80),
              child: Text(
                'Map',
                style: textStyleRed30,
              )),
        ],
      ),
    );
  }
}
