package id.wilik.udacity.androidkejarbeginner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CardActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView_name, textView_message;

    String name, message, uriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        message = intent.getStringExtra("message");
        uriString = intent.getStringExtra("uriString");

        imageView = (ImageView) findViewById(R.id.imageView);
        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_message = (TextView) findViewById(R.id.textView_message);

        textView_name.setText(name);
        textView_message.setText(message);


    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            final Uri imageUri = Uri.parse(uriString);
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(scaleBitmapAndKeepRation(selectedImage, 600, 600));
            //imageView.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, 400, 400, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap scaleBitmapAndKeepRation(Bitmap TargetBmp, int reqHeightInPixels, int reqWidthInPixels) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, TargetBmp.getWidth(), TargetBmp.getHeight()), new RectF(0, 0, reqWidthInPixels, reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(TargetBmp, 0, 0, TargetBmp.getWidth(), TargetBmp.getHeight(), m, true);
    }

    public void screenshotAndShare(View view) {
        Bitmap bitmap = screenshot(getWindow().getDecorView().getRootView());
        Uri uri = saveBitmap(bitmap);
        share(uri);
    }

    public Bitmap screenshot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Uri saveBitmap(Bitmap bitmap) {
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMd-Hms");
            String date = format.format(calendar.getTime());

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(path + "/AndroidKejar_" + date + ".png");
            FileOutputStream fileOutPutStream = null;
            fileOutPutStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutPutStream);

            fileOutPutStream.flush();
            fileOutPutStream.close();

            return Uri.parse("file://" + imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void share(Uri uri) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("image/jpeg");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Look at my cool android app! #AndroidKejar #udacity #beginner #android");
        startActivity(Intent.createChooser(sendIntent, "Share"));
    }
}
