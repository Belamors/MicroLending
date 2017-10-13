package einars.homework.microlending.aop.rest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class RequestLimitAspect {

	@Before("execution(createLoan())")
	public void checkRequestLimit() throws Exception {
		throw new Exception("WORKS!!!!!!!!!!!!!!!!!!!!");
	}
	
}
