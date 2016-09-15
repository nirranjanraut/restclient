package in.gauriinfotech.restclient;

/**
 * Created by NiRRaNjAN on 15/09/16.
 * Contact email    :   nirranjan.raut@gmail.com
 * Facebook         :   https://www.facebook.com/NiRRaNjAN.RauT
 * Stackoverflow    :   http://stackoverflow.com/users/1911941/elite
 * Blogs            :   http://technoscripts.com
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class CustomRequest extends Request<String> {

    private static final String tag = "CustomRequest";
    private Map<String, String> params;
    private Response.Listener<String> listener;
    private Map<String, String> headers;

    private CustomRequest(String url, int method, Map<String, String> params,
                          Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;
    }

    public static CustomRequest getInstance(String url, int method, Map<String, String> params,
                                            Response.Listener<String> responseListener, Response.ErrorListener errorListener,
                                            String querySep, String keyValueSep, String paramSep) {
        if(method == Method.GET && params != null) {
            url += getQueryUrl(params, querySep, keyValueSep, paramSep);
        }
        return new CustomRequest(url, method, params, responseListener, errorListener);
    }

    private CustomRequest(String url, int method, Map<String, String> params, Map<String, String> headers,
                          Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.params = params;
        this.headers = headers;
    }

    public static CustomRequest getInstance(String url, int method, Map<String, String> params, Map<String, String> headers,
                                            Response.Listener<String> responseListener, Response.ErrorListener errorListener,
                                            String querySep, String keyValueSep, String paramSep) {
        if(method == Method.GET && params != null) {
            url += getQueryUrl(params, querySep, keyValueSep, paramSep);
        }
        return new CustomRequest(url, method, params, headers, responseListener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.e(tag, "RESPONSE :: " + jsonString);
            return Response.success(jsonString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }

    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if(headers != null) {
            return headers;
        }
        return super.getHeaders();
    }

    private static String getQueryUrl(Map<String, String> params, String querySeparator,
                                      String keyValueSeparator, String paramSeparator) {
        StringBuilder url = new StringBuilder();
        if(params == null || params.isEmpty()) {
            return url.toString();
        }
        try {
            url.append(querySeparator);
            for(String key : params.keySet()) {
                url.append(URLEncoder.encode(key, "UTF-8")).append(keyValueSeparator);
                url.append(URLEncoder.encode(params.get(key), "UTF-8")).append(paramSeparator);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return url.toString();
    }

}