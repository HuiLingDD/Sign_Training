package com.hui.sign_training;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private MyView myView; // 宣告簽名畫板

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = findViewById(R.id.myView);  // 取得簽名元件

    }

    public void clear(View view) {
        myView.clear(); // 點擊按鈕clear時，將myView簽名清除
    }

    public void undo(View view) {
        myView.undo();  // 點擊按鈕undo時，返回前一個簽名筆畫
    }

    public void redo(View view) {
        myView.redo(); // 點擊按鈕redo時，將前一筆刪除的簽名復原
    }
}