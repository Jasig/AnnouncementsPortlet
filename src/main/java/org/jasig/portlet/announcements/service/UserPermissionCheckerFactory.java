package org.jasig.portlet.announcements.service;

import javax.portlet.PortletRequest;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.jasig.portlet.announcements.model.Topic;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionCheckerFactory implements InitializingBean{
    private static final Logger logger = Logger.getLogger(UserPermissionCheckerFactory.class);
    private static final String CACHE_KEY_DELIM = "|";
    private static final String CACHE_NAME = "userPermissionCheckerCache";

    private CacheManager cacheManager = null;
    private Cache cache = null;

    public UserPermissionCheckerFactory() {

    }

    public UserPermissionChecker createUserPermissionChecker(PortletRequest request,Topic topic) {
        synchronized(cache) {
            String key = getCacheKey(request,topic);
            Element element = cache.get(key);
            if(element == null) {
                if(logger.isTraceEnabled()) {
                    logger.trace("Creating cache entry for " + key);
                }
                UserPermissionChecker value = new UserPermissionChecker(request,topic);
                cache.put(new Element(key,value));
                return value;

            } else {
                if(logger.isTraceEnabled()) {
                    logger.trace("Successfully retrieved cache entry for " + key);
                }
                return (UserPermissionChecker)element.getObjectValue();
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            throw new BeanCreationException("Required " + CACHE_NAME + " could not be loaded.");
        }
        else {
            if(logger.isDebugEnabled()) {
                logger.debug(CACHE_NAME + " created.");
            }
        }
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private String getCacheKey(PortletRequest request,Topic topic) {
        String username = getPortletRequestUsername(request);
        return new StringBuilder(username).append(CACHE_KEY_DELIM).append(topic.getTitle()).toString();
    }

    private String getPortletRequestUsername(PortletRequest request) {
        String username = request.getRemoteUser();
        return ((username == null) || (username.isEmpty()) ? "guest" : username);
    }
}