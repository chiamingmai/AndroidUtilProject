package com.github.chiamingmai.androidutil.utils

import android.Manifest
import android.content.Context
import android.os.Build
import com.github.chiamingmai.androidutil.extensions.isPermissionGranted
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

/** 權限功能工具 */
object PermissionUtils {
    /** 讀取媒體類型 */
    enum class ReadMediaType {
        /** 圖片檔案 */
        IMAGE,

        /** 影片檔案 */
        VIDEO,

        /** 聲音檔案 */
        AUDIO
    }

    /** 取得媒體存取權限
     *
     * - 欲將檔案儲存在[android.os.Environment.getExternalStoragePublicDirectory]+[android.os.Environment.DIRECTORY_DOWNLOADS]路徑，需要請求[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]權限，否則無法儲存；
     * 其餘路徑如[android.os.Environment.DIRECTORY_PICTURES]則可以不用請求[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]權限
     *
     * - 若targetSDK >= 33, 需配合將request中設定 uncheck = true, 避免套件報錯
     *
     * [Partial photo/video access](https://developer.android.com/about/versions/14/changes/partial-photo-video-access?hl=zh-tw#app-gallery-picker)
     */
    @JvmStatic
    fun getReadMediaPermissions(vararg types: ReadMediaType) =
        getReadMediaPermissions(types.toList())

    /** 取得媒體存取權限
     *
     * - 欲將檔案儲存在[android.os.Environment.getExternalStoragePublicDirectory]+[android.os.Environment.DIRECTORY_DOWNLOADS]路徑，需要請求[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]權限，否則無法儲存；
     * 其餘路徑如[android.os.Environment.DIRECTORY_PICTURES]則可以不用請求[android.Manifest.permission.WRITE_EXTERNAL_STORAGE]權限
     *
     * - 若targetSDK >= 33, 需配合將request中設定 uncheck = true, 避免套件報錯
     *
     * [Partial photo/video access](https://developer.android.com/about/versions/14/changes/partial-photo-video-access?hl=zh-tw#app-gallery-picker)
     */
    @JvmStatic
    fun getReadMediaPermissions(types: List<ReadMediaType>): List<String> {
        val permissions = mutableListOf<String>()

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                types.distinct().forEach { type ->
                    permissions.add(
                        when (type) {
                            ReadMediaType.IMAGE -> Manifest.permission.READ_MEDIA_IMAGES
                            ReadMediaType.VIDEO -> Manifest.permission.READ_MEDIA_VIDEO
                            ReadMediaType.AUDIO -> Manifest.permission.READ_MEDIA_AUDIO
                        }
                    )
                }
                permissions.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                types.distinct().forEach { type ->
                    permissions.add(
                        when (type) {
                            ReadMediaType.IMAGE -> Manifest.permission.READ_MEDIA_IMAGES
                            ReadMediaType.VIDEO -> Manifest.permission.READ_MEDIA_VIDEO
                            ReadMediaType.AUDIO -> Manifest.permission.READ_MEDIA_AUDIO
                        }
                    )
                }
            }

            else -> {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                    //小於等於 Android 10需詢問寫入權限
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        return permissions
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

    /** Check if this permission is granted or not. */
    @JvmStatic
    fun isPermissionGranted(context: Context, permission: String): Boolean =
        context.isPermissionGranted(permission)
}