package com.ssafy.smartstore_jetpack.src.main.my

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.ssafy.smartstore_jetpack.BuildConfig
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentMypageBinding
import com.ssafy.smartstore_jetpack.dto.Grade
import com.ssafy.smartstore_jetpack.src.main.LoginActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.src.main.my.adapter.CompletedListAdapter
import com.ssafy.smartstore_jetpack.src.main.my.adapter.OrderListAdapter
import com.ssafy.smartstore_jetpack.util.CommonUtils
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch

// MyPage 탭
private const val TAG = "MypageFragment_싸피"
class MyPageFragment : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::bind, R.layout.fragment_mypage) {
    private lateinit var orderAdapter : OrderListAdapter
    private lateinit var completedOrderAdapter: CompletedListAdapter
    private lateinit var mainActivity: MainActivity
    private var isAdmin = false
    private var pw = ""

    private val activityViewModel : MainActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = getUserData().also {
            initData(it)
        }
        if(!isAdmin) {
            binding.levelLayout.visibility = View.VISIBLE
            binding.textLevelRest.visibility = View.VISIBLE
        }
        getGradeData(id)
    }

    private fun getUserData():String{
        val user = ApplicationClass.sharedPreferencesUtil.getUser()
        val tunText = "${user.name} 님"
        binding.textUserName.text = tunText
        isAdmin = user.isAdmin
        Log.d(TAG, "getUserData: ${user.isAdmin}")

        return user.id
    }

    private fun getGradeData(id: String) {
        lifecycleScope.launch { 
            val userinfo = RetrofitUtil.userService.getInfowithstamp(id)

            val gradeinfo = Gson().fromJson<Grade>(userinfo["grade"].toString(), object: TypeToken<Grade>(){}.type)

            pw = (userinfo["user"] as LinkedTreeMap<*,*>)["pass"].toString()

            binding.imageLevel.setImageURI(Uri.parse(
                "android.resource://" + BuildConfig.APPLICATION_ID
                        + "/drawable/" + gradeinfo.img.substring(0, gradeinfo.img.indexOf("."))))
            val tulText = "${gradeinfo.title} ${gradeinfo.step} 단계"
            binding.textUserLevel.text = tulText
            val tlrText = "다음 레벨까지 ${gradeinfo.to} 잔 남았습니다."
            binding.textLevelRest.text = tlrText
            var state = ""
            when(gradeinfo.title) {
                "씨앗" -> { state = "${10 - gradeinfo.to}/10" }
                "꽃" -> { state = "${15 - gradeinfo.to}/15" }
                "열매" -> { state = "${20 - gradeinfo.to}/20" }
                "커피콩" -> { state = "${25 - gradeinfo.to}/25" }
                "나무" -> {  }
            }
            binding.textUserNextLevel.text = state
        }
    }

    private fun initData(id:String){
        // 최근 한달간 주문내역

        if(!isAdmin){
            lifecycleScope.launch {
                val userLastOrderData = RetrofitUtil.orderService.getLastMonthOrder(id)

                orderAdapter = OrderListAdapter()
                orderAdapter.setItemClickListener(object : OrderListAdapter.ItemClickListener {
                    override fun onClick(view: View, position: Int, orderid: Int) {
                        activityViewModel.setOrderId(orderid)
                        Navigation.findNavController(view).navigate(R.id.orderDetailFragment)
                    }
                })
                orderAdapter.submitList(CommonUtils.makeLatestOrderList(userLastOrderData))
                binding.recyclerViewOrder.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = orderAdapter
                }
                Log.d(TAG, "onViewCreated: $userLastOrderData")
            }
        }else{

            activityViewModel.setCompletedOrder()
            completedOrderAdapter = CompletedListAdapter().apply{
                submitList(activityViewModel.completedOrder.value)
                setItemClickListener(object : CompletedListAdapter.ItemClickListener{
                    override fun onClick(view: View, position: Int, orderid: Int) {
                        activityViewModel.setOrderId(orderid)
                        Navigation.findNavController(view).navigate(R.id.orderDetailFragment)
                    }
                })
            }

            binding.recyclerViewOrder.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = completedOrderAdapter
            }


            activityViewModel.completedOrder.observe(viewLifecycleOwner){
                completedOrderAdapter.submitList(it)
            }

        }
        binding.userInfoUpdate.setOnClickListener {
            val dlg = AlertDialog.Builder(requireContext())
            val editText = EditText(requireContext())
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            dlg.setTitle("비밀번호를 입력해주세요")
            dlg.setView(editText)
            dlg.setPositiveButton("확인") { _, _ ->
                if(editText.text.toString()==pw){
                    Navigation.findNavController(requireView()).navigate(R.id.userInfoUpdateFragment)
                }else{
                    showToast("비밀번호를 다시 입력해주세요")
                }
            }
            dlg.setNegativeButton("취소"){dialog,_->
                dialog.dismiss()
            }
            dlg.show()

        }

        binding.logout.setOnClickListener {
            ApplicationClass.sharedPreferencesUtil.deleteUser()

            //화면이동
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
        }
    }

}