package com.yonyou.am.mob.tool;

import java.util.HashMap;
import java.util.Map;

import android.graphics.RectF;
/**
 * ��״ͼ���ɾ��ζ��󡢻�ȡXY����ȴ�����
 * ֻ�����������Ϣ����������ͼ
 * 
 * @author shizhsh1
 *
 */
public class CreateRectFHandler {
	//��ʾ��״ͼ�ؼ��Ĵ��ڿ��(�������ؼ����ߵ�padding)
	private float windowWidth;
	//��״ͼ�ؼ����
	private float viewWidth;
	//��״ͼ�ؼ��߶�
	private float viewHeight;
	//��״ͼÿ�����Ӹ߶�����
	private float[] barsHeight;
	//��״ͼÿ�����ӵĿ��
	private float barWidth;
	//��״ͼ�����������
	private String[] categoryName;
	//�������ӵĸ���
	private int num;
	//����֮��ļ��
	private float barGap;
	//������ײ�����
	private float axisBottom;
	//��һ�������һ�����ӵ�ǰ���϶
	private float padding;
	
	//
	//�������߼���߶�,��ֱ�������Դ�Ϊ��Ԫ����
	private float scaleY ;
	//������С���
	private float scaleX;
	
	
	

	
	
	
	
	private Map<String,float[]> barsXY;
	//��״ͼ���������ƾ��μ���
	private Map<String,float[]> namesBaseXY ;
	//��״ͼ���μ���
	private RectF[] barsRectF;
	//����ʰٷֱ����־��μ���
	private RectF[] percentRectF;
	//��������¼�Ч�����μ���
	private Map<String,Object> clickRectFOrBaseXY;
	
	
	
	
	
	public CreateRectFHandler(){
		
	}
	public CreateRectFHandler(float windowWidth, float viewHeight, 
			float[] barsHeight, String[] categoryName){
		//��ʼ�����ڿ��
		this.windowWidth = windowWidth;
		//��ʼ���ؼ��߶�(���������ָ߶�)
		this.viewHeight = viewHeight;
		//��ʼ��������
		this.scaleY = viewHeight/12;
		//��ʼ��������
		this.scaleX = windowWidth/5;
		//��ʼ��������ײ��߶�
		this.axisBottom = 10*scaleY;
		//���Ӹ���
		this.num = barsHeight.length;
		
		
		//��ʼ��ÿ�����ӵĸ߶�
		this.barsHeight = initbarsHeight(barsHeight);
		//��ʼ��ÿ�����ӵĿ��
		this.barWidth = windowWidth/10;
		//��ʼ������֮��ļ��
		this.barGap = scaleX-barWidth;
		//��ʼ����϶
		this.padding = barGap/2;
		//��ʼ����������
		this.categoryName = categoryName;
		
		


		//��ʼ�����Ӿ�������
		this.barsRectF = initBarsRectF();
		this.barsXY = initBarsXY();
		//��ʼ�������ı�X��Y����
		this.namesBaseXY = initTextXY();
		//��ʼ��������Ӻ��Ч�����μ��Ϻ��ı�����
		this.clickRectFOrBaseXY = initRectAfterOnClickBar();
		
		
		
	}
	/**
	 * ��ʼ����״ͼÿ�����ӵĸ߶�
	 * @param barsHeight
	 * @return
	 */
	private float[] initbarsHeight(float[] barsHeight){
		int n = barsHeight.length;
		float[] f_barsHeight=new float[n];
		//ת��Ϊ��״ͼ���Ӹ߶�
		for(int i = 0;i < n; i++){
			f_barsHeight[i]=barsHeight[i]*scaleY/10;
		}
		return f_barsHeight;
	}
	/**
	 * ��ʼ�����ӵľ��ζ�������
	 * @return ���ζ�������
	 * ������ʱ
	 * drawRect(RectF[i],paint)
	 */
	private RectF[] initBarsRectF(){
		int n = num;
		//�������Ӿ��ζ�������
		RectF[] barsRectF = new RectF[n];
		//����һ����������
		float barX = padding;
		for(int i = 0; i < n; i++){
			barsRectF[i]=new RectF(barX,axisBottom-barsHeight[i],
					barX+barWidth,axisBottom);
			barX = barX+(barWidth+barGap);
		}
		return barsRectF;
		
	}
	private Map<String,float[]> initBarsXY(){
		int n = num;
		Map<String,float[]> barsXY = new HashMap<String,float[]>();
		float[] bars_Left = new float[n];
		float[] bars_Top = new float[n];
		float[] bars_Right = new float[n];
		float[] bars_Bottom = new float[n];
		float barX = padding;
		for(int i = 0; i < n; i++){
			bars_Left[i] = barX;
			bars_Top[i] = axisBottom-barsHeight[i];
			bars_Right[i] = barX+barWidth;
			bars_Bottom[i] = axisBottom;
			barX = barX+(barWidth+barGap);
		}
		barsXY.put("bars_left", bars_Left);
		barsXY.put("bars_top",bars_Top );
		barsXY.put("bars_right", bars_Right);
		barsXY.put("bars_Bottom", bars_Bottom);
		
		return barsXY;
	}
	/**
	 * ��ʼ�������ı��Լ����Ӷ����ı���X��Y����
	 * һ�������ʾ3����һ�������ʾ2��6���֣���ֻ��һ��
	 * ��ֻȡ��һ�е�����
	 * ����ֵ����Map����
	 * key������������������
	 * value:��Ӧ������������
	 * 
	 * @return
	 */
	private Map<String,float[]> initTextXY(){
		int n = num;
		Map<String,float[]> namesBaseXY = new HashMap<String,float[]>();
		float[] first_lineBaseX = new float[n];
		float[] second_lineBaseX = new float[n];
		float[] first_lineBaseY = new float[n];
		float[] second_lineBaseY = new float[n];
		//���Ӷ�����ֵ����
		float[] barTopText_BaseX = new float[n];
		float[] barTopText_BaseY = new float[n];
		//Ϊ�ı�ȷ������ֵ
		for(int i = 0; i < n; i++){
			//�������·��ı�����
			first_lineBaseX[i] = i*scaleX+scaleX/2;
			second_lineBaseX[i] = i*scaleX+scaleX/2;
			first_lineBaseY[i] = 11*scaleY;
			second_lineBaseY[i] = 12*scaleY-5;
			//���Ӷ����ı�����(X�ļ���ֵΪ���м���)
			barTopText_BaseX[i] = i*scaleX+scaleX/2;
			barTopText_BaseY[i] = axisBottom-barsHeight[i]-4;
		}
		namesBaseXY.put("firstLine_BaseX", first_lineBaseX);
		namesBaseXY.put("secondLine_BaseX", second_lineBaseX);
		namesBaseXY.put("firstLine_BaseY", first_lineBaseY);
		namesBaseXY.put("secondLine_BaseY", second_lineBaseY);
		namesBaseXY.put("barTopText_BaseX", barTopText_BaseX);
		namesBaseXY.put("barTopText_BaseY", barTopText_BaseY);
		return namesBaseXY;
		
	}
	
