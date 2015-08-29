package com.nhnnext.android.languageexchange.common;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Alpha on 2015. 8. 21..
 * Class GsonRequest<T> : customizing GsonRequest for user list
 */
public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Type type;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private Map<String, String> params;

    /**
     * GsonRequest(String url, Class<T> clazz, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener)
     * Initialize context variables
     *
     * @param url           URL of the request to make
     * @param clazz         Relevant class object, for Gson's reflection
     * @param headers       Map of request headers
     * @param listener      Response listener
     * @param errorListener error listener
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.clazz = clazz;
        this.type = null;
        this.headers = headers;
        this.listener = listener;
    }

    /**
     * GsonRequest(String url, Type type, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener)
     * Initialize context variables
     *
     * @param method        Method type
     * @param url           URL of the request to make
     * @param type          Relevant type object, for Gson's reflection
     * @param headers       Map of request headers
     * @param listener      Response listener
     * @param errorListener error listener
     */
    public GsonRequest(int method, String url, Type type, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = null;
        this.type = type;
        this.headers = headers;
        this.listener = listener;
    }

    /**
     * GsonRequest(String url, Type type, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener)
     * Initialize context variables
     *
     * @param url           URL of the request to make
     * @param type          Relevant type object, for Gson's reflection
     * @param headers       Map of request headers
     * @param listener      Response listener
     * @param errorListener error listener
     */
    public GsonRequest(String url, Type type, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.clazz = null;
        this.type = type;
        this.headers = headers;
        this.listener = listener;
    }

    /**
     * Method getHeaders()
     * http headers getter mothod
     *
     * @return http headers
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    /**
     * Method parseNetworkResponse(NetworkResponse response)
     * http response parser
     *
     * @param response http response
     * @return gson object
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if (this.clazz != null) {
                return Response.success(
                        gson.fromJson(json, clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return (Response<T>) Response.success(
                        this.gson.fromJson(json, this.type),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * Method getParams()
     *
     * @return request params getter for user information
     * @throws AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    /**
     * Method setParams(Map<String, String> params)
     * request params setter for user information
     *
     * @param params
     */
    public void setParams(Map<String, String> params) {
        this.params = params;
        if (this.params.get("userPassword") == null)
            this.params.put("userPassword", "");
        if (this.params.get("oAuth") == null)
            this.params.put("oAuth", "");
    }
}
