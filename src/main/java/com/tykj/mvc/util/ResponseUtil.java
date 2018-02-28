package com.tykj.mvc.util;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-28 11:10
 **/
public class ResponseUtil {

	private static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";

	public static void writeMsg(HttpServletResponse response, String msg) {

		try {
			PrintWriter writer = response.getWriter();
			response.setContentType(DEFAULT_CONTENT_TYPE);
			writer.write(msg);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
