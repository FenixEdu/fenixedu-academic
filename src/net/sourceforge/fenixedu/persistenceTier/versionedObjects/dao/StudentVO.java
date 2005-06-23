package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.Predicate;

public class StudentVO extends VersionedObjectsBase implements
		IPersistentStudent {

	public IStudent readStudentByNumberAndDegreeType(final Integer number,
			final DegreeType degreeType) throws ExcepcaoPersistencia {
		
		List students = (List)readAll(Student.class);
		
		return (IStudent) CollectionUtils.find(students,new Predicate() {

			public boolean evaluate(Object arg0) {
				IStudent student = (IStudent)arg0;
				return student.getNumber().equals(number) &&
						student.getDegreeType().equals(degreeType);
			}
		});
	}

	public List readAll() throws ExcepcaoPersistencia {
		return (List) readAll(Student.class);
	}

	public IStudent readByUsername(final String username) throws ExcepcaoPersistencia {

		List students = (List)readAll(Student.class);
		
		return (IStudent) CollectionUtils.find(students,new Predicate() {

			public boolean evaluate(Object arg0) {
				IStudent student = (IStudent)arg0;
				return student.getPerson().getUsername().equals(username);
			}
		});
	}

	public IStudent readByPersonAndDegreeType(Integer personId,
			final DegreeType degreeType) throws ExcepcaoPersistencia {
		
		IPerson person = (IPerson)readByOID(Person.class,personId);
		
		if (person != null) {

			List students = person.getStudents();
			
			return (IStudent) CollectionUtils.find(students,new Predicate() {

				public boolean evaluate(Object arg0) {
					IStudent student = (IStudent)arg0;
					return student.getDegreeType().equals(degreeType);
				}
			});			
		}

		return null;
	}

	public Integer generateStudentNumber(final DegreeType degreeType)
			throws ExcepcaoPersistencia {
		Integer number = new Integer(0);

		List students = (List) readAll(Student.class);
		List<IStudent> result = (List) CollectionUtils.select(students,new Predicate () {
			public boolean evaluate(Object arg0) {
				return ((IStudent)arg0).getDegreeType().equals(degreeType);
			}		
		});

		for (IStudent student : result) {
			number = Math.max(number,student.getNumber());
		}

        return new Integer(number.intValue() + 1);
	}

	public List readMasterDegreeStudentsByNameIDnumberIDtypeAndStudentNumber(
			final String studentName, final String idNumber, final IDDocumentType idType,
			final Integer studentNumber) throws ExcepcaoPersistencia {
		List students = (List)readAll(Student.class);
		
		return (List) CollectionUtils.select(students,new Predicate() {

			public boolean evaluate(Object arg0) {
				IStudent student = (IStudent)arg0;
				
				boolean cond = (studentName == null || studentName.contains(student.getPerson().getNome()));// preventing the %string% behaviour
				cond = cond && (idNumber == null || student.getPerson().getNumeroDocumentoIdentificacao().equals(idNumber));
				cond = cond && (idType == null || student.getPerson().getIdDocumentType().equals(idType));
				cond = cond && (studentNumber == null || student.getNumber().equals(studentNumber));
				cond = cond && student.getDegreeType().equals(DegreeType.MASTER_DEGREE);				
				
				return cond;
			}
		});
	}

	public Integer countAll() throws ExcepcaoPersistencia {
		return readAll(Student.class).size();
	}

	public List readStudentByPersonRole(final RoleType roleType)
			throws ExcepcaoPersistencia {
		List students = (List)readAll(Student.class);
		
		return (List) CollectionUtils.select(students,new Predicate() {

			public boolean evaluate(Object arg0) {
				IStudent student = (IStudent)arg0;
				return student.getPerson().getPersonRoles().contains(roleType);
			}
		});
	}

	public List readAllBetweenNumbers(final Integer fromNumber, final Integer toNumber)
			throws ExcepcaoPersistencia {
		List students = (List)readAll(Student.class);
		
		return (List) CollectionUtils.select(students,new Predicate() {

			public boolean evaluate(Object arg0) {
				IStudent student = (IStudent)arg0;
				return student.getNumber().intValue() < toNumber.intValue() &&
						student.getNumber().intValue() > fromNumber.intValue();
			}
		});
	}

	public List readAllWithPayedTuition() throws ExcepcaoPersistencia {
		List students = (List)readAll(Student.class);
		
		return (List) CollectionUtils.select(students,new Predicate() {

			public boolean evaluate(Object arg0) {
				IStudent student = (IStudent)arg0;
				return student.getPayedTuition();
			}
		});
	}
}
