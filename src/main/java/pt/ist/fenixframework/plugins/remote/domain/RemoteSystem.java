package pt.ist.fenixframework.plugins.remote.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class RemoteSystem extends RemoteSystem_Base {

    private RemoteSystem() {
        super();
        RemoteSystem root = FenixFramework.getDomainRoot().getRemoteSystem();
        if (root != null && root != this) {
            throw new Error("Trying to create a 2nd instance of RemoteSystemRoot! There can only be one!");
        }
    }

    @Atomic
    public static void init() {
        if (FenixFramework.getDomainRoot().getRemoteSystem() == null) {
            FenixFramework.getDomainRoot().setRemoteSystem(new RemoteSystem());
        }
    }

    public static RemoteSystem getInstance() {
        return FenixFramework.getDomainRoot().getRemoteSystem();
    }

    @Deprecated
    public java.util.Set<pt.ist.fenixframework.plugins.remote.domain.RemoteHost> getRemoteHosts() {
        return getRemoteHostsSet();
    }

    @Deprecated
    public boolean hasAnyRemoteHosts() {
        return !getRemoteHostsSet().isEmpty();
    }

}
