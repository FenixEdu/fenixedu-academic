package net.sourceforge.fenixedu.persistenceTier.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.research.result.Authorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentAuthorship extends IPersistentObject
{
    public List<Authorship> readByPublicationId(int publicationId) throws ExcepcaoPersistencia;
}