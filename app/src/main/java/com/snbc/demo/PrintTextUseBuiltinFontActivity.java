package com.snbc.demo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.FontInfo;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelEdit;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.barcode.enumeration.Rotation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class PrintTextUseBuiltinFontActivity extends Activity {
	private TextView tv_back_print_text_use_buildin_font;
	private Spinner sp_print_text_use_buildin_font;
	private EditText et_print_text_use_buildin_font;
	private Button btn_print_text_use_buildin_font;
	private List<FontInfo> fontList = new ArrayList<FontInfo>();
	private ArrayAdapter<FontInfo> fontAdapter;
	private int fontWidth1,fontHeight1, fontWidth2,fontHeight2;
	private SNBCApplication application;
	private FontInfo selectedFont;
	private String fontName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_text_use_builtin_font);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		tv_back_print_text_use_buildin_font = (TextView) findViewById(R.id.tv_back_print_text_use_buildin_font);
		sp_print_text_use_buildin_font = (Spinner) findViewById(R.id.sp_print_text_use_buildin_font);
		et_print_text_use_buildin_font = (EditText) findViewById(R.id.et_print_text_use_buildin_font);
		btn_print_text_use_buildin_font = (Button) findViewById(R.id.btn_print_text_use_buildin_font);
		et_print_text_use_buildin_font.setText(getResources().getString(R.string.print_default_text));
		application = (SNBCApplication)getApplication();
		fontList = Arrays.asList(application.getStoredBuildinFontArray());
		fontAdapter = new ArrayAdapter<FontInfo>(this,android.R.layout.simple_spinner_item,fontList);
		fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_print_text_use_buildin_font.setAdapter(fontAdapter);
		sp_print_text_use_buildin_font.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				selectedFont = fontList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		tv_back_print_text_use_buildin_font.setOnClickListener(new BackListener());
		btn_print_text_use_buildin_font.setOnClickListener(new PrintListener());


	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PrintTextUseBuiltinFontActivity.this,MainActivity.class);
			startActivity(intent);
		}

	}

	private class PrintListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			String to_print = et_print_text_use_buildin_font.getText().toString();
			BarPrinter printer = null;
			InstructionType mType = null;
			ILabelEdit labelEdit = null;
			if (TextUtils.isEmpty(to_print)) {
				AlertDialogUtil.showDialog(getResources().getString(R.string.hint_to_print_null), PrintTextUseBuiltinFontActivity.this);
				return;
			}
			try {
				printer = application.getPrinter();
				mType = printer.labelQuery().getPrinterLanguage();
				labelEdit = printer.labelEdit();

				switch (mType) {
					case BPLZ:
						doPrintTextBPLZ(to_print, printer, labelEdit);

						break;
					case BPLE:
						doPrintTextBPLE(to_print, printer, labelEdit);
						break;
					case BPLC:
						doPrintTextBPLC(to_print, printer, labelEdit);

						break;
					case BPLT:
						doPrintTextBPLT(to_print, printer, labelEdit);
						break;
					case BPLA:
						doPrintTextBPLA(to_print, printer, labelEdit);
						break;
					default:
						break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintTextUseBuiltinFontActivity.this);
				return;
			}

		}

		/// \if English
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLA Print text use the built-in font(BPLA)
		/// \elseif Chinese
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLA 使用内置字体打印文本(BPLA)
		/// \endif
		/// \code
		private void doPrintTextBPLA(String to_print, BarPrinter printer,ILabelEdit labelEdit) throws Exception {
			fontName = selectedFont.getFontName();
			fontWidth1 = 1;fontHeight1 = 1;
			fontWidth2 = 2;fontHeight2 = 2;
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 800);
			labelEdit.printText(4, 100, fontName, to_print, Rotation.Rotation0, fontWidth1, fontHeight1, 0);
			labelEdit.printText(4, 200, fontName, to_print, Rotation.Rotation0, fontWidth2, fontHeight2, 1);
			labelEdit.printText(640, 300, fontName, to_print, Rotation.Rotation180, fontWidth1, fontHeight1, 0);
			labelEdit.printText(640, 400, fontName, to_print, Rotation.Rotation180, fontWidth2, fontHeight2, 1);
			labelEdit.printText(40, 800, fontName, to_print, Rotation.Rotation90, fontWidth1, fontHeight1, 0);
			labelEdit.printText(100, 800, fontName, to_print, Rotation.Rotation90, fontWidth2, fontHeight2, 1);
			labelEdit.printText(524, 450, fontName, to_print, Rotation.Rotation270, fontWidth1, fontHeight1, 0);
			labelEdit.printText(624, 450, fontName, to_print, Rotation.Rotation270, fontWidth2, fontHeight2, 1);

			printer.labelControl().print(1, 1);
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLT Print text use the built-in font(BPLT)
		/// \elseif Chinese
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLT 使用内置字体打印文本(BPLT)
		/// \endif
		/// \code
		private void doPrintTextBPLT(String to_print, BarPrinter printer,ILabelEdit labelEdit) throws Exception{
			fontName = selectedFont.getFontName();
			if(fontName.toUpperCase(Locale.getDefault()).contains(".TTF")){
				fontWidth1 = 12;
				fontHeight1 = 18;
				fontWidth2 = 24;
				fontHeight2 = 36;
			}else{
				fontWidth1 = 1;
				fontHeight1 = 1;
				fontWidth2 = 2;
				fontHeight2 = 2;
			}
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 800);
			labelEdit.clearPrintBuffer();
			labelEdit.printText(4,4, fontName, to_print, Rotation.Rotation0, fontWidth1, fontHeight1, 0);
			labelEdit.printText(4,50, fontName, to_print, Rotation.Rotation0, fontWidth2, fontHeight2, 0);
			labelEdit.printText(600,200, fontName, to_print, Rotation.Rotation180, fontWidth1, fontHeight1, 0);
			labelEdit.printText(600,300, fontName, to_print, Rotation.Rotation180, fontWidth2, fontHeight2, 0);
			labelEdit.printText(4,300, fontName, to_print, Rotation.Rotation90, fontWidth1, fontHeight1, 0);
			labelEdit.printText(104,300, fontName, to_print, Rotation.Rotation90, fontWidth2, fontHeight2, 0);
			labelEdit.printText(324,800, fontName, to_print, Rotation.Rotation270, fontWidth1, fontHeight1, 0);
			labelEdit.printText(424,800, fontName, to_print, Rotation.Rotation270, fontWidth2, fontHeight2, 0);
			printer.labelControl().print(1, 1);
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLC Print text use the built-in font(BPLC)
		/// \elseif Chinese
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLC 使用内置字体打印文本(BPLC)
		/// \endif
		/// \code
		private void doPrintTextBPLC(String to_print, BarPrinter printer,ILabelEdit labelEdit) throws Exception {
			fontName = selectedFont.getFontName();

			int[] fontSizeOfFont0 = {0,1,2,3,4,5,6};  //of font "0"
			int[] fontSizeOfFont1 = {0}; 			  //of font "1"
			int[] fontSizeOfFont2 = {0,1};            //of font "2"
			int[] fontSizeOfFont4 = {0,1,2,3,4,5,6,7};//of font "4"
			int[] fontSizeOfFont5 = {0,1,2,3};        //of font "5"
			int[] fontSizeOfFont6 = {0};              //of font "6"
			int[] fontSizeOfFont7 = {0,1};            //of font "7"
			int fontSizeMin = 0, fontSizeMax = 1, fontSize = 0;
			if (fontName .equals("0")) {
				fontSizeMin = fontSizeOfFont0[0];
				fontSizeMax = fontSizeOfFont0.length - 1 ;
			}else if(fontName .equals("1")){
				fontSizeMin = fontSizeOfFont1[0];
				fontSizeMax = fontSizeOfFont1.length - 1 ;
			}else if(fontName .equals("2")){
				fontSizeMin = fontSizeOfFont2[0];
				fontSizeMax = fontSizeOfFont2.length - 1 ;
			}else if(fontName .equals("4")){
				fontSizeMin = fontSizeOfFont4[0];
				fontSizeMax = fontSizeOfFont4.length - 1 ;
			}else if(fontName .equals("5")){
				fontSizeMin = fontSizeOfFont5[0];
				fontSizeMax = fontSizeOfFont5.length - 1 ;
			}else if(fontName .equals("6")){
				fontSizeMin = fontSizeOfFont6[0];
				fontSizeMax = fontSizeOfFont6.length - 1 ;
			}else if(fontName .equals("7")){
				fontSizeMin = fontSizeOfFont7[0];
				fontSizeMax = fontSizeOfFont7.length - 1 ;
			}

			for (int i = 0; i < 2; i++) {
				if (i == 0) {
					fontSize = fontSizeMin;     //print text use min size
				}else if(i == 1){
					if(fontSizeMin == fontSizeMax)  break;  //the font only have one size
					fontSize = fontSizeMax;                 //print text use max size
				}
				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(576, 800);
				labelEdit.printText(256, 400, fontName, to_print, Rotation.Rotation0, 0, fontSize, 0);
				labelEdit.printText(256, 400, fontName, to_print, Rotation.Rotation90, 0, fontSize, 0);
				labelEdit.printText(256, 400, fontName, to_print, Rotation.Rotation180, 0, fontSize, 0);
				labelEdit.printText(256, 400, fontName, to_print, Rotation.Rotation270, 0, fontSize, 0);
				printer.labelControl().print(1, 1);
			}
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLE Print text use the built-in font(BPLE)
		/// \elseif Chinese
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLE 使用内置字体打印文本(BPLE)
		/// \endif
		/// \code
		private void doPrintTextBPLE(String to_print, BarPrinter printer,ILabelEdit labelEdit) throws Exception {
			fontName = selectedFont.getFontName();
			fontWidth1 = 1;fontHeight1 = 1;
			fontWidth2 = 2;fontHeight2 = 2;
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 800);
			labelEdit.clearPrintBuffer();
			labelEdit.printText(4,4, fontName, to_print, Rotation.Rotation0, fontWidth1, fontHeight1, 0);
			labelEdit.printText(4,100, fontName, to_print, Rotation.Rotation0, fontWidth2, fontHeight2, 1);
			labelEdit.printText(640,300, fontName, to_print, Rotation.Rotation180, fontWidth1, fontHeight1, 0);
			labelEdit.printText(640,400, fontName, to_print, Rotation.Rotation180, fontWidth2, fontHeight2, 1);
			labelEdit.printText(4,400, fontName, to_print, Rotation.Rotation90, fontWidth1, fontHeight1, 0);
			labelEdit.printText(100,400, fontName, to_print, Rotation.Rotation90, fontWidth2, fontHeight2, 1);
			labelEdit.printText(324,800, fontName, to_print, Rotation.Rotation270, fontWidth1, fontHeight1, 0);
			labelEdit.printText(424,800, fontName, to_print, Rotation.Rotation270, fontWidth2, fontHeight2, 1);
			printer.labelControl().print(1, 1);
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLZ Print text use the built-in font(BPLZ)
		/// \elseif Chinese
		/// \defgroup PRINT_TEXT_USE_BUILTIN_FONT_BPLZ 使用内置字体打印文本(BPLZ)
		/// \endif
		/// \code
		private void doPrintTextBPLZ(String to_print, BarPrinter printer,ILabelEdit labelEdit) throws Exception {
			fontName = selectedFont.getFontName();
			fontWidth1 = (int) selectedFont.getFontSize().getWidth();
			fontHeight1 = (int) selectedFont.getFontSize().getHeight();
			fontWidth2 = fontWidth1 * 2;
			fontHeight2 = fontHeight1 *2;
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 800);
			labelEdit.printText(4, 4, fontName, to_print, Rotation.Rotation0, fontWidth1, fontHeight1, 0);
			labelEdit.printText(4,50, fontName, to_print, Rotation.Rotation0, fontWidth2, fontHeight2, 0);
			labelEdit.printText(4,150, fontName, to_print, Rotation.Rotation180, fontWidth1, fontHeight1, 0);
			labelEdit.printText(4,200, fontName, to_print, Rotation.Rotation180, fontWidth2, fontHeight2, 0);
			labelEdit.printText(4,300, fontName, to_print, Rotation.Rotation90, fontWidth1, fontHeight1, 0);
			labelEdit.printText(54,300, fontName, to_print, Rotation.Rotation90, fontWidth2, fontHeight2, 0);
			labelEdit.printText(324,300, fontName, to_print, Rotation.Rotation270, fontWidth1, fontHeight1, 0);
			labelEdit.printText(374,300, fontName, to_print, Rotation.Rotation270, fontWidth2, fontHeight2, 0);
			printer.labelControl().print(1, 1);
		}
		/// \endcode

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
