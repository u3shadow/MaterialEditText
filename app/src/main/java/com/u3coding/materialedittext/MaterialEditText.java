package com.u3coding.materialedittext;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class MaterialEditText extends EditText {
    private boolean hideUnderLine;
    private int bottomSpacing;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean showCleanButton = true;
    private int iconOuterWidth;
    private int iconOuterHeight;

    public MaterialEditText(Context context) {
        super(context);
        init();
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        iconOuterHeight = getPiexl(32);
        iconOuterWidth = getPiexl(48);
        bottomSpacing = getResources().getDimensionPixelSize(R.dimen.inner_components_spacing);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        setBackground(null);
        //获取一些位置信息
        int lineStartY = getScrollY()+getHeight()-getPaddingBottom();
        int startX = getScrollX()+getPaddingLeft();
        int endX = getScrollX()+getWidth()-getPaddingRight();
        if(hasFocus()&&isEnabled()&&!TextUtils.isEmpty(getText())&&showCleanButton){
            paint.setAlpha(255);
            int buttonLeft;
            if (isRTL()){
                buttonLeft = startX;
            }else{
                buttonLeft = endX - iconOuterWidth;
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.met_ic_clear);
            buttonLeft += (iconOuterWidth - bitmap.getWidth())/2;
            int iconTop = lineStartY+bottomSpacing - iconOuterHeight + (iconOuterHeight - bitmap.getHeight())/2;
            canvas.drawBitmap(bitmap,buttonLeft,iconTop,paint);
        }
        if(!hideUnderLine){//绘制下划线
            //获取位置
            lineStartY += bottomSpacing;
            if (!isInternalValid()){//是否内容合法，不合法线为红色
                paint.setColor(Color.RED);
                canvas.drawRect(startX,lineStartY,endX,lineStartY+6,paint);
            }else if(!isEnabled()){//是否开启
                paint.setColor(Color.BLACK);
                float interval  = 1;
                for (float xOffset = 0;xOffset < getWidth();xOffset += interval*3){
                    canvas.drawRect(startX+xOffset,lineStartY,startX+xOffset+interval,lineStartY+6,paint);
                }
            }else if(hasFocus()){//是否获取焦点
                paint.setColor(Color.BLUE);
                canvas.drawRect(startX,lineStartY,endX,lineStartY+6,paint);
            }else{//正常绘制
                paint.setColor(Color.YELLOW);
                canvas.drawRect(startX,lineStartY,endX,lineStartY+6,paint);
            }
        }
        super.onDraw(canvas);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean isRTL(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
            return false;
        }
        Configuration config = getResources().getConfiguration();
        return config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }
    private int getPiexl(int dp){
        return Density.dp2px(getContext(),dp);
    }
    private boolean isInternalValid() {
        return true;
    }
}
