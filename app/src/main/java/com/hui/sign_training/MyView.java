package com.hui.sign_training;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedList;

public class MyView extends View { // 繼承版面的View

    // 畫的點有順序性，使用LinkedList
    // 單條線繪製
    private LinkedList<HashMap<String, Float>> line = new LinkedList<>();
    // 單條線放入LinkedList成多條線繪製
    private LinkedList<LinkedList<HashMap<String, Float>>> lines = new LinkedList<>();
    // 用於將前一條丟棄的線，放入回收桶
    private LinkedList<LinkedList<HashMap<String, Float>>> recycler = new LinkedList<>();

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.rgb(255,240,245)); // 設定背景顏色
    }

    @Override
    protected void onDraw(Canvas canvas) { // 程式啟動就會觸發
        super.onDraw(canvas); // 畫布
        Paint paint = new Paint(); // 呼叫畫筆
        paint.setColor(Color.BLUE); // 畫筆顏色
        paint.setStrokeWidth(8); // 畫筆粗細
        // 從多條線中尋訪每一條線
        for (LinkedList<HashMap<String, Float>> line:lines) {
            // 將每條線畫出(三個點裡有兩個線段，所以從1開始(0-1的線))
            for (int i = 1; i < line.size(); i++) {
                // 線的上一個點
                HashMap<String, Float> p0 = line.get(i - 1);
                // 線的當下的點
                HashMap<String, Float> p1 = line.get(i);
                // 在畫布裡畫線，從p0 x,y 的位置畫到p1 x,y的位置，並放入畫筆
                canvas.drawLine(p0.get("x"), p0.get("y"), p1.get("x"), p1.get("y"), paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { // 當觸摸的事件被發生
        // 取得觸發的x和y位置
        float x = event.getX(), y = event.getY();
        // 用於收集一個點
        HashMap<String, Float> point = new HashMap<>();
        // 將點的x,y資料加入
        point.put("x",x); point.put("y", y);
        // 如果事件動作的值等於點下去要開始畫新線時
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 宣告一條新線
            LinkedList<HashMap<String, Float>> line = new LinkedList<>();
            // 將每個點放入line資料中
            line.add(point);
            // 將每個新線放入lines中
            lines.add(line);
            // 在畫新線時，將資源回收桶的線清除
            recycler.clear();
        }else if(event.getAction() == MotionEvent.ACTION_MOVE) { // 如果事件動作的值等於移動時
            // 將lines裡面最新的一條線找出來，並放入新的點
            lines.getLast().add(point);
        }
        // 觸發onDraw抓取事件並更新畫面
        invalidate();
        return true; // 設為true，當點擊事件發生時執行
    }

    public void clear() { // 清除畫板
        lines.clear(); // 清除記憶體中的所有線
        invalidate(); // 觸發onDraw抓取事件並更新畫面
    }

    public void undo() { // 讓上一筆畫的清除
        if (lines.size() > 0) { // 需要有線才可以執行
            recycler.add(lines.removeLast()); // 將最新的那一條線放入資源回收桶
            invalidate(); // 觸發onDraw抓取事件並更新畫面
        }
    }

    public void redo() {
        if (recycler.size() > 0) { // 資源回收桶裡需要有線才可以執行
            lines.add(recycler.removeLast()); // 將資源回收桶的刪除的最新線返回至lines裡
            invalidate(); // 觸發onDraw抓取事件並更新畫面
        }
    }
}

