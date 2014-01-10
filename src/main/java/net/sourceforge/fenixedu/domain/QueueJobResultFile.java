package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.documents.GeneratedDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.fenixframework.Atomic;

public class QueueJobResultFile extends QueueJobResultFile_Base {

    protected QueueJobResultFile(QueueJobWithFile job, Person operator, String filename, byte[] content) {
        super();
        setJob(job);
        init(GeneratedDocumentType.QUEUE_JOB, operator, operator, filename, content);
    }

    @Override
    protected Group computePermittedGroup() {
        return new RoleGroup(RoleType.MANAGER);
    }

    @Override
    public void delete() {
        setJob(null);
        super.delete();
    }

    @Atomic
    public static void store(QueueJobWithFile job, Person person, String filename, byte[] content) {
        if (!CoreConfiguration.getConfiguration().developmentMode()) {
            new QueueJobResultFile(job, person, filename, content);
        }
    }

    @Deprecated
    public boolean hasJob() {
        return getJob() != null;
    }

}
