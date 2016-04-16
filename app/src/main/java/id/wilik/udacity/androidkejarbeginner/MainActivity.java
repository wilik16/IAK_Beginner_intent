package id.wilik.udacity.androidkejarbeginner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final int SELECT_PHOTO = 1;

    TextView textView_selectPhoto;
    EditText editText_name, editText_message;
    Button button_selectPhoto, button_ok;
    String uriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_selectPhoto = (TextView) findViewById(R.id.textView_selectPhoto);
        button_selectPhoto = (Button) findViewById(R.id.button_selectPhoto);
        button_selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        button_ok = (Button) findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText_name.getText().toString();
                String message = editText_message.getText().toString();

                Intent intent = new Intent(MainActivity.this, CardActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("message", message);
                intent.putExtra("uriString", uriString);
                startActivity(intent);
            }
        });
        editText_name = (EditText) findViewById(R.id.editText_name);
        editText_message = (EditText) findViewById(R.id.editText_message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    textView_selectPhoto.setText(getString(R.string.selected));
                    uriString = imageReturnedIntent.getDataString();
                }
        }
    }
}
