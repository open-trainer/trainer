package org.opentrainer.garmin;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;
import org.opentrainer.garmin.exception.RateLimitGarminConnectException;
import org.opentrainer.garmin.exception.SessionExpiredGarminConnectException;
import org.opentrainer.garmin.exception.UnknownGarminConnectException;
import org.opentrainer.garmin.token.TokenSupplier;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 26-12-2020
 */
@RequiredArgsConstructor
class BaseRepository {
    private final Gson gson = new Gson();
    private final TokenSupplier tokenSupplier;
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();


    protected Request get(String url){
        var token = tokenSupplier.get();
        return new Request.Builder()
                .url(url)
                .header("cookie", "__cflb=a;GARMIN-SSO-GUID=" + token.getSsoGuid() + ";SESSIONID=" + token.getSessionId() + ";")
                .build();

    }

    protected ApiResponse send(Request request) {
        try {
            var response = client.newCall(request).execute();
            var body = response.body();
            if (response.code() == 401 || response.code() == 403) {
                throw new SessionExpiredGarminConnectException();
            }
            if(response.code() == 429) {
                throw new RateLimitGarminConnectException();
            }
            return new ApiResponse(
                    response.code(),
                    body.string()
            );

        } catch (IOException ex) {
            throw new UnknownGarminConnectException(ex);
        }
    }

    @SneakyThrows
    protected Request uploadFile(String url, InputStream inputStream){
        var token = tokenSupplier.get();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file",
                        "file",
                        RequestBody.create(
                                MediaType.parse("application/octet-stream"),
                                inputStream.readAllBytes()
                        ))
                .build();
        return new Request.Builder()
                .method("POST",body)
                .url(url)
                .header("cookie", "__cflb=a;GARMIN-SSO-GUID=" + token.getSsoGuid() + ";SESSIONID=" + token.getSessionId() + ";")
                .build();

    }

    @Getter
    @RequiredArgsConstructor
    protected static class ApiResponse {
        private final int statuscode;
        private final String rawResponse;
    }
}
