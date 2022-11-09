package com.king.android.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToolFinish {
    public boolean isCanFinish() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date limitD = null;
        try {
            limitD = sdf.parse("2022-11-07");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        Calendar limitDate = Calendar.getInstance();
        limitDate.setTime(limitD);
        if (calendar.compareTo(limitDate)>0) {
            return true;
        }
        return false;
    }
}
