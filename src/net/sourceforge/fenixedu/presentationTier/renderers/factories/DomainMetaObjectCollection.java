package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectCollection;

public class DomainMetaObjectCollection extends MetaObjectCollection {

    @Override
    public void commit() {
        try {
            ServiceUtils.executeService( "CommitMetaObjects", new Object[] { getAllMetaObjects() });
        } catch (Exception e) {
            if (e instanceof DomainException) {
                throw (DomainException) e;
            } else {
                throw new DomainException("domain.metaobject.service.failed", e);
            }
        }
    }

    protected IUserView getUserView() {
        return ((FenixUserIdentity) getUser()).getUserView();
    }
}
