package com.snbc.demo.General;

import android.app.Application;

import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.connect.IConnect.DeviceConnect;

public class SNBCApplication extends Application {
	private BarPrinter printer;
	private FontInfo[] storedBuildinFontArray;
	private String[] storedCustomFontArray;
	private String[] storedImageArray;
	private String[] storedFormatArray;
	private String[] osImageFileArray;
	private String[] osImageFileForPrintArray;
	private String[] osFontFileArray;
	private String[] osFormatFileArray;
	private String ramDiskSymbol;
	private String flashDiskSymbol;
	private String decodeType;
	private FontInfo fontInfo;
	private DeviceConnect connect;

	public DeviceConnect getConnect() {
		return connect;
	}

	public void setConnect(DeviceConnect connect) {
		this.connect = connect;
	}

	public String getDecodeType() {
		return decodeType;
	}

	public BarPrinter getPrinter() {
		return printer;
	}

	public void setPrinter(BarPrinter printer) {
		this.printer = printer;
	}



	public FontInfo[] getStoredBuildinFontArray() {
		return storedBuildinFontArray;
	}

	public void setStoredBuildinFontArray(FontInfo[] storedBuildinFontArray) {
		this.storedBuildinFontArray = storedBuildinFontArray;
	}

	public void setDecodeType(String decodeType) {
		this.decodeType = decodeType;
	}

	public String[] getStoredCustomFontArray() {
		return storedCustomFontArray;
	}

	public void setStoredCustomFontArray(String[] storedCustomFontArray) {
		this.storedCustomFontArray = storedCustomFontArray;
	}

	public String[] getStoredImageArray() {
		return storedImageArray;
	}

	public void setStoredImageArray(String[] storedImageArray) {
		this.storedImageArray = storedImageArray;
	}

	public String[] getStoredFormatArray() {
		return storedFormatArray;
	}

	public void setStoredFormatArray(String[] storedFormatArray) {
		this.storedFormatArray = storedFormatArray;
	}

	public String[] getOsImageFileArray() {
		return osImageFileArray;
	}

	public void setOsImageFileArray(String[] osImageFileArray) {
		this.osImageFileArray = osImageFileArray;
	}

	public String[] getOsImageFileForPrintArray() {
		return osImageFileForPrintArray;
	}

	public void setOsImageFileForPrintArray(String[] osImageFileForPrintArray) {
		this.osImageFileForPrintArray = osImageFileForPrintArray;
	}

	public String[] getOsFontFileArray() {
		return osFontFileArray;
	}

	public void setOsFontFileArray(String[] osFontFileArray) {
		this.osFontFileArray = osFontFileArray;
	}

	public String[] getOsFormatFileArray() {
		return osFormatFileArray;
	}

	public void setOsFormatFileArray(String[] osFormatFileArray) {
		this.osFormatFileArray = osFormatFileArray;
	}

	public String getRamDiskSymbol() {
		return ramDiskSymbol;
	}

	public void setRamDiskSymbol(String ramDiskSymbol) {
		this.ramDiskSymbol = ramDiskSymbol;
	}

	public String getFlashDiskSymbol() {
		return flashDiskSymbol;
	}

	public void setFlashDiskSymbol(String flashDiskSymbol) {
		this.flashDiskSymbol = flashDiskSymbol;
	}


	public FontInfo getFontInfo() {
		return fontInfo;
	}

	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		decodeType = "UTF-8";
	}

}
