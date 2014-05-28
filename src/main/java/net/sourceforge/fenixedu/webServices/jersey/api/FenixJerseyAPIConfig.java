/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.webServices.jersey.api;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Path;

import net.sourceforge.fenixedu.domain.AuthScope;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

public class FenixJerseyAPIConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FenixJerseyAPIConfig.class);

    public static Multimap<String, String> scopePathsMap = HashMultimap.create();

    public static Set<String> publicScopes = new HashSet<>();

    public static void initialize() {
        searchForAPIFenixScope(FenixAPIv1.class);
        registerAuthScopes();
    }

    @Atomic
    public static final void registerAuthScopes() {
        for (String scopeName : scopePathsMap.keySet()) {
            AuthScope authScope = AuthScope.getAuthScope(scopeName);
            Collection<String> endpoints = scopePathsMap.get(scopeName);
            if (authScope == null) {
                authScope = new AuthScope();
                authScope.setName(scopeName);
                authScope.setJerseyEndpoints(endpoints);
                LOGGER.debug("add scope {} with endpoints {}", scopeName, Joiner.on(",").join(endpoints));
            } else {
                LOGGER.debug("scope exists {}, change jersey endpoints {}", scopeName, Joiner.on(",").join(endpoints));
                authScope.changeJerseyEndpoints(endpoints);
            }
        }
        removeUnusedAuthScopes();
    }

    @Atomic
    private static void removeUnusedAuthScopes() {
        final Set<AuthScope> authScopes = Bennu.getInstance().getAuthScopesSet();
        ImmutableSet<String> authScopesNames = FluentIterable.from(authScopes).transform(new Function<AuthScope, String>() {

            @Override
            public String apply(AuthScope scope) {
                return scope.getName();
            }
        }).toSet();

        Collection<String> unusedScopes = CollectionUtils.subtract(authScopesNames, scopePathsMap.keySet());
        for (String unusedScope : unusedScopes) {
            AuthScope authScope = AuthScope.getAuthScope(unusedScope);
            if (authScope != null) {
                LOGGER.debug("delete unused scope {}", unusedScope);
                authScope.delete();
            }
        }
    }

    private static void searchForAPIFenixScope(Class<?> apiClass) {
        if (apiClass != null) {
            Path pathAnnotation = apiClass.getAnnotation(Path.class);
            if (pathAnnotation != null) {
                for (Method method : apiClass.getMethods()) {
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
                        FenixAPIPublic publicAPIAnnotation = method.getAnnotation(FenixAPIPublic.class);
                        if (publicAPIAnnotation != null) {
                            if (methodPathAnnotation != null) {
                                String path = ends(pathAnnotation.value());
                                String methodPath = ends(methodPathAnnotation.value());
                                String absolutePath = Joiner.on("/").join(path, methodPath);
                                publicScopes.add(absolutePath);
                                LOGGER.debug("add public endpoint {}", absolutePath);
                            }
                        }
                    }
                }
            } else {
                LOGGER.debug("No api class");
            }
        }
    }

    private static String ends(String path) {
        return StringUtils.removeStart(StringUtils.removeEnd(path, "/"), "/");
    }

    public static AuthScope getScope(String endpoint) {
        for (String scopeName : scopePathsMap.keySet()) {
            for (String scopeEndpoint : scopePathsMap.get(scopeName)) {
                if (endpointMatches(endpoint, scopeEndpoint)) {
                    return AuthScope.getAuthScope(scopeName);
                }
            }
        }
        return null;
    }

    public static boolean isPublicScope(String endpoint) {
        endpoint = ends(endpoint);
        LOGGER.debug("check public {}", endpoint);
        for (String publicEndpoint : publicScopes) {
            if (endpointMatches(endpoint, publicEndpoint)) {
                return true;
            }
        }
        return false;
    }

    public static boolean endpointMatches(String endpoint, String jerseyEndpoint) {
        String regexpEndpoint = jerseyEndpoint.replaceAll("\\{[a-z]+\\}", ".+");
        if (endpoint.matches(regexpEndpoint)) {
            return true;
        }
        return false;
    }
}
