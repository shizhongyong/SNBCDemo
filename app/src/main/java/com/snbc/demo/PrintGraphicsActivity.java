package com.snbc.demo;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelEdit;
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
import android.widget.Button;
import android.widget.TextView;

public class PrintGraphicsActivity extends Activity {
	private TextView tv_back_print_graphics;
	private Button btn_print_graphics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_graphics);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		tv_back_print_graphics = (TextView) findViewById(R.id.tv_back_print_graphics);
		btn_print_graphics = (Button) findViewById(R.id.btn_print_graphics);

		tv_back_print_graphics.setOnClickListener(new BackListener());
		btn_print_graphics.setOnClickListener(new PrintListener());

	}

	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PrintGraphicsActivity.this,MainActivity.class);
			startActivity(intent);
		}
	}
	private class PrintListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			BarPrinter printer =null;
			InstructionType mType = null;
			try {
				SNBCApplication application = (SNBCApplication)getApplication();
				printer = application.getPrinter();
				mType = printer.labelQuery().getPrinterLanguage();
				switch (mType) {
					case BPLZ:
						doPrintGraphicsBPLZ(printer);
						break;
					case BPLE:
						doPrintGraphicsBPLE(printer);
						break;
					case BPLC:
						doPrintGraphicsBPLC(printer);
						break;
					case BPLT:
						doPrintGraphicsBPLT(printer);
						break;
					case BPLA:
						doPrintGraphicsBPLA(printer);
						break;
					default:
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintGraphicsActivity.this);
			}
		}
	}

	/// \if English
	/// \defgroup PRINT_GRAPHICS_BPLE Print graphics(BPLE)
	/// \elseif Chinese
	/// \defgroup PRINT_GRAPHICS_BPLE 打印图形(BPLE)
	/// \endif
	/// \code
	private void doPrintGraphicsBPLE(BarPrinter printer){
		try {
			ILabelEdit labelEdit = printer.labelEdit();
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 800);
			labelEdit.clearPrintBuffer();
			//Print rectangle
			labelEdit.printRectangle(0, 0, 640, 800, 2);
			//Print square
			labelEdit.printRectangle(10, 10, 200, 200, 2);
			//Print triangle
			labelEdit.printTriangle(220, 10, 420, 10, 320, 210, 2);
			//Print line
			labelEdit.printLine(10, 270, 620, 270, 4);
			labelEdit.printLine(10, 370, 620, 370, 4);
			//Print bias
			labelEdit.printLine(10, 270, 620, 370, 4);
			labelEdit.printLine(10, 370, 620, 270, 4);
			printer.labelControl().print(1, 1);
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, PrintGraphicsActivity.this);
		}
	}
	/// \endcode

	/// \if English
	/// \defgroup PRINT_GRAPHICS_BPLA Print graphics(BPLA)
	/// \elseif Chinese
	/// \defgroup PRINT_GRAPHICS_BPLA 打印图形(BPLA)
	/// \endif
	/// \code
	private void doPrintGraphicsBPLA(BarPrinter printer) {
		try {
			ILabelEdit labelEdit = printer.labelEdit();
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 800);
			//Print rectangle
			labelEdit.printRectangle(0, 0, 640, 800, 2);
			//print square
			labelEdit.printRectangle(10, 10, 200, 200, 2);
			//print triangle
			labelEdit.printTriangle(220, 10, 420, 10, 320, 210, 2);
			//print line
			labelEdit.printLine(10, 520, 620, 520, 4);
			labelEdit.printLine(10, 620, 620, 620, 4);
			//print bias
			labelEdit.printLine(10, 520, 620, 620, 4);
			labelEdit.printLine(10, 620, 620, 520, 4);

			printer.labelControl().print(1, 1);

		} catch (Exception e) {
			AlertDialogUtil.showDialog(e, PrintGraphicsActivity.this);
		}
	}
	/// \endcode


	/// \if English
	/// \defgroup PRINT_GRAPHICS_BPLT Print graphics(BPLT)
	/// \elseif Chinese
	/// \defgroup PRINT_GRAPHICS_BPLT 打印图形(BPLT)
	/// \endif
	/// \code
	private void doPrintGraphicsBPLT(BarPrinter printer) {
		try {
			ILabelEdit labelEdit = printer.labelEdit();
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 800);
			labelEdit.clearPrintBuffer();
			//Print rectangle
			labelEdit.printRectangle(0, 0, 640, 800, 2);
			//print square
			labelEdit.printRectangle(10, 10, 200, 200, 2);
			//print triangle
			labelEdit.printTriangle(220, 10, 420, 10, 320, 210, 2);
			//print ellipse
			labelEdit.printEllipse(10, 270, 320, 200, 2);
			//print circular
			labelEdit.printEllipse(340, 270, 200, 200, 2);
			//print line
			labelEdit.printLine(10, 520, 620, 520, 4);
			labelEdit.printLine(10, 620, 620, 620, 4);
			//print bias
			labelEdit.printLine(10, 520, 620, 620, 4);
			labelEdit.printLine(10, 620, 620, 520, 4);

			printer.labelControl().print(1, 1);
		} catch (Exception e) {
			AlertDialogUtil.showDialog(e, PrintGraphicsActivity.this);
		}
	}
	/// \endcode

	/// \if English
	/// \defgroup PRINT_GRAPHICS_BPLC Print graphics(BPLC)
	/// \elseif Chinese
	/// \defgroup PRINT_GRAPHICS_BPLC 打印图形(BPLC)
	/// \endif
	/// \code
	private void doPrintGraphicsBPLC(BarPrinter printer) {
		try {
			ILabelEdit labelEdit = printer.labelEdit();
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 600);
			//Print rectangle
			labelEdit.printRectangle(0, 0, 560, 600, 2);
			//print square
			labelEdit.printRectangle(10, 10, 200, 200, 2);
			//print triangle
			labelEdit.printTriangle(220, 10, 420, 10, 320, 210, 2);
			//print line
			labelEdit.printLine(10, 240, 560, 240, 4);
			labelEdit.printLine(10, 340, 560, 340, 4);
			//print bias
			labelEdit.printLine(10, 240, 560, 340, 4);
			labelEdit.printLine(10, 340, 560, 240, 4);

			printer.labelControl().print(1, 1);
		} catch (Exception e) {
			AlertDialogUtil.showDialog(e, PrintGraphicsActivity.this);
		}
	}
	/// \endcode

	/// \if English
	/// \defgroup PRINT_GRAPHICS_BPLZ Print graphics(BPLZ)
	/// \elseif Chinese
	/// \defgroup PRINT_GRAPHICS_BPLZ 打印图形(BPLZ)
	/// \endif
	/// \code
	private void doPrintGraphicsBPLZ(BarPrinter printer){

		try {
			ILabelEdit labelEdit = printer.labelEdit();
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(640, 1000);
			//Print rectangle
			labelEdit.printRectangle(0, 0, 640, 800, 2);
			//Print square
			labelEdit.printRectangle(10, 10, 200, 200, 2);
			//Print triangle
			labelEdit.printTriangle(220, 10, 420, 10, 320, 210, 2);
			//Print ellipse
			labelEdit.printEllipse(10, 270, 320, 200, 2);
			//Print circular
			labelEdit.printEllipse(340, 270, 200, 200, 2);
			//Print line
			labelEdit.printLine(10, 520, 620, 520, 4);
			labelEdit.printLine(10, 620, 620, 620, 4);
			//Print bias
			labelEdit.printLine(10, 520, 620, 620, 4);
			labelEdit.printLine(10, 620, 620, 520, 4);
			printer.labelControl().print(1, 1);
		} catch (Exception e) {
			e.printStackTrace();
			AlertDialogUtil.showDialog(e, PrintGraphicsActivity.this);
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
