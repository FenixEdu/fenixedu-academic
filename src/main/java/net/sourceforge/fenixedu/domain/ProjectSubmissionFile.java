package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.groups.Group;

public class ProjectSubmissionFile extends ProjectSubmissionFile_Base {

    public ProjectSubmissionFile() {
        super();
    }

    public ProjectSubmissionFile(String filename, String displayName, byte[] content, Group group) {
        this();
        init(filename, displayName, content, group);

    }

    @Override
    public void delete() {
        setProjectSubmission(null);
        super.delete();
    }

    @Deprecated
    public boolean hasProjectSubmission() {
        return getProjectSubmission() != null;
    }

}
