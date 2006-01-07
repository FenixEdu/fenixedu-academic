/*
 * Created on 25/Jul/2003
 */

package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentQuestion extends IPersistentObject {

    public abstract Question readByFileNameAndMetadataId(String fileName, Metadata metadata) throws ExcepcaoPersistencia;

    public abstract List<Question> readByMetadata(Metadata metadata) throws ExcepcaoPersistencia;

    public abstract List<Question> readByMetadataAndVisibility(Metadata metadata) throws ExcepcaoPersistencia;

    public abstract int countByMetadata(Metadata metadata) throws ExcepcaoPersistencia;

    public abstract String correctFileName(String fileName, Integer metadataId) throws ExcepcaoPersistencia;

    public abstract void cleanQuestions(DistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract void deleteByMetadata(Metadata metadata) throws ExcepcaoPersistencia;
}