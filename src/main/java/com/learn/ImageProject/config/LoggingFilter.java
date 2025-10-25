package com.learn.ImageProject.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        try {
            // Extract username before controller execution
            String username = null;
            boolean isMultipart = request.getContentType() != null
                    && request.getContentType().toLowerCase().startsWith("multipart/");

            if (isMultipart) {
                // Multipart: form fields are accessible via getParameter
                username = request.getParameter("username");
//                System.out.println("multipart username=" + username);
                ThreadContext.put("username", username != null ? username : "anonymous");
                filterChain.doFilter(request, response);    
                
            } else if ("GET".equalsIgnoreCase(request.getMethod())) {
                username = request.getParameter("username");

                ThreadContext.put("username", username != null ? username : "anonymous");
                filterChain.doFilter(request, response);   
                
            } else if ("POST".equalsIgnoreCase(request.getMethod())) {
                CachedHttpServletRequest wrappedRequest = new CachedHttpServletRequest(request);
            	
                byte[] buf = wrappedRequest.getCachedRequestMessage();
                if (buf.length > 0) {
                    String requestBody = new String(buf, StandardCharsets.UTF_8);
                    if (requestBody.contains("username")) {
                        username = requestBody.replaceAll(".*\"username\"\\s*:\\s*\"([^\"]+)\".*", "$1");
                    }
                }
                ThreadContext.put("username", username != null ? username : "anonymous");
                filterChain.doFilter(wrappedRequest, response);                
            }

            // Set MDC for log4j2


        } finally {
            ThreadContext.clearAll();
        }
    }
}
