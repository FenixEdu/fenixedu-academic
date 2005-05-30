package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

/**
 * @author Ricardo Rodrigues
 */
public interface IPersistentPublicationAuthor extends IPersistentObject{
    public List readByPublicationId(Integer publicationId) throws ExcepcaoPersistencia;
}
