package in.gauriinfotech.restclient;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NiRRaNjAN on 15/09/16.
 */
public class ImageUploader<T> extends Request<T> {

    private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
    private final Response.Listener<T> mListener;
    private final File yourImageFile;
    protected Map<String, String> headers;
    private String tag = "ImageUploader";
    private Map<String, String> params;
    private Class<T> type;

    public ImageUploader(String url, Map<String, String> params, Class<T> type, Response.ErrorListener errorListener, Response.Listener<T> listener, File imageFile)   {
        super(Method.POST, url, errorListener);
        mListener = listener;
        yourImageFile = imageFile;
        this.type = type;
        this.params = params;
        addImageEntity();
        setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        headers.put("Accept", "text/html");
        return headers;
    }

    private void addImageEntity() {
        for(String key : params.keySet()) {
            mBuilder.addPart(key, new StringBody(params.get(key), ContentType.TEXT_PLAIN));
        }
        mBuilder.addBinaryBody("file", yourImageFile, ContentType.create("image/jpeg"), yourImageFile.getName());
        mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
    }

    @Override
    public String getBodyContentType()   {
        String content = mBuilder.build().getContentType().getValue();
        return content;
    }

    @Override
    public byte[] getBody() throws AuthFailureError    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
            Log.e(tag, Log.getStackTraceString(e));
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new Gson().fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception ex) {
            Log.e(tag, Log.getStackTraceString(ex));
            return Response.error(new ParseError(ex));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}