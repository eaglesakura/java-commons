package com.eaglesakura.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DateUtilTest {

    @Before
    public void setUp() throws Throwable {
        DateUtil.setTimeFunction(new DateUtil.TimeFunction() {
            @Override
            public long currentTimeMillis() {
                return System.currentTimeMillis();
            }
        });
    }

    @Test
    public void 現在時刻をチェックする() throws Throwable {
        long nowByDateUtil = DateUtil.currentTimeMillis();
        long nowBySystem = System.currentTimeMillis();
        assertEquals(nowByDateUtil, nowBySystem);
    }

    @Test
    public void 現在時刻を上書きできる() throws Throwable {
        DateUtil.setTimeFunction(new DateUtil.TimeFunction() {
            @Override
            public long currentTimeMillis() {
                return 0;
            }
        });

        assertEquals(DateUtil.currentTimeMillis(), 0);
    }

    @Test
    public void タイムゾーンをチェックする() {
//        assertEquals(TimeZone.getDefault().getRawOffset(), 1000 * 60 * 60 * 9);
//        assertEquals(DateUtil.getYear(), 2016);
//        assertEquals(DateUtil.getMonth(), 3);
//        assertEquals(DateUtil.getDay(), 16);
    }

    void checkDate(String dateString, int year, int month, int day) {
        Date date = DateUtil.parseDateISO8601(dateString, TimeZone.getTimeZone("Asia/Tokyo"));
        assertNotNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        assertEquals(DateUtil.getYear(calendar), year);
        assertEquals(DateUtil.getMonth(calendar), month);
        assertEquals(DateUtil.getDay(calendar), day);
    }

    @Test
    public void ISO8601フォーマットの解析が行える() {
        checkDate("2016-04-28T00:00:00", 2016, 4, 28);   // 時差補正が無いなら現在のタイムゾーンとして扱う
        checkDate("2016-04-28T23:00:00", 2016, 4, 28);   // 時差補正が無いなら現在のタイムゾーンとして扱う
        checkDate("2016-04-28T00:00:00+00:00", 2016, 4, 28);   // ISO-8601 Format
        checkDate("2016-04-28T23:00:00+00:00", 2016, 4, 29);   // 時差によって日付がずれる
        checkDate("2016-12-31T00:00:00+09:00", 2016, 12, 31);   // 時差が現地時刻で書き込まれている
        checkDate("2016-12-31T23:00:00+09:00", 2016, 12, 31);   // 時差が現地時刻で書き込まれている

        checkDate("2016-12-31T00:00:00-09:00", 2016, 12, 31);   // 負の時差を扱える
        checkDate("2016-12-31T23:00:00-09:00", 2017, 1, 1);   // 負の時差を扱える
    }
}
