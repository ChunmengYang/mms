package ycm.mms.service;

import ycm.mms.model.User;

public interface UserService {
	
	boolean addUser(User user);
	boolean updateUser(int id, String userName, String nickName, int sex);
	User getUser(int id);

}
