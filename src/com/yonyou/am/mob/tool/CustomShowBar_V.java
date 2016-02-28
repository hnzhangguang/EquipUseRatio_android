package com.yonyou.am.mob.tool;

import java.util.HashMap;
import java.util.Map;

import com.yonyou.am.mob.equipinuseratio.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
/**
 * 自定义垂直柱状图
 * 只关心画图步骤，不进行具体坐标的操作
 * @author shizhsh1
 *
 */
public class CustomShowBar_V extends View{
	
	//虚线路径
	private Path path;
	//路径效果
	private PathEffect effect;
	//
	private CreateRectFHandler chartRectF;
	
	//一个参数
	private float phase;
	//虚线背景画笔
	private Paint backPaint;
	//柱状图画笔
	private Paint barPaint = new Paint();
	//文本画笔
	private Paint textPaint = new Paint();
	//出发点击事件后的画笔
	private Paint clickPaint = new Paint();
	//柱状图整体宽度
	private int viewWidth;
	//柱状图控件高度
	private int viewHeight;
	//屏幕宽度
	private float windowWidth;
	
	//设备类别名称数组
	private String[] categoryName;
	//设备类别的数量
	private int categoryNum = 0;
	//设备类别完好率数组
	private int[] goodRate;
	private boolean touch;
	//柱状图横坐标名称矩阵集合
	private RectF[] nameRectF ;
	//柱状图矩阵集合
	private RectF[] barRectF;
	//完好率百分比数字矩阵集合
	private RectF[] percentRectF;
	//触发点击事件效果矩阵集合
	private RectF[] clickRectF;
	
	//柱子上方文字数组
	private String[] barsTopText;
	//是否点击，默认有点击事件
	private boolean onClick = true;
	//点击柱子的位置，默认为显示第一个
	private int clickIndex = 0;
	private Map<String,String> returnValue = new HashMap<String,String>();
	
	
	
	
	
	public CustomShowBar_V(Context context) {
		super(context);
		
	}
	public CustomShowBar_V(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//初始化路径
		path = new Path();
		//初始化画笔
		backPaint = new Paint();
		//初始化背景虚线效果
		effect = new DashPathEffect(new float[]{5,5},phase);
		
		viewWidth = 800;

		
	     
	}

	public CustomShowBar_V(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
	}
	/**
	 * 开始画图
	 * 
	 * @param canvas
	 */
	protected void onDraw(Canvas canvas){
//		Path backPath = new Path();
//		Paint backPaint = new Paint();
//		backPaint.setColor(Color.GRAY);
//		backPaint.setStyle(Paint.Style.STROKE);
//		backPaint.setStrokeWidth(1);
		
		//设置一个背景为白色的画布
		canvas.drawColor(Color.WHITE);
		if(categoryNum <=5){
			viewWidth = (int)windowWidth;
		}else{
			viewWidth = (int)(windowWidth/5)*categoryNum;
		}
		
		
		//虚线路径
		path.lineTo(viewWidth, 0);
		
		//设置虚线画笔的风格
		backPaint.setColor(Color.GRAY);
		backPaint.setStyle(Paint.Style.STROKE);
		backPaint.setStrokeWidth(1);	
		backPaint.setPathEffect(effect);
		
		
		
		//背景虚线间隔高度
		float gap = viewHeight/12;
		//画布移到0,0处开始绘制
		canvas.translate(0, 0);	
		//循环做出11条虚线
		for(int i=0;i<11;i++){
			if(i==10){
				backPaint.setPathEffect(null);
				canvas.drawPath(path, backPaint);
				canvas.translate(0,gap);
				break;
			}		
			backPaint.setPathEffect(effect);
			canvas.drawPath(path, backPaint);
			canvas.translate(0,gap);
			
		}
		//画矩形柱状图
		drawRect(canvas,gap);
		//绘制横坐标文本
		drawTextX(canvas,gap);
		//如果触发点击事件，画出点击后的效果
		if(onClick == true){
			drawAfterOnClick(canvas,gap);
		}
		
		
		
	}
	
