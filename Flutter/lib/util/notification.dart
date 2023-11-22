import 'package:flutter_local_notifications/flutter_local_notifications.dart';

class LocalNotification {
  LocalNotification._();

  static FlutterLocalNotificationsPlugin plugin = FlutterLocalNotificationsPlugin();

  static init() async {
    AndroidInitializationSettings androidInitializationSettings =
        const AndroidInitializationSettings('mipmap/ic_launcher');

    DarwinInitializationSettings initSettingsIOS =
    const DarwinInitializationSettings(
      requestSoundPermission: false,
      requestBadgePermission: false,
      requestAlertPermission: false,
    );

    InitializationSettings initSettings = InitializationSettings(
      android: androidInitializationSettings,
      iOS: initSettingsIOS,
    );

    await plugin.initialize(initSettings);
  }

  static Future<void> makeNotification(String stampstep) async {
    const AndroidNotificationDetails androidDetails =
        AndroidNotificationDetails('id', 'name',
          importance: Importance.high,
          priority: Priority.high,
          showWhen: false,
        );

    const NotificationDetails details = NotificationDetails(
      android: androidDetails,
      iOS: DarwinNotificationDetails(badgeNumber: 1),
    );

    await plugin.show(0,
        '당신의 현재 등급은 ' + stampstep + '입니다.',
        stampstep + '가 되신 것을 축하드립니다! 앞으로도 더 많이 이용해주세요',
        details,
        payload: 'deepLink',
    );

  }

}