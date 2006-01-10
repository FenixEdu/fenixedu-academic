/*
 * Created on May 30, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author jdnf
 * 
 */
public class TeacherVO extends VersionedObjectsBase implements
		IPersistentTeacher {

	public Teacher readTeacherByUsername(final String userName)
			throws ExcepcaoPersistencia {
		final Collection<Teacher> teachers = readAll(Teacher.class);
		for (final Teacher teacher : teachers) {
			if (teacher.getPerson().getUsername().equals(userName))
				return teacher;
		}
		return null;
	}

	public Teacher readByNumber(final Integer teacherNumber)
			throws ExcepcaoPersistencia {
		final Collection<Teacher> teachers = readAll(Teacher.class);
		for (final Teacher teacher : teachers) {
			if (teacher.getTeacherNumber().equals(teacherNumber))
				return teacher;
		}
		return null;
	}

	public Collection<Teacher> readByNumbers(
			final Collection<Integer> teacherNumbers)
			throws ExcepcaoPersistencia {

		final Collection<Teacher> result = new ArrayList<Teacher>();
		final Collection<Teacher> teachers = readAll(Teacher.class);

		for (final Integer teacherNumber : teacherNumbers) {
			for (final Teacher teacher : teachers) {
				if (teacher.getTeacherNumber().equals(teacherNumber)) {
					result.add(teacher);
					break;
				}
			}
		}
		return result;
	}
}
