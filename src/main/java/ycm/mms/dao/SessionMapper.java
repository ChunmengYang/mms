package ycm.mms.dao;

import ycm.mms.model.Session;

public interface SessionMapper {
   int insert(Session session);
   
   int update(Session session);
   
   int delete(int accountId);
   
   Session queryByAccountId(int accountId);
   
   Session queryBySign(String sign);
}
