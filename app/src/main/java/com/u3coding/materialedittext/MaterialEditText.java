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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class MaterialEditText extends android.support.v7.widget.AppCompatEditText {
    private boolean hideUnderLine;
    private int bottomSpacing;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean showCleanButton = true;
    private int iconOuterWidth;
    private int iconOuterHeight;
    private boolean isCharactersCounter = true;
    private int baseColor;
    private int errorColor;
    private boolean singleEllipsis = true;

    public boolean isCharactersCounter() {
        return isCharactersCounter;
    }

    public void setCharactersCounter(boolean charactersCounter) {
        isCharactersCounter = charactersCounter;
    }


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
     public boolean isShowCleanButton() {
        return showCleanButton;
    }

    public void setShowCleanButton(boolean showCleanButton) {
        this.showCleanButton = showCleanButton;
    }
    private void init(){
        iconOuterHeight = getPiexl(32);
        iconOuterWidth = getPiexl(48);
        baseColor = getResources().getColor(R.color.text);
        errorColor = getResources().getColor(R.color.error);
        bottomSpacing = getResources().getDimensionPixelSize(R.dimen.inner_components_spacing);
        if (getMaxEms() > 0){
            isCharactersCounter = true;
        }else{
            isCharactersCounter = false;
        }
        textPaint.setTextSize(getTextSize());
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float relativeHight = -metrics.ascent - metrics.descent;
        if (isCharactersCounter) {//给下方文字留出位置
            setPadding(getPaddingLeft(),getPaddingTop(),getPaddingRight(),getPaddingBottom()+(int)relativeHight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setBackground(null);
        //获取一些位置信息
        int lineStartY = getScrollY()+getHeight()-getPaddingBottom();
        int startX = getScrollX()+getPaddingLeft();
        int endX = getScrollX()+getWidth()-getPaddingRight();
        paint.setAlpha(255);
    /*    if (true){
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.met_ic_clear);
            int iconLeft = startX;
            int iconTop = lineStartY-icon.getHeight();
            canvas.drawBitmap(icon,iconLeft,iconTop,paint);
        }
        if(iconRightBitmaps != null){
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.met_ic_clear);
            int iconLeft = startX+getPaddingLeft()
            int iconTop =
            canvas.drawBitmap(icon,iconLeft,iconTop,paint);
        }*/
        textPaint.setTextSize(getTextSize());
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float relativeHight = -metrics.ascent - metrics.descent;
        if ((hasFocus()&&hasCharacterCounter())||!isCharactersCountValid()){
            textPaint.setColor(isCharactersCountValid()?Color.BLUE:errorColor);
            String counter = getCounterString();
            canvas.drawText(counter,isRTL()?startX:endX-textPaint.measureText(counter),lineStartY+getPiexl(10)+relativeHight,textPaint);
        }
        //判断是否显示
        if(hasFocus()&&isEnabled()&&!TextUtils.isEmpty(getText())&&showCleanButton){
            paint.setAlpha(255);
            int buttonLeft;//保存清除按钮的水平位置
            //确定布局方向
            if (isRTL()){
                buttonLeft = startX;
            }else{
                buttonLeft = endX - iconOuterWidth;
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.met_ic_clear);
            buttonLeft += (iconOuterWidth - bitmap.getWidth())/2;
            //获取按钮的顶部位置
            int iconTop = lineStartY+bottomSpacing - iconOuterHeight + (iconOuterHeight - bitmap.getHeight())/2;
            //绘制
            canvas.drawBitmap(bitmap,buttonLeft,iconTop,paint);
        }

        //绘制下划线
        if(!hideUnderLine){
            //获取位置
            lineStartY += bottomSpacing;
            //是否内容合法，不合法线为红色
            if (!isInternalValid()){
                paint.setColor(Color.RED);
                canvas.drawRect(startX,lineStartY,endX,lineStartY+6,paint);
            }else if(!isEnabled()){
                paint.setColor(Color.BLACK);
                float interval  = 1;
                for (float xOffset = 0;xOffset < getWidth();xOffset += interval*3){
                    canvas.drawRect(startX+xOffset,lineStartY,startX+xOffset+interval,lineStartY+6,paint);
                }
            }else if(hasFocus()){
                paint.setColor(Color.BLUE);
                canvas.drawRect(startX,lineStartY,endX,lineStartY+6,paint);
            }else{//正常绘制
                paint.setColor(Color.YELLOW);
                canvas.drawRect(startX,lineStartY,endX,lineStartY+6,paint);
            }
        }
        if (getText().toString().length() > getMaxEms()){
            singleEllipsis = true;
        }else{
            singleEllipsis = false;
        }
        if (hasFocus()&&singleEllipsis){
            paint.setColor(isInternalValid()?Color.BLUE:Color.RED);
            int startY = lineStartY + bottomSpacing;
            int bottomEllipsisSize = getPiexl(10);
            int ellipsisStartX;
            //确定布局方向
            if (isRTL()){
                ellipsisStartX = endX;
            }else{
                ellipsisStartX = startX;
            }
            int signum = isRTL() ? -1 :1;
            canvas.drawCircle(ellipsisStartX + signum * bottomEllipsisSize / 2, startY + bottomEllipsisSize / 2, bottomEllipsisSize / 2, paint);
            canvas.drawCircle(ellipsisStartX + signum * bottomEllipsisSize * 5 / 2, startY + bottomEllipsisSize / 2, bottomEllipsisSize / 2, paint);
            canvas.drawCircle(ellipsisStartX + signum * bottomEllipsisSize * 9 / 2, startY + bottomEllipsisSize / 2, bottomEllipsisSize / 2, paint);
        }
        super.onDraw(canvas);
    }

    private String getCounterString(){
        String s = "";
        if (getMaxEms()  > 0){
            s = getText().toString().length()+"/"+getMaxEms();
        }else{
            s = "";
        }
        return  s;
    }
    private boolean hasCharacterCounter() {
       return !TextUtils.isEmpty(getText())&&getText().toString().length() > 0;
    }

    private boolean isCharactersCountValid() {
        boolean result;
        if (getMaxEms() < 0){
            result = true;
        }else if (getText().toString().length() > getMaxEms()){
            result = false;
        }else{
            result = true;
        }
        return result;
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
