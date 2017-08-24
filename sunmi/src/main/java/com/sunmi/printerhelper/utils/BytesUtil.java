package com.sunmi.printerhelper.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class BytesUtil {

	//字节流转16进制字符串
	public static String getHexStringFromBytes(byte[] data) {
		if (data == null || data.length <= 0) {
			return null;
		}
		String hexString = "0123456789ABCDEF";
		int size = data.length * 2;
		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < data.length; i++) {
			sb.append(hexString.charAt((data[i] & 0xF0) >> 4));
			sb.append(hexString.charAt((data[i] & 0x0F) >> 0));
		}
		return sb.toString();
	}

	//单字符转字节
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	//16进制字符串转字节数组
	@SuppressLint("DefaultLocale")
	public static byte[] getBytesFromHexString(String hexstring){
		if(hexstring == null || hexstring.equals("")){
			return null;
		}
		hexstring = hexstring.replace(" ", "");
		hexstring = hexstring.toUpperCase();
		int size = hexstring.length()/2;
		char[] hexarray = hexstring.toCharArray();
		byte[] rv = new byte[size];
		for(int i=0; i<size; i++){
			int pos = i * 2;
			rv[i] = (byte) (charToByte(hexarray[pos]) << 4 | charToByte(hexarray[pos + 1]));
		}
		return rv;
	}

	//字节数组组合操作1
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	//字节数组组合操作2
	public static byte[] byteMerger(byte[][] byteList) {

		int length = 0;
		for (int i = 0; i < byteList.length; i++) {
			length += byteList[i].length;
		}
		byte[] result = new byte[length];

		int index = 0;
		for (int i = 0; i < byteList.length; i++) {
			byte[] nowByte = byteList[i];
			for (int k = 0; k < byteList[i].length; k++) {
				result[index] = nowByte[k];
				index++;
			}
		}
		for (int i = 0; i < index; i++) {
			// CommonUtils.LogWuwei("", "result[" + i + "] is " + result[i]);
		}
		return result;
	}

	//生成表格字节流
	public static byte[] initTable(int h, int w){
		int hh = h * 32;
		int ww = w * 4;

		byte[] data = new byte[ hh * ww + 5];


		data[0] = (byte)ww;//xL
		data[1] = (byte)(ww >> 8);//xH
		data[2] = (byte)hh;
		data[3] = (byte)(hh >> 8);

		int k = 4;
		int m = 31;
		for(int i=0; i<h; i++){
			for(int j=0; j<w; j++){
				data[k++] = (byte)0xFF;
				data[k++] = (byte)0xFF;
				data[k++] = (byte)0xFF;
				data[k++] = (byte)0xFF;
			}
			if(i == h-1) m =30;
			for(int t=0; t< m; t++){
				for(int j=0; j<w-1; j++){
					data[k++] = (byte)0x80;
					data[k++] = (byte)0;
					data[k++] = (byte)0;
					data[k++] = (byte)0;
				}
				data[k++] = (byte)0x80;
				data[k++] = (byte)0;
				data[k++] = (byte)0;
				data[k++] = (byte)0x01;
			}
		}
		for(int j=0; j<w; j++){
			data[k++] = (byte)0xFF;
			data[k++] = (byte)0xFF;
			data[k++] = (byte)0xFF;
			data[k++] = (byte)0xFF;
		}
		data[k++] = 0x0A;
		return data;
	}

	/**
	 * 生成二维码字节流
	 *
	 * @param data
	 * @param size
	 * @return
	 */
	public static byte[] getZXingQRCode(String data, int size) {
		try {
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			//图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size, hints);
			//System.out.println("bitmatrix height:" + bitMatrix.getHeight() + " width:" + bitMatrix.getWidth());
			return getBytesFromBitMatrix(bitMatrix);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] getBytesFromBitMatrix(BitMatrix bits) {
		if (bits == null) return null;

		int h = bits.getHeight();
		int w = (bits.getWidth() + 7) / 8;
		byte[] rv = new byte[h * w + 4];

		rv[0] = (byte) w;//xL
		rv[1] = (byte) (w >> 8);//xH
		rv[2] = (byte) h;
		rv[3] = (byte) (h >> 8);

		int k = 4;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				for (int n = 0; n < 8; n++) {
					byte b = getBitMatrixColor(bits, j * 8 + n, i);
					rv[k] += rv[k] + b;
				}
				k++;
			}
		}
		return rv;
	}

	private static byte getBitMatrixColor(BitMatrix bits, int x, int y) {
		int width = bits.getWidth();
		int height = bits.getHeight();
		if (x >= width || y >= height || x < 0 || y < 0) return 0;
		if (bits.get(x, y)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 将bitmap图转换为头四位有宽高的光栅位图
	 */
	public static byte[] getBytesFromBitMap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int bw = (width - 1) / 8 + 1;

		byte[] rv = new byte[height * bw + 4];
		rv[0] = (byte) bw;//xL
		rv[1] = (byte) (bw >> 8);//xH
		rv[2] = (byte) height;
		rv[3] = (byte) (height >> 8);

		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int clr = pixels[width * i + j];
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue = clr & 0x000000ff;
				byte gray = (RGB2Gray(red, green, blue));
				rv[(width * i + j) / 8 + 4] = (byte) (rv[(width * i + j) / 8 + 4] | (gray << (7 - ((width * i + j) % 8))));
			}
		}

		return rv;
	}

	private static byte RGB2Gray(int r, int g, int b) {
		return (false ? ((int) (0.29900 * r + 0.58700 * g + 0.11400 * b) > 200)
				: ((int) (0.29900 * r + 0.58700 * g + 0.11400 * b) < 200)) ? (byte) 1 : (byte) 0;
	}

	/**
	 * 生成间断性黑块数据
	 * @param w : 打印纸宽度, 单位点
	 * @return
	 */
	public static byte[] initBlackBlock(int w){
		int ww = (w + 7)/8 ;
		int n = (ww + 11)/12;
		int hh = n * 24;
		byte[] data = new byte[ hh * ww + 5];

		data[0] = (byte)ww;//xL
		data[1] = (byte)(ww >> 8);//xH
		data[2] = (byte)hh;
		data[3] = (byte)(hh >> 8);

		int k = 4;
		for(int i=0; i < n; i++){
			for(int j=0; j<24; j++){
				for(int m =0; m<ww; m++){
					if(m/12 == i){
						data[k++] = (byte)0xFF;
					}else{
						data[k++] = 0;
					}
				}
			}
		}
		data[k++] = 0x0A;
		return data;
	}

	/**
	 * 生成一大块黑块数据
	 * @param h : 黑块高度, 单位点
	 * @param w : 黑块宽度, 单位点, 8的倍数
	 * @return
	 */
	public static byte[] initBlackBlock(int h, int w){
		int hh = h;
		int ww = (w - 1)/8 + 1;
		byte[] data = new byte[ hh * ww + 6];

		data[0] = (byte)ww;//xL
		data[1] = (byte)(ww >> 8);//xH
		data[2] = (byte)hh;
		data[3] = (byte)(hh >> 8);

		int k = 4;
		for(int i=0; i<hh; i++){
			for(int j=0; j<ww; j++){
				data[k++] = (byte)0xFF;
			}
		}
		data[k++] = 0x00;data[k++] = 0x00;
		return data;
	}

	//百度小票
	public static byte[] getBaiduTestBytes() {
		byte[] rv = new byte[]{
				0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x11, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x45, 0x01, 0x1b, 0x47, 0x01, (byte) 0xb1, (byte) 0xbe
				, (byte) 0xb5, (byte) 0xea, (byte) 0xc1, (byte) 0xf4, (byte) 0xb4, (byte) 0xe6, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a
				, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
				, 0x2a, 0x0a
				, 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x11, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x45, 0x01, 0x1b, 0x47, 0x01, 0x1b, 0x61
				, 0x01, 0x23, 0x31, 0x35, 0x20, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6, (byte) 0xc8, (byte) 0xcd, (byte) 0xe2, (byte) 0xc2, (byte) 0xf4, 0x0a, 0x5b, (byte) 0xbb, (byte) 0xf5, (byte) 0xb5, (byte) 0xbd, (byte) 0xb8, (byte) 0xb6, (byte) 0xbf, (byte) 0xee, 0x5d, 0x0a, 0x1b, 0x4d, 0x00
				, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
				, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a
				, 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xc6, (byte) 0xda, (byte) 0xcd, (byte) 0xfb, (byte) 0xcb, (byte) 0xcd, (byte) 0xb4, (byte) 0xef
				, (byte) 0xca, (byte) 0xb1, (byte) 0xbc, (byte) 0xe4, (byte) 0xa3, (byte) 0xba, (byte) 0xc1, (byte) 0xa2, (byte) 0xbc, (byte) 0xb4, (byte) 0xc5, (byte) 0xe4, (byte) 0xcb, (byte) 0xcd, 0x0a, (byte) 0xb6, (byte) 0xa9, (byte) 0xb5, (byte) 0xa5, (byte) 0xb1, (byte) 0xb8, (byte) 0xd7, (byte) 0xa2, (byte) 0xa3, (byte) 0xba, (byte) 0xc7, (byte) 0xeb, (byte) 0xcb
				, (byte) 0xcd, (byte) 0xb5, (byte) 0xbd, (byte) 0xbf, (byte) 0xfc, (byte) 0xbf, (byte) 0xc6, (byte) 0xce, (byte) 0xf7, (byte) 0xc3, (byte) 0xc5, 0x2c, (byte) 0xb2, (byte) 0xbb, (byte) 0xd2, (byte) 0xaa, (byte) 0xc0, (byte) 0xb1, 0x0a, (byte) 0xb7, (byte) 0xa2, (byte) 0xc6, (byte) 0xb1, (byte) 0xd0, (byte) 0xc5, (byte) 0xcf, (byte) 0xa2, (byte) 0xa3
				, (byte) 0xba, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6, (byte) 0xc8, (byte) 0xcd, (byte) 0xe2, (byte) 0xc2, (byte) 0xf4, (byte) 0xb7, (byte) 0xa2, (byte) 0xc6, (byte) 0xb1, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47
				, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
				, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a
				, 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xb6, (byte) 0xa9, (byte) 0xb5, (byte) 0xa5, (byte) 0xb1, (byte) 0xe0, (byte) 0xba, (byte) 0xc5
				, (byte) 0xa3, (byte) 0xba, 0x31, 0x34, 0x31, 0x38, 0x37, 0x31, 0x38, 0x36, 0x39, 0x31, 0x31, 0x36, 0x38, 0x39, 0x0a, (byte) 0xcf, (byte) 0xc2, (byte) 0xb5, (byte) 0xa5, (byte) 0xca, (byte) 0xb1, (byte) 0xbc, (byte) 0xe4, (byte) 0xa3, (byte) 0xba, 0x32
				, 0x30, 0x31, 0x34, 0x2d, 0x31, 0x32, 0x2d, 0x31, 0x36, 0x20, 0x31, 0x36, 0x3a, 0x33, 0x31, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00
				, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
				, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a
				, 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xb2, (byte) 0xcb, (byte) 0xc6, (byte) 0xb7, (byte) 0xc3, (byte) 0xfb, (byte) 0xb3, (byte) 0xc6
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, (byte) 0xca, (byte) 0xfd, (byte) 0xc1, (byte) 0xbf, 0x20, 0x20, 0x20, 0x20, 0x20, (byte) 0xbd, (byte) 0xf0, (byte) 0xb6, (byte) 0xee, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61
				, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b
				, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xcf, (byte) 0xe3, (byte) 0xc0, (byte) 0xb1, (byte) 0xc3, (byte) 0xe6, (byte) 0xcc, (byte) 0xd7, (byte) 0xb2, (byte) 0xcd, 0x1b, 0x24, (byte) 0xf2, 0x00, 0x31, 0x1b, 0x24, 0x25, 0x01, (byte) 0xa3
				, (byte) 0xa4, 0x34, 0x30, 0x2e, 0x30, 0x30, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x4d, 0x00
				, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b, 0x45, 0x00, 0x1b
				, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xcb, (byte) 0xd8, (byte) 0xca, (byte) 0xb3, (byte) 0xcc, (byte) 0xec, (byte) 0xcf, (byte) 0xc2, (byte) 0xba, (byte) 0xba, (byte) 0xb1, (byte) 0xa4, 0x1b, 0x24, (byte) 0xf2, 0x00, 0x31, 0x1b, 0x24, 0x25, 0x01, (byte) 0xa3, (byte) 0xa4
				, 0x33, 0x38, 0x2e, 0x30, 0x30, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x4d, 0x00, 0x1b
				, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00
				, 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a
				, 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
				, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a, 0x1b, 0x4d, 0x00
				, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, (byte) 0xd0, (byte) 0xd5, (byte) 0xc3, (byte) 0xfb, (byte) 0xa3, (byte) 0xba, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6, (byte) 0xc8, (byte) 0xb2, (byte) 0xe2, (byte) 0xca
				, (byte) 0xd4, 0x0a, (byte) 0xb5, (byte) 0xd8, (byte) 0xd6, (byte) 0xb7, (byte) 0xa3, (byte) 0xba, (byte) 0xbf, (byte) 0xfc, (byte) 0xbf, (byte) 0xc6, (byte) 0xbf, (byte) 0xc6, (byte) 0xbc, (byte) 0xbc, (byte) 0xb4, (byte) 0xf3, (byte) 0xcf, (byte) 0xc3, 0x0a, (byte) 0xb5, (byte) 0xe7, (byte) 0xbb, (byte) 0xb0, (byte) 0xa3, (byte) 0xba, 0x31
				, 0x38, 0x37, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x0a
				, 0x1b, 0x40, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
				, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6
				, (byte) 0xc8, (byte) 0xb2, (byte) 0xe2, (byte) 0xca, (byte) 0xd4, (byte) 0xc9, (byte) 0xcc, (byte) 0xbb, (byte) 0xa7, 0x0a, 0x31, 0x38, 0x37, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d
				, 0x21, 0x00, 0x1b, 0x45, 0x00, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a
				, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a, 0x1b, 0x4d, 0x00, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x00, 0x1b, 0x45, 0x00
				, 0x1b, 0x47, 0x00, 0x1b, 0x61, 0x00, 0x1b, 0x61, 0x01, 0x23, 0x31, 0x35, 0x20, (byte) 0xb0, (byte) 0xd9, (byte) 0xb6, (byte) 0xc8, (byte) 0xcd, (byte) 0xe2, (byte) 0xc2, (byte) 0xf4, 0x20, 0x20, 0x31, 0x31, (byte) 0xd4, (byte) 0xc2, 0x30
				, 0x39, (byte) 0xc8, (byte) 0xd5, 0x20, 0x31, 0x37, 0x3a, 0x35, 0x30, 0x3a, 0x33, 0x30, 0x0a, 0x0a, 0x0a, 0x0a, 0x0a
		};
		return rv;
	}

	//美团小票
	public static byte[] getMeituanBill() {
		byte[] rv = new byte[]{
				0x1b, 0x40, 0x1b, 0x61, 0x01, 0x1d, 0x21, 0x11, (byte) 0xa3, (byte) 0xa3, 0x31, 0x20, 0x20, (byte) 0xc3, (byte) 0xc0, (byte) 0xcd, (byte) 0xc5, (byte) 0xb2, (byte) 0xe2, (byte) 0xca, (byte) 0xd4, 0x0a
				, 0x0a, 0x1d, 0x21, 0x00, (byte) 0xd4, (byte) 0xc1, (byte) 0xcf, (byte) 0xe3, (byte) 0xb8, (byte) 0xdb, (byte) 0xca, (byte) 0xbd, (byte) 0xc9, (byte) 0xd5, (byte) 0xc0, (byte) 0xb0, 0x28, (byte) 0xb5, (byte) 0xda, 0x31, (byte) 0xc1, (byte) 0xaa
				, 0x29, 0x0a, 0x1b, 0x21, 0x10, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x2a, 0x20, 0x2a, 0x20, 0x2a, 0x20
				, 0x2a, 0x20, 0x2a, 0x20, 0x2a, 0x20, 0x20, (byte) 0xd4, (byte) 0xa4, (byte) 0xb6, (byte) 0xa9, (byte) 0xb5, (byte) 0xa5, 0x20, 0x20, 0x2a, 0x20, 0x2a, 0x20, 0x2a, 0x20, 0x2a
				, 0x20, 0x2a, 0x20, 0x2a, 0x0a, (byte) 0xc6, (byte) 0xda, (byte) 0xcd, (byte) 0xfb, (byte) 0xcb, (byte) 0xcd, (byte) 0xb4, (byte) 0xef, (byte) 0xca, (byte) 0xb1, (byte) 0xbc, (byte) 0xe4, 0x3a, 0x20, 0x5b, 0x31, 0x38
				, 0x3a, 0x30, 0x30, 0x5d, 0x0a, 0x1d, 0x21, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1b, 0x61, 0x00
				, (byte) 0xcf, (byte) 0xc2, (byte) 0xb5, (byte) 0xa5, (byte) 0xca, (byte) 0xb1, (byte) 0xbc, (byte) 0xe4, 0x3a, 0x30, 0x31, 0x2d, 0x30, 0x31, 0x20, 0x31, 0x32, 0x3a, 0x30, 0x30, 0x0a, 0x1b
				, 0x21, 0x10, (byte) 0xb1, (byte) 0xb8, (byte) 0xd7, (byte) 0xa2, 0x3a, (byte) 0xb1, (byte) 0xf0, (byte) 0xcc, (byte) 0xab, (byte) 0xc0, (byte) 0xb1, 0x0a, 0x1d, 0x21, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, (byte) 0xb2, (byte) 0xcb, (byte) 0xc3, (byte) 0xfb, 0x09, 0x09, 0x20, 0x20, 0x20, (byte) 0xca, (byte) 0xfd, (byte) 0xc1, (byte) 0xbf, 0x09, 0x20, 0x20
				, 0x20, 0x20, (byte) 0xd0, (byte) 0xa1, (byte) 0xbc, (byte) 0xc6, 0x09, 0x0a, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1b, 0x21, 0x10
				, (byte) 0xba, (byte) 0xec, (byte) 0xc9, (byte) 0xd5, (byte) 0xc8, (byte) 0xe2, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x78
				, 0x31, 0x09, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x31, 0x32, 0x0a, 0x1d, 0x21, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x0a, (byte) 0xc5, (byte) 0xe4, (byte) 0xcb, (byte) 0xcd, (byte) 0xb7, (byte) 0xd1, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x35, 0x0a, (byte) 0xb2, (byte) 0xcd, (byte) 0xba, (byte) 0xd0, (byte) 0xb7, (byte) 0xd1, 0x20, 0x20
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x31, 0x0a, 0x5b, (byte) 0xb3, (byte) 0xac, (byte) 0xca, (byte) 0xb1, (byte) 0xc5, (byte) 0xe2, (byte) 0xb8, (byte) 0xb6, 0x5d, 0x20, 0x2d, (byte) 0xcf, (byte) 0xea, (byte) 0xbc, (byte) 0xfb, (byte) 0xb6, (byte) 0xa9, (byte) 0xb5
				, (byte) 0xa5, 0x0a, (byte) 0xbf, (byte) 0xc9, (byte) 0xbf, (byte) 0xda, (byte) 0xbf, (byte) 0xc9, (byte) 0xc0, (byte) 0xd6, 0x3a, 0x78, 0x31, 0x0a, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x0a, 0x1b, 0x21, 0x10, (byte) 0xba, (byte) 0xcf, (byte) 0xbc, (byte) 0xc6, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x31, 0x38, (byte) 0xd4, (byte) 0xaa, 0x0a, 0x1b, 0x40, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1d, 0x21, 0x11, (byte) 0xd5, (byte) 0xc5, 0x2a, 0x20, 0x31, 0x38, 0x33, 0x31, 0x32, 0x33, 0x34
				, 0x35, 0x36, 0x37, 0x38, 0x0a, (byte) 0xb5, (byte) 0xd8, (byte) 0xd6, (byte) 0xb7, (byte) 0xd0, (byte) 0xc5, (byte) 0xcf, (byte) 0xa2, 0x0a, 0x1d, 0x21, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x01, 0x1d, 0x21, 0x11, (byte) 0xa3, (byte) 0xa3, 0x31, 0x20, 0x20, (byte) 0xc3, (byte) 0xc0
				, (byte) 0xcd, (byte) 0xc5, (byte) 0xb2, (byte) 0xe2, (byte) 0xca, (byte) 0xd4, 0x0a, 0x1d, 0x21, 0x00, 0x1b, 0x40, 0x0a, 0x0a, 0x0a, 0x1d, 0x56, 0x00
		};
		return rv;
	}

	//饿了么小票
	public static byte[] getErlmoData() {
		byte[] rv = new byte[]{
				0x1b, 0x40, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x20, 0x1d, 0x21, 0x11, 0x23, 0x31, 0x1d, 0x21, 0x00, 0x00, (byte) 0xb6, (byte) 0xf6
				, (byte) 0xc1, (byte) 0xcb, (byte) 0xc3, (byte) 0xb4, (byte) 0xcd, (byte) (byte) 0xe2, (byte) 0xc2, (byte) 0xf4, (byte) 0xb5, (byte) 0xa5, 0x20, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a, 0x0a, 0x1b, 0x61
				, 0x01, (byte) 0xbf, (byte) 0xa8, (byte) 0xc8, (byte) 0xf8, (byte) 0xc5, (byte) 0xfb, (byte) 0xc8, (byte) 0xf8, 0x0a, 0x0a, 0x1b, 0x61, 0x00, 0x1b, 0x61, 0x01, 0x1d, 0x21, 0x11, 0x2d, 0x2d
				, (byte) 0xd2, (byte) 0xd1, (byte) 0xd6, (byte) 0xa7, (byte) 0xb8, (byte) 0xb6, 0x2d, 0x2d, 0x1d, 0x21, 0x00, 0x00, 0x0a, 0x0a, 0x1b, 0x61, 0x00, 0x1b, 0x61, 0x01, 0x1d, 0x21
				, 0x11, (byte) 0xd4, (byte) 0xa4, (byte) 0xbc, (byte) 0xc6, 0x31, 0x39, 0x3a, 0x30, 0x30, (byte) 0xcb, (byte) 0xcd, (byte) 0xb4, (byte) (byte) 0xef, 0x1d, 0x21, 0x00, 0x00, 0x0a, 0x0a, 0x1b, 0x61
				, 0x00, 0x5b, (byte) 0xcf, (byte) 0xc2, (byte) 0xb5, (byte) 0xa5, (byte) 0xca, (byte) 0xb1, (byte) 0xbc, (byte) (byte) 0xe4, 0x5d, 0x32, 0x30, 0x31, 0x34, 0x2d, 0x31, 0x32, 0x2d, 0x30, 0x33, 0x20
				, 0x31, 0x36, 0x3a, 0x32, 0x31, 0x0a, 0x5b, (byte) 0xb1, (byte) 0xb8, (byte) 0xd7, (byte) 0xa2, 0x5d, 0x1d, 0x21, 0x01, (byte) 0xb2, (byte) 0xbb, (byte) 0xb3, (byte) 0xd4, (byte) 0xc0, (byte) 0xb1, 0x20
				, (byte) 0xc0, (byte) 0xb1, (byte) 0xd2, (byte) 0xbb, (byte) 0xb5, (byte) (byte) 0xe3, 0x20, (byte) 0xb6, (byte) (byte) 0xe0, (byte) 0xbc, (byte) 0xd3, (byte) 0xc3, (byte) 0xd7, 0x20, (byte) 0xc3, (byte) 0xbb, (byte) 0xc1, (byte) (byte) 0xe3, (byte) 0xc7, (byte) 0xae, 0x1d, 0x21
				, 0x00, 0x0a, 0x5b, (byte) 0xb7, (byte) 0xa2, (byte) 0xc6, (byte) 0xb1, 0x5d, (byte) 0xd5, (byte) (byte) 0xe2, (byte) 0xca, (byte) 0xc7, (byte) 0xd2, (byte) 0xbb, (byte) 0xb8, (byte) 0xf6, (byte) 0xb7, (byte) 0xa2, (byte) 0xc6, (byte) 0xb1, (byte) 0xcc, (byte) 0xa7
				, (byte) 0xcd, (byte) 0xb7, 0x0a, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, (byte) 0xb2, (byte) 0xcb, (byte) 0xc3, (byte) 0xfb, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, (byte) 0xca, (byte) 0xfd, (byte) 0xc1, (byte) 0xbf, 0x20, 0x20, (byte) 0xd0, (byte) 0xa1
				, (byte) 0xbc, (byte) 0xc6, 0x0a, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x20, 0x31, (byte) 0xba, (byte) 0xc5, (byte) 0xc0, (byte) 0xba, (byte) 0xd7, (byte) 0xd3
				, 0x20, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1d, 0x21, 0x01, (byte) 0xb2, (byte) (byte) 0xe2, (byte) 0xca, (byte) 0xd4, (byte) 0xc3, (byte) 0xc0
				, (byte) 0xca, (byte) 0xb3, (byte) 0xd2, (byte) 0xbb, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21
				, 0x01, 0x20, 0x78, 0x34, 0x1d, 0x21, 0x00, 0x1d, 0x21, 0x01, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x34
				, 0x1d, 0x21, 0x00, 0x0a, 0x1d, 0x21, 0x01, (byte) 0xb2, (byte) (byte) 0xe2, (byte) 0xca, (byte) 0xd4, (byte) 0xc3, (byte) 0xc0, (byte) 0xca, (byte) 0xb3, (byte) 0xb6, (byte) 0xfe, 0x1d, 0x21, 0x00, 0x20, 0x20
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x20, 0x78, 0x36, 0x1d, 0x21, 0x00, 0x1d, 0x21
				, 0x01, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x36, 0x1d, 0x21, 0x00, 0x0a, 0x1d, 0x21, 0x01, (byte) 0xb2, (byte) (byte) 0xe2
				, (byte) 0xca, (byte) 0xd4, (byte) 0xc3, (byte) 0xc0, (byte) 0xca, (byte) 0xb3, (byte) 0xc8, (byte) 0xfd, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x20, 0x78, 0x32, 0x1d, 0x21, 0x00, 0x1d, 0x21, 0x01, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20
				, 0x1d, 0x21, 0x01, 0x32, 0x1d, 0x21, 0x00, 0x0a, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x20, 0x32, (byte) 0xba
				, (byte) 0xc5, (byte) 0xc0, (byte) 0xba, (byte) 0xd7, (byte) 0xd3, 0x20, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1d, 0x21, 0x01, (byte) 0xb2
				, (byte) (byte) 0xe2, (byte) 0xca, (byte) 0xd4, 0x31, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x20, 0x78, 0x31, 0x1d, 0x21, 0x00, 0x1d, 0x21, 0x01, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x1d, 0x21, 0x01, 0x31, 0x1d, 0x21, 0x00, 0x0a, 0x1d, 0x21, 0x01, (byte) 0xb2, (byte) (byte) 0xe2, (byte) 0xca, (byte) 0xd4, 0x32, 0x1d, 0x21, 0x00, 0x20, 0x20
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x20, 0x78, 0x31
				, 0x1d, 0x21, 0x00, 0x1d, 0x21, 0x01, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x31, 0x1d, 0x21, 0x00, 0x0a
				, 0x1d, 0x21, 0x01, (byte) 0xb2, (byte) (byte) 0xe2, (byte) 0xca, (byte) 0xd4, 0x33, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x20, 0x78, 0x31, 0x1d, 0x21, 0x00, 0x1d, 0x21, 0x01, 0x1d, 0x21, 0x00
				, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x32, 0x33, 0x1d, 0x21, 0x00, 0x0a, 0x1d, 0x21, 0x01, 0x28, 0x2b, 0x29, (byte) 0xb2, (byte) (byte) 0xe2, (byte) 0xca
				, (byte) 0xd4, (byte) 0xd1, (byte) 0xf3, (byte) 0xc6, (byte) 0xf8, (byte) 0xa4, (byte) 0xce, (byte) 0xce, (byte) 0xf7, (byte) 0xca, (byte) 0xbd, (byte) 0xcc, (byte) 0xf0, (byte) 0xb5, (byte) (byte) 0xe3, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x1d, 0x21
				, 0x01, 0x20, 0x78, 0x31, 0x1d, 0x21, 0x00, 0x1d, 0x21, 0x01, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x31
				, 0x1d, 0x21, 0x00, 0x0a, 0x1d, 0x21, 0x01, 0x28, 0x2b, 0x29, (byte) 0xb2, (byte) (byte) 0xe2, (byte) 0xca, (byte) 0xd4, (byte) 0xcb, (byte) (byte) 0xe1, (byte) 0xc0, (byte) 0xb1, (byte) 0xc4, (byte) 0xbe, (byte) 0xb6, (byte) 0xfa
				, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x20, 0x78, 0x31, 0x1d, 0x21, 0x00, 0x1d, 0x21
				, 0x01, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x38, 0x1d, 0x21, 0x00, 0x0a, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x20, 0x33, (byte) 0xba, (byte) 0xc5, (byte) 0xc0, (byte) 0xba, (byte) 0xd7, (byte) 0xd3, 0x20, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1d, 0x21, 0x01, (byte) 0xb2, (byte) (byte) 0xe2, (byte) 0xca, (byte) 0xd4, (byte) 0xb2, (byte) 0xcb, (byte) 0xc6, (byte) 0xb7, (byte) 0xc3, (byte) 0xfb, (byte) 0xd7, (byte) 0xd6, (byte) 0xba, (byte) 0xdc
				, (byte) 0xb3, (byte) 0xa4, (byte) 0xba, (byte) 0xdc, (byte) 0xb3, (byte) 0xa4, (byte) 0xba, (byte) 0xdc, (byte) 0xb3, (byte) 0xa4, (byte) 0xba, (byte) 0xdc, (byte) 0xb3, (byte) 0xa4, (byte) 0xba, (byte) 0xdc, (byte) 0xb3, (byte) 0xa4, 0x1d, 0x21, 0x00, 0x0a
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x1d, 0x21, 0x01, 0x20, 0x78, 0x31, 0x1d, 0x21, 0x00, 0x1d, 0x21, 0x01, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01
				, 0x33, 0x30, 0x30, 0x1d, 0x21, 0x00, 0x0a, 0x1d, 0x21, 0x01, (byte) 0xb2, (byte) (byte) 0xe2, (byte) 0xca, (byte) 0xd4, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20
				, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x20, 0x78, 0x31, 0x1d, 0x21
				, 0x00, 0x1d, 0x21, 0x01, 0x1d, 0x21, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x1d, 0x21, 0x01, 0x31, 0x1d, 0x21, 0x00, 0x0a, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x20, (byte) 0xc6, (byte) (byte) 0xe4, (byte) 0xcb, (byte) 0xfb, (byte) 0xb7, (byte) 0xd1, (byte) 0xd3, (byte) 0xc3, 0x20, 0x2d, 0x2d, 0x2d
				, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1d, 0x21, 0x01, (byte) 0xc5, (byte) (byte) 0xe4, (byte) 0xcb, (byte) 0xcd, (byte) 0xb7, (byte) 0xd1, 0x1d, 0x21, 0x00, 0x20
				, 0x20, 0x20, 0x20, 0x20
		};
		return rv;
	}

	public static byte[] getKoubeiData() {
		byte[] rv = new byte[]{
				0x1b, 0x40, 0x1b, 0x61, 0x01, 0x1d, 0x21, 0x11, 0x23, 0x34, (byte) 0xbf, (byte) 0xda, (byte) 0xb1, (byte) 0xae, (byte) 0xcd, (byte) 0xe2,
				(byte) 0xc2, (byte) 0xf4, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x01, 0x1d, 0x21, 0x11, 0x0a, 0x1b, 0x40, 0x1b, 0x61,
				0x01, (byte) 0xb7, (byte) 0xeb, (byte) 0xbc, (byte) 0xc7, (byte) 0xbb, (byte) 0xc6, (byte) 0xec, (byte) 0xcb, (byte) 0xbc, (byte) 0xa6, (byte) 0xc3, (byte) 0xd7, (byte) 0xb7, (byte) 0xb9, 0x0a,
				0x1b, 0x40, 0x1b, 0x61, 0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a,
				0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a,
				0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x11, 0x31, 0x37,
				0x3a, 0x32, 0x30, 0x20, (byte) 0xbe, (byte) 0xa1, (byte) 0xbf, (byte) 0xec, (byte) 0xcb, (byte) 0xcd, (byte) 0xb4, (byte) 0xef, 0x0a, 0x1b, 0x40, 0x1b,
				0x61, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
				0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
				0x2d, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x11, 0x31, 0x38, 0x36, 0x31, 0x30, 0x38,
				0x35, 0x38, 0x33, 0x33, 0x37, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01, (byte) 0xce, (byte) 0xa4, (byte) 0xd0, (byte) 0xa1, (byte) 0xb1,
				(byte) 0xa6, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x11, (byte) 0xb4, (byte) 0xb4, (byte) 0xd6, (byte) 0xc7, (byte) 0xcc, (byte) 0xec,
				(byte) 0xb5, (byte) 0xd8, (byte) 0xb9, (byte) 0xe3, (byte) 0xb3, (byte) 0xa1, 0x37, (byte) 0xba, (byte) 0xc5, (byte) 0xc2, (byte) 0xa5, 0x28, 0x36, 0x30, 0x35, (byte) 0xca,
				(byte) 0xd2, 0x29, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
				0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
				0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x00, 0x1d, 0x21, 0x01,
				(byte) 0xcf, (byte) 0xc2, (byte) 0xb5, (byte) 0xa5, (byte) 0xa3, (byte) 0xba, 0x31, 0x36, 0x3a, 0x33, 0x35, 0x0a, 0x1b, 0x40, 0x1b, 0x61,
				0x00, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a,
				0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a, 0x2a,
				0x2a, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x00, (byte) 0xb2, (byte) 0xcb, (byte) 0xc6, (byte) 0xb7, 0x20, 0x20, 0x20, 0x20, 0x20,
				0x20, 0x20, 0x20, 0x20, 0x20, 0x1b, 0x61, 0x00, (byte) 0xca, (byte) 0xfd, (byte) 0xc1, (byte) 0xbf, 0x1b, 0x61, 0x00, 0x20,
				0x20, (byte) 0xb5, (byte) 0xa5, (byte) 0xbc, (byte) 0xdb, 0x1b, 0x61, 0x00, 0x20, 0x20, (byte) 0xbd, (byte) 0xf0, (byte) 0xb6, (byte) 0xee, 0x0a, 0x1b,
				0x40, 0x1b, 0x61, 0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
				0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
				0x2d, 0x2d, 0x2d, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x00, (byte) 0xbb, (byte) 0xc6, (byte) 0xec, (byte) 0xcb, (byte) 0xce, (byte) 0xe5, (byte) 0xbb,
				(byte) 0xa8, (byte) 0xc8, (byte) 0xe2, (byte) 0xb7, (byte) 0xb9, (byte) 0xa3, (byte) 0xa8, (byte) 0xb4, (byte) 0xf3, (byte) 0xa3, (byte) 0xa9, 0x28, (byte) 0xb2, (byte) 0xbb, (byte) 0xc0, (byte) 0xb1,
				0x29, 0x0a, 0x1b, 0x40, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
				0x20, 0x20, 0x1b, 0x61, 0x00, 0x20, 0x31, 0x20, 0x20, 0x1b, 0x61, 0x00, 0x20, 0x20, 0x20, 0x20,
				0x32, 0x35, 0x1b, 0x61, 0x00, 0x20, 0x20, 0x20, 0x20, 0x32, 0x35, 0x0a, 0x1b, 0x40, 0x1b, 0x61,
				0x00, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
				0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
				0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x00, (byte) 0xc5, (byte) 0xe4, (byte) 0xcb, (byte) 0xcd, (byte) 0xb7, (byte) 0xd1, 0x20, 0x20, 0x20, 0x20,
				0x20, 0x20, 0x20, 0x20, 0x1b, 0x61, 0x00, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
				0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x32, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x00, 0x2d, 0x2d, 0x2d,
				0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
				0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x0a, 0x1b, 0x40, 0x1b,
				0x61, 0x00, 0x1d, 0x21, 0x01, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
				0x20, 0x20, 0x20, 0x20, 0x20, (byte) 0xca, (byte) 0xb5, (byte) 0xb8, (byte) 0xb6, (byte) 0xbd, (byte) 0xf0, (byte) 0xb6, (byte) 0xee, (byte) 0xa3, (byte) 0xba, (byte) 0xa3,
				(byte) 0xa4, 0x32, 0x37, 0x0a, 0x1b, 0x40, 0x0a, 0x1b, 0x40, 0x0a, 0x1b, 0x40, 0x1b, 0x61, 0x01, 0x1d,
				0x21, 0x11, (byte) 0xbf, (byte) 0xda, (byte) 0xb1, (byte) 0xae, (byte) 0xcd, (byte) 0xe2, (byte) 0xc2, (byte) 0xf4, 0x0a, 0x1b, 0x40, 0x0a, 0x1b, 0x40,
				0x1d, 0x56, 0x42, 0x0a, 0x0a
		};
		return rv;
	}
}



