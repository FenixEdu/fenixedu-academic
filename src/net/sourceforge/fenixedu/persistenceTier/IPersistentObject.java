package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author tfc130
 */
public interface IPersistentObject {

    void deleteByOID(Class classToQuery, Integer oid);

    public DomainObject readByOID(Class classToQuery, Integer oid);

    public RootDomainObject readRootDomainObject();

}