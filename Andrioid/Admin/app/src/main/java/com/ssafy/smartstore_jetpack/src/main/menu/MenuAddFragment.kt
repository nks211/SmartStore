package com.ssafy.smartstore_jetpack.src.main.menu

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentMenuAddBinding
import com.ssafy.smartstore_jetpack.dto.Product
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private const val TAG = "MenuAddFragment_싸피"
class MenuAddFragment : BaseFragment<FragmentMenuAddBinding>(FragmentMenuAddBinding::bind, R.layout.fragment_menu_add){

    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mainActivity: MainActivity

    private lateinit var imageUri: Uri

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgUploadBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            resultLauncher.launch(gallery)
        }

        binding.menuUpload.setOnClickListener{

            val file = File(absolutelyPath(imageUri, requireContext()))
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            var productMap = hashMapOf<String, RequestBody>()
            productMap["name"] =
                binding.productName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            productMap["type"] =
                binding.productType.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            productMap["price"] =
                binding.productPrice.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            lifecycleScope.launch {
                val bool = RetrofitUtil.productService.addProduct(
                    body, productMap
                )
                if(bool) {
                    showToast("저장 되었습니다.")
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == RESULT_OK){
            Log.d(TAG, ": ${it.data?.data}")
            imageUri = it.data?.data!!
            binding.productImg.setImageURI(imageUri)
        }
        else{
            Log.d(TAG, ": $it")
        }
    }
}

