package cn.changjiahong.bamboo.base.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 *
 * @author ChangJiahong
 * @date 2023/12/21
 */

fun Context.bluePermissions(granted: (permissions: MutableList<String>?, all: Boolean) -> Unit) {
//    XXPermissions.with(this).permission(Permission.BLUETOOTH_CONNECT)
//        .request(object : OnPermissionCallback {
//            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
//                granted(permissions, all)
//            }
//
//            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
//                if (never) {
//                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                    XXPermissions.startPermissionActivity(this@bluePermissions, permissions)
//                }
//            }
//        })

    requestPermissions(
        Permission.BLUETOOTH_CONNECT,
        Permission.BLUETOOTH_SCAN,
        Permission.ACCESS_COARSE_LOCATION,
        Permission.ACCESS_FINE_LOCATION
    ) { permissions, all -> granted(permissions, all) }
}

fun Context.requestPermissions(
    vararg permissions: String,
    granted: (permissions: MutableList<String>?, all: Boolean) -> Unit
) {
    XXPermissions.with(this).permission(permissions)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                granted(permissions, all)
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                if (never) {
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    XXPermissions.startPermissionActivity(this@requestPermissions, permissions)
                }
            }
        })
}

fun Context.checkSelfPermission(){

    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.BLUETOOTH_SCAN
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return
    }
}
