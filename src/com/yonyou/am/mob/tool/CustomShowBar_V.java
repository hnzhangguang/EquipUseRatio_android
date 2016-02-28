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
 * �Զ��崹ֱ��״ͼ
 * ֻ���Ļ�ͼ���裬�����о�������Ĳ���
 * @author shizhsh1
 *
 */
public class CustomShowBar_V extends View{
	
	//����·��
	private Path path;
	//·��Ч��
	private PathEffect effect;
	//
	private CreateRectFHandler chartRectF;
	
	//һ������
	private float phase;
	//���߱�������
	private Paint backPaint;
	//��״ͼ����
	private Paint barPaint = new Paint();
	//�ı�����
	private Paint textPaint = new Paint();
	//��������¼���Ļ���
	private Paint clickPaint = new Paint();
	//��״ͼ������
	private int viewWidth;
	//��״ͼ�ؼ��߶�
	private int viewHeight;
	//��Ļ���
	private float windowWidth;
	
	//�豸�����������
	private String[] categoryName;
	//�豸��������
	private int categoryNum = 0;
	//�豸������������
	private int[] goodRate;
	private boolean touch;
	//��״ͼ���������ƾ��󼯺�
	private RectF[] nameRectF ;
	//��״ͼ���󼯺�
	private RectF[] barRectF;
	//����ʰٷֱ����־��󼯺�
	private RectF[] percentRectF;
	//��������¼�Ч�����󼯺�
	private RectF[] clickRectF;
	
	//�����Ϸ���������
	private String[] barsTopText;
	//�Ƿ�����Ĭ���е���¼�
	private boolean onClick = true;
	//������ӵ�λ�ã�Ĭ��Ϊ��ʾ��һ��
	private int clickIndex = 0;
	private Map<String,String> returnValue = new HashMap<String,String>();
	
	
	
	
	
	public CustomShowBar_V(Context context) {
		super(context);
		
	}
	public CustomShowBar_V(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//��ʼ��·��
		path = new Path();
		//��ʼ������
		backPaint = new Paint();
		//��ʼ����������Ч��
		effect = new DashPathEffect(new float[]{5,5},phase);
		
		viewWidth = 800;

		
	     
	}

	public CustomShowBar_V(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
	}
	/**
	 * ��ʼ��ͼ
	 * 
	 * @param canvas
	 */
	protected void onDraw(Canvas canvas){
//		Path backPath = new Path();
//		Paint backPaint = new Paint();
//		backPaint.setColor(Color.GRAY);
//		backPaint.setStyle(Paint.Style.STROKE);
//		backPaint.setStrokeWidth(1);
		
		//����һ������Ϊ��ɫ�Ļ���
		canvas.drawColor(Color.WHITE);
		if(categoryNum <=5){
			viewWidth = (int)windowWidth;
		}else{
			viewWidth = (int)(windowWidth/5)*categoryNum;
		}
		
		
		//����·��
		path.lineTo(viewWidth, 0);
		
		//�������߻��ʵķ��
		backPaint.setColor(Color.GRAY);
		backPaint.setStyle(Paint.Style.STROKE);
		backPaint.setStrokeWidth(1);	
		backPaint.setPathEffect(effect);
		
		
		
		//�������߼���߶�
		float gap = viewHeight/12;
		//�����Ƶ�0,0����ʼ����
		canvas.translate(0, 0);	
		//ѭ������11������
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
		//��������״ͼ
		drawRect(canvas,gap);
		//���ƺ������ı�
		drawTextX(canvas,gap);
		//�����������¼�������������Ч��
		if(onClick == true){
			drawAfterOnClick(canvas,gap);
		}
		
		
		
	}
	
