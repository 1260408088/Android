package code.king.com.myapplication;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText ed_text;
    private Button bt_start;
    private ImageView iv_image;
    private Bitmap bitmap=null;
    //private Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intUI();
    }

    private void intUI() {
        ed_text = (EditText) findViewById(R.id.ed_text);
        bt_start = (Button) findViewById(R.id.bt_start);
        iv_image = (ImageView) findViewById(R.id.iv_image);
       /* save = (Button) findViewById(R.id.bt_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveImageToSysAlbum();
            }
        });*/
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ed_text.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    bitmap = generateBitmap(text, 600, 600);
                }
                iv_image.setImageBitmap(bitmap);
            }
        });

        iv_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SaveImageToSysAlbum();
                return true;
            }
        });
    }
    private void SaveImageToSysAlbum() {
        if(ExistSDCard()){
            BitmapDrawable bmpDrawable = (BitmapDrawable) iv_image.getDrawable();
            Bitmap bmp = bmpDrawable.getBitmap();
            if (bmp != null) {
                try {
                    ContentResolver cr = getContentResolver();
                    String url = MediaStore.Images.Media.insertImage(cr, bmp,
                            String.valueOf(System.currentTimeMillis()), "");
                    Toast.makeText(this, getString(R.string.save_succ), Toast.LENGTH_SHORT).show();

                    Uri uuUri= Uri.parse(url);
                    String path =  getRealPathFromURI(uuUri);
                    System.out.println(path);

                    //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
                    //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory()+path)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this, getString(R.string.no_iamge_save_fail), Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, getString(R.string.no_sdcard_save_fail), Toast.LENGTH_SHORT).show();
        }
        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
    }
    //测试路径转化
    public String getRealPathFromURI(Uri contentUri) {//通过本地路经 content://得到URI路径
        Cursor cursor = null;
        String locationPath = null ;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor= getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            locationPath = cursor.getString(column_index);
        } catch (Exception e) {
        }finally{
            if(cursor != null)
            {
                cursor.close();
            }
        }
        return locationPath;
    }

    //判断sdcard是否存在的方法
    private boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


}
