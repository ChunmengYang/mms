package ycm.mms.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logger {
	
	@Pointcut("execution(* ycm.mms.controller.*.*(..))")
	public void info() {}
	
	@Before("info()")
    public void doBefore(JoinPoint joinPoint){
        System.out.println("===========AOP Before Advice...===========");
    }
    
    @After("info()")
    public void doAfter(JoinPoint joinPoint){
        System.out.println("===========AOP After Advice...===========");
    }
    
    // 除了可以通过名字来指向对应的切入点表达式,还可以可以使用'&&', '||' 和 '!'来合并。
    // 切入点表达式的 args(user,..) 表示某个与切入表达式匹配的连接点它把User对象作为第一个参数, 通过这个语法我们可以在通知中访问到这个User对象。
    @Around("info()&&" +"args(id,..)")
	public Object doAround(ProceedingJoinPoint  joinPoint, int id) throws Throwable{
    	System.out.println("===========AOP Around Start Advice...===========");
    	
    	// 当定义一个Around增强处理方法时，该方法的第一个形参必须是ProceedJoinPoint类型（至少含有一个形参），
    	// 在增强处理方法体内，调用ProceedingJoinPoint参数的procedd()方法才会执行目标方法——这就是Around增强处理可以完全控制方法的执行时机、如何执行的关键；
    	// 如果程序没有调用ProceedingJoinPoint参数的proceed()方法，则目标方法不会被执行。目标方法有参数和返回值，该方法也必须有。
    	Object rvt = joinPoint.proceed(joinPoint.getArgs());
    	
    	System.out.println("===========AOP Around End Advice...===========");
    	
    	return rvt;
	}
}