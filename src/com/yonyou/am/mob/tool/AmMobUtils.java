package com.yonyou.am.mob.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 本类功能:AmMobUtils工具类
 * 
 * @author zhangguang
 */
public class AmMobUtils {

	/**
	 * json to map
	 * 
	 * @param json
	 * @return
	 * @author zhangguang
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(JSONObject json) {
		Map map = new HashMap();
		Iterator keys = json.keys();
		try {
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = json.get(key).toString();
				if (value.startsWith("{") && value.endsWith("}")) {
					map.put(key, value.subSequence(1, value.length() - 1));
				} else {
					map.put(key, value);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取一个随机数 （0~100）
	 * 
	 * @return
	 */
	public static String getRandom() {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		return decimalFormat.format(new Random().nextDouble() * 100);
	}

	/**
	 * 获取一个随机数 （0~100）
	 * 
	 * @param precision
	 *            小数位
	 * @return
	 */
	public static String getRandom(int precision) {
		if (precision < 0) {
			return 0 + "";
		}
		if (0 == precision) {
			return Double.valueOf(new Random().nextDouble() * 100 + "").toString().substring(0, 2);
		}
		return Double.valueOf(new Random().nextDouble() * 100 + "").toString().substring(0, 3 + precision);
	}

	/**
	 * 得到随机 double型 数组
	 * 
	 * @param size
	 *            数组元素个数
	 * @return
	 */
	public static double[] getDoubleDatas(int size) {
		double[] doublepub = new double[size];
		for (int i = 0; i < doublepub.length; i++) {
			doublepub[i] = Double.valueOf(AmMobUtils.getRandom());
		}
		return doublepub;
	}

	/**
	 * 得到随机 double型 数组
	 * 
	 * @param size
	 *            数组元素个数
	 * @param precision
	 *            精度（保留小数位数）
	 * @return
	 */
	public static Double[] getDoubleDatas(int size, int precision) {
		Double[] doublepub = new Double[size];
		for (int i = 0; i < doublepub.length; i++) {
			doublepub[i] = Double.valueOf(AmMobUtils.getRandom().toString().substring(0, precision + 3));
		}
		return doublepub;
	}

	/**
	 * 组装WebView =》 js的字符串 eg:"javascript:createChart('bar',[39,78,47,29]);"
	 * 
	 * @param type
	 * @param datas
	 * @return
	 * @author zhangg
	 */
	public static String buildLoadJsString(String type, Double[] datas) {

		StringBuffer sbuf = new StringBuffer();
		sbuf.append("javascript:createChart('");
		sbuf.append(type);
		sbuf.append("',[");
		int size = datas.length;
		for (int i = 0; i < datas.length; i++) {
			sbuf.append(datas[i]);
			if (size - 1 != i) {
				sbuf.append(",");
			}
		}
		sbuf.append("]);");
		return sbuf.toString();
	}

	/**
	 * 组装WebView =》 js的字符串
	 * eg:"javascript:createChart('bar',['A','V','B','T'],[39,78,47,29]);");
	 * 
	 * @param type
	 * @param array
	 * @param datas
	 * @return
	 * @author zhangg
	 */
	public static String buildLoadJsString(String type, String[] array, Double[] datas) {

		StringBuffer sbuf = new StringBuffer();
		if (null != array && null != datas) {
			// 总数据数
			int count = 0;
			if (array.length >= datas.length) {
				count = datas.length;
			} else {
				count = array.length;
			}
			sbuf.append("javascript:createChart('");
			sbuf.append(type);
			sbuf.append("',[");
			for (int i = 0; i < count; i++) {
				sbuf.append("\'");
				sbuf.append(array[i]);
				sbuf.append("\'");
				if (count - 1 != i) {
					sbuf.append(",");
				}
			}
			sbuf.append("],[");
			for (int i = 0; i < count; i++) {
				sbuf.append(datas[i]);
				if (count - 1 != i) {
					sbuf.append(",");
				}
			}
			sbuf.append("]);");
		}
		return sbuf.toString();
	}

