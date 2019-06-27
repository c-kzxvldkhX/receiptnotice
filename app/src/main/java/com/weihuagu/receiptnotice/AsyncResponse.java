package com.weihuagu.receiptnotice;

import java.util.List;
import java.util.Map;

public interface AsyncResponse {
	public void onDataReceivedSuccess(String[] returnstr);
    public void onDataReceivedFailed(String[] returnstr,Map<String ,String> postedmap);
}
