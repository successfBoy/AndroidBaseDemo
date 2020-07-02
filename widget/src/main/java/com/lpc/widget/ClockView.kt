package com.lpc.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewTreeObserver
import java.util.*

/**
 * @author : lipengcheng1
 * @date : 2020/7/1
 * desc:
 */

class ClockView: View {
    private var mContext:Context? = null;
    //view的宽
    private var mWidth = 0f;
    //View的高
    private var mHeight = 0f;
    //时钟半径
    private var mRadius = 0f;
    //时钟的各个部件
    private var mScaleLengthLong = 0f;
    private var mScaleLengthShort = 0f;
    private var mTickLengthHour = 0f;
    private var mTickLengthMinute = 0f;
    private var mTickLengthSecond = 0f;


    //绘制各部件时用的Paint
    private var mPaintScaleLong: Paint? = null
    private var mPaintScaleShort: Paint? = null
    private var mPaintOutline: Paint? = null
    private var mPaintNum: Paint? = null
    private var mPaintTickHour: Paint? = null
    private var mPaintTickMinute: Paint? = null
    private var mPaintTickSecond: Paint? = null

    private var onPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null

    constructor(mContext: Context) : super(mContext) {
        initView(context);
    }
    constructor(context:Context, attrs: AttributeSet?):super(context , attrs){
        initView(context);
    }

    constructor(context:Context, attrs: AttributeSet? , defStyleAttr:Int):super(context , attrs , defStyleAttr){
        initView(context);
    }

    fun initView(context: Context) {
        this.mContext = context;

        onPreDrawListener = ViewTreeObserver.//获取view宽高并计算各个部件的长度
        OnPreDrawListener {
            mWidth = measuredWidth.toFloat()
            mHeight = measuredHeight.toFloat()
            mRadius = Math.min(mWidth, mHeight) / 2 * 0.95f
            mScaleLengthLong = mRadius * 0.1f
            mScaleLengthShort = mRadius * 0.05f
            mTickLengthHour = mRadius * 0.3f
            mTickLengthMinute = mRadius * 0.45f
            mTickLengthSecond = mRadius * 0.6f
            true
        }

        viewTreeObserver.addOnPreDrawListener(onPreDrawListener);

        setOnClickListener(OnClickListener {
            var calendar:Calendar = Calendar.getInstance();
            var time:String = String.format("当前时间：%02d:%02d:%02d" , calendar.get(Calendar.HOUR_OF_DAY) , calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND)
            )
        })
        //初始化所有pants对象
        initializePaints();
    }

    override fun onDraw(canvas: Canvas?) {
        if (this.mWidth == 0f || mHeight == 0f){
            return
        }
        if (onPreDrawListener != null) {
            viewTreeObserver.removeOnPreDrawListener(onPreDrawListener)
            onPreDrawListener = null
        }
        drawClock(canvas);
    }

    fun initializePaints(){
        mPaintScaleLong = Paint()
        mPaintScaleLong!!.setAntiAlias(true)
        mPaintScaleLong!!.setStrokeWidth(5f)

        mPaintScaleShort = Paint()
        mPaintScaleShort!!.setAntiAlias(true)
        mPaintScaleShort!!.setStrokeWidth(3f)

        mPaintOutline = Paint()
        mPaintOutline!!.setStyle(Paint.Style.STROKE)
        mPaintOutline!!.setAntiAlias(true)
        mPaintOutline!!.setStrokeWidth(5f)

        mPaintNum = Paint()
        mPaintNum!!.setTextSize(30f)

        mPaintTickHour = Paint()
        mPaintTickHour!!.setAntiAlias(true)
        mPaintTickHour!!.setStrokeWidth(6f)

        mPaintTickMinute = Paint()
        mPaintTickMinute!!.setAntiAlias(true)
        mPaintTickMinute!!.setStrokeWidth(4f)

        mPaintTickSecond = Paint()
        mPaintTickSecond!!.setAntiAlias(true)
        mPaintTickSecond!!.setStrokeWidth(2f)
    }

    fun drawClock(canvas: Canvas?){
        //保存原始状态
        canvas?.save();
        //将坐标系远点移动到中心，并逆时针旋转90度。完成后x轴朝上。
        canvas?.translate(mWidth / 2 , mHeight / 2)
        canvas?.rotate(-90f)

        //画外围轮廓
        canvas?.drawCircle(0f , 0f , mRadius , mPaintOutline!!)

        //画刻度
        for (i in 0..11) {
            val num: String = if (i == 0) "12" else i.toString()

            if (i % 3 == 0) {
                canvas!!.drawLine(mRadius, 0f, mRadius - mScaleLengthLong, 0f, mPaintScaleLong!!)
            } else {
                canvas!!.drawLine(mRadius, 0f, mRadius - mScaleLengthShort, 0f, mPaintScaleShort!!)
            }
            canvas!!.drawText(num, mRadius - mScaleLengthLong - mPaintNum!!.measureText(num) * 2, 0f, mPaintNum!!)

            //顺时针旋转30度
            canvas!!.rotate(30f)
        }


        val calendar = Calendar.getInstance()

        //画时针

        //画时针
        val hour = calendar[Calendar.HOUR]
        canvas!!.save()
        canvas.rotate((hour * 30).toFloat())
        canvas.drawLine(0f, 0f, mTickLengthHour, 0f, mPaintTickHour!!)
        canvas.restore()

        //画分针

        //画分针
        val minute = calendar[Calendar.MINUTE]
        canvas.save()
        canvas.rotate((minute * 6).toFloat())
        canvas.drawLine(0f, 0f, mTickLengthMinute, 0f, mPaintTickMinute!!)
        canvas.restore()

        //画秒针

        //画秒针
        val second = calendar[Calendar.SECOND]
        canvas.save()
        canvas.rotate((second * 6).toFloat())
        canvas.drawLine(0f, 0f, mTickLengthSecond, 0f, mPaintTickSecond!!)
        canvas.restore()

        //恢复原始状态

        //恢复原始状态
        canvas.restore()
    }
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            invalidate()
            sendEmptyMessageDelayed(0, 500)
        }
    }
    fun start() {
        mHandler.sendEmptyMessageDelayed(0 , 500);
    }

}