	/**
	 * 执行排序【asc、desc】
	 * 
	 * @param totalLists
	 *            要排序的List
	 * @param order
	 *            【asc、desc】
	 * @param type
	 *            按此字段排序
	 * @param isInteger
	 *            排序的类型是否是整型 true=整 型
	 * @return 排好序的List
	 * @author zhangg
	 */
	public static List<Map<String, String>> exeSort(List<Map<String, String>> totalLists, final String order,
			String typeField, boolean isInteger) {
		if (null == totalLists) {
			return new ArrayList<Map<String, String>>();
		}
		// 字符排序时用此
		Map<String, Map<String, String>> tempTotalMap = new TreeMap<String, Map<String, String>>(
				new Comparator<String>() {
					@Override
					public int compare(String string1, String string2) {
						if (order.trim().equals("asc")) {
							return string1.compareTo(string2);
						} else {
							return string2.compareTo(string1);
						}
					}
				});
		// 数值排序时用此
		Map<Double, Map<String, String>> tempTotalIntegerMap = new TreeMap<Double, Map<String, String>>(
				new Comparator<Double>() {
					@Override
					public int compare(Double string1, Double string2) {
						if (order.trim().equals("asc")) {
							return string1.compareTo(string2);
						} else {
							return string2.compareTo(string1);
						}
					}
				});
		Map<String, String> tempMap = null;
		for (int i = 0; i < totalLists.size(); i++) {
			if (null == totalLists.get(i)) {
				continue;
			}
			tempMap = totalLists.get(i);
			if (isInteger) {
				String temp = tempMap.get(typeField).toString().trim();
				if (temp.endsWith("%")) {
					temp = temp.substring(0, temp.length() - 1);
				}
				tempTotalIntegerMap.put(Double.valueOf(temp), tempMap);
			} else {
				tempTotalMap.put(tempMap.get(typeField), tempMap);
			}

		}
		// 存放排好序的Map对象
		List<Map<String, String>> totalDatasList = new ArrayList<Map<String, String>>();
		if (isInteger) {
			for (Double key : tempTotalIntegerMap.keySet()) {
				totalDatasList.add(tempTotalIntegerMap.get(key));
			}
		} else {
			for (String key : tempTotalMap.keySet()) {
				totalDatasList.add(tempTotalMap.get(key));
			}
		}
		// old顺序数据 = 新顺序数据
		return totalDatasList;
	}

	/**
	 ** 执行排序【asc、desc】
	 * 
	 * @param totalLists
	 *            要排序的List
	 * @param order
	 *            【asc、desc】
	 * @param type
	 *            按此字段排序
	 * @param isDeletePercentFlag
	 *            是否去除%
	 * @return 排好序的List
	 * @author zhangg
	 */
	public static List<Map<String, String>> exeSort(List<Map<String, String>> totalLists, final String order,
			String typeField, Boolean isDeletePercentFlag) {
		if (null == totalLists) {
			return new ArrayList<Map<String, String>>();
		}
		if (isDeletePercentFlag) {

			Map<Double, Map<String, String>> tempTotalMap = new TreeMap<Double, Map<String, String>>(
					new Comparator<Double>() {
						@Override
						public int compare(Double string1, Double string2) {
							if (order.trim().equals("asc")) {
								return string1.compareTo(string2);
							} else {
								return string2.compareTo(string1);
							}
						}
					});
			Map<String, String> tempMap = null;
			for (int i = 0; i < totalLists.size(); i++) {
				if (null == totalLists.get(i)) {
					continue;
				}
				tempMap = totalLists.get(i);
				// 得到的是百分比不带%
				String infoString = tempMap.get(typeField);
				if (infoString.trim().contains("%")) {
					infoString = infoString.substring(0, infoString.length() - 1);
				}
				tempTotalMap.put(Double.valueOf(infoString), tempMap);
			}
			// 存放排好序的Map对象
			List<Map<String, String>> totalDatasList = new ArrayList<Map<String, String>>();

			for (Double key : tempTotalMap.keySet()) {
				totalDatasList.add(tempTotalMap.get(key));
			}
			// tempMap.clear();
			// old顺序数据 = 新顺序数据
			return totalDatasList;

		} else {
			return exeSort(totalLists, order, typeField, false);
		}

	}

