/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.bakingapp;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import retrofit2.Call;

/**
 * These utilities will be used to communicate with the weather servers.
 */
final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    /**
     * @return The URL to use to query the weather server.
     */
    static URL buildURL_Base_info() throws MalformedURLException {
        return new URL(BASE_URL);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        Log.v("URL", url.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\n");

                boolean hasInput = scanner.hasNext();
                urlConnection.disconnect();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        } else {
            Log.v("Connection", "Connection failed");
            return null;
        }
    }

}