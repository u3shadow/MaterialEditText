package com.u3coding.materialedittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.EditText;

public class MaterialEditText extends EditText {
    private boolean hideUnderLine;
    private int bottomSpacing;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public MaterialEditText(Context context) {
        super(context);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(){
        bottomSpacing = getResources().getDimensionPixelSize(R.dimen.inner_components_spacing);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackground(null);
        //获取一些位置信息
        int lineStartY = getScrollY()+getHeight()-getPaddingBottom();
        int startX = getScrollX()+getPaddingLeft();
        int endX = getScrollX()+getWidth()-getPaddingRight();
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


    }


    private boolean isInternalValid() {
        return true;
    }
}
