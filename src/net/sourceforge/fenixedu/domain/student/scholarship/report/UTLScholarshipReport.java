package net.sourceforge.fenixedu.domain.student.scholarship.report;

import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class UTLScholarshipReport extends UTLScholarshipReport_Base {
    
    protected UTLScholarshipReport() {
        super();
    }
    
    protected UTLScholarshipReport(final UTLScholarshipSource source) {
	if (source == null) {
	    throw new DomainException("error.UTLScholarshipReport.source.is.null");
	}

	setUtlScholarshipSource(source);
    }

    @Override
    public QueueJobResult execute() throws Exception {
	// TODO Auto-generated method stub
	return null;
    }

    @Service
    public static final UTLScholarshipReport launchQueueJob() {
	UTLScholarshipSource source = null;

	UTLScholarshipReport report = new UTLScholarshipReport(source);

	return report;
    }

}
