package com.weihuagu.receiptnotice;

import java.util.List;

public interface AsyncResponse {
	public void onDataReceivedSuccess(String returnstr);
    public  void onDataReceivedFailed();
}
