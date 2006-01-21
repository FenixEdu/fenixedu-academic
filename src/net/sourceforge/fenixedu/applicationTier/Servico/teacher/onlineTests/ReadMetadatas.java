/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;

/**
 * @author Susana Fernandes
 */
public class ReadMetadatas extends Service {

    public List<InfoMetadata> run(Integer executionCourseId, Integer testId, Boolean distributedTest) throws ExcepcaoPersistencia {
        IPersistentMetadata persistentMetadata = persistentSupport.getIPersistentMetadata();
        List<Metadata> metadatas = null;
        if (testId == null) {
            metadatas = persistentMetadata.readByExecutionCourseAndVisibility(executionCourseId);
        } else if (distributedTest == false) {
            metadatas = persistentSupport.getIPersistentMetadata().readByExecutionCourseAndNotTest(executionCourseId, testId);
        } else if (distributedTest == true) {
            metadatas = persistentSupport.getIPersistentMetadata().readByExecutionCourseAndNotDistributedTest(executionCourseId, testId);
        }
        List<InfoMetadata> infoMetadataList = new ArrayList<InfoMetadata>();
        for (Metadata metadata : metadatas) {
            infoMetadataList.add(InfoMetadata.newInfoFromDomain(metadata));
        }
        return infoMetadataList;
    }
}