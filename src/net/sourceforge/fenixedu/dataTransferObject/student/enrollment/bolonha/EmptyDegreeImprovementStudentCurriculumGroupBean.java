package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class EmptyDegreeImprovementStudentCurriculumGroupBean extends ImprovementStudentCurriculumGroupBean {

	static private final long serialVersionUID = 1L;

	public EmptyDegreeImprovementStudentCurriculumGroupBean(final CurriculumGroup curriculumGroup,
			final ExecutionSemester executionSemester) {
		super(curriculumGroup, executionSemester);
	}

	@Override
	protected List<StudentCurriculumGroupBean> buildCurriculumGroupsEnroled(CurriculumGroup parentGroup,
			ExecutionSemester executionSemester, int[] curricularYears) {

		final List<StudentCurriculumGroupBean> result = new ArrayList<StudentCurriculumGroupBean>();

		for (final CurriculumGroup curriculumGroup : filterGroups(parentGroup)) {
			result.add(new EmptyDegreeImprovementStudentCurriculumGroupBean(curriculumGroup, executionSemester));
		}

		return result;
	}

	private Set<CurriculumGroup> filterGroups(CurriculumGroup parentGroup) {
		final Set<CurriculumGroup> groups = new TreeSet<CurriculumGroup>(CurriculumModule.COMPARATOR_BY_NAME_AND_ID);

		for (final CurriculumModule curriculumModule : parentGroup.getCurriculumModules()) {

			if (!curriculumModule.isLeaf()) {

				if (curriculumModule.isNoCourseGroupCurriculumGroup()) {
					final NoCourseGroupCurriculumGroup noCourseGroup = (NoCourseGroupCurriculumGroup) curriculumModule;
					if (noCourseGroup.getNoCourseGroupCurriculumGroupType() != NoCourseGroupCurriculumGroupType.STANDALONE) {
						continue;
					}
				}

				groups.add((CurriculumGroup) curriculumModule);
			}
		}
		return groups;
	}

}
