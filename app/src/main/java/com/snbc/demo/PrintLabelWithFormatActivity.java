package com.snbc.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.enumeration.InstructionType;

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

public class PrintLabelWithFormatActivity extends Activity {
	private TextView tv_back_print_label_withformat;
	private Button btn_print_label_withformat;
	private Spinner sp_print_label_withformat;
	private List<String> formatList = new ArrayList<String>();
	private ArrayAdapter<String> formatAdapter;
	private SNBCApplication application;
	private EditText et_print_label_with_format;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_label_withformat);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		tv_back_print_label_withformat = (TextView) findViewById(R.id.tv_back_print_label_withformat);
		btn_print_label_withformat = (Button) findViewById(R.id.btn_print_label_withformat);
		sp_print_label_withformat = (Spinner) findViewById(R.id.sp_format_stored);
		et_print_label_with_format = (EditText) findViewById(R.id.et_print_label_with_format);
		et_print_label_with_format.setText(getResources().getString(R.string.print_default_text2));
		application = (SNBCApplication)getApplication();
		if(application.getPrinter().labelQuery().getPrinterLanguage() == InstructionType.BPLA ){
			AlertDialogUtil.showDialog("Sorry, BPLA instruction can't support this function.", PrintLabelWithFormatActivity.this);
		}
		try {
			formatList = new ArrayList<String>(application.getPrinter().labelQuery().getFormatFileName());
			formatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,formatList);
			formatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_print_label_withformat.setAdapter(formatAdapter);
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, PrintLabelWithFormatActivity.this);
		}

		tv_back_print_label_withformat.setOnClickListener(new BackListener());

		btn_print_label_withformat.setOnClickListener(new PrintLabelWithFormatListener());
	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PrintLabelWithFormatActivity.this,MainActivity.class);
			startActivity(intent);
		}
	}
	/// \if English
	/// \defgroup PRINT_LABEL_WITH_FORMAT Print label with format
	/// \elseif Chinese
	/// \defgroup PRINT_LABEL_WITH_FORMAT 基于Format打印标签
	/// \endif
	/// \code
	private class PrintLabelWithFormatListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			String to_Print = et_print_label_with_format.getText().toString();
			if(TextUtils.isEmpty(to_Print)||formatList.size() == 0 ){
				return;
			}
			try {
				BarPrinter printer = application.getPrinter();
				switch (printer.labelQuery().getPrinterLanguage()) {
					case BPLZ:
					case BPLE:
					case BPLC:
						doPrint(printer);
						break;
					case BPLT:
						break;
					default:
						break;
				}
			} catch (Exception e) {
				AlertDialogUtil.showDialog(e, PrintLabelWithFormatActivity.this);
			}
		}


		private void doPrint(BarPrinter printer) throws Exception {
			if(printer.labelQuery().getPrinterLanguage()== InstructionType.BPLT || printer.labelQuery().getPrinterLanguage() ==InstructionType.BPLE){
				printer.labelEdit().clearPrintBuffer();
			}
			String selectedItem = sp_print_label_withformat.getItemAtPosition(sp_print_label_withformat.getSelectedItemPosition()).toString();
			Map<String, String> map = new HashMap<String, String>();
			String to_print = et_print_label_with_format.getText().toString();
			String[] to_prints = to_print.split("\n");
			for (int i = 0; i < to_prints.length; i++) {
				map.put((i+1)+"", to_prints[i]);
			}
			printer.labelFormat().printStoredFormat(selectedItem, map);
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
