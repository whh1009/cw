package com.wuzhou.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.wuzhou.config.Constraint;

public class WZHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		request.setAttribute("title", Constraint.title);
		request.setAttribute("brand", Constraint.brand);
		nextHandler.handle(target, request, response, isHandled);
	}

}
