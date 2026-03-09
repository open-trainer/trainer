package org.opentrainer.garmin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Request;
import org.opentrainer.garmin.dto.HeartRateDay;
import org.opentrainer.garmin.exception.RateLimitGarminConnectException;
import org.opentrainer.garmin.exception.SessionExpiredGarminConnectException;
import org.opentrainer.garmin.exception.UnknownGarminConnectException;
import org.opentrainer.garmin.token.TokenSupplier;
import org.opentrainer.garmin.gson.LocalDateAdapter;
import org.opentrainer.garmin.gson.LocalDateTimeAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Jakub Trzcinski kuba@valueadd.pl
 * @since 26-12-2020
 */
class HeartRateRepository extends BaseRepository {
    private final Gson gson;

    public HeartRateRepository(TokenSupplier tokenSupplier) {
        super(tokenSupplier);
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    /**
     * @throws SessionExpiredGarminConnectException if token is expired
     * @throws RateLimitGarminConnectException if you're sending tooo much requests :)
     * @throws UnknownGarminConnectException if something wrong went :(
     */
    public HeartRateDay getHeartRate(LocalDate date) {
        Request request = get("https://connect.garmin.com/modern/proxy/wellness-service/wellness/dailyHeartRate?date=" + date.toString());

        var rawJson = send(request);

        return gson.fromJson(rawJson.getRawResponse(), HeartRateDay.class);
    }
}
