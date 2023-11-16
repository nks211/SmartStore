package com.ssafy.smartstore_jetpack.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.databinding.DialogStoreEventBinding
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.home.HomeFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region

private const val TAG = "MainActivity_싸피"
class BeaconSettingUtil(context: Context) {


    //beacon 관련 세팅 함수 모음

    private val BEACON_UUID = "fda50693-a4e2-4fb1-afcf-c6eb07647825"
    private val BEACON_MAJOR = "10004"
    private val BEACON_MINOR = "54480"
    private val REGIONS = Region(
        "estimote",
        Identifier.parse(BEACON_UUID),
        Identifier.parse(BEACON_MAJOR),
        Identifier.parse(BEACON_MINOR)
    )
    val BEACON_DISTANCE = 5.0

    private lateinit var checker : PermissionChecker

    private val runtimePermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_ADVERTISE,
        Manifest.permission.BLUETOOTH_CONNECT
    )

    private lateinit var beaconManager: BeaconManager
    private lateinit var bluetoothManager: BluetoothManager

    private var bluetoothAdapter: BluetoothAdapter? = null
    var findBeacon = false

    var rangeNotifier: RangeNotifier = RangeNotifier { beacons, region ->
        if (beacons != null) {
            if (beacons.size > 0) {
                val iterator = beacons.iterator()
                while (iterator.hasNext()) {
                    val beacon = iterator.next() as Beacon
                    // Major, Minor로 Beacon 구별 (해당 region만 들어오므로 double check.)
                    // 사정거리 내에 있을 경우 이벤트 표시 다이얼로그 팝업
                    if (beacon.distance <= BEACON_DISTANCE) {
                        AlertDialog.Builder(context).apply {
                            val binding = DialogStoreEventBinding.inflate(LayoutInflater.from(context), null, false)
//                            setView(LayoutInflater.from(context).inflate(R.layout.dialog_store_event, null))
                            setView(binding.root)
                            CoroutineScope(Dispatchers.Main).launch{
                                val res = withContext(Dispatchers.IO){
                                    CommonUtils.makeLatestOrderList(RetrofitUtil.orderService.getLastMonthOrder(ApplicationClass.sharedPreferencesUtil.getUser().id))
                                }

                                if(res.isNotEmpty()){
                                    binding.tvDialogStoreEventOrderEmpty.visibility = View.GONE
                                    Glide.with(context)
                                        .load("${ApplicationClass.MENU_IMGS_URL}${res[0].img}")
                                        .into(binding.ivDialogStoreEventOrderImg)
                                    binding.tvDialogStoreEventOrderName.text = "${res[0].productName} 외 ${res[0].orderCnt -1}건"
                                    binding.tvDialogStoreEventTotalPrice.text = CommonUtils.makeComma(res[0].totalPrice)
                                }else{
                                    binding.tvDialogStoreEventOrderEmpty.visibility = View.VISIBLE
                                }
                            }


                            setCancelable(true)
                            setPositiveButton("확인") { dialog, which ->
                                dialog.dismiss()
                            }
                        }.show()
                        stopScan()
                        break
                    } else {
                    }
                }
            }
        }
    }

    fun startScan() {
        // 리전에 비컨이 있는지 없는지..정보를 받는 클래스 지정
//        beaconManager.addMonitorNotifier(monitorNotifier);
//        beaconManager.startMonitoring(REGIONS);

        //detacting되는 해당 region의 beacon정보를 받는 클래스 지정.
        beaconManager.addRangeNotifier(rangeNotifier)
        beaconManager.startRangingBeacons(REGIONS)
    }

    fun stopScan() {
        beaconManager.stopRangingBeacons(REGIONS)
    }

    fun destroy() {
        //        beaconManager.stopMonitoring(REGIONS)
        beaconManager.stopRangingBeacons(REGIONS)
    }

    fun checkPermissions(context: Context){
        //BeaconManager 지정
        beaconManager = BeaconManager.getInstanceForApplication(context)
        //estimo 비컨을 분석 하도록 하기 위하여 beacon parser 오프셋, 버전등을 setLayout으로 지정한다.
//		m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24
//		설명: 0 ~ 1 바이트는 제조사를 나타내는 필드로 파싱하지 않는다.
//		2~3 바이트는 0x02, 0x15 이다.
//		4~19 바이트들을 첫번째 ID로 매핑한다.(UUID)
//		20~21 바이트들을 두번째 ID로 매핑한다.(Major)
//		22-23 바이트들을 세번째 ID로 매핑한다.(Minor)
//		24~24 바이트들을 txPower로 매핑한다.
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            Toast.makeText(context, "블루투스 기능을 확인해 주세요.", Toast.LENGTH_SHORT).show()
            val bleIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            (context as MainActivity).startActivityForResult(bleIntent, 1)
        }

        checker = PermissionChecker(context)

        /* permission check */
        if (!checker.checkPermission(runtimePermissions)) {
            checker.permitted = object : PermissionListener {
                override fun onGranted() {
                    //퍼미션 획득 성공일때
                }
            }
            checker.requestPermissionLauncher.launch(runtimePermissions)
        } else { //이미 전체 권한이 있는 경우
        }
        /* permission check */

    }

    interface PermissionListener{
        fun onGranted()
    }

    inner class PermissionChecker(private val context: Context) {

        lateinit var permitted: PermissionListener

        // 권한 체크
        fun checkPermission(permissions: Array<String>): Boolean {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }

            return true
        }

        // 권한 호출한 이후 결과받아서 처리할 Launcher (startPermissionRequestResult )
        val requestPermissionLauncher = (context as AppCompatActivity).registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            Log.d(TAG, "requestPermissionLauncher: 건수 : ${it.size}")

            if(it.values.contains(false)){ //false가 있는 경우라면..
                Toast.makeText(context, "권한이 부족합니다.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "모든 권한이 허가되었습니다.", Toast.LENGTH_SHORT).show()
                permitted.onGranted()
            }
        }

    }

}