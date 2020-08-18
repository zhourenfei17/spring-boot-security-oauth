package com.jackeroo.oauth.controller;

import com.jackeroo.oauth.adapter.ClientDetailsAdapter;
import com.jackeroo.oauth.service.OauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author alex
 * @date 2020/08/11
 */
@Controller
@SessionAttributes("authorizationRequest")
public class ApprovalController {

    @Autowired
    private OauthClientDetailsService clientDetailsService;

    /**
     * 自定义批准页面
     * @param model
     * @return
     */
    @RequestMapping("approval")
    public ModelAndView approval(Map<String, Object> model){
        AuthorizationRequest authorizationRequest = (AuthorizationRequest)model.get("authorizationRequest");

        ModelAndView view = new ModelAndView();
        view.setViewName("approval");

        ClientDetailsAdapter clientDetails = (ClientDetailsAdapter)clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());

        Set<String> scope = authorizationRequest.getScope();
        if(scope.contains("read")){
            view.addObject("get", true);
        }else{
            view.addObject("get", false);
        }
        if(authorizationRequest.getAuthorities().contains(new SimpleGrantedAuthority("admin"))
                && scope.contains("write")){
            view.addObject("update", true);
        }else{
            view.addObject("update", false);
        }

        if(clientDetails != null){
            view.addObject("appName", clientDetails.getClientDetails().getAppName());
        }

        return view;
    }
}