	/**
	 * ��ʼ�����ӱ������ľ���Ч��
	 * �������Ρ��������Σ��ı�����
	 * 
	 * @return
	 */
	private Map<String,Object> initRectAfterOnClickBar(){
		int n = num;
		Map<String,Object> onClickBaseXY = new HashMap<String,Object>();
		float barX = padding;
		RectF[] clickRects_BackGround = new RectF[n];
		RectF[] clickRects_TopTextBackGround = new RectF[n];
		float[] click_TopTextX = new float[n];
		float[] click_TopTextY = new float[n];
		
		for(int i = 0; i < n; i++){
			
			clickRects_BackGround[i]=new RectF(barX-barGap/2,axisBottom-barsHeight[i]-scaleY,
					barX+barWidth+barGap/2,axisBottom);
			clickRects_TopTextBackGround[i]=new RectF(barX-barGap/2,axisBottom-barsHeight[i]-3*scaleY,
					barX+barWidth+barGap/2,axisBottom-barsHeight[i]-scaleY);
			click_TopTextX[i] = i*scaleX+scaleX/2;
			click_TopTextY[i] = axisBottom-barsHeight[i]-scaleY-scaleY/2;
			barX = barX+(barWidth+barGap);		
		}
		onClickBaseXY.put("clickRects_BackGround", clickRects_BackGround);
		onClickBaseXY.put("clickRects_TopTextBackGround", clickRects_TopTextBackGround);
		onClickBaseXY.put("click_TopTextX", click_TopTextX);
		onClickBaseXY.put("click_TopTextY", click_TopTextY);
		return onClickBaseXY;
	}

	public Map<String, float[]> getBarsXY() {
		return barsXY;
	}
	public void setBarsXY(Map<String, float[]> barsXY) {
		this.barsXY = barsXY;
	}
	public Map<String, float[]> getNamesBaseXY() {
		return namesBaseXY;
	}
	public void setNamesBaseXY(Map<String, float[]> namesBaseXY) {
		this.namesBaseXY = namesBaseXY;
	}
	public RectF[] getBarsRectF() {
		return barsRectF;
	}
	public void setBarsRectF(RectF[] barsRectF) {
		this.barsRectF = barsRectF;
	}
	public Map<String, Object> getClickRectFOrBaseXY() {
		return clickRectFOrBaseXY;
	}
	public void setClickRectFOrBaseXY(Map<String, Object> clickRectFOrBaseXY) {
		this.clickRectFOrBaseXY = clickRectFOrBaseXY;
	}
	public float[] getBarsHeight() {
		return barsHeight;
	}
	public void setBarsHeight(float[] barsHeight) {
		this.barsHeight = barsHeight;
	}
	
}
