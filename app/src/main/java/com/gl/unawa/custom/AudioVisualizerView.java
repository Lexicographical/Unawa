package com.gl.unawa.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.gl.unawa.R;

import java.util.LinkedList;
import java.util.Queue;

public class AudioVisualizerView extends FrameLayout {

    private static final int DEFAULT_NUM_COLUMNS = 20;

    private int mNumColumns;
    private int mRenderColor;

    private int mBaseY;

    private Canvas mCanvas;
    private Bitmap mCanvasBitmap;
    private Rect mRect = new Rect();
    private Paint mPaint = new Paint();
    private Paint mFadePaint = new Paint();
    private Matrix matrix;

    private Queue<Float> data;

    private float mColumnWidth;
    private float mSpace;

    public AudioVisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        mPaint.setColor(mRenderColor);
        mFadePaint.setColor(Color.argb(138, 255, 255, 255));
        matrix = new Matrix();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.AudioVisualizerView);
        mNumColumns = args.getInteger(R.styleable.AudioVisualizerView_numColumns, DEFAULT_NUM_COLUMNS);
        mRenderColor = args.getColor(R.styleable.AudioVisualizerView_renderColor, Color.WHITE);
        args.recycle();
        this.data = new LinkedList<>();
        for (int i = 0; i < mNumColumns; i++) {
            data.add(0f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRect.set(0, 0, getWidth(), getHeight());

        if (mCanvasBitmap == null) {
            mCanvasBitmap = Bitmap.createBitmap(
                    canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        }

        if (mCanvas == null) {
            mCanvas = new Canvas(mCanvasBitmap);
        }

        if (mNumColumns > getWidth()) {
            mNumColumns = DEFAULT_NUM_COLUMNS;
        }

        mColumnWidth = (float) getWidth() / (float) mNumColumns;
        mSpace = mColumnWidth / 8f;

        if (mBaseY == 0) {
            mBaseY = getHeight() / 2;
        }

        canvas.drawBitmap(mCanvasBitmap, matrix, null);
    }

    public void receive(final float rms) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (mCanvas == null) {
                    return;
                }
                if (rms == 0) {
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                } else {
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                }
                data.remove();
                data.add(rms);
                drawBar();
                invalidate();
            }
        });
    }

    private void drawBar() {
        float hfac = getHeight() / 2f;
        int i = 0;
        for (double volume : data) {
//            float height = (float) Math.max(volume - 25, 0);
//            height /= 20;
//            height *= hfac;
//            height += 1;
            float height = (float) Math.max(volume, 0) / 10;
            height *= hfac;
            height++;
            float left = i * mColumnWidth + mSpace;
            float right = (i + 1) * mColumnWidth - mSpace;

            RectF rect = createRectF(left, right, height);
            mCanvas.drawRect(rect, mPaint);
            i++;
        }
    }

    private RectF createRectF(float left, float right, float height) {
        return new RectF(left, mBaseY - height, right, mBaseY + height);
    }

}
