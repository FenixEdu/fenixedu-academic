package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class ExecutionCourseAuditFile extends ExecutionCourseAuditFile_Base {

	private static final String ROOT_DIR_DESCRIPTION = "QUC Auxiliar Audit File";
	private static final String ROOT_DIR = "QUCAuditFile";

	public ExecutionCourseAuditFile(ExecutionCourseAudit executionCourseAudit, String filename, byte[] file) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setExecutionCourseAudit(executionCourseAudit);
		super.init(getVirtualPath(), filename, filename, null, file, getPermissionGroup());
	}

	private Group getPermissionGroup() {
		PersonGroup teacherGroup = new PersonGroup(getExecutionCourseAudit().getTeacherAuditor().getPerson());
		PersonGroup studentGroup = new PersonGroup(getExecutionCourseAudit().getStudentAuditor().getPerson());
		RoleGroup pedagogicalCouncil = new RoleGroup(RoleType.PEDAGOGICAL_COUNCIL);
		return new GroupUnion(teacherGroup, studentGroup, pedagogicalCouncil);
	}

	private VirtualPath getVirtualPath() {
		final VirtualPath filePath = new VirtualPath();
		filePath.addNode(new VirtualPathNode(ROOT_DIR, ROOT_DIR_DESCRIPTION));
		ExecutionSemester executionPeriod = getExecutionCourseAudit().getExecutionCourse().getExecutionPeriod();
		String nodeName = executionPeriod.getName() + "-" + executionPeriod.getExecutionYear().getName();
		filePath.addNode(new VirtualPathNode(nodeName, nodeName));
		filePath.addNode(new VirtualPathNode(getExecutionCourseAudit().getExecutionCourse().getExternalId(),
				getExecutionCourseAudit().getExecutionCourse().getName()));
		return filePath;
	}

	@Override
	public void delete() {
		removeExecutionCourseAudit();
		super.delete();
	}
}
