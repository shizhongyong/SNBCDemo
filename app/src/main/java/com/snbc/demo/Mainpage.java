package com.snbc.demo;
/**
 * \mainpage SNBC SDK for Barcode Printer
 * \if English
 * This document introduces the using method of SNBC SDK for Barcode Printer in details. Please read carefully before use.
 * \par I want to...\n
 * -# \ref PRINT_LABEL_WIFI_BPLZ \n
 * -# \ref PRINT_FORMAT_WIFI \n
 * -# \ref GET_PRINTER_STATUS_WIFI \n
 * -# \ref DOWNLOAD_IMAGE_FONT_FORMAT_WIFI\n
 *
 * \par Cautions while using SDK
 * -# Each Connection and Printer can only be used in single thread\n
 * -# Some functions only apply to particular command set. When the method that you call cannot be supported by your selected command set, SDK will cause BarFunctionNoSupportException type exception.
 *
 * \par android permission
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />\n
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>\n
 * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />\n
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>\n
 * <uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE"/>\n
 * <uses-permission android:name="android.permission.WAKE_LOCK" />\n
 * <uses-permission android:name="android.permission.INTERNET"></uses-permission>\n
 * <uses-feature android:name="android.hardware.wifi" />\n
 * <uses-permission android:name="android.permission.BLUETOOTH" />\n
 * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />\n
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />\n
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />\n
 *
 * \par Activity onCreate() need code
 * 	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork()\n.penaltyLog().build());\n
 *	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());\n
 *
 * \par Development environment information
 * -# Use above android4.0 \n
 *
 * \par Applicable product
 * -# BPLZ printer: Firmware version V2.100 and above \n
 * -# BPLE printer: Firmware version V2.100 and above \n
 * -# BPLT printer: Firmware version V3.100 and above \n
 * -# BPLC printer: Firmware version V1.000.40 and above \n
 *
 * \elseif Chinese
 * 本文档版详细介绍了SNBC SDK for Barcode Printer软件的使用方法，在使用SDK软件前，请仔细阅读
 * \par 我想...\n
 * -# \ref PRINT_LABEL_WIFI_BPLZ \n
 * -# \ref PRINT_FORMAT_WIFI \n
 * -# \ref GET_PRINTER_STATUS_WIFI \n
 * -# \ref DOWNLOAD_IMAGE_FONT_FORMAT_WIFI\n
 *
 * \par 使用SDK的注意事项
 * -# 每个 Connection 对象及 Printer 对象只能在单个线程中使用\n
 * -# 一些功能只适用于特定的指令集，当您调用的方法不被您选择的指令集支持时，SDK将会抛出 BarFunctionNoSupportException 类型的异常
 *
 * \par android中用到的权限
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />\n
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>\n
 * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />\n
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>\n
 * <uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE"/>\n
 * <uses-permission android:name="android.permission.WAKE_LOCK" />\n
 * <uses-permission android:name="android.permission.INTERNET"></uses-permission>\n
 * <uses-feature android:name="android.hardware.wifi" />\n
 * <uses-permission android:name="android.permission.BLUETOOTH" />\n
 * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />\n
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />\n
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />\n
 *
 * \par Activity onCreate()中需要添加如下代码
 * 	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());\n
 *	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
 *
 * \par 开发环境信息
 * -# 支持android4.0以上平台
 *
 * \par 适用的产品
 * -# BPLZ语言打印机:固件版本V2.100及以上 \n
 * -# BPLE语言打印机:固件版本V2.100及以上 \n
 * -# BPLT语言打印机:固件版本V3.100及以上 \n
 * -# BPLC语言打印机:固件版本V1.000.40 及以上
 * \endif
 */

