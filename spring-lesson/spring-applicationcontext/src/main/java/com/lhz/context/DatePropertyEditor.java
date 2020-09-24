package com.lhz.context;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: lhz
 * @date: 2020/9/24
 **/
public class DatePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        System.out.println("text: " + text);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(text);
            this.setValue(date);
        } catch (Exception e) {

        }
    }
}