	/**
	 * 说明：根据返回的retObj组装JSON。[可以对特定字段进行排序.]
	 * 
	 * @param orderField
	 * @param retObj
	 *            形式 : 1001Z910000000001LVV={material_name=扫描码001,material_code=
	 *            1070001}
	 * @param typeBoolean
	 *            是否是表头，否则就是表体
	 * @return { "material_name":"基础设施物料" },{ "material_name":"扫描码002" }
	 * 
	 *         作者：zhangg
	 */
	@SuppressWarnings("unchecked")
	public String retObjForLoop(String orderField, Map<String, Object> retObj, Boolean typeBoolean) {
		if (retObj.isEmpty()) {
			return new String();
		}
		if (typeBoolean) {
			return buildJsonBufferByBigArea(retObj).toString();
		}
		Map<String, Object> tempretObjMap = new TreeMap<String, Object>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		Map<String, Object> headmaps;
		for (String key : retObj.keySet()) {
			headmaps = (Map<String, Object>) retObj.get(key);
			tempretObjMap.put(headmaps.get(orderField) + key, headmaps);
		}

		int i = 1, retObjSize = tempretObjMap.size();
		StringBuffer sBuffer = new StringBuffer();
		for (String key : tempretObjMap.keySet()) {
			headmaps = (Map<String, Object>) tempretObjMap.get(key);
			if (null == headmaps || headmaps.isEmpty()) {
				continue;
			}
			sBuffer.append(buildJsonBufferByBigArea(headmaps));
			// 是最后一组时不加,
			if (i != retObjSize) {
				sBuffer.append(commaMark);
			}
			i++;
		}

		return sBuffer.toString();
	}

	/**
	 * 功能介绍: 加引号 。 eg ： name -> "name"
	 * 
	 * @param string
	 * @return
	 * 
	 *         author zhangg
	 */
	private static String buildSingleJson(String string) {
		if (string == null || nullMark.equals(string)) {
			return fanMark + fanMark;
		}
		return fanMark + string + fanMark;
	}

	/**
	 * 功能介绍: 加引号 。 eg ： name -> "name"
	 * 
	 * @param string
	 * @return
	 * 
	 *         author zhangg
	 */
	private static String buildSingleJson(Object string) {
		if (string == null || nullMark.equals(string)) {
			return fanMark + fanMark;
		}
		return fanMark + string + fanMark;
	}

	/**
	 * 功能介绍: 组对 - json。 eg ： name zhangsan - > "name":"zhangsan"
	 * 
	 * @param keyString
	 * @param valueString
	 * @return
	 * 
	 *         author zhangg
	 */
	private static String buildDoubleString(String keyString, Object valueString) {

		if (null == keyString) {
			return new StringBuffer().toString();
		}

		StringBuffer sufBuffer = new StringBuffer();
		sufBuffer.append(buildSingleJson(keyString));
		sufBuffer.append(colonMark);
		sufBuffer.append(buildSingleJson(valueString));

		return sufBuffer.toString();

	}

	/**
	 * 功能介绍: 组装json串。 eg : pk_wo:001 pk_equip:333333333 ->
	 * "pk_wo":"001","pk_equip":"333333333"
	 * 
	 * @param mapinfos
	 * @return
	 * 
	 *         author zhangg
	 */
	public static String buildJsonBuffer(Map<String, Object> mapinfos) {

		if (null == mapinfos || mapinfos.isEmpty()) {
			return BLANK_STRING;
		}
		StringBuffer sbufBuffer = new StringBuffer();
		for (String keyString : mapinfos.keySet()) {
			sbufBuffer.append(buildDoubleString(keyString, mapinfos.get(keyString)));
			sbufBuffer.append(commaMark);
		}
		if (sbufBuffer.length() != 0) {
			sbufBuffer = new StringBuffer(sbufBuffer.subSequence(0, sbufBuffer.length() - 1));
			return sbufBuffer.toString();
		}
		return sbufBuffer.toString();
	}

