package net.sourceforge.fenixedu.presentationTier.renderers.providers.spaceManager;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;

import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembersType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PersistentGroupMembersProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<PersistentGroupMembers> result = new ArrayList<PersistentGroupMembers>();
        for (PersistentGroupMembers persistentGroupMembers : Bennu.getInstance().getPersistentGroupMembersSet()) {
            if (persistentGroupMembers.getType().equals(PersistentGroupMembersType.SPACE_OCCUPATION)) {
                result.add(persistentGroupMembers);
            }
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}
