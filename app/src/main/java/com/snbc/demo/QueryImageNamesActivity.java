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

public class QueryImageNamesActivity extends Activity {
	private TextView tv_back_query_images;
	private ListView lv_query_images_result;
	private Button btn_query_images;
	private List<String> imageList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_image_names);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		tv_back_query_images = (TextView) findViewById(R.id.tv_back_query_image);
		lv_query_images_result = (ListView) findViewById(R.id.lv_query_images);
		btn_query_images = (Button) findViewById(R.id.btn_query_images);

		tv_back_query_images.setOnClickListener(new BackListener());
		btn_query_images.setOnClickListener(new QueryListener());
	}
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(QueryImageNamesActivity.this,MainActivity.class);
			startActivity(intent);
		}
	}

	/// \if English
	/// \defgroup QUERY_IMAGE_FROM_PRINTER Query image from printer
	/// \elseif Chinese
	/// \defgroup QUERY_IMAGE_FROM_PRINTER 查询打印机存储的图像
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
					AlertDialogUtil.showDialog("Sorry, BPLA instruction can't support this function.", QueryImageNamesActivity.this);
					return;
				}
				imageList =	new ArrayList<String>(labelQuery.getImageFileName());
				ArrayAdapter<String> imageAdapter = new ArrayAdapter<String>(QueryImageNamesActivity.this, android.R.layout.simple_list_item_1,imageList);
				lv_query_images_result.setAdapter(imageAdapter);
				lv_query_images_result.setSelector(android.R.color.background_light);
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, QueryImageNamesActivity.this);
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
