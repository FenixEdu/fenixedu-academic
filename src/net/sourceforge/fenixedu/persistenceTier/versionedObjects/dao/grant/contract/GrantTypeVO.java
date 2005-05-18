package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class GrantTypeVO extends VersionedObjectsBase implements IPersistentGrantType {

    public IGrantType readGrantTypeBySigla(String sigla) throws ExcepcaoPersistencia {
        List<IGrantType> grantTypes = (List<IGrantType>) readAll(GrantType.class);

        for (IGrantType type : grantTypes) {
            if (type.getSigla().equals(sigla)) {
                return type;
            }
        }

        return null;
    }

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(GrantType.class);
    }

}