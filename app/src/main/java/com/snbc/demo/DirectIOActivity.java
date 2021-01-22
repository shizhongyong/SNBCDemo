package com.snbc.demo;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.connect.IConnect.DeviceConnect;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DirectIOActivity extends Activity {
	private TextView tv_back_direct_io;
	private EditText et_to_send;
	private Button btn_direct_io;
	private SNBCApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		setContentView(R.layout.activity_directio);
		tv_back_direct_io = (TextView) findViewById(R.id.tv_back_direct_io);
		et_to_send = (EditText) findViewById(R.id.et_to_send);
		btn_direct_io = (Button) findViewById(R.id.btn_direct_io);
		tv_back_direct_io.setOnClickListener(new BackListener());
		btn_direct_io.setOnClickListener(new SendListener());

		application = (SNBCApplication)getApplication();
		try {
			switch (application.getPrinter().labelQuery().getPrinterLanguage()) {
				case BPLZ:
					et_to_send.setText(getResources().getString(R.string.default_send_BPLZ));
					break;
				case BPLE:
					et_to_send.setText(getResources().getString(R.string.default_send_BPLE));
					break;
				case BPLC:
					et_to_send.setText(getResources().getString(R.string.default_send_BPLC));
					break;
				case BPLT:
					et_to_send.setText(getResources().getString(R.string.default_send_BPLT));
					break;
				case BPLA:
					et_to_send.setText(new String(new char[]{(char)0x02})+getResources().getString(R.string.default_send_BPLA));
					break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, DirectIOActivity.this);
		}


	}

	private class BackListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(DirectIOActivity.this, MainActivity.class);
			startActivity(intent);
		}

	}

	private class SendListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			/// \if English
			/// \defgroup SEND_DATA_TO_DEVICE Send data to device
			/// \elseif Chinese
			/// \defgroup SEND_DATA_TO_DEVICE 将数据发送给打印机
			/// \endif
			/// \code
			String to_send = et_to_send.getText().toString();
			if (TextUtils.isEmpty(to_send)) {
				return ;
			}
			try {
				DeviceConnect connect = application.getConnect();
				connect.write(to_send.getBytes(application.getDecodeType()));
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, DirectIOActivity.this);
			}
			/// \endcode
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
