package ycm.mms.model;

import java.sql.Timestamp;

public class Session {
	private int id;
    private int accountId;
    private String sign;
    private Timestamp expiredTime;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Timestamp getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(Timestamp expiredTime) {
		this.expiredTime = expiredTime;
	}
	 
}
