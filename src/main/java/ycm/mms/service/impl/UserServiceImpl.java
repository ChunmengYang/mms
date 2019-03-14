package ycm.mms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ycm.mms.dao.UserMapper;
import ycm.mms.model.User;
import ycm.mms.service.UserService;


@Service("userService")
public class UserServiceImpl implements UserService {
    
    private UserMapper userMapper;

    public UserMapper getUserMapper() {
        return userMapper;
    }
    
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    @Transactional
	@Override
	public boolean addUser(User user) {
		// TODO Auto-generated method stub
    	int status = userMapper.insert(user);
    	if (status == 1) {
			return true;
		}
    	return false;
	}
	
	@Transactional
	@Override
	public boolean updateUser(int id, String userName, String nickName, int sex) {
		// TODO Auto-generated method stub
		User user = userMapper.query(id);
		if (user != null) {
			if (userName != null && !"".equals(userName)) {
				user.setUserName(userName);
			}
			if (nickName != null && !"".equals(nickName)) {
				user.setNickName(nickName);
			}
			user.setSex(sex);
			
			int status = userMapper.update(user);
			if (status == 1) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		return userMapper.query(id);
	}
	
}