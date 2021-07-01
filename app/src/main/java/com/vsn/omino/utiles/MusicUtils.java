package com.vsn.omino.utiles;


import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.concurrent.TimeUnit;

import static com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS;

public class MusicUtils {
    public static final int MAX_PROGRESS = 10000;

    public String milliSecondsToTimer(long j) {
        String stringBuilder;
        StringBuilder stringBuilder2;
        String stringBuilder3;
        int i = (int) (j / 3600000);
        j %= 3600000;
        int i2 = ((int) j) / 60000;
        int i3 = (int) ((j % DEFAULT_TRACK_BLACKLIST_MS) / 1000);
        String str = ":";
        String str2 = "";
        if (i > 0) {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(i);
            stringBuilder4.append(str);
            stringBuilder = stringBuilder4.toString();
        } else {
            stringBuilder = str2;
        }
        if (i3 < 10) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("0");
            stringBuilder2.append(i3);
            stringBuilder3 = stringBuilder2.toString();
        } else {
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append(str2);
            stringBuilder5.append(i3);
            stringBuilder3 = stringBuilder5.toString();
        }
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(stringBuilder);
        stringBuilder2.append(i2);
        stringBuilder2.append(str);
        stringBuilder2.append(stringBuilder3);
        return stringBuilder2.toString();
    }

    public int getProgressSeekBar(long j, long j2) {
        Double.valueOf(FirebaseRemoteConfig.DEFAULT_VALUE_FOR_DOUBLE);
        double d = (double) j;
        double d2 = (double) j2;
        Double.isNaN(d);
        Double.isNaN(d2);
        return Double.valueOf((d / d2) * 10000.0d).intValue();
    }

    public int progressToTimer(int i, int i2) {
        i2 /= 1000;
        double d = (double) i;
        Double.isNaN(d);
        d /= 10000.0d;
        double d2 = (double) i2;
        Double.isNaN(d2);
        return ((int) (d * d2)) * 1000;
    }

    public String convertDurationMillis(Integer getDurationInMillis){
        int getDurationMillis = getDurationInMillis;

        String convertHours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(getDurationMillis));
        String convertMinutes = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(getDurationMillis) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(getDurationMillis))); //I needed to add this part.
        String convertSeconds = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(getDurationMillis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getDurationMillis)));


        String getDuration = convertHours + ":" + convertMinutes + ":" + convertSeconds;

        return getDuration;

    }


}
