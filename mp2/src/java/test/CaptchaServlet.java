package test;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CaptchaServlet extends HttpServlet
{
	
	// Returns true if given two strings are same
	static boolean checkCaptcha(String captcha, String user_captcha)
	{
		return captcha.equals(user_captcha);
	}
	
	// Generates a CAPTCHA of given length
	static String generateCaptcha(int n)
	{
		//to generate random integers in the range [0-61]
		Random rand = new Random(62); 
		
		// Characters to be included
		String chrs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
		// Generate n characters from above set and
		// add these characters to captcha.
		String captcha = "";
		while (n-- > 0){
			int index = (int)(Math.random()*62);
			captcha+=chrs.charAt(index);
		}
		
		return captcha;
	}
        
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        
           HttpSession session = req.getSession();
            System.out.println(session.getAttribute("CaptchaString"));
            System.out.println((String)req.getParameter("cuser"));
               
                
                if(checkCaptcha((String)session.getAttribute("CaptchaString"),(String)req.getParameter("cuser"))){
                        res.sendRedirect("success.jsp");}
                    else{
                        session.setAttribute("CaptchaString", generateCaptcha(Integer.parseInt(getInitParameter("captchaLength"))));
                        res.sendRedirect("error_6.jsp");}
                
                
            
        }
        
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        HttpSession session = request.getSession();
        
        try{
            if(session!=null){
                
                session.setAttribute("CaptchaString", generateCaptcha(Integer.parseInt(getInitParameter("captchaLength"))));
                
                
                response.sendRedirect("captcha.jsp"); 
             
        }
        else{
            throw new NullValueException();
        }
       
        }catch(NullValueException e){
            System.err.println(e);
        }
        
    }
	

}