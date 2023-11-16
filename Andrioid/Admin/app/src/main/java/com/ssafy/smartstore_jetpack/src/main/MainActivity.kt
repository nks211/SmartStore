package com.ssafy.smartstore_jetpack.src.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseActivity
import com.ssafy.smartstore_jetpack.databinding.ActivityMainBinding
import com.ssafy.smartstore_jetpack.src.main.home.HomeFragment
import com.ssafy.smartstore_jetpack.src.main.menu.MapFragment
import com.ssafy.smartstore_jetpack.src.main.menu.MenuDetailFragment
import com.ssafy.smartstore_jetpack.src.main.menu.MenuFragment
import com.ssafy.smartstore_jetpack.src.main.menu.ShoppingListFragment
import com.ssafy.smartstore_jetpack.src.main.my.MyPageFragment
import com.ssafy.smartstore_jetpack.src.main.my.OrderDetailFragment
import com.ssafy.smartstore_jetpack.util.BeaconSettingUtil
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region

private const val TAG = "MainActivity_싸피"
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    val viewModel : MainActivityViewModel by viewModels()
    val beaconsetting = BeaconSettingUtil(this)
    lateinit var nfcAdapter: NfcAdapter
    lateinit var pendingIntent: PendingIntent
    lateinit var filters: Array<IntentFilter>
    var tablenumber = -1

    override fun onResume() {
        super.onResume()
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val action = intent.action
        readdata(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 가장 첫 화면은 홈 화면의 Fragment로 지정

        setNdef()

        setBeacon()

        createNotificationChannel("ssafy_channel", "ssafy")


        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout_main, HomeFragment())
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_page_1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, HomeFragment())
                        .commit()
                    true
                }
                R.id.navigation_page_2 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, MenuFragment())
                        .commit()
                    true
                }
                R.id.navigation_page_3 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_main, MyPageFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigation.setOnItemReselectedListener { item ->
            // 재선택시 다시 랜더링 하지 않기 위해 수정
            if(binding.bottomNavigation.selectedItemId != item.itemId){
                binding.bottomNavigation.selectedItemId = item.itemId
            }
        }
    }

    fun openFragment(index:Int, key:String, value:Int){
        moveFragment(index, key, value)
    }

    fun openFragment(index: Int) {
        moveFragment(index, "", -1)
    }

    private fun moveFragment(index:Int, key:String, value:Int){
        val transaction = supportFragmentManager.beginTransaction()
        when(index){
            //장바구니
            1 -> {
                Log.d(TAG, "moveFragment: $value")
                transaction.replace(R.id.frame_layout_main, ShoppingListFragment(value))
                .addToBackStack(null)
            }
            //주문 상세 보기
            2 -> transaction.replace(R.id.frame_layout_main, OrderDetailFragment())
                .addToBackStack(null)
            //메뉴 상세 보기
            3 -> transaction.replace(R.id.frame_layout_main, MenuDetailFragment())
                .addToBackStack(null)
            //map으로 가기
            4 -> transaction.replace(R.id.frame_layout_main, MapFragment())
                .addToBackStack(null)
            //logout
            5 -> {
                logout()
            }
        }
        transaction.commit()
    }

    private fun logout(){
        //preference 지우기
        ApplicationClass.sharedPreferencesUtil.deleteUser()

        //화면이동
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent)
    }

    fun hideBottomNav(state : Boolean){
        if(state) binding.bottomNavigation.visibility =  View.GONE
        else binding.bottomNavigation.visibility = View.VISIBLE
    }

    fun setNdef(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        var intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filter.addDataType("text/plain")
        filters = arrayOf(filter)
    }

    fun readdata(intent: Intent) {
        val action = intent.action
        Log.d(TAG, "setNdef: ${action}")
        if (action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            val messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            messages?.forEachIndexed { _, pacelable ->
                val message = pacelable as NdefMessage
                message.records!!.forEachIndexed { index, ndefRecord ->
                    val record = ndefRecord as NdefRecord
                    Toast.makeText(this, "${String(record.payload).substring(3)}번이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    tablenumber = String(record.payload).substring(9).toInt()
                }
            }
        }
    }

    fun setBeacon(){ beaconsetting.checkPermissions(this) }

    // NotificationChannel 설정
    fun createNotificationChannel(id: String, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance)

            val notificationManager: NotificationManager
                    = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}