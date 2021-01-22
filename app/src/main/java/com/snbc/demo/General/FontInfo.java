package com.snbc.demo.General;
public class FontInfo {
	public static final int FONT_ZOOMIN_MODE_DOT = 1;
	public static final int FONT_ZOOMIN_MODE_MULTIPLE = 2;
	private String fontName;
	private CGSize fontSize;
	private String isStrtchable;
	private int zoomInMode;

	public FontInfo(String fontName,CGSize fontSize){
		this.fontName = fontName;
		this.fontSize = fontSize;
	}

	public FontInfo(String fontName,CGSize font,int zoomInMode){
		this.fontName = fontName;
		this.fontSize = font;
		this.zoomInMode = zoomInMode;
	}

	public FontInfo(String fontName){
		this.fontName = fontName;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public CGSize getFontSize() {
		return fontSize;
	}

	public void setFontSize(CGSize fontSize) {
		this.fontSize = fontSize;
	}

	public String getIsStrtchable() {
		return isStrtchable;
	}
	public void setIsStrtchable(String isStrtchable) {
		this.isStrtchable = isStrtchable;
	}
	public int getZoomInMode() {
		return zoomInMode;
	}
	public void setZoomInMode(int zoomInMode) {
		this.zoomInMode = zoomInMode;
	}


	@Override
	public String toString() {
		return this.fontName;
	}
}
