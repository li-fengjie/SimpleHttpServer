package com.example.lifen.simplehttpserver;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by Unchangedman on 2017/12/28.
 */
public class MainActivity extends AppCompatActivity {

    private SimpleHttpServer shs;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);

        WebConfiguration wc = new WebConfiguration();
        wc.setPort(8088);
        wc.setMaxParallels(50);

        shs = new SimpleHttpServer(wc);
        /*
        向 shs 中添加 IRsourceUriHander 实例
         */
        shs.registerResourceHandler(new ResourceInAssetsHandler(this));
        shs.registerResourceHandler(new UploadImageHandler(this){
            @Override
            public void onImageLoaded(final String path) {//重写onImageLoaded方法 在UiTread 主线程中更新Ui
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });

        shs.startAsync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shs.stopAsync();
    }
}
