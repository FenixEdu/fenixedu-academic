/*
 * Created on Apr 27, 2005
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class NonAffiliatedTeacher extends NonAffiliatedTeacher_Base {

	public NonAffiliatedTeacher() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public NonAffiliatedTeacher(final String name, final Unit institution) {
		this();
		setName(name);
		setInstitutionUnit(institution);
	}

	public static Set<NonAffiliatedTeacher> findNonAffiliatedTeacherByName(final String name) {
		Pattern pattern = Pattern.compile(name.toLowerCase());
		final Set<NonAffiliatedTeacher> nonAffiliatedTeachers = new HashSet<NonAffiliatedTeacher>();
		for (final NonAffiliatedTeacher nonAffiliatedTeacher : RootDomainObject.getInstance().getNonAffiliatedTeachersSet()) {
			Matcher matcher = pattern.matcher(nonAffiliatedTeacher.getName().toLowerCase());
			if (matcher.find()) {
				nonAffiliatedTeachers.add(nonAffiliatedTeacher);
			}
		}
		return nonAffiliatedTeachers;
	}

	@Service
	public static void associateToInstitutionAndExecutionCourse(final String nonAffiliatedTeacherName, final Unit institution,
			final ExecutionCourse executionCourse) {

		NonAffiliatedTeacher nonAffiliatedTeacher = institution.findNonAffiliatedTeacherByName(nonAffiliatedTeacherName);
		if (nonAffiliatedTeacher == null) {
			nonAffiliatedTeacher = new NonAffiliatedTeacher(nonAffiliatedTeacherName, institution);
		}

		if (nonAffiliatedTeacher.getExecutionCourses().contains(executionCourse)) {
			throw new DomainException("error.invalid.executionCourse");
		} else {
			nonAffiliatedTeacher.addExecutionCourses(executionCourse);
		}

	}

	@Service
	public void removeExecutionCourse(final ExecutionCourse executionCourse) {
		getExecutionCourses().remove(executionCourse);
	}

	public void delete() {

		if (hasAnyAssociatedInquiriesTeachers()) {
			throw new DomainException("error.NonAffiliatedTeacher.hasAnyAssociatedInquiriesTeachers");
		}

		removeRootDomainObject();
		removeInstitutionUnit();
		getExecutionCourses().clear();

		super.deleteDomainObject();
	}
}
