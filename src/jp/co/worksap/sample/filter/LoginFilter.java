package jp.co.worksap.sample.filter;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.remoteType;

public class LoginFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session =req.getSession(true);
		session.setAttribute("ajaxStatus", "ok");
		String url = req.getRequestURI();
		if(url.endsWith("Login.jsf")){
			String role=(String) session.getAttribute("role");
//			if(role!=null){
//				if("consultant manager".contains(role)){
//					res.sendRedirect("http://localhost:9090"+req.getContextPath()+"/user/main.jsf");	
//				}else if(role.equals("cand")){
//					res.sendRedirect("http://localhost:9090"+req.getContextPath()+"/cand/candPortal.jsf");	
//				}else if(role.equals("client")){
//					res.sendRedirect("http://localhost:9090"+req.getContextPath()+"/client/candPortal.jsf");
//				}
//				return;
//			}
				chain.doFilter(request, response);
				return;
			
		}
		String id = req.getParameter("id");
		String pwd =req.getParameter("pwd");
		try {
		if(id!=null && pwd!=null){
			   if(url.contains("/client/") ){
				   res.sendRedirect("clientLogin.jsf" + "?id=" + id + "&pwd="+pwd );
				   return;
			   }else{
				res.sendRedirect("candLogin.jsf" + "?id=" + id + "&pwd="+pwd );	
				return;
			   }
		}else {
			 if(url.contains("/client/") && 
					 (session.getAttribute("role")==null       
					  ||!session.getAttribute("role").equals("client"))){
				 if(!ajaxCheck((HttpServletRequest)request , session)){
				 res.sendRedirect("../client/clientLogin.jsf");
				 return;
				 }
			 }
			 else if(url.contains("/cand/") && 
					 (session.getAttribute("role")==null       
					  || !session.getAttribute("role").equals("cand"))){
				 if(!ajaxCheck((HttpServletRequest)request , session)){
				 res.sendRedirect("../cand/candLogin.jsf");
				 return;
				 }
			 }
			 else if(url.contains("/user/")
					 && 
					 (session.getAttribute("role")==null       
					  || !"consultant manager".contains((String)session.getAttribute("role")))){
				 if(!ajaxCheck((HttpServletRequest)request , session)){
				 res.sendRedirect("../user/userLogin.jsf");
				 return;
				 }
				
			 }else if(session.getAttribute("role")==null       
					  || (!"consultant manager".contains((String)session.getAttribute("role"))&& url.endsWith(req.getContextPath() +"/")) ){
				// FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:9090"+req.getContextPath()+"/user/userLogin.jsf")
				 res.sendRedirect("http://localhost:9090"+req.getContextPath()+"/user/userLogin.jsf");	
				 return;
			 }
			
		}
		chain.doFilter(request, response);
		return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
    public boolean ajaxCheck(HttpServletRequest request , HttpSession session){
    	String requestType = request.getHeader("X-Requested-With");
    	if(requestType!=null &&requestType.equals("XMLHttpRequest")){
    		session.setAttribute("ajaxStatus", "wrong");
    		return true;
    	}
    	else{
    		return false;
    	}
    }
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("tst");
		
	}
	
}
