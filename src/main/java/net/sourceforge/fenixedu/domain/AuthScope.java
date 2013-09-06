package net.sourceforge.fenixedu.domain;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.base.Joiner;

public class AuthScope extends AuthScope_Base {

    public final static String PERSONAL_SCOPE = "personal info";
    public final static String CURRICULUM_SCOPE = "curriculum";
    public final static String SCHEDULE_SCOPE = "schedule info";
    public final static String REGISTRATIONS_SCOPE = "registrations";

    public AuthScope() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public static AuthScope getAuthScope(String name) {
        for (AuthScope scope : RootDomainObject.getInstance().getAuthScopesSet()) {
            if (scope.getName().equals(name)) {
                return scope;
            }
        }
        return null;
    }

    public void setJerseyEndpoints(Collection<String> endpoints) {
        setEndpoints(Joiner.on("|").join(endpoints));
    }

    public Collection<String> getJerseyEndpoints() {
        return Arrays.asList(getEndpoints().split("|"));
    }

    public void changeJerseyEndpoints(Collection<String> endpoints) {
        Collection<String> currentEndpoints = getJerseyEndpoints();
        Collection<String> removedEndpoints = CollectionUtils.subtract(currentEndpoints, endpoints);
        Collection<String> newEndpoints = CollectionUtils.subtract(endpoints, currentEndpoints);
        if (!removedEndpoints.isEmpty() || !newEndpoints.isEmpty()) {
            notifyAllAppsScopeHasChanged();
        }
        setJerseyEndpoints(endpoints);
    }

    private void notifyAllAppsScopeHasChanged() {
        for (ExternalApplication app : getAppSet()) {
            app.scopeHasChanged();
        }
    }
}