	/**
	 * 点击柱子后画出强调效果图
	 * @param canvas
	 * @param lineGap
	 */
	private void drawAfterOnClick(Canvas canvas,float lineGap){
		clickPaint.setAntiAlias(true);
		barPaint.setStyle(Paint.Style.FILL);
		clickPaint.setColor(getResources().getColor(R.color.clickbar));
		//获取点击后的矩形对象(包括坐标信息)
		Map<String,Object> clickObject = chartRectF.getClickRectFOrBaseXY();
		//点击后的灰色矩形背景效果
		RectF[] clickRects_BackGround = (RectF[])clickObject.get("clickRects_BackGround");
		canvas.drawRect(clickRects_BackGround[clickIndex], clickPaint);
		
		//画底部文字和顶部矩形、原有矩形（同一颜色）
		clickPaint.setColor(getResources().getColor(R.color.barcolor));
		RectF[] rectFs = chartRectF.getBarsRectF();
		RectF[] clickRects_TopTextBackGround = (RectF[])clickObject.get("clickRects_TopTextBackGround");
		canvas.drawRect(rectFs[clickIndex], clickPaint);
		canvas.drawRect(clickRects_TopTextBackGround[clickIndex], clickPaint);
		
		//画顶部点击后显示的文字
		clickPaint.setColor(Color.WHITE);
		clickPaint.setTextSize(50);
		clickPaint.setTextAlign(Align.CENTER);
		float[] click_TopTextX = (float[])clickObject.get("click_TopTextX");
		float[] click_TopTextY = (float[])clickObject.get("click_TopTextY");
		canvas.drawText(barsTopText[clickIndex],click_TopTextX[clickIndex], 
								click_TopTextY[clickIndex], clickPaint);
		
	}

	
	/**
	 * 画矩形柱状图
	 * @param canvas
	 * @param lineGap 虚线之间线间隔
	 */
	private void drawRect(Canvas canvas , float lineGap){
		//画布移到0,0开始画柱状图
		canvas.translate(0, -11*lineGap);

//		//设备类别的数量
//		int num = categoryNum;
//	
//		float barWidth = windowWidth/10;
//		float[] barHeightList ={6*lineGap,7*lineGap,4*lineGap,5*lineGap,8*lineGap,7*lineGap,2*lineGap,4*lineGap};
//		float barGap = windowWidth/5-barWidth;
//		float padding = barGap/2;
//		float bottom = 10*lineGap;
//		
//		//填充
//		barPaint.setStyle(Paint.Style.FILL);
//		barPaint.setColor(getResources().getColor(R.color.barcolor));
//		//设置一个自增变量
//		float barX = padding;
//		
//		for(int i=0;i<num;i++){
//			
//			canvas.drawRect(barX,bottom-barHeightList[i],
//					barX+barWidth,bottom,barPaint);
//			barX =barX+(barWidth+barGap);
//		}
		//填充
		barPaint.setStyle(Paint.Style.FILL);
		barPaint.setColor(getResources().getColor(R.color.barcolor));
		RectF[] rectFs = chartRectF.getBarsRectF();
		for(int i=0;i<rectFs.length;i++){
			
			canvas.drawRect(rectFs[i], barPaint);
		}
	}
	/**
	 * 绘制柱状图控件中的文本
	 * @param canvas
	 * @param lineGap
	 */
	private void drawTextX(Canvas canvas,float lineGap){
		
//		float textWidth = windowWidth/5;
//		float barGap = windowWidth/5-textWidth;
//		float padding = barGap/2;
		textPaint.setAntiAlias(true);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setStrokeWidth(1);
		textPaint.setTextSize(36);
		textPaint.setColor(Color.BLACK);
		//设置文字居中显示
		textPaint.setTextAlign(Align.CENTER);
		if(touch == true){
			textPaint.setColor(Color.RED);
		}
		
		
		Map<String,float[]> textXY = chartRectF.getNamesBaseXY();
		float[] first_lineBaseX = textXY.get("firstLine_BaseX");
		float[] first_lineBaseY = textXY.get("firstLine_BaseY");
		float[] second_lineBaseX = textXY.get("secondLine_BaseX");
		float[] second_lineBaseY = textXY.get("secondLine_BaseY");
		
		//处理类别名称换行
			for(int i=0;i<categoryNum;i++){
				String nameStr = categoryName[i];
				//4个字
				if(nameStr.length() == 4){
					String first_line = nameStr.substring(0, 2);
					String second_line = nameStr.substring(2);
					canvas.drawText(first_line,first_lineBaseX[i],first_lineBaseY[i], textPaint);
					canvas.drawText(second_line, second_lineBaseX[i], second_lineBaseY[i], textPaint);
					continue;
				}
				//5个字
				if(nameStr.length() == 5){
					String first_line = nameStr.substring(0, 3);
					String second_line = nameStr.substring(3);
					canvas.drawText(first_line,first_lineBaseX[i],first_lineBaseY[i], textPaint);
					canvas.drawText(second_line, second_lineBaseX[i], second_lineBaseY[i], textPaint);
					continue;
				}
				if(nameStr.length() == 6){
					String first_line = nameStr.substring(0, 3);
					String second_line = nameStr.substring(3);
					canvas.drawText(first_line,first_lineBaseX[i],first_lineBaseY[i], textPaint);
					canvas.drawText(second_line, second_lineBaseX[i], second_lineBaseY[i], textPaint);
					continue;
				}
				//6个字以上取前六个子显示
				if(nameStr.length() > 6){
					String first_line = nameStr.substring(0, 3);
					String second_line = nameStr.substring(3,6);
					canvas.drawText(first_line,first_lineBaseX[i],first_lineBaseY[i], textPaint);
					canvas.drawText(second_line, second_lineBaseX[i], second_lineBaseY[i], textPaint);
					continue;
				}
				canvas.drawText(categoryName[i], first_lineBaseX[i],first_lineBaseY[i], textPaint);
				
			}
		
		
//		//处理类别名称换行
//		for(int i=0;i<categoryNum;i++){
//			String nameStr = categoryName[i];
//			//4个字
//			if(nameStr.length() == 4){
//				String first_line = nameStr.substring(0, 2);
//				String second_line = nameStr.substring(2);
//				canvas.drawText(first_line, i*textWidth+textWidth/2, 11*lineGap, textPaint);
//				canvas.drawText(second_line, i*textWidth+textWidth/2, 12*lineGap-5, textPaint);
//				continue;
//			}
//			//5个字
//			if(nameStr.length() == 5){
//				String first_line = nameStr.substring(0, 3);
//				String second_line = nameStr.substring(3);
//				canvas.drawText(first_line, i*textWidth+textWidth/2, 11*lineGap, textPaint);
//				canvas.drawText(second_line, i*textWidth+textWidth/2, 12*lineGap-5, textPaint);
//				continue;
//			}
//			if(nameStr.length() == 6){
//				String first_line = nameStr.substring(0, 3);
//				String second_line = nameStr.substring(3);
//				canvas.drawText(first_line, i*textWidth+textWidth/2, 11*lineGap, textPaint);
//				canvas.drawText(second_line, i*textWidth+textWidth/2, 12*lineGap-5, textPaint);
//				continue;
//			}
//			//6个字以上取前六个子显示
//			if(nameStr.length() > 6){
//				String first_line = nameStr.substring(0, 3);
//				String second_line = nameStr.substring(3,6);
//				canvas.drawText(first_line, i*textWidth+textWidth/2, 11*lineGap, textPaint);
//				canvas.drawText(second_line, i*textWidth+textWidth/2, 12*lineGap-5, textPaint);
//				continue;
//			}
//			canvas.drawText(categoryName[i], i*textWidth+textWidth/2, 11*lineGap, textPaint);
//			
//		}
		drawPercentFigure(canvas,lineGap);
		
	}
		
