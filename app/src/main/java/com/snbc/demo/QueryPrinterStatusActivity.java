package com.snbc.demo;

import java.io.IOException;
import java.net.SocketTimeoutException;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelQuery;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.connect.IConnect.DeviceConnect;
import com.snbc.sdk.connect.connectImpl.USBConnect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class QueryPrinterStatusActivity extends Activity {
	private TextView tv_back;
	private Button btn_query_printer_status;
	private TextView tv_status_result;
	private SNBCApplication application;
	@SuppressLint("CutPasteId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_printer_status);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		application = (SNBCApplication)getApplication();
		tv_back = (TextView) findViewById(R.id.tv_back_printer_status);
		btn_query_printer_status = (Button) findViewById(R.id.btn_query_printer_status);
		tv_status_result = (TextView) findViewById(R.id.tv_printer_status);
		tv_back.setOnClickListener(new BackListener());
		btn_query_printer_status.setOnClickListener(new QueryListener());

	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(QueryPrinterStatusActivity.this, MainActivity.class);
			startActivity(intent);
		}

	}

	/// \if English
	/// \defgroup QUERY_STATUS Query printer status
	/// \elseif Chinese
	/// \defgroup QUERY_STATUS 查询打印机状态
	/// \endif
	/// \code
	private class QueryListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			BarPrinter printer = null;
			InstructionType mType = null;
			StringBuilder buffer = new StringBuilder();
			byte[] status = new byte[] {0x00,0x00};
			int nReturn = 0;
			try {
				printer = application.getPrinter();
				mType = printer.labelQuery().getPrinterLanguage();
				SNBCApplication application =  ((SNBCApplication)getApplication());
				ILabelQuery labelQuery = printer.labelQuery();
				String string = " 正常";
				switch (mType) {
					case BPLZ:
						nReturn = labelQuery.getPrinterStatus(status);
						if (nReturn < 0) {
							//读取失败
							string = " 读取失败  ";
						}
						if((status[0] & 0x01) == 0x01){
							//缺纸
							string +=  " 缺纸  ";
						}
						if((status[0] & 0x02) == 0x02){
							//上盖抬起
							string += " 上盖抬起 ";
						}
						tv_status_result.setText(string);
						break;
					case BPLE:
						nReturn = labelQuery.getPrinterStatus(status);
						if (nReturn < 0) {
							//读取失败
							string = " 读取失败  ";
						}
						if((status[0] & 0x01) == 0x01){
							//缺纸
							string +=  " 缺纸  ";
						}
						if((status[0] & 0x02) == 0x02){
							//上盖抬起
							string += " 上盖抬起 ";
						}
						tv_status_result.setText(string);
						break;
					case BPLC:
						nReturn = labelQuery.getPrinterStatus(status);
						if (nReturn < 0) {
							//读取失败
							string = " 读取失败  ";
						}
						if((status[0] & 0x01) == 0x01){
							//缺纸
							string +=  " 缺纸  ";
						}
						if((status[0] & 0x02) == 0x02){
							//上盖抬起
							string += " 上盖抬起 ";
						}
						tv_status_result.setText(string);
						break;
					case BPLT:
						AlertDialogUtil.showDialog("Sorry, BPLT instruction can't support this function.", QueryPrinterStatusActivity.this);
						break;
					case BPLA:
						labelQuery = printer.labelQuery();
						Boolean papo = labelQuery.isHeadOpened();
						buffer.append(getResources().getString(R.string.hint_paper_out)).append(papo==true?" YES":" NO").append("\r\n");
						//AlertDialogUtil.showDialog("Sorry, BPLA instruction can't support this function.", QueryPrinterStatusActivity.this);
						tv_status_result.setText(buffer.toString());
						break;
					default:
						break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, QueryPrinterStatusActivity.this);
				buffer = new StringBuilder(getResources().getString(R.string.hint_query_status_failed));
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
