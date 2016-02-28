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
 * ���๦��:AmMobUtils������
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
	 * ��ȡһ������� ��0~100��
	 * 
	 * @return
	 */
	public static String getRandom() {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		return decimalFormat.format(new Random().nextDouble() * 100);
	}

	/**
	 * ��ȡһ������� ��0~100��
	 * 
	 * @param precision
	 *            С��λ
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
	 * �õ���� double�� ����
	 * 
	 * @param size
	 *            ����Ԫ�ظ���
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
	 * �õ���� double�� ����
	 * 
	 * @param size
	 *            ����Ԫ�ظ���
	 * @param precision
	 *            ���ȣ�����С��λ����
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
	 * ��װWebView =�� js���ַ��� eg:"javascript:createChart('bar',[39,78,47,29]);"
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
	 * ��װWebView =�� js���ַ���
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
			// ��������
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
	 * ִ������asc��desc��
	 * 
	 * @param totalLists
	 *            Ҫ�����List
	 * @param order
	 *            ��asc��desc��
	 * @param type
	 *            �����ֶ�����
	 * @param isInteger
	 *            ����������Ƿ������� true=�� ��
	 * @return �ź����List
	 * @author zhangg
	 */
	public static List<Map<String, String>> exeSort(List<Map<String, String>> totalLists, final String order,
			String typeField, boolean isInteger) {
		if (null == totalLists) {
			return new ArrayList<Map<String, String>>();
		}
		// �ַ�����ʱ�ô�
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
		// ��ֵ����ʱ�ô�
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
		// ����ź����Map����
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
		// old˳������ = ��˳������
		return totalDatasList;
	}

	/**
	 ** ִ������asc��desc��
	 * 
	 * @param totalLists
	 *            Ҫ�����List
	 * @param order
	 *            ��asc��desc��
	 * @param type
	 *            �����ֶ�����
	 * @param isDeletePercentFlag
	 *            �Ƿ�ȥ��%
	 * @return �ź����List
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
				// �õ����ǰٷֱȲ���%
				String infoString = tempMap.get(typeField);
				if (infoString.trim().contains("%")) {
					infoString = infoString.substring(0, infoString.length() - 1);
				}
				tempTotalMap.put(Double.valueOf(infoString), tempMap);
			}
			// ����ź����Map����
			List<Map<String, String>> totalDatasList = new ArrayList<Map<String, String>>();

			for (Double key : tempTotalMap.keySet()) {
				totalDatasList.add(tempTotalMap.get(key));
			}
			// tempMap.clear();
			// old˳������ = ��˳������
			return totalDatasList;

		} else {
			return exeSort(totalLists, order, typeField, false);
		}

	}

	/**
	 * ˵�������ݷ��ص�retObj��װJSON��[���Զ��ض��ֶν�������.]
	 * 
	 * @param orderField
	 * @param retObj
	 *            ��ʽ : 1001Z910000000001LVV={material_name=ɨ����001,material_code=
	 *            1070001}
	 * @param typeBoolean
	 *            �Ƿ��Ǳ�ͷ��������Ǳ���
	 * @return { "material_name":"������ʩ����" },{ "material_name":"ɨ����002" }
	 * 
	 *         ���ߣ�zhangg
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
			// �����һ��ʱ����,
			if (i != retObjSize) {
				sBuffer.append(commaMark);
			}
			i++;
		}

		return sBuffer.toString();
	}

	/**
	 * ���ܽ���: ������ �� eg �� name -> "name"
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
	 * ���ܽ���: ������ �� eg �� name -> "name"
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
	 * ���ܽ���: ��� - json�� eg �� name zhangsan - > "name":"zhangsan"
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
	 * ���ܽ���: ��װjson���� eg : pk_wo:001 pk_equip:333333333 ->
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
	 * ˵�������ݷ��ص�retObj��װJSON��
	 * 
	 * @param retObj
	 *            ��ʽ : 1001Z910000000001LVV={material_name=ɨ����001,material_code=
	 *            1070001}
	 * @param typeBoolean
	 *            �Ƿ��Ǳ�ͷ��������Ǳ���
	 * @return
	 * 
	 *         ���ߣ�zhangg
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
			// �����һ��ʱ����,
			if (i != retObjSize) {
				sBuffer.append(commaMark);
			}
			i++;
		}
		return sBuffer.toString();
	}

	/**
	 * ˵���� ��
	 * 
	 * @param mapinfos
	 * @return
	 * 
	 *         ���ߣ�zhangg
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
	 * ���ܽ���: ��װjson���������š� eg : pk_wo:001 pk_equip:333333333 ->
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
	 * ���ܽ���: ��װjson���������š� eg : pk_wo:001 pk_equip:333333333 ->
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
	 * ���ܽ���: У��stringValue �Ƿ����regex��ʽ������ƥ�䡿
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
	 * ���ܽ���: �ѡ�stringValue���еķ���������ʽ��regex���Ĳ����滻ΪreplaceValue
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
	 * ���ܽ���: У��stringValue �Ƿ����regex��ʽ������ƥ�䡿
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
	 * ���ܽ��� �� �пա�
	 * 
	 * @param keyString
	 * @return ���� zhangg
	 */
	@SuppressWarnings("unused")
	private static boolean isNull(String keyString) {
		if (null == keyString || BLANK_STRING.trim().equals(keyString.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * ���ܽ���: ���ļ�-У��JSON����
	 * 
	 * @param file
	 *            ��JSON����
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
	 * ˵������ȡ��ʵ���õ�ֵ,Ŀ���ǹ���null ��
	 * 
	 * @param sourceMap
	 * @param field
	 * @return
	 * 
	 *         ���ߣ�zhangg
	 */
	@SuppressWarnings("rawtypes")
	public static String getUsedValue(Map sourceMap, String field) {
		if (sourceMap.isEmpty() || field == null || null == sourceMap.get(field)) {
			return BLANK_STRING;
		}
		return sourceMap.get(field) + BLANK_STRING;
	}

	/**
	 * ���ܽ���: �ֶο�ֵУ�飨null ��""���沢
	 * 
	 * @param saveMap
	 * @param fields
	 * @return String[]{"pk_group","pk_org","pk_orgs"������}
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
			// ��Ϊnull
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
	 * ���ܽ���: �ֶ�nullֵУ��
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
	 * ���ܽ���: ��pk_group-pk_org-pk_orgs������ ת��Ϊ new
	 * String[]{"pk_group","pk_org","pk_orgs"������}
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
	 * ���ܽ���: �Ժ����⴦����
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
	 * ���ܽ���: �ж�str�����Ƿ񳬹�maxNum
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
				// ������
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
	 * ���ܽ���: �ж�str�Ƿ��Ǻ���
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
	 * �����ʾ��Ϣ
	 * 
	 * @param msg
	 * @return
	 */
	public static String busiExceptionInfo(String msg) {
		return "{\"resultcode\":\"" + "2" + "\",\"resultmsg\":\"" + msg + "\"}";
	}

	/**
	 * �����ʾ��Ϣ
	 * 
	 * @param code
	 * @param msg
	 * @return
	 */
	public static String busiExceptionInfo(String code, String msg) {
		return "{\"resultcode\":\"" + code + "\",\"resultmsg\":\"" + msg + "\"}";
	}

	/**
	 * ��������:����StringBuffer��valueΪnull��ת��Ϊ""
	 * 
	 * @param strArray
	 * @param serviceMap
	 * @return
	 * @author��zhangg
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
	 * ��������:��ȡkey����Ӧ��value,���valueΪnull����"null"ת��Ϊ""
	 * 
	 * @param key
	 * @param serviceMap
	 * @return
	 * @author��zhangg
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
	 * ��������:����nullֵ,����nullʱ������
	 * 
	 * @param strArray
	 *            �����ֶμ�
	 * @param serviceMap
	 *            �ֶμ��Ķ�Ӧvalue��
	 * @return JSON
	 * @author��zhangg
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
	 * ��������:�����ֶμ�����Ӧ�õ�ȡֵmap����JSON
	 * 
	 * @param strArray
	 * @param serviceMap
	 * @return JSON
	 * @author��zhangg
	 */
	@SuppressWarnings("rawtypes")
	public static StringBuffer buildJSON4Fields(String[] strArray, Map serviceMap) {
		StringBuffer sbuf = new StringBuffer();
		if (null == serviceMap || serviceMap.isEmpty() || null == strArray || strArray.length == 0) {
			return sbuf;
		}
		// ��ȡ����ֵ
		Object keyValue = BLANK_STRING;
		for (String field : strArray) {
			sbuf.append(fanMark + field + fanMark);
			sbuf.append(colonMark);
			// �����ʵnull����
			keyValue = (null == serviceMap.get(field) || serviceMap.get(field).toString().trim().equals(nullMark)) ? BLANK_STRING
					: serviceMap.get(field);

			sbuf.append(fanMark + keyValue + fanMark);
			sbuf.append(commanulMark);
		}
		return new StringBuffer(sbuf.substring(0, sbuf.length() - 2));
	}

	/* ����뾶 */
	private final static double EARTH_RADIUS = 6378.137;

	/**
	 * ��������γ�ȣ������������
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
		s = s * 1000; // �������
		return s; // �õ�����
	}

	/**
	 * ���㻡��
	 * 
	 * @param d
	 * @return
	 */
	private static double rad(double d) {
		return d * Math.PI / 180.0; // ���㻡��
	}

	/************************ �� �� **********************/
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
