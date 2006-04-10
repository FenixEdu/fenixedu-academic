/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoMetadata;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadMetadatas extends Service {

    public List<InfoMetadata> run(Integer executionCourseId, Integer testId, Boolean isDistributedTest) throws ExcepcaoPersistencia {
    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

        final Set<Metadata> metadatas;
        if (testId == null) {
            metadatas = executionCourse.findVisibleMetadata();
        } else if (isDistributedTest == false) {
        	final TestQuestion testQuestion = rootDomainObject.readTestQuestionByOID(testId);
            metadatas = Metadata.findVisibleMetadataFromExecutionCourseNotOfTestQuestion(executionCourse, testQuestion);
        } else if (isDistributedTest == true) {
        	final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(testId);
        	metadatas = Metadata.findVisibleMetadataFromExecutionCourseNotOfTestQuestion(executionCourse, distributedTest);
        } else {
        	return null;
        }

        final List<InfoMetadata> infoMetadataList = new ArrayList<InfoMetadata>(metadatas.size());
        for (Metadata metadata : metadatas) {
            infoMetadataList.add(InfoMetadata.newInfoFromDomain(metadata));
        }
        return infoMetadataList;
    }
}