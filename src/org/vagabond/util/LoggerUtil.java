/**
 * 
 */
package org.vagabond.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.log4j.Logger;


/**
 *
 * Part of Project SesamUtil
 * @author Boris Glavic
 *
 */
public class LoggerUtil {

	public static void logException (Exception e, Logger log) {
		log.error(getCompleteTrace(e));
	}
	
	public static void logDebugException (Exception e, Logger log) {
		log.debug(getCompleteTrace(e));
	}
	
	public static String getCompleteTrace (Exception e) {
		StringBuilder trace;
		Throwable exception;
		
		trace = new StringBuilder ();
		trace.append(getStackString (e));
		exception = e;
		while (exception.getCause() != null) {
			exception = exception.getCause();
			trace.append("\ncaused by:\n");
			trace.append(getStackString (exception));
		}
		
		return trace.toString();
	}
	
	private static String getStackString (Throwable e) {
		StringBuilder stackString;
		StackTraceElement[] stack;
		
		stack = e.getStackTrace();
		stackString = new StringBuilder ();
		stackString.append("Exception occured: " + e.getClass().getName() + "\n");
		stackString.append("Message: " + e.getMessage() + "\n");
		for (int i = 0; i < stack.length; i++) {
			stackString.append(stack[i].toString() + "\n");
		}
		return stackString.toString();
	}
	
	public static String arrayToString (String[] array) {
		StringBuilder result;
		
		if (array.length == 0)
			return "";
		
		result = new StringBuilder();
		
		for (String elem: array) {
			result.append("'" + elem + "',");
		}
		result.deleteCharAt(result.length() - 1);
		
		return result.toString();
	}
	
	public static void logArray (Logger log, Object[] array) {
		logArray(log, array, null);
	}
		
	public static void logArray (Logger log, Object[] array, String message) {
		StringBuffer result = new StringBuffer();
		
		if (message != null)
			result.append(message + ":\n");
		
		for (Object o : array) {
			result.append(o.toString() + ",");
		}
		result.deleteCharAt(result.length() - 1);
		
		log.debug(result.toString());
	}
	
	public static void logObjectColWithMethod (Logger log, Collection<?> objs,
			Class<?> objClass, String methodName) throws  Exception {
		log.debug(ObjectColToStringWithMethod(objs,objClass, methodName));
	}
	
	public static String ObjectColToStringWithMethod (Collection<?> objs, 
			Class<?> objClass, String methodName) throws Exception {
		StringBuffer result = new StringBuffer();
		Method method;
		String callResult;
		
		method = objClass.getMethod(methodName);
		
		result.append('[');
		for(Object elem: objs) {
			if (result.length() != 1)
				result.append(',');
			callResult = (String) method.invoke(elem);
			result.append(callResult);
		}
		result.append(']');
		
		return result.toString();
	}
	
}
