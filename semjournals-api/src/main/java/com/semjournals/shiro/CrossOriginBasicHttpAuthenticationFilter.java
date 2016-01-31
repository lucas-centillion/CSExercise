package com.semjournals.shiro;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Necessary to make Shiro work with CORS requests
 */
public class CrossOriginBasicHttpAuthenticationFilter extends BasicHttpAuthenticationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		if ("OPTIONS".equals(httpRequest.getMethod())) {
			return true;
		}
		return super.isAccessAllowed(request, response, mappedValue);
	}
}