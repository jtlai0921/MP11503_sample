package jp.co.se.android.recipe.chapter10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;

import jp.co.se.android.recipe.chapter10.ParseJson.Route;
import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

/**
 * 搜尋路徑
 */
public class RequestDirectionsTask extends AsyncTask<Void, Void, List<Route>> {

    private LatLng mOrigin;
    private LatLng mDest;
    private String mTravelMode;
    private RequestDirectionsTaskCallback mCallback;
    private Throwable mThrowable;

    public RequestDirectionsTask(Context context, LatLng origin, LatLng dest,
            String travelMode) {
        mOrigin = origin;
        mDest = dest;
        mTravelMode = travelMode;
    }

    @Override
    protected List<Route> doInBackground(Void... args) {
        // 建立進行路徑搜尋的URL
        String directionsUrl = getDirectionsUrl(mOrigin, mDest, mTravelMode);
        try {
            // 詢問伺服器以取得路徑搜尋結果
            String result = reqeust(directionsUrl);

            // 接收伺服器取得結果的路徑
            List<Route> directions = ParseJson.parseDirections(result);
            return directions;
        } catch (IOException e) {
            mThrowable = e;
        } catch (JSONException e) {
            mThrowable = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Route> directions) {
        if (mThrowable == null) {
            mCallback.onSucceed(directions);
        } else {
            mCallback.onFailed(mThrowable);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest,
            String travelMode) {
        Builder builder = Uri.parse(
                "https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon();

        builder.appendQueryParameter("origin", origin.latitude + ","
                + origin.longitude);
        builder.appendQueryParameter("destination", dest.latitude + ","
                + dest.longitude);
        builder.appendQueryParameter("language", "ja");
        builder.appendQueryParameter("sensor", "false");
        builder.appendQueryParameter("mode", travelMode);

        return builder.build().toString();
    }

    private String reqeust(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public RequestDirectionsTask setRequestDirectionsTaskCallback(
            RequestDirectionsTaskCallback l) {
        mCallback = l;

        return this;
    }

    public interface RequestDirectionsTaskCallback {
        public void onSucceed(List<Route> result);

        public void onFailed(Throwable e);
    }
}
