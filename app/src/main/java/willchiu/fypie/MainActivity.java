package willchiu.fypie;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class MainActivity extends Activity {

    ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button selectPic = (Button)findViewById(R.id.selectPic);
        imageView1 = (ImageView)findViewById(R.id.imageView1);

        selectPic.setOnClickListener(new Button.OnClickListener(){

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();

            Toast.makeText(getApplicationContext(), "ImageView: " + imageView1.getWidth()
            + " x " + imageView1.getHeight(), Toast.LENGTH_LONG).show();

            Bitmap bitmap;
            bitmap = decodeBitMapFromUri(targetUri, imageView1.getWidth(), imageView1.getHeight());



            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "the image data could not be decoded",
                        Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Decoded Bitmap: " + bitmap.getWidth()
                + " x " + bitmap.getHeight(), Toast.LENGTH_LONG).show();
                imageView1.setImageBitmap(bitmap);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Bitmap decodeBitMapFromUri(Uri uri, int reqWidth, int reqHeight) {
        Bitmap bMap = null;

        try {
            //decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null,
                    options);
            //calculate bitmap with inSampleSize set
            options.inSampleSize = calcSampleSize(options, reqWidth, reqHeight);

            //decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),
                    null, options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return bMap;
    }

    public int calcSampleSize(BitmapFactory.Options options,
                              int reqWidth, int reqHeight) {
        //original width and height of the image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int sampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                sampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                sampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return sampleSize;
    }
}
