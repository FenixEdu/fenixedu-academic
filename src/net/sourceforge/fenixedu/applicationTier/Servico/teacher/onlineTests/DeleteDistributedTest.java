package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;

public class DeleteDistributedTest extends FenixService {

	public void run(Integer executionCourseId, final Integer distributedTestId) {
		final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);

		for (Metadata metadata : rootDomainObject.getMetadatasSet()) {
			if (metadata.getVisibility() != null && !metadata.getVisibility().booleanValue()
					&& metadata.getQuestionsSet().size() == 0) {
				metadata.delete();
			}
		}

		distributedTest.delete();
	}

}
