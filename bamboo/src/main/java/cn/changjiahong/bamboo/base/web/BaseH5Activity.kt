package cn.changjiahong.bamboo.base.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.changjiahong.bamboo.R

class BaseH5Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_h5)
    }
}