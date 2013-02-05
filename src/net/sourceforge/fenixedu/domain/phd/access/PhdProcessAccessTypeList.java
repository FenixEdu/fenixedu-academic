package net.sourceforge.fenixedu.domain.phd.access;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.phd.PhdElementsList;

import org.apache.commons.lang.StringUtils;

public class PhdProcessAccessTypeList extends PhdElementsList<PhdProcessAccessType> {

    static private final long serialVersionUID = 1L;

    protected PhdProcessAccessTypeList() {
        super();
    }

    protected PhdProcessAccessTypeList(final String types) {
        super(types);
    }

    public PhdProcessAccessTypeList(Collection<PhdProcessAccessType> types) {
        super(types);
    }

    @Override
    protected PhdProcessAccessType convertElementToSet(String valueToParse) {
        return PhdProcessAccessType.valueOf(valueToParse);
    }

    @Override
    protected String convertElementToString(PhdProcessAccessType element) {
        return element.name();
    }

    @Override
    protected PhdProcessAccessTypeList createNewInstance() {
        return new PhdProcessAccessTypeList();
    }

    @Override
    public PhdProcessAccessTypeList addAccessTypes(PhdProcessAccessType... types) {
        return (PhdProcessAccessTypeList) super.addAccessTypes(types);
    }

    static public PhdProcessAccessTypeList importFromString(String value) {
        return StringUtils.isEmpty(value) ? EMPTY : new PhdProcessAccessTypeList(value);
    }

    final static public PhdProcessAccessTypeList EMPTY = new PhdProcessAccessTypeList() {

        static private final long serialVersionUID = 1L;

        @Override
        public Set<PhdProcessAccessType> getTypes() {
            return Collections.emptySet();
        }

        @Override
        public String toString() {
            return "";
        }
    };

}
