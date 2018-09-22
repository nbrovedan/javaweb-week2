package br.com.voffice.java.jwptf02.week2.application.controllers;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "/*" })
public class CorsFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;

		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
		((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods",
				"GET, OPTIONS, HEAD, PUT, POST");

		HttpServletResponse resp = (HttpServletResponse) servletResponse;

		if (request.getMethod().equals("OPTIONS")) {
			resp.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}

		chain.doFilter(request, servletResponse);
	}

}
