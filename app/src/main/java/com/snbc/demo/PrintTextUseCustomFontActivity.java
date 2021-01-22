package com.snbc.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class PrintTextUseCustomFontActivity extends Activity {
	private TextView tv_back_print_text_use_custom_font;
	private Spinner sp_print_text_use_custom_font;
	private EditText et_print_text_use_custom_font;
	private Button btn_print_text_use_custom_font;
	private List<String> fontList = new ArrayList<String>();
	private ArrayAdapter<String> fontAdapter;
	private int fontWidth1,fontHeight1;
	private SNBCApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_print_text_use_custom_font);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		tv_back_print_text_use_custom_font = (TextView) findViewById(R.id.tv_back_print_text_use_custom_font);
		sp_print_text_use_custom_font = (Spinner) findViewById(R.id.sp_print_text_use_custom_font);
		et_print_text_use_custom_font = (EditText) findViewById(R.id.et_print_text_use_custom_font);
		btn_print_text_use_custom_font = (Button) findViewById(R.id.btn_print_text_use_custom_font);
		et_print_text_use_custom_font.setText(getResources().getString(R.string.print_default_text2));
		BarPrinter printer = null;
		InstructionType mType = null;
		try {
			application = (SNBCApplication)getApplication();
			printer = application.getPrinter();
			mType = printer.labelQuery().getPrinterLanguage();
			if(mType == InstructionType.BPLC){
				AlertDialogUtil.showDialog("Sorry, BPLC instruction can't support this function.", PrintTextUseCustomFontActivity.this);
			}else{
				fontList = Arrays.asList(application.getStoredCustomFontArray());
				fontAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,fontList);
				fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				sp_print_text_use_custom_font.setAdapter(fontAdapter);
			}
		} catch (Exception e) {
			AlertDialogUtil.showDialog(e, PrintTextUseCustomFontActivity.this);
		}
		tv_back_print_text_use_custom_font.setOnClickListener(new BackListener());
		btn_print_text_use_custom_font.setOnClickListener(new PrintListener());

	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PrintTextUseCustomFontActivity.this,MainActivity.class);
			startActivity(intent);

		}

	}

	private class PrintListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			String to_Print = et_print_text_use_custom_font.getText().toString();
			if(TextUtils.isEmpty(to_Print) || fontList.size() == 0){
				return ;
			}
			String fontName = sp_print_text_use_custom_font.getItemAtPosition(sp_print_text_use_custom_font.getSelectedItemPosition()).toString();
			try {
				BarPrinter printer = application.getPrinter();
				ILabelEdit labelEdit = printer.labelEdit();
				InstructionType mType = printer.labelQuery().getPrinterLanguage();
				switch (mType) {
					case BPLZ:
						doPrintTextBPLZ(to_Print, fontName, printer, labelEdit);
						break;
					case BPLE:
					case BPLA:
						AlertDialogUtil.showDialog("No cumstom font", PrintTextUseCustomFontActivity.this);
						break;
					case BPLC:
						doPrintTextBPLC(to_Print, fontName, printer, labelEdit);
						break;
					case BPLT:

						doPrintTextBPLT(to_Print, fontName, printer, labelEdit);
						break;
					default:
						break;
				}


			}catch(Exception e){
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintTextUseCustomFontActivity.this);
			}
		}
		/// \if English
		/// \defgroup PRINT_TEXT_USE_DOWNLOADED_FONT_BPLT Print text use the downloaded font(BPLT)
		/// \elseif Chinese
		/// \defgroup PRINT_TEXT_USE_DOWNLOADED_FONT_BPLT 使用用户下载字体打印文本(BPLT)
		/// \endif
		/// \code
		private void doPrintTextBPLT(String to_Print, String fontName,BarPrinter printer, ILabelEdit labelEdit) throws Exception {
			fontWidth1 = 12;
			fontHeight1 = 18;
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 800);
			labelEdit.clearPrintBuffer();

			labelEdit.printText(4, 4, fontName,to_Print, Rotation.Rotation0, fontWidth1, fontHeight1, 0);
			labelEdit.printText(4, 50,fontName ,to_Print, Rotation.Rotation0, fontWidth1 * 2, fontHeight1 * 2, 0);
			labelEdit.printText(600, 200, fontName,to_Print, Rotation.Rotation180, fontWidth1, fontHeight1, 0);
			labelEdit.printText(600, 300, fontName,to_Print, Rotation.Rotation180, fontWidth1 * 2, fontHeight1 * 2, 0);
			labelEdit.printText(4, 300, fontName,to_Print, Rotation.Rotation90, fontWidth1, fontHeight1, 0);
			labelEdit.printText(104, 300, fontName,to_Print, Rotation.Rotation90, fontWidth1* 2, fontHeight1* 2, 0);
			labelEdit.printText(324, 800, fontName,to_Print, Rotation.Rotation270, fontWidth1, fontHeight1, 0);
			labelEdit.printText(424, 800, fontName,to_Print, Rotation.Rotation270, fontWidth1* 2, fontHeight1* 2, 0);

			printer.labelControl().print(1, 1);
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_TEXT_USE_DOWNLOADED_FONT_BPLC Print text use the downloaded font(BPLC)
		/// \elseif Chinese
		/// \defgroup PRINT_TEXT_USE_DOWNLOADED_FONT_BPLC 使用用户下载字体打印文本(BPLC)
		/// \endif
		/// \code
		private void doPrintTextBPLC(String to_Print, String fontName,BarPrinter printer, ILabelEdit labelEdit) throws Exception {
			fontWidth1 = 12;
			fontHeight1 = 18;
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(576, 800);

			labelEdit.printText(256,400, fontName,to_Print, Rotation.Rotation0, fontWidth1, fontHeight1, 0);
			labelEdit.printText(256, 400,fontName ,to_Print, Rotation.Rotation90, fontWidth1, fontHeight1, 0);
			labelEdit.printText(256, 400, fontName,to_Print, Rotation.Rotation180, fontWidth1, fontHeight1, 0);
			labelEdit.printText(256, 400, fontName,to_Print, Rotation.Rotation270, fontWidth1, fontHeight1, 0);

			printer.labelControl().print(1, 1);
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_TEXT_USE_DOWNLOADED_FONT_BPLZ Print text use the downloaded font(BPLZ)
		/// \elseif Chinese
		/// \defgroup PRINT_TEXT_USE_DOWNLOADED_FONT_BPLZ 使用用户下载字体打印文本(BPLZ)
		/// \endif
		/// \code
		private void doPrintTextBPLZ(String to_Print, String fontName,BarPrinter printer, ILabelEdit labelEdit) throws Exception {
			fontWidth1 = 12;
			fontHeight1 = 18;
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 800);
			labelEdit.printText(4, 4, fontName,to_Print, Rotation.Rotation0, fontWidth1, fontHeight1, 0);
			labelEdit.printText(4, 50,fontName ,to_Print, Rotation.Rotation0, fontWidth1 * 2, fontHeight1 * 2, 0);
			labelEdit.printText(4, 150, fontName,to_Print, Rotation.Rotation180, fontWidth1, fontHeight1, 0);
			labelEdit.printText(4, 200, fontName,to_Print, Rotation.Rotation180, fontWidth1 * 2, fontHeight1 * 2, 0);
			labelEdit.printText(4, 300, fontName,to_Print, Rotation.Rotation90, fontWidth1, fontHeight1, 0);
			labelEdit.printText(54, 300, fontName,to_Print, Rotation.Rotation90, fontWidth1* 2, fontHeight1* 2, 0);
			labelEdit.printText(324, 300, fontName,to_Print, Rotation.Rotation270, fontWidth1, fontHeight1, 0);
			labelEdit.printText(374, 300, fontName,to_Print, Rotation.Rotation270, fontWidth1* 2, fontHeight1* 2, 0);

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
