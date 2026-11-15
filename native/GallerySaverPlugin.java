package __PKG__;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@CapacitorPlugin(name = "GallerySaver")
public class GallerySaverPlugin extends Plugin {
    @PluginMethod
    public void save(PluginCall call) {
        String name = call.getString("name");
        String mime = call.getString("mime", "video/mp4");
        if (name == null) { call.reject("name missing"); return; }
        if (Build.VERSION.SDK_INT < 29) { call.reject("Android 10+ required"); return; }
        File src = new File(getContext().getCacheDir(), name);
        if (!src.exists()) { call.reject("file not found"); return; }
        try {
            ContentResolver cr = getContext().getContentResolver();
            ContentValues v = new ContentValues();
            v.put(MediaStore.Video.Media.DISPLAY_NAME, name);
            v.put(MediaStore.Video.Media.MIME_TYPE, mime.split(";")[0]);
            v.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/BallBattle");
            v.put(MediaStore.Video.Media.IS_PENDING, 1);
            Uri out = cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, v);
            if (out == null) { call.reject("could not create gallery entry"); return; }
            try (InputStream is = new FileInputStream(src); OutputStream os = cr.openOutputStream(out)) {
                byte[] buf = new byte[65536];
                int n;
                while ((n = is.read(buf)) > 0) os.write(buf, 0, n);
            }
            v.clear();
            v.put(MediaStore.Video.Media.IS_PENDING, 0);
            cr.update(out, v, null, null);
            src.delete();
            JSObject r = new JSObject();
            r.put("saved", true);
            call.resolve(r);
        } catch (IOException e) {
            call.reject(String.valueOf(e.getMessage()));
        }
    }
}
