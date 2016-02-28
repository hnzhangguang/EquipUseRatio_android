package com.yonyou.am.mob.tool;

import java.util.HashMap;
import java.util.Map;

import android.graphics.RectF;
/**
 * 柱状图生成矩形对象、获取XY坐标等处理类
 * 只关心坐标等信息，不处理作图
 * 
 * @author shizhsh1
 *
 */
public class CreateRectFHandler {
	//显示柱状图控件的窗口宽度(不包含控件两边的padding)
	private float windowWidth;
	//柱状图控件宽度
	private float viewWidth;
	//柱状图控件高度
	private float viewHeight;
	//柱状图每个柱子高度数组
	private float[] barsHeight;
	//柱状图每个柱子的宽度
	private float barWidth;
	//柱状图横轴各项名称
	private String[] categoryName;
	//生成柱子的个数
	private int num;
	//柱子之间的间隔
	private float barGap;
	//坐标轴底部坐标
	private float axisBottom;
	//第一个与最后一个柱子的前后间隙
	private float padding;
	
	//
	//背景虚线间隔高度,垂直的坐标以此为单元计算
	private float scaleY ;
	//横轴最小间隔
	private float scaleX;
	
	
	

	
	
	
	
	private Map<String,float[]> barsXY;
	//柱状图横坐标名称矩形集合
	private Map<String,float[]> namesBaseXY ;
	//柱状图矩形集合
	private RectF[] barsRectF;
	//完好率百分比数字矩形集合
	private RectF[] percentRectF;
	//触发点击事件效果矩形集合
	private Map<String,Object> clickRectFOrBaseXY;
	
	
	
	
	
	public CreateRectFHandler(){
		
	}
	public CreateRectFHandler(float windowWidth, float viewHeight, 
			float[] barsHeight, String[] categoryName){
		//初始化窗口宽度
		this.windowWidth = windowWidth;
		//初始化控件高度(横轴下文字高度)
		this.viewHeight = viewHeight;
		//初始化纵轴间隔
		this.scaleY = viewHeight/12;
		//初始化横轴间隔
		this.scaleX = windowWidth/5;
		//初始化坐标轴底部高度
		this.axisBottom = 10*scaleY;
		//柱子个数
		this.num = barsHeight.length;
		
		
		//初始化每个柱子的高度
		this.barsHeight = initbarsHeight(barsHeight);
		//初始化每个柱子的宽度
		this.barWidth = windowWidth/10;
		//初始化柱子之间的间隔
		this.barGap = scaleX-barWidth;
		//初始化间隙
		this.padding = barGap/2;
		//初始化名字数组
		this.categoryName = categoryName;
		
		


		//初始化柱子矩形数组
		this.barsRectF = initBarsRectF();
		this.barsXY = initBarsXY();
		//初始化横轴文本X、Y坐标
		this.namesBaseXY = initTextXY();
		//初始化点击柱子后的效果矩形集合和文本坐标
		this.clickRectFOrBaseXY = initRectAfterOnClickBar();
		
		
		
	}
	/**
	 * 初始化柱状图每个柱子的高度
	 * @param barsHeight
	 * @return
	 */
	private float[] initbarsHeight(float[] barsHeight){
		int n = barsHeight.length;
		float[] f_barsHeight=new float[n];
		//转换为柱状图柱子高度
		for(int i = 0;i < n; i++){
			f_barsHeight[i]=barsHeight[i]*scaleY/10;
		}
		return f_barsHeight;
	}
	/**
	 * 初始化柱子的矩形对象数组
	 * @return 矩形对象数组
	 * 画矩形时
	 * drawRect(RectF[i],paint)
	 */
	private RectF[] initBarsRectF(){
		int n = num;
		//创建柱子矩形对象数组
		RectF[] barsRectF = new RectF[n];
		//设置一个自增变量
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
	 * 初始化横轴文本以及柱子顶部文本的X、Y坐标
	 * 一行最多显示3个字一共最多显示2行6个字，若只有一行
	 * 则只取第一行的坐标
	 * 返回值类型Map集合
	 * key：行数横纵坐标名称
	 * value:对应横纵坐标数组
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
		//柱子顶部数值数组
		float[] barTopText_BaseX = new float[n];
		float[] barTopText_BaseY = new float[n];
		//为文本确定坐标值
		for(int i = 0; i < n; i++){
			//坐标轴下方文本坐标
			first_lineBaseX[i] = i*scaleX+scaleX/2;
			second_lineBaseX[i] = i*scaleX+scaleX/2;
			first_lineBaseY[i] = 11*scaleY;
			second_lineBaseY[i] = 12*scaleY-5;
			//柱子顶部文本坐标(X的计算值为居中计算)
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
	 * 初始化柱子被点击后的矩形效果
	 * 背景矩形、顶部矩形，文本坐标
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