	private void drawPercentFigure(Canvas canvas, float lineGap){
//		float bottom = 10*lineGap;
//		float textWidth = windowWidth/5;
//		float barGap = windowWidth/5-textWidth;
//		float[] barHeightList ={6*lineGap,7*lineGap,4*lineGap,5*lineGap,8*lineGap,7*lineGap,2*lineGap,4*lineGap};
//		String[] percentList = {"50%","70%","40%","50%","80%","70%","20%","40%"};
		textPaint.setStrokeWidth(1);
		textPaint.setTextSize(30);
		Map<String,float[]> textXY = chartRectF.getNamesBaseXY();
		float[] barTopText_BaseX = textXY.get("barTopText_BaseX");
		float[] barTopText_BaseY = textXY.get("barTopText_BaseY");
		//float[] barsTopText = chartRectF.getBarsHeight();
		
		for(int i=0;i<barsTopText.length;i++){
			
			canvas.drawText(barsTopText[i], barTopText_BaseX[i], barTopText_BaseY[i], textPaint);
			
			
		}
//		for(int i=0;i<barsTopText.length;i++){
//			
//			canvas.drawText(barsTopText[i]+"%", i*textWidth+textWidth/2, bottom-barHeightList[i]-4, textPaint);
//			
//			
//		}
	
	}
	
