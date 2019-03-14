package ycm.mms.dao;

import ycm.mms.model.User;

public interface UserMapper {
	
   int insert(User user);
   
   int update(User user);
   
   int verify(int id);
   
   User query(int id);
   
   User queryByAccountId(int accountId);
}
