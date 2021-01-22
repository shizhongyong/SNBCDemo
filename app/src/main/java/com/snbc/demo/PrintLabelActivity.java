package com.snbc.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.snbc.barcode.sdk.demo.R;
import com.snbc.demo.MainActivity;
import com.snbc.demo.General.AlertDialogUtil;
import com.snbc.demo.General.SNBCApplication;
import com.snbc.sdk.barcode.BarInstructionImpl.BarPrinter;
import com.snbc.sdk.barcode.IBarInstruction.ILabelConfig;
import com.snbc.sdk.barcode.IBarInstruction.ILabelEdit;
import com.snbc.sdk.barcode.IBarInstruction.ILabelQuery;
import com.snbc.sdk.barcode.enumeration.Area;
import com.snbc.sdk.barcode.enumeration.BarCodeType;
import com.snbc.sdk.barcode.enumeration.HRIPosition;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.barcode.enumeration.PaperMode;
import com.snbc.sdk.barcode.enumeration.PrintMethod;
import com.snbc.sdk.barcode.enumeration.PrintMode;
import com.snbc.sdk.barcode.enumeration.PrinterDirection;
import com.snbc.sdk.barcode.enumeration.QRLevel;
import com.snbc.sdk.barcode.enumeration.QRMode;
import com.snbc.sdk.barcode.enumeration.Rotation;
import com.snbc.sdk.exception.BarFunctionNoSupportException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class PrintLabelActivity extends Activity {
	private TextView tv_back_print_label;
	private Button btn_print_label;
	private Button btn_paper_type;
	private TextView paperType;
	private String[] labelArray = {"CHINA POST Airmail","CHINA POST Goods"};
	private Spinner sp_print_label;
	private List<String> labelList = new ArrayList<String>();
	private SNBCApplication application;
	private String mDecodeType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_label);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		tv_back_print_label = (TextView) findViewById(R.id.tv_back_print_label);
		btn_print_label = (Button) findViewById(R.id.btn_print_label);
		sp_print_label = (Spinner) findViewById(R.id.sp_print_label);
		labelList = Arrays.asList(labelArray);
		ArrayAdapter<String> labelAdapter = new ArrayAdapter<String>(PrintLabelActivity.this, android.R.layout.simple_spinner_item,labelList);
		labelAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		sp_print_label.setAdapter(labelAdapter);

		application = (SNBCApplication)getApplication();
		mDecodeType = application.getDecodeType();
		tv_back_print_label.setOnClickListener(new BackListener());
		btn_print_label.setOnClickListener(new PrintListener());
	}
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PrintLabelActivity.this,MainActivity.class);
			startActivity(intent);
		}
	}

	private class PrintListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			BarPrinter printer = application.getPrinter();
			InstructionType mType = printer.labelQuery().getPrinterLanguage();
			String seletedItem = sp_print_label.getItemAtPosition(sp_print_label.getSelectedItemPosition()).toString();
			switch (mType) {
				case BPLZ:
					doPrintLabelBPLZ(printer,seletedItem);
					break;
				case BPLE:
					doPrintLabelBPLE(printer,seletedItem);
					break;
				case BPLT:
					doPrintLabelBPLT(printer,seletedItem);
					break;
				case BPLC:
					doPrintLabelBPLC(printer,seletedItem);
					break;
				case BPLA:
					doPrintLabelBPLA(printer,seletedItem);
					break;
				default:
					break;
			}
		}

		private void doPrintLabelBPLA(BarPrinter printer, String seletedItem) {
			if (seletedItem.equals(labelArray[0])) {
				doPrintLabelChinaPostAirmailBPLA(printer);
			}else if(seletedItem.equals(labelArray[1])){
				doPrintLabelChinaPostGoodsBPLA(printer);
			}
		}
		/// \if English
		/// \defgroup PRINT_LABEL_BPLA_SAMPLE2 Print the second sample(BPLA)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLA_SAMPLE2 打印第2个示例样张(BPLA)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostGoodsBPLA(BarPrinter printer) {
			try {
				printer.labelConfig().setPrintDirection(PrinterDirection.Normal);
				ILabelEdit labelEdit = printer.labelEdit();

				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(640, 1000);

				labelEdit.printRectangle(0, 0, 640, 1000, 2);

				labelEdit.printImage(10, 923, BitmapFactory.decodeStream(getAssets().open("ChinaPostLogo.bmp")));

				labelEdit.printBarcodeQR(447, 876, Rotation.Rotation0, "LK002325883CN", QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());

				labelEdit.printText(10, 890, "1", "IMPORTANT:", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(10, 860, "1", "The item/parcel may be", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(10, 830, "1", "opened officially.", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(10, 800, "1", "Please print in English.", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printRectangle(270, 802, 72, 72, 2);
				labelEdit.printText(286, 812, "3", "2", Rotation.Rotation0, 2, 2, 0);

				labelEdit.printText(8, 760, "2", "FROM: HANK HUANG", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 735, "2", "4/F DONG HAI COMMERCIAL", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 710, "2", "BUILDING 618 YAN AN ", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 685, "2", "ROAD SHANGHAI", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 660, "2", "CHINA 190893", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 635, "2", "PHONE:13908473278", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printLine(327, 798, 640, 798, 1);
				labelEdit.printLine(327, 798, 327, 526, 1);
				labelEdit.printLine(0, 603, 327, 603, 1);
				labelEdit.printLine(0, 563, 327, 563, 1);
				labelEdit.printLine(0, 524, 640, 524, 1);
				labelEdit.printLine(0, 479, 640, 479, 1);
				labelEdit.printLine(0, 219, 640, 219, 1);
				labelEdit.printLine(0, 191, 640, 191, 1);

				labelEdit.printLine(36, 524, 33, 191, 1);
				labelEdit.printLine(84, 524, 80, 191, 1);
				labelEdit.printLine(346, 524, 346, 191, 1);
				labelEdit.printLine(408, 524, 408, 191, 1);
				labelEdit.printLine(501, 524, 501, 191, 1);

				labelEdit.printText(343, 760, "2", "SHIP TO: QIAUBUYER ",Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 735, "2", "JIM CARRY",Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 710, "2", "2125 HAMILTON AVE",Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 685, "2", "SAN JOSE CA",Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 660, "2", "UNITED STATES OF ",Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 635, "2", "AMERICA 95125-5905",Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 610, "2", "PHONE:4083672341",Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 568, "2", "Fees(US $):", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 568, "2", "Certificate No.", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 488, "2", "No", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(40, 488, "2", "Qty", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(102, 488, "2", "Description", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(358, 488, "2", "Kg", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(416, 488, "2", "Val($)", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(516, 488, "2", "Origin", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 450, "2", "1", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(40, 450, "2", "2", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(102, 450, "2", "Pages", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(350, 450, "2", "0.20", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(420, 450, "2", "10.0", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(516, 450, "2", "China", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(40, 196, "2", "2", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(102, 196, "2", "Total Weight (Kg):", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(350, 196, "2", "0.40", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(420, 196, "2", "20.0", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(4, 180, "1", "I certify the particulars given in this customs declaration ", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(4, 165, "1", "are correct. This item does not contain any dangerous ", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(4, 150, "1", "article, or articles prohibited by legislation or by postal or ", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(4, 135, "1", "customs regulations. I have met all applicable export ", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(4, 120, "1", "filing requirements underthe Foreign Trade ", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(4, 105, "1", "Regulations.", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(4, 80, "2", "Sender's Signature & Date Signed:",  Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(536, 40, "3", "CN22", Rotation.Rotation0, 1, 1, 0);

				printer.labelControl().print(1, 1);
			} catch (Exception e) {
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_LABEL_BPLA_SAMPLE1 Print the first sample(BPLA)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLA_SAMPLE1 打印第1个示例样张(BPLA)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostAirmailBPLA(BarPrinter printer) {
			try {
				ILabelConfig labelConfig = printer.labelConfig();
				labelConfig.setPrintMode(PrintMode.TearOff, PaperMode.WebSensing,PrintMethod.DirectThermal);
				labelConfig.setPrintDirection(PrinterDirection.Normal);
				ILabelEdit labelEdit = printer.labelEdit();

				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(640, 800);

				labelEdit.printRectangle(0, 0, 640, 800, 2);

				labelEdit.printRectangle(18, 619, 118, 165, 4);
				labelEdit.printText(44, 650, "5", "F", Rotation.Rotation0, 3, 2, 0);

				labelEdit.printImage(185, 720, BitmapFactory.decodeStream(getAssets().open("ChinaPostLogo.bmp")));
				labelEdit.printLine(170, 710, 420, 710, 2);
				labelEdit.printImage(177, 620, BitmapFactory.decodeStream(getAssets().open("USPost.bmp")));

				labelEdit.printRectangle(445, 635, 180, 138, 4);
				labelEdit.printText(452, 728, "2", "Airmail", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(452, 698, "2", "Postage Paid", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(17, 590, "2", "From:", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(512, 590, "3", "2", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printLine(0, 560, 640, 560, 2);
				labelEdit.printLine(305, 560, 305, 373, 2);
				labelEdit.printLine(0, 372, 640, 372, 2);
				labelEdit.printLine(94, 373, 94, 200, 2);
				labelEdit.printLine(0, 200, 640, 200, 2);

				labelEdit.printText(12, 530, "2", "Andrew 0631-5675888", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(12, 500, "2", "Shandong Newbeiyang", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(12, 470, "2", "NO.126 Kunlun Rd ", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(12, 440, "2", "Huancui District", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(12, 410, "2", "Weihai Shandong China", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printBarcode1D(320, 408, BarCodeType.Code128, Rotation.Rotation0, "264205".getBytes(application.getDecodeType()), 80, HRIPosition.AlignCenter, 3, 6);

				labelEdit.printText(20, 278, "3", "To:", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 336, "2", "QIAUBUYER JIM CARRY", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 306, "2", "2125 HAMILTON AVE", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 276, "2", "SAN JOSE CA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 246, "2", "UNITED STATES OF AMERICA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 216, "2", "95125-5905", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printBarcode1D(60, 40, BarCodeType.Code128, Rotation.Rotation0, "LK002325888CN".getBytes(application.getDecodeType()),  80, HRIPosition.AlignCenter, 3, 6);

				printer.labelControl().print(1, 1);
			} catch (Exception e) {
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}
		}
		/// \endcode

		private void doPrintLabelBPLC(BarPrinter printer, String seletedItem) {
			if (seletedItem.equals(labelArray[0])) {
				doPrintLabelChinaPostAirmailBPLC(printer);
			}else if(seletedItem.equals(labelArray[1])){
				doPrintLabelChinaPostGoodsBPLC(printer);
			}

		}
		/// \if English
		/// \defgroup PRINT_LABEL_BPLC_SAMPLE2 Print the second sample(BPLC)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLC_SAMPLE2 打印第2个示例样张(BPLC)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostGoodsBPLC(BarPrinter printer) {
			try {
				printer.labelConfig().setPrintDirection(PrinterDirection.Normal);
				ILabelEdit labelEdit = printer.labelEdit();

				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(560, 900);

				labelEdit.printRectangle(0, 0, 560, 900, 2);

				//labelEdit.printImage(10, 24, BitmapFactory.decodeStream(getAssets().open("ChinaPostLogo.bmp")));

				labelEdit.printBarcodeQR(400, 24, Rotation.Rotation0, "LK002325883CN", QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());

				labelEdit.printText(10, 120, "0", "IMPORTANT:", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(10, 140, "0", "The item/parcel may be", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(10, 160, "0", "opened officially.", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(10, 180, "0", "Please print in English.", Rotation.Rotation0, 0, 0, 0);

				labelEdit.printRectangle(240, 120, 72, 72, 2);
				labelEdit.printText(256, 130, "4", "2", Rotation.Rotation0, 0, 0, 0);

				labelEdit.printText(8, 219, "0", "FROM: HANK HUANG", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(8, 239, "0", "4/F DONG HAI COMMERCIAL", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(8, 259, "0", "BUILDING 618 YAN AN ROAD", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(8, 279, "0", "SHANGHAI", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(8, 299, "0", "CHINA 190893", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(8, 320, "0", "PHONE:13908473278", Rotation.Rotation0, 0, 1, 0);

				labelEdit.printLine(287, 202, 560, 204, 1);
				labelEdit.printLine(287, 202, 287, 424, 1);
				labelEdit.printLine(0, 347, 287, 347, 1);
				labelEdit.printLine(0, 387, 287, 387, 1);
				labelEdit.printLine(0, 426, 560, 426, 1);
				labelEdit.printLine(0, 471, 560, 471, 1);
				labelEdit.printLine(0, 631, 560, 631, 1);
				labelEdit.printLine(0, 659, 560, 659, 1);
				labelEdit.printLine(33, 426, 33, 659, 1);
				labelEdit.printLine(80, 426, 80, 659, 1);
				labelEdit.printLine(346, 426, 346, 659, 1);
				labelEdit.printLine(408, 426, 408, 659, 1);
				labelEdit.printLine(501, 426, 501, 659, 1);

				labelEdit.printText(290, 219, "0", "SHIP TO: QIAUBUYER JIM", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(290, 239, "0", "CARRY", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(290, 259, "0", "2125 HAMILTON AVE", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(290, 279, "0", "SAN JOSE CA", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(290, 299, "0", "UNITED STATES OF AMERICA", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(290, 319, "0", "95125-5905", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(290, 349, "0", "PHONE:4083672341", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8,358, "0", "Fees(US $):", Rotation.Rotation0, 0, 0, 0);

				labelEdit.printText(8,398 ,"0", "Certificate No.", Rotation.Rotation0, 0, 0, 0);

				labelEdit.printText(8, 440, "0", "No", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(43, 440, "0", "Qty", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(102, 440, "0", "Description of Contents", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(358, 440, "0", "Kg", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(409, 440, "0", "Val(US $)", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(510, 440, "0", "Goods Origin", Rotation.Rotation0, 0, 0, 0);

				labelEdit.printText(8, 480, "0", "1", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(43, 480, "0", "2", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(102, 480, "0", "Pages", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(358, 480, "0", "0.200", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(409, 480, "0", "10.00", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(510, 480, "0", "China", Rotation.Rotation0, 0, 0, 0);

				labelEdit.printText(43, 636, "0", "2", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(102, 636, "Total Gross Weight (Kg):", "Pages", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(358, 636, "0.400", "0.200", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(409, 636, "20.00", "10.00", Rotation.Rotation0, 0, 0, 0);

				labelEdit.printText(8, 746, "0", "Sender's Signature & Date Signed:", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(450, 746, "4", "CN22", Rotation.Rotation0, 0, 0, 0);
				printer.labelControl().print(1, 1);
			} catch (Exception e) {
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_LABEL_BPLC_SAMPLE1 Print the first sample(BPLC)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLC_SAMPLE1 打印第1个示例样张(BPLC)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostAirmailBPLC(BarPrinter printer) {
			try {
				printer.labelConfig().setPrintDirection(PrinterDirection.Normal);
				ILabelEdit labelEdit = printer.labelEdit();

				labelEdit.setColumn(1, 0);

/*				labelEdit.setLabelSize(78*5, 60*8);
				labelEdit.printText(0, 0, "8", "山东新北洋信息技术股份有限公司", Rotation.Rotation0, 1, 1, 0, 0);
				labelEdit.printBarcode1D(10, 50,BarCodeType.Code128, Rotation.Rotation0, "1234ABC24C".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 1, 2);
				labelEdit.printText(0, 150, "8", "山东新北洋信息技术股份有限公司", Rotation.Rotation0, 1, 1, 0, 0);
				printer.labelControl().print(1, 1);*/

/*				labelEdit.setLabelSize(560, 200);
				//labelEdit.printDottedLine(0, 100, 560, 100, 2);
				//labelEdit.printRectangle(0, 0, 560, 200, 2);
				//labelEdit.printText(100, 100, "0", "", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(100, 100, "8", "SNBC", Rotation.Rotation0, 0, 0, 1,0);
				//labelEdit.printText(100, 150, "55", "新北洋SNBC", Rotation.Rotation0, 0, 0, 0);
				//labelEdit.printTextAreaAlign(0,0,560,200,Area.Middle,"山东新北洋信息技术股份有限公司Andrew 0631-5675888NO.126 Kunlun Rd 威海Huancui ",300,100,2,false, false, 10,true);
				//labelEdit.printBarcode1D(0, 0,BarCodeType.Code128, Rotation.Rotation0, "1234ABC24C".getBytes(application.getDecodeType()), 80, HRIPosition.AlignCenter, 2, 5);
				//labelEdit.printBarcode1DAreaAlign(0, 0,560,200,Area.Middle, BarCodeType.Code128, Rotation.Rotation0, "1234567SN", 80, HRIPosition.AlignCenter, 2, 5,true);
				printer.labelControl().print(1, 1);*/

				labelEdit.setLabelSize(560, 800);


				labelEdit.printRectangle(0, 0, 560, 800, 2);

				labelEdit.printRectangle(8, 16, 108, 165, 4);
				labelEdit.printText(20, 43, "4", "F", Rotation.Rotation0, 0, 0, 0);

				//labelEdit.printImage(145, 32, BitmapFactory.decodeStream(getAssets().open("fee.bmp")));
				labelEdit.printLine(130, 90, 400, 90, 2);
				//labelEdit.printImage(137, 95, BitmapFactory.decodeStream(getAssets().open("USPost.bmp")));

				labelEdit.printRectangle(408, 27, 150, 138, 4);

				labelEdit.printText(418, 62, "5", "Airmail", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(418, 102, "5", "Postage Paid", Rotation.Rotation0, 0, 0, 0);

				labelEdit.printText(18, 200, "4", "From:", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(512, 200, "4", "2", Rotation.Rotation0, 0, 0, 0);

				labelEdit.printLine(0, 240, 640, 240, 2);
				labelEdit.printLine(300, 240, 300, 427, 2);
				labelEdit.printLine(0, 428, 640, 428, 2);
				labelEdit.printLine(94, 427, 94, 600, 2);
				labelEdit.printLine(0, 600, 640, 600, 2);

				labelEdit.printText(12, 258, "0", "Andrew 0631-5675888", Rotation.Rotation0, 0, 2, 0);
				labelEdit.printText(12, 288, "0", "Shandong Newbeiyang", Rotation.Rotation0, 0, 2, 0);
				labelEdit.printText(12, 318, "0", "NO.126 Kunlun Rd Huancui", Rotation.Rotation0, 0, 2, 0);
				labelEdit.printText(12, 348, "0", "District", Rotation.Rotation0, 0, 2, 0);
				labelEdit.printText(12, 378, "0", "Weihai Shandong China", Rotation.Rotation0, 0, 2, 0);

				labelEdit.printBarcode1D(320, 288, BarCodeType.Code128, Rotation.Rotation0, "264205".getBytes(application.getDecodeType()), 80, HRIPosition.AlignCenter, 2, 5);

				labelEdit.printText(20, 492, "4", "To:", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(120, 450, "0", "QIAUBUYER JIM CARRY", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(120, 480, "0", "2125 HAMILTON AVE", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(120, 510, "0", "SAN JOSE CA", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(120, 540, "0", "UNITED STATES OF AMERICA", Rotation.Rotation0, 0, 0, 0);
				labelEdit.printText(120, 570, "0", "95125-5905", Rotation.Rotation0, 0, 0, 0);
				//labelEdit.printBarcode1D(60, 640, BarCodeType.Code128, Rotation.Rotation0, "LK002325888CN".getBytes(application.getDecodeType()), 80, HRIPosition.AlignCenter, 2, 5);
				//labelEdit.printTextLineWrap(60, 640, "山东新北洋信息技术股份有限公司Andrew 0631-5675888NO.126 Kunlun Rd 威海Huancui ", 300, 100, 2, false, false, 10);
				//labelEdit.printTextAreaAlign(0,600,560,800,Area.Middle,"山东新北洋信息技术股份有限公司Andrew 0631-5675888NO.126 Kunlun Rd 威海Huancui ",300,100,2,false, false, 10,true);
				//labelEdit.setMultiArea(0,600,560,800,Area.Middle,"山东新北洋信息技术股份有限公司Andrew 0631-5675888NO.126 Kunlun Rd 威海Huancui ",300,100,2,false, false, 10);
				labelEdit.printBarcode1DAreaAlign(0, 600,560,800,Area.Middle, BarCodeType.Code128, Rotation.Rotation0, "12345ABCD", 80, HRIPosition.AlignCenter, 2, 5,true);
				printer.labelControl().print(1, 1);

			} catch (Exception e) {
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}
		}
		/// \endcode

		private void doPrintLabelBPLT(BarPrinter printer, String seletedItem) {
			if (seletedItem.equals(labelArray[0])) {
				doPrintLabelChinaPostAirmailBPLT(printer);
			}else if(seletedItem.equals(labelArray[1])){
				doPrintLabelChinaPostGoodsBPLT(printer);
			}
		}

		/// \if English
		/// \defgroup PRINT_LABEL_BPLT_SAMPLE2 Print the second sample(BPLT)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLT_SAMPLE2 打印第2个示例样张(BPLT)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostGoodsBPLT(BarPrinter printer) {
			try {
				printer.labelConfig().setPrintDirection(PrinterDirection.Rotation180);
				ILabelEdit labelEdit = printer.labelEdit();

				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(640, 800);
				labelEdit.clearPrintBuffer();

				labelEdit.printRectangle(0, 0, 640, 800, 2);
				labelEdit.printImage(10, 24, BitmapFactory.decodeStream(getAssets().open("ChinaPostLogo.bmp")));
				labelEdit.printBarcodeQR(447, 24, Rotation.Rotation0, "LK002325883CN", QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());

				labelEdit.printText(10, 120, "1", "IMPORTANT:", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(10, 140, "1", "The item/parcel may be", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(10, 160, "1", "opened officially.", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(10, 180, "1", "Please print in English.", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printRectangle(248, 120, 72, 72, 2);
				labelEdit.printText(264, 130, "5", "2", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 219, "1", "FROM: HANK HUANG", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 239, "1", "4/F DONG HAI COMMERCIAL", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 259, "1", "BUILDING 618 YAN AN ROAD", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 279, "1", "SHANGHAI", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 299, "1", "CHINA 190893", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 320, "1", "PHONE:13908473278", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printLine(327, 202, 640, 204, 1);
				labelEdit.printLine(327, 202, 327, 424, 1);
				labelEdit.printLine(0, 347, 327, 347, 1);
				labelEdit.printLine(0, 387, 327, 387, 1);
				labelEdit.printLine(0, 426, 640, 426, 1);
				labelEdit.printLine(0, 471, 640, 471, 1);
				labelEdit.printLine(0, 631, 640, 631, 1);
				labelEdit.printLine(0, 659, 640, 659, 1);
				labelEdit.printLine(33, 426, 33, 659, 1);
				labelEdit.printLine(80, 426, 80, 659, 1);
				labelEdit.printLine(346, 426, 346, 659, 1);
				labelEdit.printLine(408, 426, 408, 659, 1);
				labelEdit.printLine(501, 426, 501, 659, 1);

				labelEdit.printText(343, 219, "1", "SHIP TO: QIAUBUYER JIM", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 239, "1", "CARRY", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 259, "1", "2125 HAMILTON AVE", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 279, "1", "SAN JOSE CA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 299, "1", "UNITED STATES OF AMERICA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 319, "1", "95125-5905", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 349, "1", "PHONE:4083672341", Rotation.Rotation0, 1, 2, 0);

				labelEdit.printText(8, 358, "1", "Fees(US $):", Rotation.Rotation0, 1, 2, 0);
				labelEdit.printText(8, 398, "1", "Certificate No.", Rotation.Rotation0, 1, 2, 0);

				labelEdit.printText(8, 440, "1", "No", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(43, 440, "1", "Qty", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(102, 440, "1", "Description of Contents", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(358, 440, "1", "Kg", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(409, 440, "1", "Val(US $)", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(510, 440, "1", "Goods Origin", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 480, "1", "1", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(43, 480, "2", "Qty", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(102, 480, "Pages", "Description of Contents", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(358, 480, "0.200", "Kg", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(409, 480, "10.00", "Val(US $)", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(510, 480, "China", "Goods Origin", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(43, 636, "1", "2", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(102, 636, "1", "Total Gross Weight (Kg):", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(358, 636, "1", "0.400", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(409, 636, "1", "20.00", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 676, "1", "I certify the particulars given in this customs declaration are correct. This item does not contain any ", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 691, "1", "dangerous article, or articles prohibited by legislation or by postal or customs regulations. I have met", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 706, "1", "all applicable export filing requirements underthe Foreign Trade Regulations.", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 726, "2", "Sender's Signature & Date Signed:", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(536, 727, "3", "CN22", Rotation.Rotation0, 1, 1, 0);

				printer.labelControl().print(1, 1);
			} catch (Exception e) {
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_LABEL_BPLT_SAMPLE1 Print the first sample(BPLT)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLT_SAMPLE1 打印第1个示例样张(BPLT)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostAirmailBPLT(BarPrinter printer) {
			try {
				printer.labelConfig().setPrintDirection(PrinterDirection.Rotation180);
				ILabelEdit labelEdit = printer.labelEdit();

				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(640, 800);
				labelEdit.clearPrintBuffer();
				labelEdit.printRectangle(0, 0, 640, 800, 2);

				labelEdit.printRectangle(18, 16, 118, 165, 4);
				labelEdit.printText(44, 43, "5", "F", Rotation.Rotation0, 2, 2, 0);

				labelEdit.printImage(185, 32, BitmapFactory.decodeStream(getAssets().open("ChinaPostLogo.bmp")));
				labelEdit.printLine(170, 90, 420, 90, 2);
				labelEdit.printImage(177, 95, BitmapFactory.decodeStream(getAssets().open("USPost.bmp")));

				labelEdit.printRectangle(450, 27, 177, 138, 4);
				labelEdit.printText(456, 72, "2", "Airmail", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(456, 102, "2", "Postage Paid", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(17, 200, "4", "From:", Rotation.Rotation0,  1, 1, 0);
				labelEdit.printText(512, 200, "4", "2",Rotation.Rotation0,  1, 1, 0);

				labelEdit.printLine(0,   240, 640, 240, 2);
				labelEdit.printLine(320, 240, 320, 427, 2);
				labelEdit.printLine(0,   428, 640, 428, 2);
				labelEdit.printLine(88,  427, 88,  600, 2);
				labelEdit.printLine(0,   600, 640, 600, 2);

				labelEdit.printText(8, 258, "2", "Andrew 0631-5675888", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 288, "2", "Shandong Newbeiyang", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 318, "2", "NO.126 Kunlun Rd", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 348, "2", "Huancui District", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 378, "2", "Weihai Shandong China", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printBarcode1D(340, 288, BarCodeType.Code128, Rotation.Rotation0, "264205".getBytes(application.getDecodeType()), 80, HRIPosition.AlignCenter, 3, 6);

				labelEdit.printText(20, 492, "3", "To:", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 450, "2", "QIAUBUYER JIM CARRY", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 480, "2", "2125 HAMILTON AVE", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 510, "2", "SAN JOSE CA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 540, "2", "UNITED STATES OF AMERICA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 570, "3", "95125-5905", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printBarcode1D(60, 640, BarCodeType.Code128, Rotation.Rotation0, "LK002325888CN".getBytes(application.getDecodeType()), 80, HRIPosition.AlignCenter, 3, 6);
				printer.labelControl().print(1, 1);
			} catch (Exception e) {
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}

		}
		/// \endcode

		private void doPrintLabelBPLE(BarPrinter printer, String seletedItem) {
			if (seletedItem.equals(labelArray[0])) {
				doPrintLabelChinaPostAirmailBPLE(printer);
			}else if(seletedItem.equals(labelArray[1])){
				doPrintLabelChinaPostGoodsBPLE(printer);
			}

		}
		/// \if English
		/// \defgroup PRINT_LABEL_BPLZ_SAMPLE1 Print the first sample(BPLZ)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLZ_SAMPLE1 打印第1个示例样张(BPLZ)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostAirmailBPLZ(BarPrinter printer) {
			try {
				printer.labelConfig().setPrintDirection(PrinterDirection.Normal);
				ILabelEdit labelEdit = printer.labelEdit();
				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(640, 400);

				labelEdit.printRectangle(0, 0, 640, 800, 2);
				labelEdit.printRectangle(18, 16, 118, 165, 4);
				//反显开始
				labelEdit.setBplzReverseArea(44, 43, 77, 80);
				labelEdit.printText(51, 53, "Z:B.FNT", "F", Rotation.Rotation0, 70, 70, 1,1);
				//反显结束

				labelEdit.printImage(185, 32, BitmapFactory.decodeStream(getAssets().open("ChinaPostLogo.bmp")));
				labelEdit.printLine(170, 90, 420, 90, 2);
				labelEdit.printImage(177, 95, BitmapFactory.decodeStream(getAssets().open("USPost.bmp")));

				labelEdit.printRectangle(450, 27, 177, 138, 4);
				labelEdit.printText(465, 72, "Z:D.FNT", "Airmail", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(465, 102, "Z:D.FNT", "Postage Paid", Rotation.Rotation0, 10, 18, 1);

				labelEdit.printText(17, 200, "Z:D.FNT", "From", Rotation.Rotation0, 20, 36, 0);
				labelEdit.printText(512, 200, "Z:D.FNT", "2", Rotation.Rotation0, 20, 36, 0);

				labelEdit.printLine(0, 240, 640, 240, 2);
				labelEdit.printLine(300, 240, 300, 427, 2);
				labelEdit.printLine(0, 428, 640, 428, 2);
				labelEdit.printLine(94, 427, 94, 600, 2);
				labelEdit.printLine(0, 600, 640, 600, 2);

				labelEdit.printText(12, 258, "Z:D.FNT", "Andrew 0631-5675888", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(12, 288, "Z:D.FNT", "Shandong Newbeiyang", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(12, 318, "Z:D.FNT", "NO.126 Kunlun Rd Huancui", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(12, 348, "Z:D.FNT", "District", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(12, 378, "Z:D.FNT", "Weihai Shandong China", Rotation.Rotation0, 10, 18, 0);

				labelEdit.printBarcode1D(320, 288, BarCodeType.Code128, Rotation.Rotation0, "264205".getBytes(mDecodeType), 80, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printText(20, 492, "Z:D.FNT", "To:", Rotation.Rotation0, 20, 36, 0);
				labelEdit.printText(120, 450, "Z:D.FNT", "QIAUBUYER JIM CARRY", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(120, 480, "Z:D.FNT", "2125 HAMILTON AVE", Rotation.Rotation0,10, 18, 0);
				labelEdit.printText(120, 510, "Z:D.FNT", "SAN JOSE CA", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(120, 540, "Z:D.FNT", "UNITED STATES OF AMERICA", Rotation.Rotation0,10, 18, 0);
				labelEdit.printText(120, 570, "Z:D.FNT", "95125-5905", Rotation.Rotation0, 10, 18, 0);

				labelEdit.printBarcode1D(60, 640, BarCodeType.Code128, Rotation.Rotation0, "LK002325888CN".getBytes(mDecodeType), 80, HRIPosition.AlignCenter, 3, 6);

				printer.labelControl().print(1, 1);

			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_LABEL_BPLZ_SAMPLE2 Print the sencond sample(BPLZ)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLZ_SAMPLE2 打印第2个示例样张(BPLZ)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostGoodsBPLZ(BarPrinter printer) {
			try {
				printer.labelConfig().setPrintDirection(PrinterDirection.Normal);
				ILabelEdit labelEdit = printer.labelEdit();
				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(640, 800);

				labelEdit.printRectangle(0, 0, 640, 800, 2);
				labelEdit.printImage(10, 24, BitmapFactory.decodeStream(getAssets().open("ChinaPostLogo.bmp")));
				labelEdit.printBarcodeQR(447, 24, Rotation.Rotation0, "LK002325883CN", QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());

				labelEdit.printText(10, 120, "Z:B.FNT", "IMPORTANT:", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(10, 140, "Z:B.FNT", "The item/parcel may be", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(10, 160, "Z:B.FNT", "opened officially.", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(10, 180, "Z:B.FNT", "Please print in English.", Rotation.Rotation0, 7, 11, 0);

				labelEdit.printRectangle(240, 120, 72, 72, 2);
				labelEdit.printText(256, 130, "Z:B.FNT", "2", Rotation.Rotation0, 42,48, 0);

				labelEdit.printText(8, 219, "Z:B.FNT", "FROM: HANK HUANG", Rotation.Rotation0, 7,11, 0);
				labelEdit.printText(8, 239, "Z:B.FNT", "4/F DONG HAI COMMERCIAL", Rotation.Rotation0, 7,11, 0);
				labelEdit.printText(8, 259, "Z:B.FNT", "BUILDING 618 YAN AN ROAD", Rotation.Rotation0, 7,11, 0);
				labelEdit.printText(8, 279, "Z:B.FNT", "SHANGHAI", Rotation.Rotation0, 7,11, 0);
				labelEdit.printText(8, 299, "Z:B.FNT", "CHINA 190893", Rotation.Rotation0, 7,11, 0);
				labelEdit.printText(8, 320, "Z:D.FNT", "PHONE:13908473278", Rotation.Rotation0, 10,18, 0);

				labelEdit.printLine(327, 202, 640, 202, 1);
				labelEdit.printLine(327, 202, 327, 424, 1);
				labelEdit.printLine(0, 347, 327, 347, 1);
				labelEdit.printLine(0, 387, 327, 387, 1);
				labelEdit.printLine(0, 426, 640, 426, 1);
				labelEdit.printLine(0, 471, 640, 471, 1);
				labelEdit.printLine(0, 631, 640, 631, 1);
				labelEdit.printLine(0, 659, 640, 659, 1);
				labelEdit.printLine(33, 426, 33, 659, 1);
				labelEdit.printLine(80, 426, 80, 659, 1);
				labelEdit.printLine(346, 426, 346, 659, 1);
				labelEdit.printLine(408, 426, 408, 659, 1);
				labelEdit.printLine(501, 426, 501, 659, 1);

				labelEdit.printText(343, 219, "Z:D.FNT", "SHIP TO: QIAUBUYER JIM", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(343, 239, "Z:D.FNT", "CARRY", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(343, 259, "Z:D.FNT", "2125 HAMILTON AVE", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(343, 279, "Z:D.FNT", "SAN JOSE CA", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(343, 299, "Z:D.FNT", "UNITED STATES OF AMERICA", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(343, 319, "Z:D.FNT", "95125-5905", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(343, 349, "Z:D.FNT", "PHONE:4083672341", Rotation.Rotation0, 10, 18, 0);

				labelEdit.printText(8, 358, "Z:D.FNT", "Fees(US $):", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(8, 398, "Z:D.FNT", "Certificate No.", Rotation.Rotation0, 10, 18, 0);

				labelEdit.printText(8, 440, "Z:B.FNT", "No", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(43, 440, "Z:B.FNT", "Qty", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(102, 440, "Z:B.FNT", "Description of Contents", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(358, 440, "Z:B.FNT", "Kg", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(409, 440, "Z:B.FNT", "Val(US $)", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(510, 440, "Z:B.FNT", "Goods Origin", Rotation.Rotation0, 7, 11, 0);

				labelEdit.printText(8, 480, "Z:B.FNT", "1", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(43, 480, "Z:B.FNT", "2", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(102, 480, "Z:B.FNT", "Pages", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(358, 480, "Z:B.FNT", "0.200", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(409, 480, "Z:B.FNT", "10.00", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(510, 480, "Z:B.FNT", "China", Rotation.Rotation0, 7, 11, 0);

				labelEdit.printText(43, 636, "Z:B.FNT", "2", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(102, 636, "Z:B.FNT", "Total Gross Weight (Kg):", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(358, 636, "Z:B.FNT", "0.400", Rotation.Rotation0, 7, 11, 0);
				labelEdit.printText(409, 636, "Z:B.FNT", "20.00", Rotation.Rotation0, 7, 11, 0);

				labelEdit.printText(8, 676, "Z:A.FNT", "I certify the particulars given in this customs declaration are correct. This item does not contain any ", Rotation.Rotation0, 5, 9, 0);
				labelEdit.printText(8, 691, "Z:A.FNT", "dangerous article, or articles prohibited by legislation or by postal or customs regulations. I have met", Rotation.Rotation0, 5, 9, 0);
				labelEdit.printText(8, 706, "Z:A.FNT", "all applicable export filing requirements underthe Foreign Trade Regulations.", Rotation.Rotation0, 5, 9, 0);

				labelEdit.printText(8, 726, "Z:A.FNT", "Sender's Signature & Date Signed:", Rotation.Rotation0, 10, 18, 0);
				labelEdit.printText(536, 727, "Z:D.FNT", "CN22", Rotation.Rotation0, 20, 36, 0);

				printer.labelControl().print(1, 1);

			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}
		}
		/// \endcode


		private void doPrintLabelBPLZ(BarPrinter printer, String seletedItem) {
			if (seletedItem.equals(labelArray[0])) {
				doPrintLabelChinaPostAirmailBPLZ(printer);
			}else if(seletedItem.equals(labelArray[1])){
				doPrintLabelChinaPostGoodsBPLZ(printer);
			}

		}

		/// \if English
		/// \defgroup PRINT_LABEL_BPLE_SAMPLE2 Print the second sample(BPLE)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLE_SAMPLE2 打印第2个示例样张(BPLE)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostGoodsBPLE(BarPrinter printer) {

			try {
				printer.labelConfig().setPrintDirection(PrinterDirection.Rotation180);
				ILabelEdit labelEdit = printer.labelEdit();
				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(640, 800);
				labelEdit.clearPrintBuffer();

				labelEdit.printRectangle(0, 0, 640, 800, 2);
				labelEdit.printImage(10, 24, BitmapFactory.decodeStream(getAssets().open("ChinaPostLogo.bmp")));
				labelEdit.printBarcodeQR(447, 24, Rotation.Rotation0, "LK002325883CN", QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());

				labelEdit.printText(10, 120, "1", "IMPORTANT:", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(10, 140, "1", "The item/parcel may be", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(10, 160, "1", "opened officially.", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(10, 180, "1", "Please print in English.", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printRectangle(240, 120, 72, 72, 2);
				labelEdit.printText(256, 130, "5", "2", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 219, "1", "FROM: HANK HUANG", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 239, "1", "4/F DONG HAI COMMERCIAL", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 259, "1", "BUILDING 618 YAN AN ROAD", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 279, "1", "SHANGHAI", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 299, "1", "CHINA 190893", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 320, "1", "PHONE:13908473278", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printLine(327, 202, 640, 204, 1);
				labelEdit.printLine(327, 202, 327, 424, 1);
				labelEdit.printLine(0, 347, 327, 347, 1);
				labelEdit.printLine(0, 387, 327, 387, 1);
				labelEdit.printLine(0, 426, 640, 426, 1);
				labelEdit.printLine(0, 471, 640, 471, 1);
				labelEdit.printLine(0, 631, 640, 631, 1);
				labelEdit.printLine(0, 659, 640, 659, 1);

				labelEdit.printLine(33, 426, 33, 659, 1);
				labelEdit.printLine(80, 426, 80, 659, 1);
				labelEdit.printLine(346, 426, 346, 659, 1);
				labelEdit.printLine(408, 426, 408, 659, 1);
				labelEdit.printLine(501, 426, 501, 659, 1);

				labelEdit.printText(343, 219, "2", "SHIP TO: QIAUBUYER JIM", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 239, "2", "CARRY", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 259, "2", "2125 HAMILTON AVE", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 279, "2", "SAN JOSE CA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 299, "2", "UNITED STATES OF AMERICA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 319, "2", "95125-5905", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(343, 349, "2", "PHONE:4083672341", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 358, "2", "Fees(US $):", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 398, "2", "Certificate No.", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 440, "2", "No", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(43, 440, "2", "Qty", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(102, 440, "2", "Description of Contents", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(358, 440, "2", "Kg", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(409, 440, "2", "Val(US $)", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(510, 440, "2", "Goods Origin", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 480, "2", "1", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(43, 480, "2", "2", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(102, 480, "2", "Pages", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(358, 480, "2", "0.200", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(409, 480, "2", "10.00", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(510, 480, "2", "China", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(43, 636, "2", "2", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(102, 636, "2", "Total Gross Weight (Kg):", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(358, 636, "2", "0.400", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(409, 636, "2", "20.00", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 676, "1", "I certify the particulars given in this customs declaration are correct. This item does not contain any ", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 691, "1", "dangerous article, or articles prohibited by legislation or by postal or customs regulations. I have met", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(8, 706, "1", "all applicable export filing requirements underthe Foreign Trade Regulations.", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(8, 726, "3", "Sender's Signature & Date Signed:", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(536, 727, "3", "CN22", Rotation.Rotation0, 1, 1, 0);

				printer.labelControl().print(1, 1);
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_LABEL_BPLE_SAMPLE1 Print the first sample(BPLE)
		/// \elseif Chinese
		/// \defgroup PRINT_LABEL_BPLE_SAMPLE1 打印第1个示例样张(BPLE)
		/// \endif
		/// \code
		private void doPrintLabelChinaPostAirmailBPLE(BarPrinter printer) {

			try {
				printer.labelConfig().setPrintDirection(PrinterDirection.Rotation180);
				ILabelEdit labelEdit = printer.labelEdit();
				labelEdit.setColumn(1, 0);
				labelEdit.setLabelSize(640, 800);
				labelEdit.clearPrintBuffer();

				labelEdit.printRectangle(0, 0, 640, 800, 2);

				labelEdit.printRectangle(18, 16, 118, 165, 4);
				labelEdit.printText(44, 43, "5", "F", Rotation.Rotation0, 2, 2, 0);

				labelEdit.printImage(185, 32, BitmapFactory.decodeStream(getAssets().open("ChinaPostLogo.bmp")));
				labelEdit.printLine(170, 90, 420, 90, 2);
				labelEdit.printImage(177, 95, BitmapFactory.decodeStream(getAssets().open("USPost.bmp")));

				labelEdit.printRectangle(450, 27, 177, 138, 4);
				labelEdit.printText(465, 72, "2", "Airmail", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(465, 102, "2", "Postage Paid", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printText(17, 200, "4", "From:", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(512, 200, "4", "2", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printLine(0,   240, 640, 240, 2);
				labelEdit.printLine(300, 240, 300, 427, 2);
				labelEdit.printLine(0,   428, 640, 428, 2);
				labelEdit.printLine(94,  427, 94, 600, 2);
				labelEdit.printLine(0,   600, 640, 600, 2);

				labelEdit.printText(12, 258, "2", "Andrew 0631-5675888", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(12, 288, "2", "Shandong Newbeiyang", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(12, 318, "2", "NO.126 Kunlun Rd Huancui", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(12, 348, "2", "District", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(12, 378, "2", "Weihai Shandong China", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printBarcode1D(320, 288, BarCodeType.Code128, Rotation.Rotation0, "264205".getBytes(mDecodeType), 80, HRIPosition.AlignCenter, 3, 6);

				labelEdit.printText(20, 492, "3", "To:", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 450, "2", "QIAUBUYER JIM CARRY", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 480, "2", "2125 HAMILTON AVE", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 510, "2", "SAN JOSE CA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 540, "2", "UNITED STATES OF AMERICA", Rotation.Rotation0, 1, 1, 0);
				labelEdit.printText(120, 570, "2", "95125-5905", Rotation.Rotation0, 1, 1, 0);

				labelEdit.printBarcode1D(60, 640, BarCodeType.Code128, Rotation.Rotation0, "LK002325888CN".getBytes(mDecodeType), 80, HRIPosition.AlignCenter, 3, 6);

				printer.labelControl().print(1, 1);

			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintLabelActivity.this);
			}
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
