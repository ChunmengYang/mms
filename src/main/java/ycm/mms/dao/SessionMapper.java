package ycm.mms.dao;

import java.sql.Timestamp;

import ycm.mms.model.Session;

public interface SessionMapper {
   int insert(Session session);
   
   int update(Session session);
   
   int delete(int accountId);
   
   void deleteExpired(Timestamp time);
   
   Session queryByAccountId(int accountId);
   
   Session queryBySign(String sign);
}
