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
import com.snbc.sdk.barcode.enumeration.BarCodeType;
import com.snbc.sdk.barcode.enumeration.HRIPosition;
import com.snbc.sdk.barcode.enumeration.InstructionType;
import com.snbc.sdk.barcode.enumeration.QRLevel;
import com.snbc.sdk.barcode.enumeration.QRMode;
import com.snbc.sdk.barcode.enumeration.Rotation;

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
import android.widget.Spinner;
import android.widget.TextView;

public class PrintBarcodeActivity extends Activity {
	private TextView tv_back_print_barcode;
	private Spinner sp_print_barcode;
	private Button btn_print_barcode;
	private List<String> typeList = new ArrayList<String>();
	private ArrayAdapter<String> typeAdapter;
	private int BARCODE_PDF417 ;
	private int BARCODE_QR ;
	private int BARCODE_DATAMATRIX ;
	private int BARCODE_MAXICODE ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_barcode);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		tv_back_print_barcode = (TextView) findViewById(R.id.tv_back_print_barcode);
		sp_print_barcode = (Spinner) findViewById(R.id.sp_print_barcode_type);
		btn_print_barcode = (Button) findViewById(R.id.btn_barcode_doprint);
		int BarCode_UPCE = BarCodeType.UPCE.getBarCodeType();
		BARCODE_PDF417 = BarCode_UPCE+1;
		BARCODE_QR = BarCode_UPCE+2;
		BARCODE_DATAMATRIX = BarCode_UPCE+3;
		BARCODE_MAXICODE =  BarCode_UPCE+4;

		String[] barcodeTypeArray = {"Code 128","Code 39","Code 93","EAN-8","EAN-13","Codebar","ITF25","UPC-A","UPC-E","PDF417","QR","DataMatrix","MaxiCode"};
		typeList = Arrays.asList(barcodeTypeArray);
		typeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typeList);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_print_barcode.setAdapter(typeAdapter);

		tv_back_print_barcode.setOnClickListener(new BackListener());
		btn_print_barcode.setOnClickListener(new PrintClickListener());

	}
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PrintBarcodeActivity.this,MainActivity.class);
			startActivity(intent);
		}

	}
	private class PrintClickListener  implements OnClickListener{
		@Override
		public void onClick(View v) {

			BarPrinter printer = null;;
			InstructionType mType = null;
			int barCode = 0;
			try {
				String barcodeType = null;
				SNBCApplication application = (SNBCApplication)getApplication();
				printer = application.getPrinter();
				mType = printer.labelQuery().getPrinterLanguage();
				barcodeType = sp_print_barcode.getItemAtPosition(sp_print_barcode.getSelectedItemPosition()).toString();
				if ("Code 128".equals(barcodeType)) {
					barCode = BarCodeType.Code128.getBarCodeType();
				}else if("Code 39".equals(barcodeType)){
					barCode = BarCodeType.Code39.getBarCodeType();
				}
				else if("Code 93".equals(barcodeType)){
					barCode = BarCodeType.Code93.getBarCodeType();
				}
				else if("EAN-8".equals(barcodeType)){
					barCode = BarCodeType.CodeEAN8.getBarCodeType();
				}
				else if("EAN-13".equals(barcodeType)){
					barCode = BarCodeType.CodeEAN13.getBarCodeType();
				}
				else if("Codebar".equals(barcodeType)){
					barCode = BarCodeType.CODABAR.getBarCodeType();
				}
				else if("ITF25".equals(barcodeType)){
					barCode = BarCodeType.ITF25.getBarCodeType();
				}
				else if("UPC-A".equals(barcodeType)){
					barCode = BarCodeType.UPCA.getBarCodeType();
				}
				else if("UPC-E".equals(barcodeType)){
					barCode = BarCodeType.UPCE.getBarCodeType();
				}
				else if("PDF417".equals(barcodeType)){
					barCode = BARCODE_PDF417;
				}
				else if("QR".equals(barcodeType)){
					barCode = BARCODE_QR;
				}
				else if("DataMatrix".equals(barcodeType)){
					barCode = BarCodeType.UPCE.getBarCodeType()+3;
				}
				else if("MaxiCode".equals(barcodeType)){
					barCode = BarCodeType.UPCE.getBarCodeType()+4;
				}
				switch (mType) {
					case BPLZ:
						doPrintBarcodeBPLZ(barCode, application);
						break;
					case BPLE:
						doPrintBarcodeBPLE(barCode, application);
						break;
					case BPLT:
						doPrintBarcodeBPLT(barCode, application);
						break;
					case BPLC:
						if(barCode == BARCODE_DATAMATRIX || barCode == BARCODE_MAXICODE){
							AlertDialogUtil.showDialog("For BPLC, SDK can't support printBarcodeDataMatrix and printBarcodeMaxiCode", PrintBarcodeActivity.this);
							return;
						}
						doPrintBarcodeBPLC(barCode, application);
						break;
					case BPLA:
						doPrintBarcodeBPLA(barCode, application);
						break;
					default:
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				AlertDialogUtil.showDialog(e, PrintBarcodeActivity.this);
			}
		}
		/// \if English
		/// \defgroup PRINT_BARCODE_BPLA Print barcode(BPLA)
		/// \elseif Chinese
		/// \defgroup PRINT_BARCODE_BPLA 打印条码(BPLA)
		/// \endif
		/// \code
		private void doPrintBarcodeBPLA(int barCode, SNBCApplication application) throws Exception {
			BarPrinter printer = ((SNBCApplication)getApplication()).getPrinter();
			ILabelEdit labelEdit = printer.labelEdit();

			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(800, 1000);
			if(BarCodeType.Code128.getBarCodeType() == barCode){
				byte[] barcodeData = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code128, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code128, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code128, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code128, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.Code39.getBarCodeType() == barCode){
				byte[] barcodeData = "123ABC".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.Code93.getBarCodeType() == barCode){
				byte[] barcodeData = "12345ABCDE".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code93, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code93, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code93, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(400, 500, BarCodeType.Code39, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.CodeEAN8.getBarCodeType() == barCode){
				//data is limited to exactly 7 characters
				byte[] barcodeData = "1234567".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN8, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN8, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN8, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN8, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.CodeEAN13.getBarCodeType() == barCode){
				//data is limited to exactly 12 characters, printer automatically truncates or pads on the left with zeros to achieve the required number of characters
				byte[] barcodeData = "012345678901".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN13, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN13, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN13, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(400, 500, BarCodeType.CodeEAN13, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.CODABAR.getBarCodeType() == barCode){
				byte[] barcodeData = "A1234567A".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(400, 500, BarCodeType.CODABAR, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.CODABAR, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.CODABAR, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(400, 500, BarCodeType.CODABAR, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.ITF25.getBarCodeType() == barCode){
				byte[] barcodeData = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(400, 500, BarCodeType.ITF25, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.ITF25, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.ITF25, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(400, 500, BarCodeType.ITF25, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}
			else if(BarCodeType.UPCA.getBarCodeType() == barCode){
				//data is limited to exactly 11 characters
				byte[] barcodeData = "01234567890".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(400, 500, BarCodeType.UPCA, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.UPCA, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.UPCA, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(400, 500, BarCodeType.UPCA, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}
			else if(BarCodeType.UPCE.getBarCodeType() == barCode){
				//data is limited to exactly 10 characters
				byte[] barcodeData = "0123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(400, 500, BarCodeType.UPCE, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.UPCE, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter,2, 5);
				labelEdit.printBarcode1D(400, 500, BarCodeType.UPCE, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(400, 500, BarCodeType.UPCE, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}
			else if(BARCODE_PDF417 == barCode){
				String barcodeData = "www.snbc.com 0631-5675888 PDF417";
				labelEdit.printBarcodePDF417(10, 150, Rotation.Rotation0, barcodeData, 3, 4, 4, 18, 3);
				labelEdit.printBarcodePDF417(600, 350, Rotation.Rotation180, barcodeData, 3, 4, 4, 18, 3);
				labelEdit.printBarcodePDF417(10, 990, Rotation.Rotation90, barcodeData, 3, 5, 5, 20, 4);
				labelEdit.printBarcodePDF417(750, 300, Rotation.Rotation270, barcodeData, 3, 5, 5,20, 4);
			}
			else if(BARCODE_QR == barCode){
				String barcodeData = "www.snbc.com 0631-5675888 QR";
				labelEdit.printBarcodeQR(10, 10, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());
				labelEdit.printBarcodeQR(310, 10, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_Q.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());
				labelEdit.printBarcodeQR(10, 310, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_M.getLevel(), 5, QRMode.QR_MODE_ORIGINAL.getMode());
				labelEdit.printBarcodeQR(310, 310, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_L.getLevel(), 6, QRMode.QR_MODE_ORIGINAL.getMode());
			}else if(BARCODE_DATAMATRIX == barCode){
				String barcodeData = "www.snbc.com 0631-5675888 Data Matrix";
				labelEdit.printBarcodeDataMatrix(10, 10, Rotation.Rotation0, barcodeData, 16, 48, 5);
				labelEdit.printBarcodeDataMatrix(310, 10, Rotation.Rotation90, barcodeData, 16, 48, 5);
				labelEdit.printBarcodeDataMatrix(10, 310, Rotation.Rotation180, barcodeData, 16, 48, 5);
				labelEdit.printBarcodeDataMatrix(310, 310, Rotation.Rotation270, barcodeData, 16, 48, 5);
			}else if(BARCODE_MAXICODE == barCode){
				String barcodeData = "www.snbc.com 0631-5675888 maxicode";
				labelEdit.printBarcodeMaxiCode(10, 10, barcodeData,2);
			}
			printer.labelControl().print(1, 1);
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_BARCODE_BPLC Print barcode(BPLC)
		/// \elseif Chinese
		/// \defgroup PRINT_BARCODE_BPLC 打印条码(BPLC)
		/// \endif
		/// \code
		private void doPrintBarcodeBPLC(int barCode, SNBCApplication application) throws Exception {
			BarPrinter printer = ((SNBCApplication)getApplication()).getPrinter();
			ILabelEdit labelEdit = printer.labelEdit();

			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(800, 1000);
			if(BarCodeType.Code128.getBarCodeType() == barCode){
				byte[] barcodeData = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.Code128, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.Code128, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.Code128, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.Code128, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.Code39.getBarCodeType() == barCode){
				byte[] barcodeData = "123ABC".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.Code39, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.Code39, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.Code39, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.Code39, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.Code93.getBarCodeType() == barCode){
				byte[] barcodeData = "12345ABCDE".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.Code93, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.Code93, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.Code93, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.Code93, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.CodeEAN8.getBarCodeType() == barCode){
				//data is limited to exactly 7 characters
				byte[] barcodeData = "1234567".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.CodeEAN8, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.CodeEAN8, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.CodeEAN8, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.CodeEAN8, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.CodeEAN13.getBarCodeType() == barCode){
				//data is limited to exactly 12 characters, printer automatically truncates or pads on the left with zeros to achieve the required number of characters
				byte[] barcodeData = "012345678901".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.CodeEAN13, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.CodeEAN13, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.CodeEAN13, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.CodeEAN13, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.CODABAR.getBarCodeType() == barCode){
				byte[] barcodeData = "A1234567A".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.CODABAR, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.CODABAR, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.CODABAR, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.CODABAR, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}else if(BarCodeType.ITF25.getBarCodeType() == barCode){
				byte[] barcodeData = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.ITF25, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.ITF25, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.ITF25, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.ITF25, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}
			else if(BarCodeType.UPCA.getBarCodeType() == barCode){
				//data is limited to exactly 11 characters
				byte[] barcodeData = "01234567890".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.UPCA, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.UPCA, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.UPCA, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.UPCA, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}
			else if(BarCodeType.UPCE.getBarCodeType() == barCode){
				//data is limited to exactly 10 characters
				byte[] barcodeData = "0123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.UPCE, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.UPCE, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.UPCE, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.UPCE, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}
			else if(BARCODE_PDF417 == barCode){
				String barcodeData = "PDF417 123456";
				labelEdit.printBarcodePDF417(10, 10, Rotation.Rotation0, barcodeData, 4, 4, 4, 0, 3);
				labelEdit.printBarcodePDF417(10, 110, Rotation.Rotation0, barcodeData, 3, 5, 6, 0, 3);
				labelEdit.printBarcodePDF417(10, 300, Rotation.Rotation90, barcodeData, 3, 4, 4, 0, 3);
				labelEdit.printBarcodePDF417(110, 300, Rotation.Rotation90, barcodeData, 3, 5, 6, 0, 3);
			}
			else if(BARCODE_QR == barCode){
				String barcodeData = "www.snbc.com 0631-5675888 QR";
				labelEdit.printBarcodeQR(10, 10, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ORIGINAL.getMode());
				labelEdit.printBarcodeQR(10, 300, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_Q.getLevel(), 6, QRMode.QR_MODE_ENHANCED.getMode());

			}
			else if(BarCodeType.CODABAR.getBarCodeType() == barCode){
				byte[] barcodeData = "A1234567A".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(10, 10, BarCodeType.CODABAR, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 200, BarCodeType.CODABAR, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
				labelEdit.printBarcode1D(10, 900, BarCodeType.CODABAR, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(200, 900, BarCodeType.CODABAR, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 3, 6);
			}

			printer.labelControl().print(1, 1);
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_BARCODE_BPLT Print barcode(BPLT)
		/// \elseif Chinese
		/// \defgroup PRINT_BARCODE_BPLT 打印条码(BPLT)
		/// \endif
		/// \code
		private void doPrintBarcodeBPLT(int barCode, SNBCApplication application) throws Exception {
			BarPrinter printer = ((SNBCApplication)getApplication()).getPrinter();
			ILabelEdit labelEdit = printer.labelEdit();
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(800, 1000);
			labelEdit.clearPrintBuffer();

			if(BarCodeType.Code128.getBarCodeType() == barCode){
				byte[] barcodeData = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(160, 10, BarCodeType.Code128, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 210, BarCodeType.Code128, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(100, 10, BarCodeType.Code128, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 300, BarCodeType.Code128, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
//				//use subsets A to print "TEST"
//				labelEdit.printBarcode1D(10, 610, BarCodeType.Code128, Rotation.Rotation0, ">952375152".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 3, 7);
//				//use subsets B to print "CODE128"
//				labelEdit.printBarcode1D(10, 710, BarCodeType.Code128, Rotation.Rotation0, ">:CODE128".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 3, 7);
//				//use subset C to print "0123456789"
//				labelEdit.printBarcode1D(10, 810, BarCodeType.Code128, Rotation.Rotation0, ">;0123456789".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 3, 7);

			}else if(BarCodeType.Code39.getBarCodeType() == barCode){
				byte[] barcodeData = "123ABC".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(160, 10, BarCodeType.Code39, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 210, BarCodeType.Code39, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(100, 10, BarCodeType.Code39, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 300, BarCodeType.Code39, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}else if(BarCodeType.Code93.getBarCodeType() == barCode){
				byte[] barcodeData = "12345ABCDE".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(160, 10, BarCodeType.Code93, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 210, BarCodeType.Code93, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(100, 10, BarCodeType.Code93, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 300, BarCodeType.Code93, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}else if(BarCodeType.CodeEAN8.getBarCodeType() == barCode){
				//data is limited to exactly 7 characters
				byte[] barcodeData = "1234567".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(160, 10, BarCodeType.CodeEAN8, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 210, BarCodeType.CodeEAN8, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(100, 10, BarCodeType.CodeEAN8, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 300, BarCodeType.CodeEAN8, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}else if(BarCodeType.CodeEAN13.getBarCodeType() == barCode){
				//data is limited to exactly 12 characters, printer automatically truncates or pads on the left with zeros to achieve the required number of characters
				byte[] barcodeData = "012345678901".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(160, 10, BarCodeType.CodeEAN13, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 210, BarCodeType.CodeEAN13, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(100, 10, BarCodeType.CodeEAN13, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 300, BarCodeType.CodeEAN13, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}else if(BarCodeType.CODABAR.getBarCodeType() == barCode){
				byte[] barcodeData = "A1234567A".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(160, 10, BarCodeType.CODABAR, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 210, BarCodeType.CODABAR, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(100, 10, BarCodeType.CODABAR, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 300, BarCodeType.CODABAR, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}else if(BarCodeType.ITF25.getBarCodeType() == barCode){
				byte[] barcodeData = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(160, 10, BarCodeType.ITF25, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 210, BarCodeType.ITF25, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(100, 10, BarCodeType.ITF25, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 300, BarCodeType.ITF25, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}else if(BarCodeType.UPCA.getBarCodeType() == barCode){
				//data is limited to exactly 11 characters
				byte[] barcodeData = "01234567890".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(160, 10, BarCodeType.UPCA, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 210, BarCodeType.UPCA, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(100, 10, BarCodeType.UPCA, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 300, BarCodeType.UPCA, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}else if(BarCodeType.UPCE.getBarCodeType() == barCode){
				//data is limited to exactly 10 characters
				byte[] barcodeData = "1230000045".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(160, 10, BarCodeType.UPCE, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(400, 210, BarCodeType.UPCE, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(100, 10, BarCodeType.UPCE, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 300, BarCodeType.UPCE, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}else if(BARCODE_PDF417 == barCode){
				String barcodeData = "www.snbc.com 0631-5675888 PDF417";
				labelEdit.printBarcodePDF417(110, 10, Rotation.Rotation0,barcodeData, 0, 4, 4, 18, 3);
				labelEdit.printBarcodePDF417(610, 160, Rotation.Rotation180,barcodeData, 3, 4, 4, 18, 3);
				labelEdit.printBarcodePDF417(100, 10, Rotation.Rotation90,barcodeData, 3, 5, 5, 20, 4);
				labelEdit.printBarcodePDF417(710, 810, Rotation.Rotation270,barcodeData, 3, 5, 5, 20, 4);
			}else if(BARCODE_QR == barCode){
				String barcodeData = "www.snbc.com 0631-5675888 QR";
				labelEdit.printBarcodeQR(110, 10, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());
				labelEdit.printBarcodeQR(410, 10, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_Q.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());
				labelEdit.printBarcodeQR(110, 400, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_M.getLevel(), 5, QRMode.QR_MODE_ORIGINAL.getMode());
				labelEdit.printBarcodeQR(410, 400, Rotation.Rotation0, barcodeData, QRLevel.QR_LEVEL_L.getLevel(), 6, QRMode.QR_MODE_ORIGINAL.getMode());
			}else if(BARCODE_DATAMATRIX == barCode){
				String barcodeData = "www.snbc.com 0631-5675888 Data Matrix";
				labelEdit.printBarcodeDataMatrix(110, 10, Rotation.Rotation0, barcodeData, 16, 48, 5);
				labelEdit.printBarcodeDataMatrix(410, 10, Rotation.Rotation90, barcodeData, 16, 48, 5);
				labelEdit.printBarcodeDataMatrix(110, 400, Rotation.Rotation180, barcodeData, 16, 48, 5);
				labelEdit.printBarcodeDataMatrix(410, 410, Rotation.Rotation270, barcodeData, 16, 48, 5);
			}else if(BARCODE_MAXICODE == barCode){
				String barcodeData = "www.snbc.com 0631-5675888 maxicode";
				labelEdit.printBarcodeMaxiCode(10, 10, barcodeData, 2);
			}
			printer.labelControl().print(1, 1);
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_BARCODE_BPLE Print barcode(BPLE)
		/// \elseif Chinese
		/// \defgroup PRINT_BARCODE_BPLE 打印条码(BPLE)
		/// \endif
		/// \code
		private void doPrintBarcodeBPLE(int barCode, SNBCApplication application)throws Exception {
			BarPrinter printer = ((SNBCApplication)getApplication()).getPrinter();
			ILabelEdit labelEdit = printer.labelEdit();
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(800, 1000);
			labelEdit.clearPrintBuffer();
			if (BarCodeType.Code128.getBarCodeType() == barCode) {
				byte[] barcodeData = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code128, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code128, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code128, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code128, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				//use subsets A to print "TEST"
				labelEdit.printBarcode1D(10, 610, BarCodeType.Code128, Rotation.Rotation0, ">952375152".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 3, 7);
				//use subsets B to print "CODE128"
				labelEdit.printBarcode1D(10, 710, BarCodeType.Code128, Rotation.Rotation0, ">:CODE128".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 3, 7);
				//use subsets B to print "0123456789"
				labelEdit.printBarcode1D(10, 810, BarCodeType.Code128, Rotation.Rotation0, ">;0123456789".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 3, 7);
			}
			else if(BarCodeType.Code39.getBarCodeType() == barCode){
				byte[] barcodeData = "123ABC".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code39, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code39, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code39, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code39, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}
			else if(BarCodeType.Code93.getBarCodeType() == barCode){
				byte[] barcodeData = "12345ABCDE".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code93, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code93, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code93, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.Code93, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}
			else if(BarCodeType.CodeEAN8.getBarCodeType() == barCode){
				//data is limited to exactly 7 characters
				byte[] barcodeData = "1234567".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(320, 400, BarCodeType.CodeEAN8, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.CodeEAN8, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.CodeEAN8, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.CodeEAN8, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}
			else if(BarCodeType.CodeEAN13.getBarCodeType() == barCode){
				//data is limited to exactly 12 characters,printer automatically truncates or pads on the left with
				//zeros to achieve the required number of characters
				byte[] barcodeData = "012345678901".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(320, 400, BarCodeType.CodeEAN13, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.CodeEAN13, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.CodeEAN13, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.CodeEAN13, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}
			else if(BarCodeType.CODABAR.getBarCodeType() == barCode){
				byte[] barcodeData = "1234567".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(320, 400, BarCodeType.CODABAR, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.CODABAR, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.CODABAR, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.CODABAR, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}
			else if(BarCodeType.ITF25.getBarCodeType() == barCode){
				byte[] barcodeData = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(320, 400, BarCodeType.ITF25, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.ITF25, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.ITF25, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.ITF25, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}
			else if(BarCodeType.UPCA.getBarCodeType() == barCode){
				//data is limited to exactly 11 characters
				byte[] barcodeData = "01234567890".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(320, 400, BarCodeType.UPCA, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.UPCA, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.UPCA, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.UPCA, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}
			else if(BarCodeType.UPCE.getBarCodeType() == barCode){
				//data is limited to exactly 10 characters
				byte[] barcodeData = "012345".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(320, 400, BarCodeType.UPCE, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.UPCE, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.UPCE, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(320, 400, BarCodeType.UPCE, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
			}
			else if(BARCODE_PDF417 == barCode){
				byte[] barcodeData = "www.snbc.com  PDF417".getBytes(application.getDecodeType());
				labelEdit.printBarcodePDF417(10, 10, Rotation.Rotation0, new String(barcodeData), 0, 4, 4, 18, 3);
				labelEdit.printBarcodePDF417(10, 110, Rotation.Rotation180, new String(barcodeData), 3, 4, 4, 18, 3);
				labelEdit.printBarcodePDF417(10, 210, Rotation.Rotation90, new String(barcodeData), 3, 5, 5, 20,4);
				labelEdit.printBarcodePDF417(310, 210, Rotation.Rotation270, new String(barcodeData), 3, 5, 5, 20,4);
			}
			else if(BARCODE_QR == barCode){
				byte[] barcodeData = "www.snbc.com 0631-5675888 QR".getBytes(application.getDecodeType());
				labelEdit.printBarcodeQR(10, 10, Rotation.Rotation0, new String(barcodeData), QRLevel.QR_LEVEL_H.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());
				labelEdit.printBarcodeQR(310, 10, Rotation.Rotation0, new String(barcodeData), QRLevel.QR_LEVEL_Q.getLevel(), 4, QRMode.QR_MODE_ENHANCED.getMode());
				labelEdit.printBarcodeQR(10, 310, Rotation.Rotation0, new String(barcodeData), QRLevel.QR_LEVEL_M.getLevel(), 5, QRMode.QR_MODE_ORIGINAL.getMode());
				labelEdit.printBarcodeQR(310, 310, Rotation.Rotation0, new String(barcodeData), QRLevel.QR_LEVEL_L.getLevel(), 6, QRMode.QR_MODE_ORIGINAL.getMode());
			}
			else if(BARCODE_DATAMATRIX == barCode){
				byte[] barcodeData = "www.snbc.com 0631-5675888  Matrix".getBytes(application.getDecodeType());
				labelEdit.printBarcodeDataMatrix(10, 10, Rotation.Rotation0, new String(barcodeData), 16,48,5);
				labelEdit.printBarcodeDataMatrix(310,10, Rotation.Rotation90, new String(barcodeData),16,48,5);
				labelEdit.printBarcodeDataMatrix(10,310, Rotation.Rotation180, new String(barcodeData), 16,48,5);
				labelEdit.printBarcodeDataMatrix(310,310, Rotation.Rotation270, new String(barcodeData), 16,48,5);
			}
			else if(BARCODE_MAXICODE == barCode){
				byte[] barcodeData = "www.snbc.com 0631-5675888 maxicode".getBytes(application.getDecodeType());
				labelEdit.printBarcodeMaxiCode(10, 10, new String(barcodeData), 4);//Accepted values:2,3,4,5,6
			}
			printer.labelControl().print(1, 1);
		}
		/// \endcode

		/// \if English
		/// \defgroup PRINT_BARCODE_BPLZ Print barcode(BPLZ)
		/// \elseif Chinese
		/// \defgroup PRINT_BARCODE_BPLZ 打印条码(BPLZ)
		/// \endif
		/// \code
		private void doPrintBarcodeBPLZ(int barCode, SNBCApplication application)throws Exception {
			BarPrinter printer = ((SNBCApplication)getApplication()).getPrinter();
			ILabelEdit labelEdit = printer.labelEdit();
			labelEdit.setColumn(1, 8);
			labelEdit.setLabelSize(800, 1000);
			if (BarCodeType.Code128.getBarCodeType()== barCode) {
				byte[] barcodeData = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(110, 10, BarCodeType.Code128, Rotation.Rotation0, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(110, 160, BarCodeType.Code128, Rotation.Rotation180, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 10, BarCodeType.Code128, Rotation.Rotation90, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 10, BarCodeType.Code128, Rotation.Rotation270, barcodeData, 60, HRIPosition.AlignCenter, 2, 5);
				//use subsets A to print "TEST"
				labelEdit.printBarcode1D(10, 610, BarCodeType.Code128, Rotation.Rotation0, ">952375152".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 3, 7);
				//use subsets B to print "CODE128"
				labelEdit.printBarcode1D(10, 710, BarCodeType.Code128, Rotation.Rotation0, ">:CODE128".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 3, 7);
				//use subset C to print "0123456789"
				labelEdit.printBarcode1D(10, 810, BarCodeType.Code128, Rotation.Rotation0, ">;0123456789".getBytes(application.getDecodeType()), 60, HRIPosition.AlignCenter, 3, 7);
			}
			else if(BarCodeType.Code39.getBarCodeType()==barCode){
				byte[] byteDatas = "123ABC".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(110, 10, BarCodeType.Code39, Rotation.Rotation0, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(110, 160, BarCodeType.Code39, Rotation.Rotation180, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 10, BarCodeType.Code39, Rotation.Rotation90, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 10, BarCodeType.Code39, Rotation.Rotation270, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
			}else if(BarCodeType.Code93.getBarCodeType() ==  barCode){
				byte[] byteDatas = "12345ABCDE".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(110, 10, BarCodeType.Code93, Rotation.Rotation0, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(110, 160, BarCodeType.Code93, Rotation.Rotation180, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 10, BarCodeType.Code93, Rotation.Rotation90, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 10, BarCodeType.Code93, Rotation.Rotation270, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);

			}else if(BarCodeType.CodeEAN8.getBarCodeType() == barCode){
				//data is limited to exactly 7 characters
				byte[] byteDatas = "1234567".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(110, 10, BarCodeType.CodeEAN8, Rotation.Rotation0, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(110, 160, BarCodeType.CodeEAN8, Rotation.Rotation180, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 10, BarCodeType.CodeEAN8, Rotation.Rotation90, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 10, BarCodeType.CodeEAN8, Rotation.Rotation270, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);

			}else if (BarCodeType.CodeEAN13.getBarCodeType() == barCode) {
				//data is limited to exactly 12 characters,printer automatically truncates or pads on the left with zeros to 
				//achieve the required number of characters
				byte[] byteDatas = "012345678901".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(110, 10, BarCodeType.CodeEAN13, Rotation.Rotation0, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(110, 160, BarCodeType.CodeEAN13, Rotation.Rotation180, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 10, BarCodeType.CodeEAN13, Rotation.Rotation90, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 10, BarCodeType.CodeEAN13, Rotation.Rotation270, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
			}else if (BarCodeType.CODABAR.getBarCodeType() == barCode) {
				byte[] byteDatas = "1234567".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(110, 10, BarCodeType.CODABAR, Rotation.Rotation0, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(110, 160, BarCodeType.CODABAR, Rotation.Rotation180, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 10, BarCodeType.CODABAR, Rotation.Rotation90, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 10, BarCodeType.CODABAR, Rotation.Rotation270, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
			}else if (BarCodeType.ITF25.getBarCodeType() == barCode) {
				byte[] byteDatas = "123456".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(110, 10, BarCodeType.ITF25, Rotation.Rotation0, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(110, 160, BarCodeType.ITF25, Rotation.Rotation180, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 10, BarCodeType.ITF25, Rotation.Rotation90, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 10, BarCodeType.ITF25, Rotation.Rotation270, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
			}else if (BarCodeType.UPCA.getBarCodeType() == barCode) {
				//data is limited to exactly 11 characters
				byte[] byteDatas = "01234567890".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(110, 10, BarCodeType.UPCA, Rotation.Rotation0, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(110, 160, BarCodeType.UPCA, Rotation.Rotation180, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 10, BarCodeType.UPCA, Rotation.Rotation90, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 10, BarCodeType.UPCA, Rotation.Rotation270, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
			}else if (BarCodeType.UPCE.getBarCodeType() == barCode ) {
				//data is limited to exactly 10 characters
				byte[] byteDatas = "1230000045".getBytes(application.getDecodeType());
				labelEdit.printBarcode1D(110, 10, BarCodeType.UPCE, Rotation.Rotation0, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(110, 160, BarCodeType.UPCE, Rotation.Rotation180, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(10, 10, BarCodeType.UPCE, Rotation.Rotation90, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
				labelEdit.printBarcode1D(710, 10, BarCodeType.UPCE, Rotation.Rotation270, byteDatas, 60, HRIPosition.AlignCenter, 2, 5);
			}
			else if (BARCODE_PDF417 == barCode) {
				byte[] byteDatas = "www.snbc.com 0631-5675888 PDF417".getBytes(application.getDecodeType());
				labelEdit.printBarcodePDF417(10, 10, Rotation.Rotation0, new String(byteDatas), 0, 4, 4, 18, 3);
				labelEdit.printBarcodePDF417(10, 110, Rotation.Rotation180, new String(byteDatas), 3, 4, 4, 18, 3);
				labelEdit.printBarcodePDF417(10, 210, Rotation.Rotation90, new String(byteDatas), 3, 5, 5, 20, 4);
				labelEdit.printBarcodePDF417(310, 210, Rotation.Rotation270, new String(byteDatas), 3, 5, 5, 20, 4);

			}
			else if (BARCODE_QR == barCode) {
				byte[] byteDatas = "www.snbc.com 0631-5675888 QR".getBytes(application.getDecodeType());
				labelEdit.printBarcodeQR(10, 10, Rotation.Rotation0, new String(byteDatas),QRLevel.QR_LEVEL_H.getLevel() ,4, QRMode.QR_MODE_ENHANCED.getMode());
				labelEdit.printBarcodeQR(310, 10, Rotation.Rotation0, new String(byteDatas),QRLevel.QR_LEVEL_Q.getLevel() ,4, QRMode.QR_MODE_ENHANCED.getMode());
				labelEdit.printBarcodeQR(10, 310, Rotation.Rotation0, new String(byteDatas),QRLevel.QR_LEVEL_M.getLevel() ,5, QRMode.QR_MODE_ORIGINAL.getMode());
				labelEdit.printBarcodeQR(310,310, Rotation.Rotation0, new String(byteDatas),QRLevel.QR_LEVEL_L.getLevel() ,6, QRMode.QR_MODE_ORIGINAL.getMode());
			}
			else if (BARCODE_DATAMATRIX == barCode) {
				byte[] byteDatas = "www.snbc.com 0631-5675888 Data Matrix".getBytes(application.getDecodeType());
				labelEdit.printBarcodeDataMatrix(10, 10, Rotation.Rotation0, new String(byteDatas), 16, 48, 5);
				labelEdit.printBarcodeDataMatrix(310,10, Rotation.Rotation90, new String(byteDatas), 16, 48, 5);
				labelEdit.printBarcodeDataMatrix(10,310, Rotation.Rotation180, new String(byteDatas), 16, 48, 5);
				labelEdit.printBarcodeDataMatrix(310,310,Rotation.Rotation270, new String(byteDatas), 16, 48, 5);
			}
			else if (BARCODE_MAXICODE == barCode) {
				byte[] byteDatas = "www.snbc.com 0631-5675888 maxicode".getBytes(application.getDecodeType());
				labelEdit.printBarcodeMaxiCode(10, 10, new String(byteDatas), 2);//Accepted values:2,3,4,5,6

			}
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
