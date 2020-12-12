package cz.cuni.mff.nutritionalassistant.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DownloadImage extends AsyncTask<String, Integer, Drawable> {

    private ImageView characterImage;
    private Context context;

    DownloadImage(Context context, ImageView characterImage) {
        this.context = context;
        this.characterImage = characterImage;
    }

    private void setImage(Drawable drawable) {
        characterImage.setForeground(drawable);
    }

    @Override
    protected Drawable doInBackground(String... arg0) {
        // This is done in a background thread
        return downloadImage(arg0[0]);
    }

    @Override
    protected void onPostExecute(Drawable image) {
        setImage(image);
    }

    private Drawable downloadImage(String _url) {
        //Prepare to download image
        URL url;
        BufferedOutputStream out;
        InputStream in;
        BufferedInputStream buf;

        // BufferedInputStream buf;
        try {
            url = new URL(_url);
            in = url.openStream();

            // Read the inputstream
            buf = new BufferedInputStream(in);

            // Convert the BufferedInputStream to a Bitmap
            Bitmap bMap = BitmapFactory.decodeStream(buf);
            if (in != null) {
                in.close();
            }
            if (buf != null) {
                buf.close();
            }

            return new BitmapDrawable(context.getResources(), bMap);

        } catch (Exception e) {
            Log.e("Error reading file", e.toString());
        }

        return null;
    }
}
