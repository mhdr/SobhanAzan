package com.nasimeshomal;

/**
 * Created by ma.ramezani on 2/28/2016.
 */
public class Fa2En {
    public static String Convert(String text)
    {
        String value=text;

        value=value.replace("۰","0");
        value=value.replace("۱","1");
        value=value.replace("۲","2");
        value=value.replace("۳","3");
        value=value.replace("۴","4");
        value=value.replace("۵","5");
        value=value.replace("۶","6");
        value=value.replace("۷","7");
        value=value.replace("۸","8");
        value=value.replace("۹","9");

        return value;
    }
}
