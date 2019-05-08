package ycm.mms.model;

import java.sql.Timestamp;

/**
 * 用户信息实体
 * @author Chunmeng
 */
public class User {

    private int id;
    private int accountId;
    private String userName;
    private String nickName;
    private byte[] icon;
    private String iconSuffix;
    private int sex;
    private Timestamp createTime;
    private Timestamp modifyTime;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
	
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Timestamp getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public byte[] getIcon() {
		return icon;
	}
	public void setIcon(byte[] icon) {
		this.icon = icon;
	}
	public String getIconSuffix() {
		return iconSuffix;
	}
	public void setIconSuffix(String iconSuffix) {
		this.iconSuffix = iconSuffix;
	}
    
}