package ycm.mms.task;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ycm.mms.service.AccountService;
import ycm.mms.util.SpringContextHolder;

@Component
public class UserSessionCheckTask {

	// 每天执行一次
	@Scheduled(fixedDelay = 86400000)
	public void taskCycle() {
		System.out.println("========UserSessionCheckTask Start========");
		
		ApplicationContext appContext = SpringContextHolder.getApplicationContext();
		AccountService accountService = (AccountService)appContext.getBean("accountService");
		
		// 过期时间为1天
		accountService.deleteExpiredSession(86400000);
	}
}