	/**
	 * ������Ӻ󻭳�ǿ��Ч��ͼ
	 * @param canvas
	 * @param lineGap
	 */
	private void drawAfterOnClick(Canvas canvas,float lineGap){
		clickPaint.setAntiAlias(true);
		barPaint.setStyle(Paint.Style.FILL);
		clickPaint.setColor(getResources().getColor(R.color.clickbar));
		//��ȡ�����ľ��ζ���(����������Ϣ)
		Map<String,Object> clickObject = chartRectF.getClickRectFOrBaseXY();
		//�����Ļ�ɫ���α���Ч��
		RectF[] clickRects_BackGround = (RectF[])clickObject.get("clickRects_BackGround");
		canvas.drawRect(clickRects_BackGround[clickIndex], clickPaint);
		
		//���ײ����ֺͶ������Ρ�ԭ�о��Σ�ͬһ��ɫ��
		clickPaint.setColor(getResources().getColor(R.color.barcolor));
		RectF[] rectFs = chartRectF.getBarsRectF();
		RectF[] clickRects_TopTextBackGround = (RectF[])clickObject.get("clickRects_TopTextBackGround");
		canvas.drawRect(rectFs[clickIndex], clickPaint);
		canvas.drawRect(clickRects_TopTextBackGround[clickIndex], clickPaint);
		
		//�������������ʾ������
		clickPaint.setColor(Color.WHITE);
		clickPaint.setTextSize(50);
		clickPaint.setTextAlign(Align.CENTER);
		float[] click_TopTextX = (float[])clickObject.get("click_TopTextX");
		float[] click_TopTextY = (float[])clickObject.get("click_TopTextY");
		canvas.drawText(barsTopText[clickIndex],click_TopTextX[clickIndex], 
								click_TopTextY[clickIndex], clickPaint);
		
	}

	
	/**
	 * ��������״ͼ
	 * @param canvas
	 * @param lineGap ����֮���߼��
	 */
	private void drawRect(Canvas canvas , float lineGap){
		//�����Ƶ�0,0��ʼ����״ͼ
		canvas.translate(0, -11*lineGap);

//		//�豸��������
//		int num = categoryNum;
//	
//		float barWidth = windowWidth/10;
//		float[] barHeightList ={6*lineGap,7*lineGap,4*lineGap,5*lineGap,8*lineGap,7*lineGap,2*lineGap,4*lineGap};
//		float barGap = windowWidth/5-barWidth;
//		float padding = barGap/2;
//		float bottom = 10*lineGap;
//		
//		//���
//		barPaint.setStyle(Paint.Style.FILL);
//		barPaint.setColor(getResources().getColor(R.color.barcolor));
//		//����һ����������
//		float barX = padding;
//		
//		for(int i=0;i<num;i++){
//			
//			canvas.drawRect(barX,bottom-barHeightList[i],
//					barX+barWidth,bottom,barPaint);
//			barX =barX+(barWidth+barGap);
//		}
		//���
		barPaint.setStyle(Paint.Style.FILL);
		barPaint.setColor(getResources().getColor(R.color.barcolor));
		RectF[] rectFs = chartRectF.getBarsRectF();
		for(int i=0;i<rectFs.length;i++){
			
			canvas.drawRect(rectFs[i], barPaint);
		}
	}
	/**
	 * ������״ͼ�ؼ��е��ı�
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
		//�������־�����ʾ
		textPaint.setTextAlign(Align.CENTER);
		if(touch == true){
			textPaint.setColor(Color.RED);
		}
		
		
		Map<String,float[]> textXY = chartRectF.getNamesBaseXY();
		float[] first_lineBaseX = textXY.get("firstLine_BaseX");
		float[] first_lineBaseY = textXY.get("firstLine_BaseY");
		float[] second_lineBaseX = textXY.get("secondLine_BaseX");
		float[] second_lineBaseY = textXY.get("secondLine_BaseY");
		
		//����������ƻ���
			for(int i=0;i<categoryNum;i++){
				String nameStr = categoryName[i];
				//4����
				if(nameStr.length() == 4){
					String first_line = nameStr.substring(0, 2);
					String second_line = nameStr.substring(2);
					canvas.drawText(first_line,first_lineBaseX[i],first_lineBaseY[i], textPaint);
					canvas.drawText(second_line, second_lineBaseX[i], second_lineBaseY[i], textPaint);
					continue;
				}
				//5����
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
				//6��������ȡǰ��������ʾ
				if(nameStr.length() > 6){
					String first_line = nameStr.substring(0, 3);
					String second_line = nameStr.substring(3,6);
					canvas.drawText(first_line,first_lineBaseX[i],first_lineBaseY[i], textPaint);
					canvas.drawText(second_line, second_lineBaseX[i], second_lineBaseY[i], textPaint);
					continue;
				}
				canvas.drawText(categoryName[i], first_lineBaseX[i],first_lineBaseY[i], textPaint);
				
			}
		
		
//		//����������ƻ���
//		for(int i=0;i<categoryNum;i++){
//			String nameStr = categoryName[i];
//			//4����
//			if(nameStr.length() == 4){
//				String first_line = nameStr.substring(0, 2);
//				String second_line = nameStr.substring(2);
//				canvas.drawText(first_line, i*textWidth+textWidth/2, 11*lineGap, textPaint);
//				canvas.drawText(second_line, i*textWidth+textWidth/2, 12*lineGap-5, textPaint);
//				continue;
//			}
//			//5����
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
//			//6��������ȡǰ��������ʾ
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
	 * �������¼���ֻ���������ھ����������õ���¼�
	 * 
	 */
	public boolean onTouchEvent(MotionEvent event){
		float x = event.getX();
		float y = event.getY();
		//��ȡÿ�����ӵ�����ֵ(left��top��right��bottom)
		Map<String,float[]> barsXY = chartRectF.getBarsXY();
		float[] bars_Left = barsXY.get("bars_left");
		float[] bars_Top = barsXY.get("bars_top");
		float[] bars_Right = barsXY.get("bars_right");
		float[] bars_Bottom = barsXY.get("bars_Bottom");
		//���¶���
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
			//��״ͼ�߶�ȡ���ָ߶�
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
		//�ػ�
		this.invalidate();
		//�ز�
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
