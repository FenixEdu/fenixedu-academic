/*
 * Created on 25/Jul/2003
 */

package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentQuestion extends IPersistentObject {

    public abstract IQuestion readByFileNameAndMetadataId(String fileName, IMetadata metadata) throws ExcepcaoPersistencia;

    public abstract List<IQuestion> readByMetadata(IMetadata metadata) throws ExcepcaoPersistencia;

    public abstract List<IQuestion> readByMetadataAndVisibility(IMetadata metadata) throws ExcepcaoPersistencia;

    public abstract int countByMetadata(IMetadata metadata) throws ExcepcaoPersistencia;

    public abstract String correctFileName(String fileName, Integer metadataId) throws ExcepcaoPersistencia;

    public abstract void cleanQuestions(IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract void deleteByMetadata(IMetadata metadata) throws ExcepcaoPersistencia;
}