	/**
	 * 说明：根据返回的retObj组装JSON。
	 * 
	 * @param retObj
	 *            形式 : 1001Z910000000001LVV={material_name=扫描码001,material_code=
	 *            1070001}
	 * @param typeBoolean
	 *            是否是表头，否则就是表体
	 * @return
	 * 
	 *         作者：zhangg
	 */
	@SuppressWarnings("unchecked")
	public String retObjForLoop(Map<String, Object> retObj, Boolean typeBoolean) {
		if (retObj.isEmpty()) {
			return BLANK_STRING;
		}

		if (typeBoolean) {
			return buildJsonBufferByBigArea(retObj).toString();
		}

		Map<String, Object> headmaps;
		int i = 1, retObjSize = retObj.size();
		StringBuffer sBuffer = new StringBuffer();
		for (String key : retObj.keySet()) {
			headmaps = (Map<String, Object>) retObj.get(key);
			if (null == headmaps || headmaps.isEmpty()) {
				continue;
			}
			sBuffer.append(buildJsonBufferByBigArea(headmaps));
			// 是最后一组时不加,
			if (i != retObjSize) {
				sBuffer.append(commaMark);
			}
			i++;
		}
		return sBuffer.toString();
	}

	/**
	 * 说明： 。
	 * 
	 * @param mapinfos
	 * @return
	 * 
	 *         作者：zhangg
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String buildJsonBufferDelHeadKey(Map<String, Object> mapinfos) {

		if (null == mapinfos || mapinfos.isEmpty()) {
			return BLANK_STRING;
		}
		StringBuffer sbufBuffer = new StringBuffer();
		for (String keyString : mapinfos.keySet()) {
			sbufBuffer.append(leftBigMark);
			sbufBuffer.append(buildJsonBuffer((Map) mapinfos.get(keyString)));
			sbufBuffer.append(rightBigMark);
			sbufBuffer.append(commaMark);
		}
		if (sbufBuffer.length() != 0) {
			sbufBuffer = new StringBuffer(sbufBuffer.subSequence(0, sbufBuffer.length() - 1));
			return sbufBuffer.toString();
		}
		return sbufBuffer.toString();
	}

	/**
	 * 功能介绍: 组装json串带大括号。 eg : pk_wo:001 pk_equip:333333333 ->
	 * {"pk_wo":"001","pk_equip":"333333333"}
	 * 
	 * @param mapinfos
	 * @return
	 * 
	 *         author zhangg
	 */
	public static String buildJsonBufferByCenterArea(Map<String, Object> mapinfos) {

		if (null == mapinfos || mapinfos.isEmpty()) {
			return BLANK_STRING;
		}
		StringBuffer sbufBuffer = new StringBuffer();
		sbufBuffer.append(leftcenterMark);
		sbufBuffer.append(buildJsonBuffer(mapinfos));
		sbufBuffer.append(rightcenterMark);
		return sbufBuffer.toString();
	}

	/**
	 * 功能介绍: 组装json串带大括号。 eg : pk_wo:001 pk_equip:333333333 ->
	 * {"pk_wo":"001","pk_equip":"333333333"}
	 * 
	 * @param mapinfos
	 * @return
	 * 
	 *         author zhangg
	 */
	public static String buildJsonBufferByBigArea(Map<String, Object> mapinfos) {

		if (null == mapinfos || mapinfos.isEmpty()) {
			return BLANK_STRING;
		}
		StringBuffer sbufBuffer = new StringBuffer();
		sbufBuffer.append(leftBigMark);
		sbufBuffer.append(buildJsonBuffer(mapinfos));
		sbufBuffer.append(rightBigMark);
		return sbufBuffer.toString();
	}

