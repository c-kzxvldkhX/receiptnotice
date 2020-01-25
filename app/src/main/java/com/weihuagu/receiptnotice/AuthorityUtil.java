package com.weihuagu.receiptnotice;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

public class AuthorityUtil {
    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = "com.weihuagu.receiptnotice/com.weihuagu.receiptnotice.ReceiptnoticeAccessibilityService";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            LogUtil.debugLog("accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            LogUtil.debugLog("Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            LogUtil.debugLog("***ACCESSIBILIY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    LogUtil.debugLog("绑定的accessabilityService :: " + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        LogUtil.debugLog("accessibility 被启用");
                        return true;
                    }
                }
            }
        } else {
            LogUtil.debugLog("***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }
}
