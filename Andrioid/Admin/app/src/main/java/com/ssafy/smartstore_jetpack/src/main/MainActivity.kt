package com.ssafy.smartstore_jetpack.src.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseActivity
import com.ssafy.smartstore_jetpack.databinding.ActivityMainBinding
import com.ssafy.smartstore_jetpack.util.BeaconSettingUtil

private const val TAG = "MainActivity_싸피"
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    val beaconsetting = BeaconSettingUtil(this)
    lateinit var nfcAdapter: NfcAdapter
    lateinit var pendingIntent: PendingIntent
    lateinit var filters: Array<IntentFilter>
    var tablenumber = -1

    private lateinit var navController: NavController

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
        navController = (supportFragmentManager.findFragmentById(binding.frameLayoutMain.id) as NavHostFragment).navController


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_page_1 -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.navigation_page_2 -> {
                    navController.navigate(R.id.menuFragment)
                    true
                }
                R.id.navigation_page_3 -> {
                    navController.navigate(R.id.myPageFragment)
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

    private fun logout(){
        //preference 지우기
        ApplicationClass.sharedPreferencesUtil.deleteUser()

        //화면이동
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

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