	/**
	 * 功能介绍: 校验stringValue 是否符合regex格式【部分匹配】
	 * 
	 * @param stringValue
	 * @param regex
	 * @return
	 * @author: zhangg
	 */
	public static boolean validatorByRegexEx(String stringValue, String regex) {

		if (regex.length() == 0) {
			return false;
		}
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(stringValue);
		if (m.find()) {
			return true;
		}
		return false;

	}

	/**
	 * 功能介绍: 把【stringValue】中的符合正则表达式【regex】的部分替换为replaceValue
	 * 
	 * @param stringValue
	 * @param regex
	 * @param replaceValue
	 * @return
	 * @author: zhangg
	 */
	public static String replaceStrByRegex(String stringValue, String regex, String replaceValue) {

		if (null == stringValue || null == regex || stringValue.length() == 0 || regex.length() == 0) {
			return stringValue;
		}
		return stringValue.replaceAll(regex, replaceValue);
	}

	/**
	 * 功能介绍: 校验stringValue 是否符合regex格式【整体匹配】
	 * 
	 * @param stringValue
	 * @param regex
	 * @return
	 * @author: zhangg
	 */
	public static boolean validatorByRegex(String stringValue, String regex) {

		if (regex.length() == 0) {
			return false;
		}
		Pattern p = Pattern.compile(regex);
		return p.matcher(stringValue).matches();

	}

