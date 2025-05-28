package com.github.chiamingmai.androidutil.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Environment
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

/** 權限功能工具 */
object PermissionUtils {
    /** 外部儲存權限列表
     *
     * - 欲將檔案儲存在[Environment.getExternalStoragePublicDirectory]+[Environment.DIRECTORY_DOWNLOADS]路徑，需要請求[Manifest.permission.WRITE_EXTERNAL_STORAGE]權限，否則無法儲存；
     * 其餘路徑如[Environment.DIRECTORY_PICTURES]則可以不用請求[Manifest.permission.WRITE_EXTERNAL_STORAGE]權限
     *
     * - 若targetSDK >= 33, 需配合將request中設定 uncheck = true, 避免套件報錯
     * */
    val storagePermissions: List<String> = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_IMAGES)
            add(Manifest.permission.READ_MEDIA_VIDEO)
            add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                //小於等於 Android 10需詢問寫入權限
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    /** 前往應用程式設定頁 */
    @JvmStatic
    fun goToSettingPage(context: Context) = XXPermissions.startPermissionActivity(context)

    interface Callback {
        /**
         * 權限拒絕事件
         *
         * @param permissions 請求成功權限
         * @param allGranted 是否全部授予權限
         */
        fun onPermissionGranted(permissions: List<String>, allGranted: Boolean)

        /**
         * 權限拒絕事件
         *
         * @param permissions 請求失敗權限
         * @param doNotAskAgain 是否勾選不再詢問選項
         */
        fun onPermissionDenied(permissions: List<String>, doNotAskAgain: Boolean)
    }

    /** 請求多個權限
     *
     * @param context Context
     * @param permissions 請求權限列表
     * @param uncheck 是否關閉套件權限檢查
     * @param callback 事件
     */
    @JvmStatic
    fun request(
        context: Context,
        permissions: List<String>,
        uncheck: Boolean = false,
        callback: Callback
    ) {
        XXPermissions.with(context)
            .apply {
                if (uncheck) this.unchecked()
            }
            .permission(permissions)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, allGranted: Boolean) {
                    callback.onPermissionGranted(permissions, allGranted)
                }

                override fun onDenied(permissions: List<String>, doNotAskAgain: Boolean) {
                    callback.onPermissionDenied(permissions, doNotAskAgain)
                }
            })
    }

    /** 請求單一權限
     *
     * @param context Context
     * @param permissions 請求權限列表
     * @param uncheck 是否關閉套件權限檢查
     * @param callback 事件
     */
    @JvmStatic
    fun request(
        context: Context,
        permissions: String,
        uncheck: Boolean = false,
        callback: Callback
    ) = request(context, listOf(permissions), uncheck, callback)
}