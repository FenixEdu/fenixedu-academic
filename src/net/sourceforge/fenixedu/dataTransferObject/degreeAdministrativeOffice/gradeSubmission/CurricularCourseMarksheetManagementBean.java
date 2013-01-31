package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission;

import static net.sourceforge.fenixedu.util.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class CurricularCourseMarksheetManagementBean implements Serializable {

	private static final long serialVersionUID = -7798826573352027291L;

	static final public Comparator<CurricularCourseMarksheetManagementBean> COMPARATOR_BY_NAME =
			new Comparator<CurricularCourseMarksheetManagementBean>() {
				@Override
				public int compare(CurricularCourseMarksheetManagementBean o1, CurricularCourseMarksheetManagementBean o2) {
					if (isEmpty(o1.getName()) && isEmpty(o2.getName())) {
						return 0;
					}
					if (isEmpty(o1.getName())) {
						return -1;
					}
					if (isEmpty(o2.getName())) {
						return 1;
					}
					return o1.getName().compareTo(o2.getName());
				}
			};

	private CurricularCourse curricularCourse;
	private ExecutionSemester executionSemester;

	public CurricularCourseMarksheetManagementBean(final CurricularCourse curricularCourse,
			final ExecutionSemester executionSemester) {
		setCurricularCourse(curricularCourse);
		setgetExecutionSemester(executionSemester);
	}

	public CurricularCourse getCurricularCourse() {
		return this.curricularCourse;
	}

	public void setCurricularCourse(CurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}

	public ExecutionSemester getExecutionSemester() {
		return this.executionSemester;
	}

	public void setgetExecutionSemester(ExecutionSemester attribute) {
		this.executionSemester = attribute;
	}

	public String getKey() {
		return String.valueOf(getCurricularCourse().getOID() + ":" + getExecutionSemester().getOID());
	}

	public String getName() {
		return getCurricularCourse().getName(getExecutionSemester());
	}

	public String getCode() {
		return getCurricularCourse().getCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CurricularCourseMarksheetManagementBean) {
			return getCurricularCourse().equals(((CurricularCourseMarksheetManagementBean) obj).getCurricularCourse());
		}
		return false;
	}
}
