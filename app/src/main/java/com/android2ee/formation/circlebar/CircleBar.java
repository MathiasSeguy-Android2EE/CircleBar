/**<ul>
 * <li>POCEkito</li>
 * <li>com.android2ee.expertise.ekito.myconso.poc.ekito</li>
 * <li>22 déc. 2013</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage except training and can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.android2ee.formation.circlebar;

import com.android2ee.expertise.ekito.myconso.poc.ekito.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class CircleBar extends View {

	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public CircleBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		finishInitialization();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CircleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		finishInitialization();
	}

	/**
	 * @param context
	 */
	public CircleBar(Context context) {
		super(context);
		finishInitialization();
	}

	/**
	 * 
	 */
	private void finishInitialization() {
		// Define how to draw text
		paintUsual.setTextSize(20);
		paintUsual.setTextAlign(Paint.Align.CENTER);
		paintUsual.setStyle(Paint.Style.FILL_AND_STROKE);
		paintUsual.setAntiAlias(true);
		mFontMetrics = paintUsual.getFontMetrics();
		fontHeight = mFontMetrics.ascent + mFontMetrics.descent;
		paintCircleBackground.setStyle(Paint.Style.FILL_AND_STROKE);
		paintCircleBackground.setAntiAlias(true);
		//Launch the shining animation
		launchShining() ;
	}

	/******************************************************************************************/
	/** Managing Drawing **************************************************************************/
	/******************************************************************************************/
	/**
	 * Weight,Heigth, Center X, center Y,
	 */
	int w, h, cx, cy;
	/**
	 * View initialization is done
	 */
	boolean initialized = false;

	/**
	 * The distance between the center of the view and the inner circle
	 */
	private float circleInnerRadius = 0;
	/**
	 * The distance between the center of the view and the outer circle
	 */
	private float circleOuterRadius = 0;
	/**
	 * The background to draw
	 */
	RectF backgroundRect = new RectF(-cx, -cy, cx, cy);
	/******************************************************************************************/
	/** Managing Data **************************************************************************/
	/******************************************************************************************/
	// TODO Assign and link to the real variable
	/**
	 * Min Degree Value, Max Degree Value, Target Degree Value, Value To display in Degree
	 */
	float minInDegree = 0, maxInDegree = 225, targetInDegree = 160, valueInDegree = 145;

	/**
	 * Limit for the error color to be used, Limit for the warning color to be used
	 * It's +-20% of the TargetValue
	 */
	float colorErrorInDegree = Math.min(targetInDegree + targetInDegree / 5, maxInDegree),
			colorWaringInDegree = targetInDegree - targetInDegree / 5;
	/******************************************************************************************/
	/** Managing Drawer objects **************************************************************************/
	/******************************************************************************************/

	/**
	 * The Gradient to draw the circle
	 */
	LinearGradient circleLinearGradientBackground = null, circleLinearGradientForeground = null;
	/**
	 * The paint to draw the view
	 */
	private Paint paintUsual = new Paint();
	private Paint paintCircleBackground = new Paint();
	private Paint.FontMetrics mFontMetrics;
	private float fontHeight;
	private int circleBarForegroundColor;

	/******************************************************************************************/
	/** onDraw Method **************************************************************************/
	/******************************************************************************************/

	/*
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if (!initialized) {
			initializeGeometryValues();
			Log.e("CircleBar", "onDraw initialisation done with H " + h + ",W " + w);
		}
		canvas.save();
		canvas.translate(cx, cy);
		// draw the circle
		drawBackground(canvas);
		// draw the circle
		drawCircleBar(canvas);
		// Draw the icon
		drawIcon(canvas);
		// Draw Graduation
		drawGraduation(canvas);
		// Draw the title and the maxLabel
		drawTitle(canvas);
		drawMaxLabel(canvas);
		// Draw the target label
		drawTarget(canvas);
		canvas.restore();
		// draw the target
		// super.onDraw(canvas);
		if (!initialized) {
			initialized = true;
		}
	}

	/**
	 * Initialize the values of geometry of the view at the first call of onDraw
	 */
	private void initializeGeometryValues() {
		cx = w / 2;
		cy = h / 2;
		circleOuterRadius = 17 * Math.min(cx, cy) / 20;
		circleInnerRadius = 16 * circleOuterRadius / 20;// 8 * circleOuterRadius / 9
		
		if (valueInDegree < colorWaringInDegree) {
			// set the level 1 color
			circleBarForegroundColor = minColor;
		} else if (valueInDegree < colorErrorInDegree) {
			// set the level 2 color
			circleBarForegroundColor = warningColor;
		} else {
			// set the level 3 color
			circleBarForegroundColor = errorColor;
		}
		circleLinearGradientForeground = new LinearGradient(0, circleInnerRadius, 0, circleOuterRadius,
				circleBarForegroundColor, Color.WHITE, Shader.TileMode.MIRROR);
		circleLinearGradientBackground = new LinearGradient(0, circleInnerRadius, 0, circleOuterRadius, Color.GRAY,
				Color.WHITE, Shader.TileMode.MIRROR);
		paintCircleBackground.setShader(circleLinearGradientBackground);
	}

	/**
	 * Draw the Background
	 * 
	 * @param canvas
	 */
	private void drawBackground(Canvas canvas) {
		paintUsual.setColor(Color.WHITE);
		canvas.drawRect(backgroundRect, paintUsual);
	}

	/**
	 * Draw the CircleBar
	 * 
	 * @param canvas
	 */
	private void drawCircleBar(Canvas canvas) {
		// first save the canvas configuration (orientation, translation)
		canvas.save();
		paintUsual.setTextAlign(Paint.Align.LEFT);
		paintUsual.setShader(circleLinearGradientForeground);

		for (float degree = minInDegree; degree < maxInDegree; degree = degree + 0.2f) {
			if (degree < valueInDegree) {
				canvas.drawLine(0, circleInnerRadius, 0, circleOuterRadius, paintUsual);
			} else {
				canvas.drawLine(0, circleInnerRadius, 0, circleOuterRadius, paintCircleBackground);
			}
			canvas.rotate(0.2f);
		}
		paintUsual.setShader(null);
		paintUsual.setColor(Color.BLACK);
		canvas.restore();

	}

	/**
	 * Draw the icon
	 * 
	 * @param canvas
	 */
	private void drawIcon(Canvas canvas) {
		Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_center);
		canvas.drawBitmap(icon, -icon.getWidth()/2, -1*icon.getHeight(), paintUsual);
	}

	/**
	 * Draw the Graduations
	 * 
	 * @param canvas
	 */
	private void drawGraduation(Canvas canvas) {
		int rotationGraduationStep = 20;
		canvas.save();
		paintUsual.setTextAlign(Paint.Align.CENTER);
		paintUsual.setColor(minColor);
		int iMin = (int) minInDegree;
		for (int i = (int) minInDegree; i < colorWaringInDegree && i < maxInDegree; i = i + rotationGraduationStep) {
			canvas.drawText("" + (int) degreeToValue(i), 0, circleInnerRadius + fontHeight, paintUsual);
			canvas.drawLine(0, circleInnerRadius, 0, circleOuterRadius, paintUsual);
			// uncomment that line to see the graduation of degree
			// canvas.drawText("" + i, 0, circleOuterRadius - fontHeight, paintUsual);
			iMin = iMin + rotationGraduationStep;
			canvas.rotate(rotationGraduationStep);
		}

		paintUsual.setColor(warningColor);
		for (int i = iMin; i < colorErrorInDegree && i < maxInDegree; i = i + rotationGraduationStep) {
			canvas.drawText("" + (int) degreeToValue(i), 0, circleInnerRadius + fontHeight, paintUsual);
			canvas.drawLine(0, circleInnerRadius, 0, circleOuterRadius, paintUsual);
			// uncomment that line to see the graduation of degree
			// canvas.drawText("" + i, 0, circleOuterRadius - fontHeight, paintUsual);
			iMin = iMin + rotationGraduationStep;
			canvas.rotate(rotationGraduationStep);
		}
		paintUsual.setColor(errorColor);
		for (int i = iMin; i < maxInDegree; i = i + rotationGraduationStep) {
			canvas.drawText("" + (int) degreeToValue(i), 0, circleInnerRadius + fontHeight, paintUsual);
			canvas.drawLine(0, circleInnerRadius, 0, circleOuterRadius, paintUsual);
			// uncomment that line to see the graduation of degree
			// canvas.drawText("" + i, 0, circleOuterRadius - fontHeight, paintUsual);
			canvas.rotate(rotationGraduationStep);
		}
		// and now go back to the initial canvas state
		canvas.restore();
		paintUsual.setColor(Color.BLACK);
	}

	/**
	 * Draw the Title and the Subtitle and MaxLabel
	 * 
	 * @param canvas
	 */
	private void drawTitle(Canvas canvas) {
		// first save the canvas configuration (orientation, translation)
		paintUsual.setTextAlign(Paint.Align.CENTER);
		paintUsual.setColor(circleBarForegroundColor);
		canvas.drawText(title, 0, -2 * fontHeight + 5, paintUsual);
		canvas.drawText(subtitle, 0, -4 * fontHeight + 10, paintUsual);
		paintUsual.setColor(Color.BLACK);
	}

	/**
	 * @param canvas
	 */
	private void drawMaxLabel(Canvas canvas) {
		canvas.save();
		canvas.rotate(maxInDegree);
		canvas.drawLine(0, circleInnerRadius, 0, circleOuterRadius, paintUsual);
		canvas.translate(fontHeight, circleOuterRadius - fontHeight);
		canvas.rotate(-maxInDegree);
		canvas.drawText(maxLabel, 0, 0, paintUsual);
		canvas.restore();
	}

	/**
	 * Draw target line and target label
	 * 
	 * @param canvas
	 */
	private void drawTarget(Canvas canvas) {
		canvas.save();
		canvas.rotate(targetInDegree);
		canvas.drawLine(0, circleInnerRadius, 0, circleOuterRadius, paintUsual);
		canvas.translate(0, circleOuterRadius - fontHeight);
		canvas.rotate(-targetInDegree);
		canvas.drawText(targetLabel, 0, 0, paintUsual);
		canvas.restore();
	}

	/**
	 * Given a value (between min and max)
	 * 
	 * @return its degree representation
	 */
	private float degreeToValue(float degree) {
		return max * degree / maxInDegree;
	}

	/**
	 * Given a degree (between minInDegree na d maxInDegree)
	 * 
	 * @return the value
	 */
	private float valueToDegree(float value) {
		return maxInDegree * value / max;
	}

	/******************************************************************************************/
	/** Managing Shining **************************************************************************/
	/******************************************************************************************/
	/** * Shapes Animation: The Runnable that does the work */
	Runnable mShineAnimator;
	/** * Shapes Animation: The handler that manages the runnable */
	Handler mShineAnimHandler = new Handler();
	/** * Shapes Animation: The number of iteration to do */
	int mCounterForAnimShape = 0;
	/**
	 * The offset for the linearGradient to change
	 */
	float mOffSet=0;

	/** * Turn around the marker */
	private void launchShining() {
		mShineAnimator = new Runnable() {
			@Override
			public void run() {
				// Do your animation here, you can animate what you want, markers, shapes...
				changeLinearGradient();
				// Launch again the Runnable in 32 ms (16ms is equal to the refresh android time)
				mShineAnimHandler.postDelayed(this, 32);
			}
		};
		// launch the Runnable in 5 seconds
		mShineAnimHandler.postDelayed(mShineAnimator, 50);
	}
	/**
	 * Update the LinearGradient
	 */
	private void changeLinearGradient(){
		mOffSet=mOffSet+0.1f;
		circleLinearGradientForeground = new LinearGradient(0, circleInnerRadius+mOffSet, 0, circleOuterRadius+mOffSet,
				circleBarForegroundColor, Color.WHITE, Shader.TileMode.MIRROR);
		circleLinearGradientBackground = new LinearGradient(0, circleInnerRadius+mOffSet, 0, circleOuterRadius+mOffSet, Color.GRAY,
				Color.WHITE, Shader.TileMode.MIRROR);
		paintCircleBackground.setShader(circleLinearGradientBackground);
		invalidate();
	}
	/******************************************************************************************/
	/** Managing size **************************************************************************/
	/******************************************************************************************/
	/**
	 * Default Height
	 */
	private int defaultHeitghSize = 450;
	/**
	 * Default Width
	 */
	private int defaultWidthSize = 450;

	/*
	 * (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureWidth(widthMeasureSpec);
		int min=Math.min(measuredHeight,measuredWidth);
		h=w=min;
		Log.e("CircleBar", "H " + measuredHeight + ",W " + measuredWidth);
		// The method to call
		setMeasuredDimension(min, min);
	}

	/**
	 * Height size calculation
	 * 
	 * @param heightMeasureSpec
	 * @return The height of the view depending on its Mode and Size
	 */
	private int measureHeight(int heightMeasureSpec) {
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSize = MeasureSpec.getSize(heightMeasureSpec);
		// Default size if no limit are set
		int result = defaultHeitghSize;
		if (specMode == MeasureSpec.AT_MOST) {
			Log.e("CircleBar", "H MeasureSpec.AT_MOST");
			// AT_MOST:Measure specification mode: The child can be as large as it wants up to the specified size.
			// Returns the max size your control can take
			// in the max size gave by specSize;
			 result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			Log.e("CircleBar", "H MeasureSpec.EXACTLY");
			// EXACTLY:Measure specification mode: The parent has determined an exact size for the child. The child is
			// going to be given those bounds regardless of how big it wants to be.
			// Returns the specSize
			result = specSize;
		} else {
			Log.e("CircleBar", "H MeasureSpec.Unkknow");
		}
		return result;
	}

	/**
	 * Width size calculation
	 * 
	 * @param widthMeasureSpec
	 * @return The width of the view depending on its Mode and Size
	 */
	private int measureWidth(int widthMeasureSpec) {
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		// Default size if no limit are set
		int result = defaultWidthSize;
		if (specMode == MeasureSpec.AT_MOST) {
			// AT_MOST:Measure specification mode: The child can be as large as it wants up to the specified size.
			// Returns the max size your control can take
			// in the max size gave by specSize;
			 result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			// EXACTLY:Measure specification mode: The parent has determined an exact size for the child. The child is
			// going to be given those bounds regardless of how big it wants to be.
			// Returns the specSize
			result = specSize;
		}
		return result;
	}

	/******************************************************************************************/
	/** Managing Bounds and attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The min value
	 */
	private final static int min = 0;
	/**
	 * The max Value displayed by the view
	 */
	private int max = 145;
	/**
	 * The target value
	 */
	private int target = 65;
	/**
	 * The Normal color
	 */
	private int minColor = 0XFF00AA00;
	/**
	 * The warning color
	 */
	private int warningColor = 0Xffffd700;
	/**
	 * The errorColor
	 */
	private int errorColor = 0XFFFF0000;
	/**
	 * The value to display
	 */
	private int value = 55;
	/**
	 * The target label
	 */
	private String targetLabel = "target";
	/**
	 * The target label
	 */
	private String maxLabel = "145m3";
	/**
	 * The target label
	 */
	private String title = "consommation";
	/**
	 * The target label
	 */
	private String subtitle = "135 m3";

	/**
	 * @param maxLabel
	 *            the maxLabel to set
	 */
	public void setMaxLabel(String maxLabel) {
		this.maxLabel = maxLabel;
		invalidate();
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
		invalidate();
	}

	/**
	 * @param subtitle
	 *            the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
		invalidate();
	}

	/**
	 * @param targetLabel
	 *            the targetLabel to set
	 */
	public void setTargetLabel(String targetLabel) {
		this.targetLabel = targetLabel;
		invalidate();
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(int max) {
		this.max = max;
		maxInDegree = valueToDegree(max);
		maxLabel = "" + max + "m3";
		setTarget(target);
		setValue(value);
		initialized = false;
		invalidate();
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
		targetInDegree = valueToDegree(target);
		colorErrorInDegree = Math.min(targetInDegree + targetInDegree / 5, maxInDegree);
		colorWaringInDegree = targetInDegree - targetInDegree / 5;
		invalidate();
	}

	/**
	 * @param minColor
	 *            the minColor to set
	 */
	public void setMinColor(int minColor) {
		this.minColor = minColor;
		invalidate();
	}

	/**
	 * @param warningColor
	 *            the warningColor to set
	 */
	public void setWarningColor(int warningColor) {
		this.warningColor = warningColor;
		invalidate();
	}

	/**
	 * @param errorColor
	 *            the errorColor to set
	 */
	public void setErrorColor(int errorColor) {
		this.errorColor = errorColor;
		invalidate();
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
		valueInDegree = valueToDegree(value);
		initialized = false;
		invalidate();
	}

}
