package pt.ist.fenixframework.plugins.remote.domain;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.Strings;

public class RemoteHost extends RemoteHost_Base {

    public RemoteHost() {
        super();
        setRemoteSystem(RemoteSystem.getInstance());
        setAllowInvocationAccess(Boolean.FALSE);
    }

    public RemoteHost(final Strings url, final String username, final String password, final Boolean allowInvocationAccess) {
        this();
        setUrl(url);
        setUsername(username);
        setPassword(password);
        setAllowInvocationAccess(allowInvocationAccess);
    }

    public void delete() {
        removeRemoteSystem();
        deleteDomainObject();
    }

    @Override
    public Boolean getAllowInvocationAccess() {
        final Boolean value = super.getAllowInvocationAccess();
        return value == null ? Boolean.FALSE : value;
    }

    public boolean matches(final String host, final String username, final String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return false;
        }
        return getUrl().contains(host) && username.equalsIgnoreCase(getUsername()) && password.equals(getPassword());
    }

}
