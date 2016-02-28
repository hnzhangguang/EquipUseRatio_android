package com.yonyou.am.mob.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

/**
 * ����־��¼���ļ��У�����Logger.islog = true,(Ĭ�ϴ�)�򿪿���;����Logger.dir =
 * "",�޸��ļ���¼��Ŀ¼(Ĭ��Ϊlog), ��Ŀ¼Ϊ/sdcard/Android/data/(package
 * name)/;����Logger.max_file_count = 3,�������ɵ���־�ļ������������� (ÿ��ֻ����һ����־�ļ�)��
 * 
 * @author hujieh -aa bb vv
 * 
 */

@SuppressLint("SimpleDateFormat")
public class Logger {

	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	private static DateFormat dayFormatter = new SimpleDateFormat("yyyy-MM-dd");
	public static boolean islog = true;
	public static String dir = "log";

	public static int max_file_count = 2;

	/**
	 * ����־��¼���ļ�
	 * 
	 * @param tag
	 *            ��־�Ĺؼ���
	 * @param message
	 *            ��־����
	 */
	public static void info(Context context, String tag, String message) {
		if (islog) {
			long currTime = System.currentTimeMillis();
			Date date = new Date(currTime);
			String time = formatter.format(date);
			String preMessage = "[" + time + "]";
			String realMessage = "\n" + preMessage + tag + "--" + message;
			String fileName = "log-" + dayFormatter.format(date) + ".log";
			saveInFile(context, fileName, realMessage);
		}

	}

	private static void saveInFile(Context context, String fileName, String message) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String path = context.getApplicationContext().getExternalFilesDir(null)
					+ File.separator + dir + File.separator;
			File fileDir = new File(path);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			File file = new File(path, fileName);
			try {
				if (!file.exists()) {
					deleteFile(path);
					file.createNewFile();
				}
				FileWriter writer = new FileWriter(file, true);
				writer.write(message);
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * ɾ������޸�����������ļ�(�������õ��ļ������)
	 * 
	 * @param dir
	 */
	private static void deleteFile(String path) {
		File dir = new File(path);
		if (dir != null) {
			File[] files = dir.listFiles();
			if (files.length >= max_file_count) {
				File teDelFile = getOldestFile(files);
				teDelFile.delete();
				deleteFile(path);
			} else {
				return;
			}
		}
	}

	private static File getOldestFile(File[] files) {
		long time = Long.MAX_VALUE;
		File result = null;
		if (files != null && files.length > 0) {
			for (File file : files) {
				if (file.lastModified() < time) {
					time = file.lastModified();
					result = file;
				}
			}
		}
		return result;
	}
}
