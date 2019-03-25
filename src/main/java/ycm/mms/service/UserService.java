package ycm.mms.service;

import ycm.mms.model.User;

public interface UserService {
	
	boolean addUser(User user);
	boolean updateUser(int id, String userName, String nickName, int sex);
	boolean updateUserIcon(int id, String iconPath);
	User getUser(int id);
	User getUserByAccountId(int accountId);

}
