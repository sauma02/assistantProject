/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assisantsProject.asistantProject.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *
 * @author USUARIO
 */

public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    protected Log logger = LogFactory.getLog(this.getClass());
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public CustomSuccessHandler() {
    }
//Se crean los metodos para configurar la redireccion por rol, en este caso se crea un logger y una estrategia de redireccion. se crea
    //un metodo handle que sera el cual va tomar los datos directos para que se logre la autenticacion
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }
    //Se crea la string para determinar la url que se quiere, se crea un Map con los keys y su valor para que se determine a donde ir 
    //cuando sea admin y cuando sea user, se crea una collection con las authorities actuales y se busca co un metodo for dentro del mapa,
    //si existe que retorne una excepcion y si no que retorne el valor
    
    protected String determineTargetUrl(Authentication authentication){
        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put("asistente", "/");
        roleTargetUrlMap.put("usuario", "/");
        roleTargetUrlMap.put("admin", "/");
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if(roleTargetUrlMap.containsKey(authorityName)){
                return roleTargetUrlMap.get(authorityName);
            }
            
            
        }
        throw new IllegalStateException();
    }
    public String determineTargetUrlForAuthentication(Authentication authentication){
        return determineTargetUrl(authentication);
    }
    protected void clearAuthenticationAttributes(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)throws IOException, ServletException{
        String targetUrl = determineTargetUrl(authentication);
        if(response.isCommitted()){
            logger.debug("Response has already been commited, unable to redirect to"
                    +targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
