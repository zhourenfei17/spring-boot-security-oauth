package com.jackeroo.oauth.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 日志过滤器
 */
@Slf4j
@Aspect
@Component
public class LogFilter {

	private static final ObjectMapper mapper = new ObjectMapper();


	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void requestMapping() {
	}

	@Before("requestMapping()")
	public void doBefore(JoinPoint joinPoint) {
		org.springframework.web.context.request.RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		log.info("===================== request start =====================", new Object[0]);
		log.info("URL:[ {} ]", new Object[] { request.getRequestURL() });
		// log.info("Token=[ {} ]", new Object[] { request.getHeader("Token") });
		printMap("Request Params", request.getParameterMap());
		log.info("===================== request end =====================\n", new Object[0]);
	}

	@AfterReturning(pointcut = "requestMapping()", returning = "returnValue")
	public JoinPoint afterReturning(JoinPoint joinPoint, Object returnValue) {
		log.info("===================== return start =====================", new Object[0]);

		org.springframework.web.context.request.RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		log.info("请求结束： URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
		         request.getRequestURI(), Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime.getRuntime().totalMemory() / 1024 / 1024,
		         Runtime.getRuntime().freeMemory() / 1024 / 1024,
		         (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);

		log.info("===================== return end =====================\n", new Object[0]);
		return joinPoint;
	}

	@AfterThrowing(pointcut = "requestMapping()", throwing = "e")
	public void afterThrowing(Throwable e) {
		log.error("ERROR:", new Object[] { e });
	}

	private void printMap(String prefix, Map map) {
		StringBuilder buf = new StringBuilder();
		for (Object o : map.entrySet()) {
			Map.Entry entry = (Map.Entry) o;
			buf.append("  ");
			buf.append((String) entry.getKey());
			buf.append(" = ");
			try {
				buf.append(mapper.writeValueAsString(entry.getValue()));
				buf.append("\n");
			}
			catch (IOException e) {
				log.error("print map error", new Object[] { e.getMessage() });
			}
		}

		if (buf.length() > 0)
			log.info("{}:\n{}", new Object[] { prefix, buf.substring(0, buf.length() - 1) });
	}
}
