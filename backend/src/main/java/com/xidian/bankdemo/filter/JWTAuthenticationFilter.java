package com.xidian.bankdemo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xidian.bankdemo.config.SecurityConfig;
import com.xidian.bankdemo.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = request.getRequestURI().replaceFirst("/bank","");//TODO:动态获取项目前缀
        String header = request.getHeader(TokenUtil.AUTHORIZATION);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        Map<String, String> json=new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();

        if(Arrays.asList(SecurityConfig.AUTH_WHITELIST).contains(url)){//跳过不需要验证的路径
            chain.doFilter(request, response);
            return;
        }
        if (StringUtils.isBlank(header) || !header.startsWith(TokenUtil.TOKEN_PREFIX)) {
            json.put("message", "Token为空");
            response.getWriter().write(mapper.writeValueAsString(json));
            response.setStatus(403);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request,response);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);

        }catch (ExpiredJwtException e) {
            json.put("message", "Token已过期");
            response.getWriter().write(mapper.writeValueAsString(json));
            response.setStatus(403);
            logger.error("Token已过期: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            json.put("message", "Token格式错误");
            response.getWriter().write(mapper.writeValueAsString(json));
            response.setStatus(403);
            logger.error("Token格式错误: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            json.put("message", "Token没有被正确构造");
            response.getWriter().write(mapper.writeValueAsString(json));
            response.setStatus(403);
            logger.error("Token没有被正确构造: {}", e.getMessage());
        } catch (SignatureException e) {
            json.put("message", "Token签名失败");
            response.getWriter().write(mapper.writeValueAsString(json));
            response.setStatus(403);
            logger.info("签名失败: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            json.put("message", "Token非法参数异常");
            response.getWriter().write(mapper.writeValueAsString(json));
            response.setStatus(403);
            logger.error("非法参数异常: {}", e.getMessage());
        }catch (Exception e){
            json.put("message", "Invalid Token");
            response.getWriter().write(mapper.writeValueAsString(json));
            response.setStatus(403);
            logger.error("Invalid Token" + e.getMessage());
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request,HttpServletResponse response)  {
        String token = request.getHeader(TokenUtil.AUTHORIZATION);
        if (token != null) {
            String username = TokenUtil.validateToken(token);
            if (StringUtils.isNotBlank(username)) {
                return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());//用户名，密码，权限
            }
            return null;
        }
        return null;
    }

}
