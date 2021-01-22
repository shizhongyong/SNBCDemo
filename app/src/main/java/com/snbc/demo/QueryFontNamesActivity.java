package com.snbc.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelQuery;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.connect.connectImpl.USBConnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class QueryFontNamesActivity extends Activity {
	private TextView tv_back_query_fonts;
	private ListView lv_query_fonts_result;
	private Button btn_query_fonts;
	private List<String> fontNameList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_font_names);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		tv_back_query_fonts = (TextView) findViewById(R.id.tv_back_query_font);
		btn_query_fonts = (Button) findViewById(R.id.btn_query_fonts);
		lv_query_fonts_result = (ListView) findViewById(R.id.lv_query_fonts);

		tv_back_query_fonts.setOnClickListener(new BackListener());
		btn_query_fonts.setOnClickListener(new QueryListener());
	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(QueryFontNamesActivity.this,MainActivity.class);
			startActivity(intent);

		}

	}
	/// \if English
	/// \defgroup QUERY_FONT_FROM_PRINTER Query font from printer
	/// \elseif Chinese
	/// \defgroup QUERY_FONT_FROM_PRINTER 查询打印机存储的字库
	/// \endif
	/// \code
	private class QueryListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			try {
				SNBCApplication application = (SNBCApplication)getApplication();
				BarPrinter printer = application.getPrinter();
				ILabelQuery labelQuery = printer.labelQuery();
				if(labelQuery.getPrinterLanguage() == InstructionType.BPLA && !(application.getConnect() instanceof USBConnect)){
					AlertDialogUtil.showDialog("Sorry, BPLA instruction can't support this function.", QueryFontNamesActivity.this);
					return;
				}
				Set<String> fontSet = labelQuery.getFontFileName();
				fontNameList = new ArrayList<String>(fontSet);
				ArrayAdapter<String> fontAdapter = new ArrayAdapter<String>(QueryFontNamesActivity.this, android.R.layout.simple_list_item_1,fontNameList );
				lv_query_fonts_result.setAdapter(fontAdapter);
				lv_query_fonts_result.setSelector(android.R.color.background_light);
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, QueryFontNamesActivity.this);
			}
		}

	}
	/// \endcode

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