	/**
	 * 定义点击事件，只对柱子所在矩形区域设置点击事件
	 * 
	 */
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();
		//获取每个柱子的坐标值(left、top、right、bottom)
		Map<String,float[]> barsXY = chartRectF.getBarsXY();
		float[] bars_Left = barsXY.get("bars_left");
		float[] bars_Top = barsXY.get("bars_top");
		float[] bars_Right = barsXY.get("bars_right");
		float[] bars_Bottom = barsXY.get("bars_Bottom");
		//按下动作
		if(event.getAction()== MotionEvent.ACTION_DOWN){
			for(int i = 0;i<bars_Left.length;i++){
				if(bars_Left[i] < x && x < bars_Right[i]&&
				   bars_Top[i] < y  && y < bars_Bottom[0]){
					onClick = true;
					clickIndex=i;
					returnValue.put("categoryName", categoryName[i]);
					returnValue.put("goodRate", barsTopText[i]);
					Log.v("-CustomShowBar-", "the event on in_down");
					invalidate();
				}	
			}
		}
		return true;
		
	}
	
	/**
	 * 
	 * 
	 */
	protected void onMeasure(int widthMeasureSpec , int heightMeasureSpec){
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMose = MeasureSpec.getMode(heightMeasureSpec);
		
		int width;
		int height;
		if(widthMode == MeasureSpec.EXACTLY){
			width = widthSize;
			
		}else{
			width =viewWidth;
		}
		
		if(heightMose == MeasureSpec.EXACTLY){
			height = heightSize;
			//柱状图高度取布局高度
			this.viewHeight = heightSize;
		}else{
			height =viewHeight;
		
		}
		setMeasuredDimension(width,height);
		
	}
	
	
	public String[] getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String[] categoryName) {
		this.categoryName = categoryName;
		this.categoryNum = categoryName.length;
	}
	public int[] getGoodRate() {
		return goodRate;
	}
	public void setGoodRate(int[] goodRate) {
		this.goodRate = goodRate;
	}
	public float getWindowWidth() {
		return windowWidth;
		
	}
	public void setWindowWidth(float windowWidth) {
		this.windowWidth = windowWidth;
		//重绘
		this.invalidate();
		//重测
		this.requestLayout();
	}
	public CreateRectFHandler getChartRectF() {
		return chartRectF;
	}
	public void setChartRectF(CreateRectFHandler chartRectF) {
		this.chartRectF = chartRectF;
	}
	public String[] getBarsTopText() {
		return barsTopText;
	}
	public void setBarsTopText(String[] barsTopText) {
		this.barsTopText = barsTopText;
	}
	public Map<String, String> getGetReturnValue() {
		return returnValue;
	}
	public void setGetReturnValue(Map<String, String> returnValue) {
		this.returnValue = returnValue;
	}
	public int getClickIndex() {
		return clickIndex;
	}
	public void setClickIndex(int clickIndex) {
		this.clickIndex = clickIndex;
	}
	
}
