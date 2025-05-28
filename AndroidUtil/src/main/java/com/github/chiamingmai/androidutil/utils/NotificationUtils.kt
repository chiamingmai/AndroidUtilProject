package com.github.chiamingmai.androidutil.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.github.chiamingmai.androidutil.extensions.getNotificationBuilder

/** 通知功能工具 */
class NotificationUtils constructor(private val context: Context) {
    companion object {
        /** 取得 Notification Builder */
        @JvmStatic
        fun getNotificationBuilder(context: Context, channelId: String): Notification.Builder =
            context.getNotificationBuilder(channelId)
    }

    private val importanceList = listOf(
        NotificationManager.IMPORTANCE_UNSPECIFIED,
        NotificationManager.IMPORTANCE_NONE,
        NotificationManager.IMPORTANCE_MIN,
        NotificationManager.IMPORTANCE_LOW,
        NotificationManager.IMPORTANCE_DEFAULT,
        NotificationManager.IMPORTANCE_HIGH
    )

    /** The notificationManager instance for this class */
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /** Create a basic notification channel
     *
     * @param channelId ID of the notification channel.
     * @param name Name of the notification channel.     *
     * @param importance Importance of this notification channel.
     */
    fun createNotificationChannel(
        channelId: String,
        name: String,
        importance: Int
    ) {
        // Create a Notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (importance !in importanceList) {
                throw IllegalArgumentException("Invalid importance value")
            }
            // Create the NotificationChannel
            val mChannel = NotificationChannel(channelId, name, importance)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    /** 建立通知頻道群組以及通知頻道
     *
     * @param groupId ID of the channel group.
     * @param group The notification channel group.
     * @param channel The Notification channel.
     */
    fun createNotificationGroupAndChannel(
        groupId: String,
        group: NotificationChannelGroup,
        channel: NotificationChannel
    ) {
        // Create a Notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /** Create notification group and channel */
            fun create() {
                notificationManager.createNotificationChannelGroup(group)
                channel.group = groupId
                notificationManager.createNotificationChannel(channel)
            }

            //Check if name and description are not the same
            val existedGroup = findNotificationGroup(groupId)
            if (existedGroup != null) {
                val nameNotTheSame = existedGroup.name != group.name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && (nameNotTheSame || existedGroup.description != group.description)) {
                    //Remove existed channel group and channel
                    notificationManager.deleteNotificationChannel(channel.id)
                    notificationManager.deleteNotificationChannelGroup(groupId)
                    create()
                } else if (nameNotTheSame) {
                    //Remove existed channel group and channel
                    notificationManager.deleteNotificationChannel(channel.id)
                    notificationManager.deleteNotificationChannelGroup(groupId)
                    create()
                }
            } else {
                create()
            }
        }
    }

    /** Clear all NotificationChannels */
    fun clearAllNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.notificationChannels?.forEach {
                notificationManager.deleteNotificationChannel(it.id)
            }
        }
    }

    /** Clear all NotificationGroups */
    fun clearAllNotificationGroups() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //刪除NotificationGroups
            notificationManager.notificationChannelGroups?.forEach { group ->
                //刪除NotificationGroup底下NotificationChannel
                group.channels?.forEach { channel ->
                    notificationManager.deleteNotificationChannel(channel.id)
                }
                notificationManager.deleteNotificationChannelGroup(group.id)
            }
        }
    }

    /** Delete NotificationChannel by channelId */
    fun deleteNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(channelId)
        }
    }

    /** Delete NotificationChannelGroup by notification group id*/
    fun deleteNotificationChannelGroup(groupId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            findNotificationGroup(groupId)
                ?.let { group ->
                    group.channels?.forEach { channel ->
                        notificationManager.deleteNotificationChannel(channel.id)
                    }
                }

            notificationManager.deleteNotificationChannelGroup(groupId)
        }
    }

    private fun findNotificationGroup(groupId: String): NotificationChannelGroup? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.notificationChannelGroups?.find { it.id == groupId }
        } else {
            null
        }
    }

    /** Open notification channel settings
     *
     * @param channelId Notification channel's ID
     */
    fun openNotificationChannelSettings(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startActivity(
                Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
                }
            )
        }
    }
}
