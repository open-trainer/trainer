package org.opentrainer.garmin.token;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UserPasswordTokenSupplier implements TokenSupplier {
    private final String baseUrl = "https://connect.garmin.com";
    private static final String LOGIN_URL =
            "https://sso.garmin.com/sso/signin?service=https://connect.garmin.com/modern";

    private final PersistentCookieJar cookies = new PersistentCookieJar();

    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .cookieJar(cookies)
            .build();

    private final String login;

    private final String password;

    private String getLoginPage() throws IOException {

        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .header("User-Agent", "Mozilla/5.0")
                .build();

        Response response = client.newCall(request).execute();

        assert response.body() != null;
        return response.body().string();
    }

    private String extract(String html, String pattern) {

        Matcher matcher = Pattern.compile(pattern).matcher(html);

        if (matcher.find()) {
            return matcher.group(1);
        }

        throw new RuntimeException("Parameter not found");
    }

    private void exchangeTicket(String redirectUrl) throws IOException {

        Request request = new Request.Builder()
                .url(redirectUrl)
                .header("User-Agent", "Mozilla/5.0")
                .build();

        client.newCall(request).execute();
    }

    private void login() {
        try {
            String loginPage = getLoginPage();
            String lt = extract(loginPage, "name=\"lt\" value=\"(.*?)\"");
            String execution = extract(loginPage, "name=\"execution\" value=\"(.*?)\"");

            RequestBody body = new FormBody.Builder()
                    .add("username", login)
                    .add("password", password)
                    .add("lt", lt)
                    .add("execution", execution)
                    .add("_eventId", "submit")
                    .add("embed", "true")
                    .build();

            Request request = new Request.Builder()
                    .url(LOGIN_URL)
                    .header("User-Agent", "Mozilla/5.0")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code() != 302) {
                throw new RuntimeException("Login failed");
            }

            String redirect = response.header("Location");

            exchangeTicket(redirect);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Authentication failed");
        }

    }

    @Override
    @SneakyThrows
    public GarminToken get() {
        MediaType mediaType = MediaType.parse("text/plain");


        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", login)
                .addFormDataPart("password", password)
                .addFormDataPart("_eventId", "submit")
                .addFormDataPart("embed", "true")
                .build();
        Request request = new Request.Builder()
                .url("https://sso.garmin.com/sso/signin?service=https://connect.garmin.com/modern&clientId=GarminConnect&gauthHost=https://sso.garmin.com/sso&consumeServiceTicket=false")
                .header("origin", "https://sso.garmin.com")
                .method("POST", body)
                .build();
        Response ssoResponse = client.newCall(request).execute();

        System.out.println("SSO Response code: " + ssoResponse.code());
        String responseBody = ssoResponse.body().string();

        // Check for authentication failures
        if (responseBody.contains("FAIL") || responseBody.contains("ACCOUNT_LOCKED")) {
            throw new RuntimeException("Authentication failed. Check credentials.");
        }
        if (responseBody.contains("renewPassword")) {
            throw new RuntimeException("Password renewal required.");
        }


        var redeemResponse = client.newCall(new Request.Builder()
                .url(baseUrl+"/modern")
                .get()
                .build()).execute().networkResponse();

        int maxRedirectCount = 7;
        int currentRedirectCount = 1;

        while (true) {
            var url = redeemResponse.header("location");
            if(url == null){
                break;
            }
            if(url.startsWith("/")) {
                url = baseUrl + url;
            }
            redeemResponse = client.newCall(new Request.Builder()
                    .url(url).get()
                    .build()
            ).execute().networkResponse();
            if(redeemResponse.isSuccessful()){
                break;
            }
            currentRedirectCount++;
            if(currentRedirectCount > maxRedirectCount) {
                break;
            }
        }

        System.out.println("Total redirects: " + currentRedirectCount);
        System.out.println("All cookies collected:");
        cookies.getCookies().forEach(cookie ->
            System.out.println("  - " + cookie.name() + " = " + cookie.value())
        );

        String sessionId = cookies.getCookies().stream()
                .filter(e->e.name().equals("SESSION"))
                .findFirst()
                .map(Cookie::value)
                .orElseThrow(() -> new RuntimeException("SESSIONID cookie not found. Available cookies: " +
                    cookies.getCookies().stream().map(Cookie::name).toList()));

        String ssoGuid = cookies.getCookies().stream()
                .filter(e->e.name().equals("GARMIN-SSO-GUID"))
                .findFirst()
                .map(Cookie::value)
                .orElseThrow(() -> new RuntimeException("GARMIN-SSO-GUID cookie not found. Available cookies: " +
                    cookies.getCookies().stream().map(Cookie::name).toList()));

        return new GarminToken(sessionId, ssoGuid);
    }
}
