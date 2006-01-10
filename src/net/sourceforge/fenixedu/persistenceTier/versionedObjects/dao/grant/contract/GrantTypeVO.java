package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantType;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class GrantTypeVO extends VersionedObjectsBase implements IPersistentGrantType {

    public GrantType readGrantTypeBySigla(String sigla) throws ExcepcaoPersistencia {
        List<GrantType> grantTypes = (List<GrantType>) readAll(GrantType.class);

        for (GrantType type : grantTypes) {
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