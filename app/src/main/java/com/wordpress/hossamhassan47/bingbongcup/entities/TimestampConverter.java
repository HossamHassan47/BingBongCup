package com.wordpress.hossamhassan47.bingbongcup.entities;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Hossam on 3/7/2018.
 */

public class TimestampConverter {
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("Date Parse Error:" , e.getMessage());
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date value) {

        return value == null ? null : df.format(value);
    }
}
