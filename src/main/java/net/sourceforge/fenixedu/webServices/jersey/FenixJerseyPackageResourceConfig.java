package net.sourceforge.fenixedu.webServices.jersey;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import javax.ws.rs.Path;

import net.sourceforge.fenixedu.domain.AuthScope;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.services.Service;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sun.jersey.api.core.PackagesResourceConfig;

public class FenixJerseyPackageResourceConfig extends PackagesResourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FenixJerseyPackageResourceConfig.class);

    public static Multimap<String, String> scopePathsMap = HashMultimap.create();

    public FenixJerseyPackageResourceConfig(Map<String, Object> props) {
        super(props);
        searchForAPIFenixScope();
    }

    @Service
    public static final void registerAuthScopes() {
        for (String scopeName : scopePathsMap.keySet()) {
            AuthScope authScope = AuthScope.getAuthScope(scopeName);
            Collection<String> endpoints = scopePathsMap.get(scopeName);
            if (authScope == null) {
                authScope = new AuthScope();
                authScope.setName(scopeName);
                authScope.setJerseyEndpoints(endpoints);
                LOGGER.info("add scope {} with endpoints {}", scopeName, Joiner.on(",").join(endpoints));
            } else {
                LOGGER.info("scope exists {}, change jersey endpoints {}", scopeName, Joiner.on(",").join(endpoints));
                authScope.changeJerseyEndpoints(endpoints);
            }
        }
    }

    private void searchForAPIFenixScope() {
        for (Class<?> clazz : getRootResourceClasses()) {
            Path pathAnnotation = clazz.getAnnotation(Path.class);
            if (pathAnnotation != null) {
                for (Method method : clazz.getMethods()) {
                    Path methodPathAnnotation = method.getAnnotation(Path.class);
                    FenixAPIScope apiScopeAnnotation = method.getAnnotation(FenixAPIScope.class);
                    if (apiScopeAnnotation != null) {
                        if (methodPathAnnotation != null) {
                            String path = ends(pathAnnotation.value());
                            String methodPath = ends(methodPathAnnotation.value());
                            String absolutePath = Joiner.on("/").join(path, methodPath);
                            String scopeName = apiScopeAnnotation.value();
                            scopePathsMap.put(scopeName, absolutePath);
                            LOGGER.debug("add {} to scope {}", absolutePath, scopeName);
                        } else {
                            LOGGER.debug("No path for method {}", method.getName());
                        }
                    } else {
                        LOGGER.debug("No apiScope for method {}", method.getName());
                    }
                }
            } else {
                LOGGER.debug("No path for class {}", clazz.getName());
            }
        }
    }

    private static String ends(String path) {
        return StringUtils.removeStart(StringUtils.removeEnd(path, "/"), "/");
    }

    public static AuthScope getScope(String endpoint) {
        for (String scopeName : scopePathsMap.keySet()) {
            if (scopePathsMap.get(scopeName).contains(endpoint)) {
                return AuthScope.getAuthScope(scopeName);
            }
        }
        return null;
    }
}
