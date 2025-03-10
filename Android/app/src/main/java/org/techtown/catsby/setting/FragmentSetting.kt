package org.techtown.catsby.setting

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResultListener
import com.example.catsbe.account
import com.example.catsbe.alert
import android.content.DialogInterface
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.write_main_bowl.*
import org.techtown.catsby.qrcode.LoadingActivity
import org.techtown.catsby.R
import org.techtown.catsby.retrofit.RetrofitClient
import org.techtown.catsby.retrofit.dto.NicknameResponse
import org.techtown.catsby.retrofit.dto.User
import org.techtown.catsby.retrofit.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSetting.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSetting : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var userService = RetrofitClient.getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    val OPEN_GALLERY=1
    lateinit var imageButton : ImageButton
    lateinit var editNickName : EditText
    lateinit var nickName : TextView
    lateinit var local : TextView
    lateinit var personalNum : TextView
    lateinit var editButton : Button
    lateinit var backButton : Button

    lateinit var alertManage : TextView
    lateinit var accountManage : TextView
    lateinit var writingList : TextView

    var txtadd: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        imageButton = view.findViewById<ImageButton>(R.id.imageButton)
        editNickName = view.findViewById<EditText>(R.id.editNickName)
        nickName = view.findViewById<TextView>(R.id.nickName)
        local = view.findViewById<TextView>(R.id.local)
        editButton = view.findViewById<Button>(R.id.editButton)
        alertManage = view.findViewById<TextView>(R.id.alertManage)
        accountManage = view.findViewById<TextView>(R.id.accountManage)
        writingList = view.findViewById<TextView>(R.id.writingList)

        setFragmentResultListener("myaddkey") { key, bundle ->
            bundle.getString("myaddkey")?.let {
                //프로필에 주소 등록
                //나중에 데이터 베이스에 등록 후 전역으로 setText 해야할듯..?
                local.setText(it)
            }
        }

        alertManage.setOnClickListener {
            //setFrag(0)
            replaceFragment(alert())
            //inflater.inflate(R.layout.fragment_alert, container, false)
        }
        accountManage.setOnClickListener {
            //setFrag(1)
            replaceFragment(account())
            //inflater.inflate(R.layout.fragment_account, container, false)
        }
        writingList.setOnClickListener {
            //setFrag(2)
            replaceFragment(com.example.catsby.writingList())
            //inflater.inflate(R.layout.fragment_writing_list, container, false)
        }

        //프로필 이미지 버튼 클릭 시 (프로필 사진 변경 알림창)
        imageButton.setOnClickListener {
            var builder = AlertDialog.Builder(activity)
            builder.setTitle("알림")
            builder.setMessage("프로필 사진을 변경하시겠습니까?")
            builder.setIcon(R.mipmap.ic_launcher)

            // 버튼 클릭시에 무슨 작업을 할 것인가!
            var listener = object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    when (p1) {
                        DialogInterface.BUTTON_POSITIVE ->
                            tv1.text = "BUTTON_POSITIVE"
                        DialogInterface.BUTTON_NEGATIVE ->
                            tv1.text = "BUTTON_NEGATIVE"
                    }
                }
            }

            builder.setPositiveButton("네", DialogInterface.OnClickListener { dialog, which ->
                openGallery()
            })
                builder.setNegativeButton("아니오", listener)
                builder.show()
            }

        //닉네임 수정 버튼
        editButton.setOnClickListener {

            //수정 버튼 클릭 시
            if (editButton.text.equals("수정")) {
                nickName.setVisibility(View.GONE)
                editNickName.setVisibility(View.VISIBLE)
                editButton.setText("완료")
            }

            //(수정)완료 버튼 클릭 시
            else if (editButton.text.equals("완료")) {
                nickName.setText(editNickName.getText().toString())
                nickName.setVisibility(View.VISIBLE)
                editNickName.setVisibility(View.GONE)
                editButton.setText("수정")

                updateNickname(nickName.text as String)
            }
        }

        loadUser();
    }

    private fun updateNickname(nickname: String) {
        userService.updateNickname(FirebaseAuth.getInstance().uid, nickname).enqueue(object : Callback<NicknameResponse> {
            override fun onResponse(call: Call<NicknameResponse>, response: Response<NicknameResponse>) {
                Toast.makeText(activity, "닉네임이 변경 되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
            }
        })

    }

    private fun loadUser() {
        userService.getUser(FirebaseAuth.getInstance().uid).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                nickName.setText(response.body()?.nickname)
                local.setText(response.body()?.address ?: "동네를 설정하세요")

            }

            override fun onFailure(call: Call<User>, t: Throwable) {
            }

        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_setting, container, false)
    }



    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    //프로필 사진 변경-갤러리 오픈 fun
    fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }
    //https://taekwang.tistory.com/6
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY) {
                var currentImageUrl : Uri? = data?.data

                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, currentImageUrl)
                    imageButton.setImageBitmap(bitmap)
                } catch(e: Exception){
                    e.printStackTrace()
                }
            }
        } else {
            Log.d("ActivityResult", "something wrong")
        }
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentSetting.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                FragmentSetting().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
