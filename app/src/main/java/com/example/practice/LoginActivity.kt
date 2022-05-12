package com.example.practice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practice.databinding.ActivityLoginBinding

class LoginActivity:AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login(){
        if(binding.loginIdEt.text.toString().isEmpty()||binding.loginDirectInputEt.text.toString().isEmpty()){
            Toast.makeText(this,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show()
            return
        }
        val email: String = binding.loginIdEt.text.toString() + "@" +binding.loginDirectInputEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!! // SongDB연결
        val user = songDB.userDao().getUser(email,pwd)

        user?.let { // user!=null의 의미
            Log.d("LOGIN_ACT/GET_USER","userId : ${user.id}, $user")
            saveJwt(user.id) // userId를 jwt로 전송
            startMainActivity() // 메인 액티비티로 전환
            return
        }
        Toast.makeText(this,"회원 정보가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
    }

    private fun saveJwt(jwt:Int) { // 인자값으로 받은 jwt를 SharedPreference로 저장
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt",jwt)
        editor.apply()
    }

    private fun startMainActivity(){
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)

    }
}