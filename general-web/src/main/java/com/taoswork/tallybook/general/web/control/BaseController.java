package com.taoswork.tallybook.general.web.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Gao Yuan on 2015/5/6.
 */
public abstract class BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    public static final String AJAX_VIEW_NAME_PREFIX = "ajax:";
    public static final String AJAX_REQUEST_KEY = "ajax";
    public static final String SIMPLE_VIEW_REQUEST_KEY = "simpleView";
    public static String ajaxViewName(String viewName){
        return AJAX_VIEW_NAME_PREFIX + viewName;
    }

    public BaseController(){
    }

    protected String getPathVariable(Map<String, String> pathVars, String pathKey) {
        return pathVars.get(pathKey);
    }

    public static boolean isSimpleViewRequest(HttpServletRequest request) {
        return isSimpleViewRequest(request, SIMPLE_VIEW_REQUEST_KEY);
    }

    public static boolean isSimpleViewRequest(HttpServletRequest request, String modalKey) {
        boolean isModalByUrl = false;
        if(!StringUtils.isEmpty(modalKey)){
            String isModalString = request.getParameter(modalKey);
            isModalByUrl = ("".equals(isModalString) || "true".equals(isModalString));
            if(isModalByUrl)
                return true;
        }
        String requestedWithHeader = request.getHeader("RequestInSimpleView");
        boolean result = "true".equals(requestedWithHeader);

        return result;
    }

    public static boolean isAjaxDataRequest(HttpServletRequest request){
        if(isAjaxRequest(request) && (!isSimpleViewRequest(request))){
            return true;
        }
        return false;
    }

    public static boolean isAjaxRequest(HttpServletRequest request){
        return isAjaxRequest(request, AJAX_REQUEST_KEY);
    }

    public static boolean isAjaxRequest(HttpServletRequest request, String ajaxKey) {
        boolean isAjaxByUrl = false;
        if(!StringUtils.isEmpty(ajaxKey)){
            String isAjaxString = request.getParameter(ajaxKey);
            isAjaxByUrl = "true".equals(isAjaxString);
            if(isAjaxByUrl)
                return true;
        }
        String requestedWithHeader = request.getHeader("X-Requested-With");
        boolean result = "XMLHttpRequest".equals(requestedWithHeader);

        return result;
    }

    protected DataMapBuilder makeDataMapBuilder(String attributeName){
        return new DataMapBuilder(attributeName, getObjectMapper());
    }
    protected abstract ObjectMapper getObjectMapper();

    protected String getObjectInJson(Object data) {
        try {
            return getObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException exp) {
            throw new RuntimeException(exp);
        }
    }

    protected static URI uriFromRequest(HttpServletRequest request){
        try {
            URI uriobj = new URI(null, null, request.getRequestURI(), request.getQueryString(), null);
            return uriobj;
        } catch (URISyntaxException e) {
            String query = request.getQueryString();
            String uri = request.getRequestURI();
            if(StringUtils.isEmpty(query)){
                return URI.create(uri);
            }else {
                return URI.create(uri + "?" + query);
            }
        }
    }

    protected Locale getLocale(HttpServletRequest request){
        return request.getLocale();
    }
}
