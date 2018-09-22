package com.holla.group1.holla;

import android.text.Layout;
import android.widget.TextView;

public class Utils {
    public static Boolean isTextViewTruncated(TextView textView) {
        if (textView != null) {
            Layout layout = textView.getLayout();
            if (layout != null) {
                int lines = layout.getLineCount();
                if (lines > 0) {
                    int ellipsisCount = layout.getEllipsisCount(lines - 1);
                    if (ellipsisCount > 0) {
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
