/*
 * Created on 25/Jul/2003
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.Question;

/**
 * @author Susana Fernandes
 */
public interface IPersistentQuestion extends IPersistentObject {

    public abstract Question readByFileNameAndMetadataId(String fileName, IMetadata metadata)
            throws ExcepcaoPersistencia;

    public abstract List readByMetadata(IMetadata metadata) throws ExcepcaoPersistencia;

    public abstract List readByMetadataAndVisibility(IMetadata metadata) throws ExcepcaoPersistencia;

    public int countByMetadata(IMetadata metadata) throws ExcepcaoPersistencia;

    public String correctFileName(String fileName, Integer metadataId) throws ExcepcaoPersistencia;

    public void cleanQuestions(IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract void deleteByMetadata(IMetadata metadata) throws ExcepcaoPersistencia;

    public abstract void delete(IQuestion question) throws ExcepcaoPersistencia;
}