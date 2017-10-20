package com.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


public class ResponseUtil {

	public static void write(HttpServletResponse response,Object o,String callback){
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out= null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String output = o.toString();
		if(callback!=null){
			output = callback+"("+output+");";
		}
		out.println(output);
		out.flush();
		out.close();
//		try {
//			DbUtil.getCon().close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
}
