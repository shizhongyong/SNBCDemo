package com.snbc.demo;

import java.util.ArrayList;
import java.util.List;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelQuery;
import com.snbc.sdk.barcode.enumeration.InstructionType;

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

public class QueryFormatNamesActivity extends Activity {
	private TextView tv_back_query_formats;
	private ListView lv_query_formats_result;
	private Button btn_query_formats;
	private List<String> formatList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_format_names);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		tv_back_query_formats = (TextView) findViewById(R.id.tv_back_query_formats);
		lv_query_formats_result = (ListView) findViewById(R.id.lv_query_formats);
		btn_query_formats = (Button) findViewById(R.id.btn_query_formats);



		tv_back_query_formats.setOnClickListener(new BackListener());

		btn_query_formats.setOnClickListener(new QueryListener());
	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(QueryFormatNamesActivity.this,MainActivity.class);
			startActivity(intent);
		}
	}

	/// \if English
	/// \defgroup QUERY_FORMAT_FROM_PRINTER Query format from printer
	/// \elseif Chinese
	/// \defgroup QUERY_FORMAT_FROM_PRINTER 查询打印机存储的format
	/// \endif
	/// \code
	private class QueryListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			try {
				BarPrinter printer = ((SNBCApplication)getApplication()).getPrinter();
				ILabelQuery labelQuery = printer.labelQuery();
				if(labelQuery.getPrinterLanguage() == InstructionType.BPLA){
					AlertDialogUtil.showDialog("Sorry, BPLA instruction can't support this function.", QueryFormatNamesActivity.this);
					return;
				}
				formatList = new ArrayList<String>(labelQuery.getFormatFileName());
				ArrayAdapter<String> formatAdapter = new ArrayAdapter<String>(QueryFormatNamesActivity.this, android.R.layout.simple_list_item_1,formatList );
				lv_query_formats_result.setAdapter(formatAdapter);
				lv_query_formats_result.setSelector(android.R.color.background_light);
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, QueryFormatNamesActivity.this);
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