	/**
	 * 功能介绍 ： 判空。
	 * 
	 * @param keyString
	 * @return 作者 zhangg
	 */
	@SuppressWarnings("unused")
	private static boolean isNull(String keyString) {
		if (null == keyString || BLANK_STRING.trim().equals(keyString.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 功能介绍: 读文件-校验JSON串用
	 * 
	 * @param file
	 *            【JSON串】
	 * @return
	 * @throws IOException
	 * @author: zhangg
	 */
	public static String readFile(File file) throws IOException {
		String retStr = BLANK_STRING;
		StringBuffer sb = new StringBuffer();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
			String tempStr = BLANK_STRING;
			while ((tempStr = bufferedReader.readLine()) != null) {
				sb.append(tempStr);
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
				bufferedReader = null;
			}
		}
		retStr = sb.toString();
		return retStr;
	}

	/**
	 * 说明：获取真实可用的值,目的是过滤null 。
	 * 
	 * @param sourceMap
	 * @param field
	 * @return
	 * 
	 *         作者：zhangg
	 */
	@SuppressWarnings("rawtypes")
	public static String getUsedValue(Map sourceMap, String field) {
		if (sourceMap.isEmpty() || field == null || null == sourceMap.get(field)) {
			return BLANK_STRING;
		}
		return sourceMap.get(field) + BLANK_STRING;
	}

	/**
	 * 功能介绍: 字段空值校验（null 、""）兼并
	 * 
	 * @param saveMap
	 * @param fields
	 * @return String[]{"pk_group","pk_org","pk_orgs"···}
	 * @throws JSONException
	 * @author: zhangg
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String[] validateNull(Map map, String[] fields) throws JSONException {

		StringBuffer sbuf = new StringBuffer();
		if (null == map || map.isEmpty() || fields.length == 0) {
			return new String[] {};
		}
		for (String field : fields) {
			// 若为null
			if (validateNull(map, field)) {
				sbuf.append(field + "-");
			}
		}

		if (sbuf.length() == 0) {
			return new String[] {};
		}

		String[] strArrays = analyseSbuf4StrArrs(sbuf);
		return strArrays;
	}

	/**
	 * 功能介绍: 字段null值校验
	 * 
	 * @param saveMap
	 * @param field
	 * @return
	 * @throws JSONException
	 * @author: zhangg
	 */
	@SuppressWarnings({ "rawtypes" })
	public static boolean validateNull(Map saveMap, String field) throws JSONException {

		if (null == saveMap || saveMap.isEmpty() || field.equals(BLANK_STRING)) {
			return false;
		}
		Object obj = saveMap.get(field);
		if (null == obj || obj.equals(BLANK_STRING)) {
			return true;
		}

		return false;
	}

	/**
	 * 功能介绍: 将pk_group-pk_org-pk_orgs··· 转换为 new
	 * String[]{"pk_group","pk_org","pk_orgs"···}
	 * 
	 * @param sbuf
	 * @return
	 * @author: zhangg
	 */
	public static String[] analyseSbuf4StrArrs(StringBuffer sbuf) {
		if (null == sbuf || 0 == sbuf.length()) {
			return new String[] {};
		}
		String[] strArrs = sbuf.toString().split("-");
		return strArrs;
	}

	/**
	 * 功能介绍: 以后特殊处理用
	 * 
	 * @param parmKeys
	 * @param saveMap
	 * @param parmMap
	 * @param headMap
	 * @param flag
	 * @author: zhangg
	 */
	@SuppressWarnings("rawtypes")
	public static void dealWithParmMap(String[] parmKeys, Map saveMap, Map<String, Object> parmMap,
			Map<String, String> headMap, boolean flag) {

	}

	/**
	 * 功能介绍: 判断str长短是否超过maxNum
	 * 
	 * @param str
	 * @param maxNum
	 * @return boolean
	 * @author: zhangg
	 */
	public static boolean isExMaxLength(String str, int maxNum) {
		if (str != null && !str.equals(BLANK_STRING)) {
			int i = 0;
			char[] c1 = str.toCharArray();
			for (char c : c1) {
				// 是中文
				if (isChineseChar(c + BLANK_STRING)) {
					i += 3;
				} else {
					i += 1;
				}
			}
			if (i > maxNum) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 功能介绍: 判断str是否是汉字
	 * 
	 * @param str
	 * @return
	 * @author: zhangg
	 */
	public static boolean isChineseChar(String str) {
		boolean temp = false;
		if (str.matches("[\u4e00-\u9fa5]+")) {
			return true;
		}
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}

	/**
	 * 输出提示信息
	 * 
	 * @param msg
	 * @return
	 */
	public static String busiExceptionInfo(String msg) {
		return "{\"resultcode\":\"" + "2" + "\",\"resultmsg\":\"" + msg + "\"}";
	}

	/**
	 * 输出提示信息
	 * 
	 * @param code
	 * @param msg
	 * @return
	 */
	public static String busiExceptionInfo(String code, String msg) {
		return "{\"resultcode\":\"" + code + "\",\"resultmsg\":\"" + msg + "\"}";
	}

	/**
	 * 函数功能:构建StringBuffer若value为null则转化为""
	 * 
	 * @param strArray
	 * @param serviceMap
	 * @return
	 * @author：zhangg
	 */
	@SuppressWarnings("rawtypes")
	public static String buildJSON4Fields4Qmarks(String[] strArray, Map serviceMap) {

		if (null == strArray || strArray.length == 0 || null == serviceMap || serviceMap.isEmpty()) {
			return BLANK_STRING;
		}
		StringBuffer sbuf = new StringBuffer();
		for (String field : strArray) {
			sbuf.append(fanMark + field + fanMark);
			sbuf.append(colonMark);
			sbuf.append(fanMark + getValue4Key(field, serviceMap) + fanMark);
			sbuf.append(commanulMark);
		}
		return new StringBuffer(sbuf.substring(0, sbuf.length() - 2)).toString();
	}

	/**
	 * 函数功能:获取key所对应的value,如果value为null或者"null"转化为""
	 * 
	 * @param key
	 * @param serviceMap
	 * @return
	 * @author：zhangg
	 */
	@SuppressWarnings("rawtypes")
	public static Object getValue4Key(String key, Map serviceMap) {
		if (serviceMap.get(key) == null || (serviceMap.get(key) + BLANK_STRING).equals(BLANK_STRING)
				|| (serviceMap.get(key) + BLANK_STRING).equals(nullMark)) {
			return BLANK_STRING;
		}
		return serviceMap.get(key);
	}

	/**
	 * 函数功能:忽略null值,碰到null时不处理
	 * 
	 * @param strArray
	 *            构造字段集
	 * @param serviceMap
	 *            字段集的对应value集
	 * @return JSON
	 * @author：zhangg
	 */
	@SuppressWarnings("rawtypes")
	public static String buildJSON4FieldsIgnoreNULL(String[] strArray, Map serviceMap) {

		if (null == serviceMap || serviceMap.isEmpty() || null == strArray || strArray.length == 0) {
			return BLANK_STRING;
		}
		StringBuffer sbuf = new StringBuffer();
		for (String field : strArray) {
			if (null != serviceMap.get(field) && !serviceMap.get(field).equals(nullMark)) {
				sbuf.append(fanMark + field + fanMark);
				sbuf.append(colonMark);
				sbuf.append(fanMark + serviceMap.get(field) + fanMark);
				sbuf.append(commanulMark);
			}
		}
		if (sbuf.length() == 0) {
			return sbuf.toString();
		}
		return new StringBuffer(sbuf.substring(0, sbuf.length() - 2)).toString();
	}

	/**
	 * 函数功能:根据字段集及所应用的取值map构造JSON
	 * 
	 * @param strArray
	 * @param serviceMap
	 * @return JSON
	 * @author：zhangg
	 */
	@SuppressWarnings("rawtypes")
	public static StringBuffer buildJSON4Fields(String[] strArray, Map serviceMap) {
		StringBuffer sbuf = new StringBuffer();
		if (null == serviceMap || serviceMap.isEmpty() || null == strArray || strArray.length == 0) {
			return sbuf;
		}
		// 获取到的值
		Object keyValue = BLANK_STRING;
		for (String field : strArray) {
			sbuf.append(fanMark + field + fanMark);
			sbuf.append(colonMark);
			// 解决现实null问题
			keyValue = (null == serviceMap.get(field) || serviceMap.get(field).toString().trim().equals(nullMark)) ? BLANK_STRING
					: serviceMap.get(field);

			sbuf.append(fanMark + keyValue + fanMark);
			sbuf.append(commanulMark);
		}
		return new StringBuffer(sbuf.substring(0, sbuf.length() - 2));
	}

	/* 地球半径 */
	private final static double EARTH_RADIUS = 6378.137;

	/**
	 * 传入两经纬度，计算两点距离
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @return
	 */
	public static double getShortestDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = s * 1000; // 换算成米
		return s; // 得到米数
	}

	/**
	 * 计算弧长
	 * 
	 * @param d
	 * @return
	 */
	private static double rad(double d) {
		return d * Math.PI / 180.0; // 计算弧长
	}

	/************************ 符 号 **********************/
	public static final String fanMark = "\"";
	public static final String colonMark = ":";
	public static final String commaMark = ",";
	public static final String commanulMark = ", ";
	public static final String leftcenterMark = "[ ";
	public static final String rightcenterMark = " ]";
	public static final String leftMark = "{ ";
	public static final String rightMark = " }";
	public static final String leftBigMark = "{ ";
	public static final String rightBigMark = " }";
	public static final String nodeMark = ".";
	public static final String hengxianMark = "-";
	public static final String BLANK_STRING = "";
	public static final String nullMark = "null";
	public static final String MSG = "msg";
	public static final String wrapMark = "\n";
	public static final String newLineMark = "\n";
	public static final String singleQuoteMark = "\"";
	public static final String leftrightcenterMark = "[]";
	public static final String controller_msg_nc = "controller_msg_nc";
	public static final String controller_msg_ma = "controller_msg_ma";
	public static final String NC_NOT_INTERFACE_msg = "com.yonyou.uap.um.gateway.exception.GatewayServiceException: nc.bs.framework.exception.ComponentNotFoundException";

}
