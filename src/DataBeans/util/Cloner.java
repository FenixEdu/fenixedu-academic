package DataBeans.util;

import org.apache.commons.beanutils.BeanUtils;

import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

import DataBeans.InfoClass;
import DataBeans.InfoCountry;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoLesson;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoPerson;
import DataBeans.InfoRole;
import DataBeans.InfoRoom;
import DataBeans.InfoShift;
import DataBeans.InfoStudent;
import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoBibliographicReference;
import DataBeans.gesdis.InfoCurriculum;
import DataBeans.gesdis.InfoItem;
import DataBeans.gesdis.InfoSection;
import DataBeans.gesdis.InfoSite;
import Dominio.Announcement;
import Dominio.Aula;
import Dominio.BibliographicReference;
import Dominio.Country;
import Dominio.CurricularCourse;
import Dominio.Curriculum;
import Dominio.Curso;
import Dominio.CursoExecucao;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IAnnouncement;
import Dominio.IAula;
import Dominio.IBibliographicReference;
import Dominio.ICountry;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IItem;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.IPlanoCurricularCurso;
import Dominio.IRole;
import Dominio.ISala;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.IStudent;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Item;
import Dominio.MasterDegreeCandidate;
import Dominio.Pessoa;
import Dominio.PlanoCurricularCurso;
import Dominio.Sala;
import Dominio.Section;
import Dominio.Site;
import Dominio.Student;
import Dominio.Turma;
import Dominio.Turno;

/**
 * @author jpvl
 *
 */
public abstract class Cloner {

	public static ITurno copyInfoShift2Shift(InfoShift infoShift) {
		ITurno shift = new Turno();
		IDisciplinaExecucao executionCourse =
			Cloner.copyInfoExecutionCourse2ExecutionCourse(
				infoShift.getInfoDisciplinaExecucao());

		copyObjectProperties(shift, infoShift);

		shift.setDisciplinaExecucao(executionCourse);
		return shift;
	}
	/**
	 * Method copyInfoExecutionCourse2ExecutionCourse.
	 * @param infoExecutionCourse
	 * @return IDisciplinaExecucao
	 */
	public static IDisciplinaExecucao copyInfoExecutionCourse2ExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
		IExecutionPeriod executionPeriod =
			Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
				infoExecutionCourse.getInfoExecutionPeriod());

		copyObjectProperties(executionCourse, infoExecutionCourse);

		executionCourse.setExecutionPeriod(executionPeriod);
		return executionCourse;
	}

	public static InfoExecutionCourse copyIExecutionCourse2InfoExecutionCourse(IDisciplinaExecucao executionCourse) {
		InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
		InfoExecutionPeriod infoExecutionPeriod =
			Cloner.copyIExecutionPeriod2InfoExecutionPeriod(
				executionCourse.getExecutionPeriod());

		copyObjectProperties(infoExecutionCourse, executionCourse);

		infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod);
		return infoExecutionCourse;
	}

	/**
	 * Method copyInfoCurricularCourse2ICurricularCourse.
	 * @param infoCurricularCourse
	 * @return ICurricularCourse
	 */
	public static ICurricularCourse copyInfoCurricularCourse2ExecutionCourse(InfoCurricularCourse infoCurricularCourse) {
		ICurricularCourse curricularCourse = new CurricularCourse();
		IPlanoCurricularCurso planoCurricularCurso =
			copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(
				infoCurricularCourse.getInfoDegreeCurricularPlan());

		copyObjectProperties(curricularCourse, infoCurricularCourse);

		curricularCourse.setDegreeCurricularPlan(planoCurricularCurso);
		return curricularCourse;
	}

	public static InfoCurricularCourse copyICurricularCourse2InfoCurricularCourse(ICurricularCourse curricularCourse) {
		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(
				curricularCourse.getDegreeCurricularPlan());

		copyObjectProperties(infoCurricularCourse, curricularCourse);

		infoCurricularCourse.setInfoDegreeCurricularPlan(
			infoDegreeCurricularPlan);
		return infoCurricularCourse;
	}

	/**
	 * Method copyInfoLesson2Lesson.
	 * @param lessonExample
	 * @return IAula
	 */
	public static IAula copyInfoLesson2Lesson(InfoLesson infoLesson) {
		IAula lesson = new Aula();
		ISala sala = null;

		try {
			BeanUtils.copyProperties(lesson, infoLesson);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		sala = Cloner.copyInfoRoom2Room(infoLesson.getInfoSala());
		lesson.setSala(sala);

		return lesson;
	}
	/**
	 * Method copyInfoRoom2Room.
	 * @param infoRoom
	 */
	public static ISala copyInfoRoom2Room(InfoRoom infoRoom) {
		ISala room = new Sala();
		copyObjectProperties(room, infoRoom);
		return room;
	}
	/**
	 * 
	 * @param room
	 * @return IAula
	 */
	public static InfoRoom copyRoom2InfoRoom(ISala room) {
		InfoRoom infoRoom = new InfoRoom();

		copyObjectProperties(infoRoom, room);

		return infoRoom;
	}

	/**
	 * Method copyInfoLesson2Lesson.
	 * @param lessonExample
	 * @return IAula
	 */
	public static InfoLesson copyILesson2InfoLesson(IAula lesson) {
		InfoLesson infoLesson = new InfoLesson();
		InfoExecutionCourse infoExecutionCourse =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				lesson.getDisciplinaExecucao());

		InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(lesson.getSala());

		copyObjectProperties(infoLesson, lesson);

		infoLesson.setInfoSala(infoRoom);
		infoLesson.setInfoDisciplinaExecucao(infoExecutionCourse);
		return infoLesson;
	}

	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static ITurno copyInfoShift2IShift(InfoShift infoShift) {
		ITurno shift = new Turno();
		IDisciplinaExecucao executionCourse =
			Cloner.copyInfoExecutionCourse2ExecutionCourse(
				infoShift.getInfoDisciplinaExecucao());

		copyObjectProperties(shift, infoShift);

		shift.setDisciplinaExecucao(executionCourse);

		return shift;
	}
	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static InfoShift copyShift2InfoShift(ITurno shift) {
		InfoShift infoShift = new InfoShift();

		InfoExecutionCourse infoExecutionCourse =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				shift.getDisciplinaExecucao());

		copyObjectProperties(infoShift, shift);

		infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

		return infoShift;
	}

	/**
	 * Method copyInfoShift2Shift.
	 * @param infoShift
	 * @return ITurno
	 */
	public static InfoClass copyClass2InfoClass(ITurma classD) {
		InfoClass infoClass = new InfoClass();
		InfoExecutionDegree infoExecutionDegree =
			Cloner.copyIExecutionDegree2InfoExecutionDegree(
				classD.getExecutionDegree());
		InfoExecutionPeriod infoExecutionPeriod =
			Cloner.copyIExecutionPeriod2InfoExecutionPeriod(
				classD.getExecutionPeriod());

		copyObjectProperties(infoClass, classD);

		infoClass.setInfoExecutionDegree(infoExecutionDegree);
		infoClass.setInfoExecutionPeriod(infoExecutionPeriod);
		return infoClass;
	}
	/**
	 * Method copyIExecutionPeriod2InfoExecutionPeriod.
	 * @param iExecutionPeriod
	 * @return InfoExecutionPeriod
	 */
	public static InfoExecutionPeriod copyIExecutionPeriod2InfoExecutionPeriod(IExecutionPeriod executionPeriod) {
		InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
		InfoExecutionYear infoExecutionYear =
			Cloner.copyIExecutionYear2InfoExecutionYear(
				executionPeriod.getExecutionYear());

		copyObjectProperties(infoExecutionPeriod, executionPeriod);

		infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);
		return infoExecutionPeriod;
	}

	private static void copyObjectProperties(
		Object destination,
		Object source) {
		if (source != null)
			try {
				BeanUtils.copyProperties(destination, source);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}

	/**
	 * 
	 * @param infoExecutionDegree
	 * @return ICursoExecucao
	 */
	public static ICursoExecucao copyInfoExecutionDegree2ExecutionDegree(InfoExecutionDegree infoExecutionDegree) {

		ICursoExecucao executionDegree = new CursoExecucao();
		IPlanoCurricularCurso degreeCurricularPlan =
			Cloner.copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(
				infoExecutionDegree.getInfoDegreeCurricularPlan());

		IExecutionYear executionYear =
			Cloner.copyInfoExecutionYear2IExecutionYear(
				infoExecutionDegree.getInfoExecutionYear());

		copyObjectProperties(executionDegree, infoExecutionDegree);

		executionDegree.setExecutionYear(executionYear);
		executionDegree.setCurricularPlan(degreeCurricularPlan);

		return executionDegree;

	}
	/**
	 * Method copyInfoDegreeCurricularPlan2IDegreeCurricularPlan.
	 * @param infoDegreeCurricularPlan
	 * @return IPlanoCurricularCurso
	 */
	public static IPlanoCurricularCurso copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		IPlanoCurricularCurso degreeCurricularPlan = new PlanoCurricularCurso();

		ICurso degree =
			Cloner.copyInfoDegree2IDegree(
				infoDegreeCurricularPlan.getInfoDegree());
		try {
			BeanUtils.copyProperties(
				degreeCurricularPlan,
				infoDegreeCurricularPlan);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		degreeCurricularPlan.setCurso(degree);

		return degreeCurricularPlan;

	}

	public static InfoExecutionDegree copyIExecutionDegree2InfoExecutionDegree(ICursoExecucao executionDegree) {

		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(
				executionDegree.getCurricularPlan());

		InfoExecutionYear infoExecutionYear =
			Cloner.copyIExecutionYear2InfoExecutionYear(
				executionDegree.getExecutionYear());
		try {
			BeanUtils.copyProperties(infoExecutionDegree, executionDegree);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e.getMessage());
		}

		infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
		infoExecutionDegree.setInfoDegreeCurricularPlan(
			infoDegreeCurricularPlan);

		return infoExecutionDegree;

	}
	/**
	 * Method copyInfoExecutionYear2IExecutionYear.
	 * @param infoExecutionYear
	 * @return IExecutionYear
	 */
	public static IExecutionYear copyInfoExecutionYear2IExecutionYear(InfoExecutionYear infoExecutionYear) {
		IExecutionYear executionYear = new ExecutionYear();
		try {
			BeanUtils.copyProperties(executionYear, infoExecutionYear);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException(e);
		}
		return executionYear;
	}
	/**
	 * Method copyInfoExecutionYear2IExecutionYear.
	 * @param infoExecutionYear
	 * @return IExecutionYear
	 */
	public static InfoExecutionYear copyIExecutionYear2InfoExecutionYear(IExecutionYear executionYear) {
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		copyObjectProperties(infoExecutionYear, executionYear);
		return infoExecutionYear;
	}

	public static InfoDegreeCurricularPlan copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(IPlanoCurricularCurso degreeCurricularPlan) {
		InfoDegreeCurricularPlan infoDegreeCurricularPlan =
			new InfoDegreeCurricularPlan();

		InfoDegree infoDegree =
			Cloner.copyIDegree2InfoDegree(degreeCurricularPlan.getCurso());
		try {
			BeanUtils.copyProperties(
				infoDegreeCurricularPlan,
				degreeCurricularPlan);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		infoDegreeCurricularPlan.setInfoDegree(infoDegree);

		return infoDegreeCurricularPlan;
	}
	/**
	 * Method copyIDegree2InfoDegree.
	 * @param iCurso
	 * @return InfoDegree
	 */
	public static InfoDegree copyIDegree2InfoDegree(ICurso degree) {
		InfoDegree infoDegree = new InfoDegree();
		try {
			BeanUtils.copyProperties(infoDegree, degree);
			infoDegree.setDegreeType(degree.getTipoCurso().toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return infoDegree;
	}
	/**
	 * Method copyInfoDegree2IDegree.
	 * @param infoDegree
	 * @return ICurso
	 */
	public static ICurso copyInfoDegree2IDegree(InfoDegree infoDegree) {
		ICurso degree = new Curso();
		try {
			BeanUtils.copyProperties(degree, infoDegree);
			degree.setTipoCurso(new TipoCurso(infoDegree.getDegreeType()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return degree;

	}
	/**
	 * Method copyInfoExecutionPeriod2IExecutionPeriod.
	 * @param infoExecutionPeriod
	 * @return IExecutionPeriod
	 */
	public static IExecutionPeriod copyInfoExecutionPeriod2IExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {

		IExecutionPeriod executionPeriod = new ExecutionPeriod();

		IExecutionYear executionYear =
			Cloner.copyInfoExecutionYear2IExecutionYear(
				infoExecutionPeriod.getInfoExecutionYear());

		copyObjectProperties(executionPeriod, infoExecutionPeriod);

		executionPeriod.setExecutionYear(executionYear);

		return executionPeriod;
	}
	/**
	 * Method copyInfoClass2Class.
	 * @param infoTurma
	 * @return ITurma
	 */
	public static ITurma copyInfoClass2Class(InfoClass infoClass) {
		ITurma domainClass = new Turma();

		IExecutionPeriod executionPeriod =
			Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
				infoClass.getInfoExecutionPeriod());
		ICursoExecucao executionDegree =
			Cloner.copyInfoExecutionDegree2ExecutionDegree(
				infoClass.getInfoExecutionDegree());

		copyObjectProperties(domainClass, infoClass);

		domainClass.setExecutionDegree(executionDegree);
		domainClass.setExecutionPeriod(executionPeriod);
		return domainClass;
	}
	/**
	 * Method copyIShift2InfoShift.
	 * @param elem
	 * @return Object
	 */
	public static InfoShift copyIShift2InfoShift(ITurno shift) {
		InfoShift infoShift = new InfoShift();
		InfoExecutionCourse infoExecutionCourse =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				shift.getDisciplinaExecucao());
		copyObjectProperties(infoShift, shift);
		infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
		return infoShift;
	}
	/**
	 * Method copyInfoStudent2IStudent.
	 * @param infoStudent
	 * @return IStudent
	 */
	public static IStudent copyInfoStudent2IStudent(InfoStudent infoStudent) {
		IStudent student = new Student();
		IPessoa person =
			Cloner.copyInfoPerson2IPerson(infoStudent.getInfoPerson());
		copyObjectProperties(student, infoStudent);
		student.setPerson(person);

		return student;
	}

	/**
	 * Method copyIStudent2InfoStudent.
	 * @param elem
	 * @return Object
	 */
	public static InfoStudent copyIStudent2InfoStudent(IStudent student) {
		InfoStudent infoStudent = new InfoStudent();
		copyObjectProperties(infoStudent, student);
		infoStudent.setInfoPerson(
			Cloner.copyIPerson2InfoPerson(student.getPerson()));
		return infoStudent;
	}

	/**
	 * Method copyInfoPerson2IPerson.
	 * @param infoPerson
	 * @return IPessoa
	 */
	public static IPessoa copyInfoPerson2IPerson(InfoPerson infoPerson) {
		IPessoa person = new Pessoa();
		copyObjectProperties(person, infoPerson);
		return person;
	}

	/**
	 * Method copyInfoPerson2IPerson.
	 * @param infoPerson
	 * @return IPessoa
	 */
	public static InfoPerson copyIPerson2InfoPerson(IPessoa person) {
		InfoPerson infoPerson = new InfoPerson();
		copyObjectProperties(infoPerson, person);
		return infoPerson;
	}

	/**
	 * Method copyInfoMasterDegreeCandidate2IMasterDegreCandidate
	 * @param infoMasterDegreeCandidate
	 * @return IMasterDegreeCandidate
	 */
	public static IMasterDegreeCandidate copyInfoMasterDegreeCandidate2IMasterDegreCandidate(InfoMasterDegreeCandidate infoMasterDegreeCandidate) {
		IMasterDegreeCandidate masterDegreeCandidate =
			new MasterDegreeCandidate();
		ICountry country = Cloner.copyInfoCountry2ICountry(infoMasterDegreeCandidate.getInfoCountry());
		ICountry nationality = Cloner.copyInfoCountry2ICountry(infoMasterDegreeCandidate.getInfoNationality());
		IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoMasterDegreeCandidate.getInfoExecutionYear());
		// FIXME
		ICurso degree = Cloner.copyInfoDegree2IDegree(infoMasterDegreeCandidate.getInfoDegree());
		copyObjectProperties(masterDegreeCandidate, infoMasterDegreeCandidate);
		masterDegreeCandidate.setIdentificationDocumentType(new TipoDocumentoIdentificacao(infoMasterDegreeCandidate.getInfoIdentificationDocumentType()));
		masterDegreeCandidate.setCountry(country);
		masterDegreeCandidate.setNationality(nationality);
		masterDegreeCandidate.setExecutionYear(executionYear);
		masterDegreeCandidate.setDegree(degree);
		return masterDegreeCandidate;
	}

	/**
	 * Method copyIMasterDegreeCandidate2InfoMasterDegreCandidate
	 * @param masterDegreeCandidate 
	 * @return InfoMasterDegreeCandidate 
	 */
	public static InfoMasterDegreeCandidate copyIMasterDegreeCandidate2InfoMasterDegreCandidate(IMasterDegreeCandidate masterDegreeCandidate) {
		InfoMasterDegreeCandidate infoMasterDegreeCandidate =
			new InfoMasterDegreeCandidate();
		copyObjectProperties(infoMasterDegreeCandidate, masterDegreeCandidate);
		InfoDegree infoDegree =
			Cloner.copyIDegree2InfoDegree(masterDegreeCandidate.getDegree());
		InfoCountry infoCountry =
			Cloner.copyICountry2InfoCountry(masterDegreeCandidate.getCountry());
		InfoCountry infoNationality =
			Cloner.copyICountry2InfoCountry(
				masterDegreeCandidate.getNationality());
		InfoExecutionYear infoExecutionYear =
			Cloner.copyIExecutionYear2InfoExecutionYear(
				masterDegreeCandidate.getExecutionYear());
		infoMasterDegreeCandidate.setInfoDegree(infoDegree);
		infoMasterDegreeCandidate.setInfoCountry(infoCountry);
		infoMasterDegreeCandidate.setInfoNationality(infoNationality);
		infoMasterDegreeCandidate.setInfoIdentificationDocumentType(
			masterDegreeCandidate.getIdentificationDocumentType().toString());
		infoMasterDegreeCandidate.setInfoExecutionYear(infoExecutionYear);
		return infoMasterDegreeCandidate;
	}

	/**
	 * Method copyInfoCountry2ICountry
	 * @param infoCountry
	 * @return
	 */
	public static ICountry copyInfoCountry2ICountry(InfoCountry infoCountry) {
		ICountry country = new Country();
		copyObjectProperties(country, infoCountry);
		return country;
	}

	/**
	 * Method copyICountry2InfoCountry
	 * @param country
	 * @return
	 */
	public static InfoCountry copyICountry2InfoCountry(ICountry country) {
		InfoCountry infoCountry = new InfoCountry();
		copyObjectProperties(infoCountry, country);
		return infoCountry;
	}
	/**
	 * @param role
	 * @return InfoRole
	 */
	public static InfoRole copyIRole2InfoRole(IRole role) {
		InfoRole infoRole = new InfoRole();
		copyObjectProperties(infoRole, role);
		return infoRole;
	}

	public static IBibliographicReference copyInfoBibliographicReference2IBibliographicReference(InfoBibliographicReference infoBibliographicReference) {
		IBibliographicReference bibliographicReference =
			new BibliographicReference();
		IDisciplinaExecucao executionCourse =
			Cloner.copyInfoExecutionCourse2ExecutionCourse(
				infoBibliographicReference.getInfoExecutionCourse());
		copyObjectProperties(
			bibliographicReference,
			infoBibliographicReference);
		bibliographicReference.setExecutionCourse(executionCourse);
		return bibliographicReference;
	}

	public static InfoBibliographicReference copyIBibliographicReference2InfoBibliographicReference(IBibliographicReference bibliographicReference) {
		InfoBibliographicReference infoBibliographicReference =
			new InfoBibliographicReference();
		InfoExecutionCourse infoExecutionCourse =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				bibliographicReference.getExecutionCourse());
		copyObjectProperties(
			infoBibliographicReference,
			bibliographicReference);
		infoBibliographicReference.setInfoExecutionCourse(infoExecutionCourse);
		return infoBibliographicReference;
	}
	/**
			 * Method copyInfoSite2ISite.
			 * @param infoSite
			 * @return ISite
			 */
	public static ISite copyInfoSite2ISite(InfoSite infoSite) {
		ISite site = new Site();
		IDisciplinaExecucao executionCourse =
			Cloner.copyInfoExecutionCourse2ExecutionCourse(
				infoSite.getInfoExecutionCourse());

		copyObjectProperties(site, infoSite);
		site.setExecutionCourse(executionCourse);

		return site;
	}

	/**
		 * Method copyInfoSection2ISection.
		 * @param infoSection
		 * @return ISection
		 **/

	public static ISection copyInfoSection2ISection(InfoSection infoSection) {

		ISection section = new Section();

		ISection fatherSection = null;

		ISite site = Cloner.copyInfoSite2ISite(infoSection.getSite());

		InfoSection infoSuperiorSection =
			(InfoSection) infoSection.getSuperiorSection();

		while (infoSuperiorSection != null) {
			fatherSection =
				Cloner.copyInfoSection2ISection(infoSuperiorSection);
		}

		copyObjectProperties(section, infoSection);

		section.setSuperiorSection(fatherSection);
		section.setSite(site);

		return section;

	}

	/**
	 * Method copyInfoItem2IItem.
	 * @param infoItem
	 * @return IItem
	 **/

	public static IItem copyInfoItem2IItem(InfoItem infoItem) {

		IItem item = new Item();

		ISection section =
			Cloner.copyInfoSection2ISection(infoItem.getInfoSection());

		copyObjectProperties(item, infoItem);

		item.setSection(section);

		return item;

	}

	/**
	 * Method copyInfoAnnouncement2IAnnouncement.
	 * @param infoAnnouncement
	 * @return IAnnouncement
	 */
	public static IAnnouncement copyInfoAnnouncement2IAnnouncement(InfoAnnouncement infoAnnouncement) {
		IAnnouncement announcement = new Announcement();

		ISite site = Cloner.copyInfoSite2ISite(infoAnnouncement.getInfoSite());

		copyObjectProperties(announcement, infoAnnouncement);
		announcement.setSite(site);

		return announcement;
	}

	/**
	 * Method copyIAnnouncement2InfoAnnouncement.
	 * @param announcement
	 * @return InfoAnnouncement
	 */
	public static InfoAnnouncement copyIAnnouncement2InfoAnnouncement(IAnnouncement announcement) {
		InfoAnnouncement infoAnnouncement = new InfoAnnouncement();

		InfoSite infoSite = Cloner.copyISite2InfoSite(announcement.getSite());

		copyObjectProperties(infoAnnouncement, announcement);
		infoAnnouncement.setInfoSite(infoSite);

		return infoAnnouncement;
	}

	/**
	 * Method copyISite2InfoSite.
	 * @param site
	 * @return InfoSite
	 */
	public static InfoSite copyISite2InfoSite(ISite site) {
		InfoSite infoSite = new InfoSite();

		InfoExecutionCourse infoExecutionCourse =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				site.getExecutionCourse());

		copyObjectProperties(infoSite, site);
		infoSite.setInfoExecutionCourse(infoExecutionCourse);

		return infoSite;
	}

	/**
	 * 
	 * @param curriculum
	 * @return InfoCurriculum
	 */
	public static InfoCurriculum copyICurriculum2InfoCurriculum(ICurriculum curriculum) {
		InfoCurriculum infoCurriculum = new InfoCurriculum();

		InfoExecutionCourse infoExecutionCourse =
			Cloner.copyIExecutionCourse2InfoExecutionCourse(
				curriculum.getExecutionCourse());

		copyObjectProperties(infoCurriculum, curriculum);
		infoCurriculum.setInfoExecutionCourse(infoExecutionCourse);

		return infoCurriculum;
	}

	/**
	 * 
	 * @param infoCurriculum
	 * @return ICurriculum
	 */
	public static ICurriculum copyICurriculum2InfoCurriculum(InfoCurriculum infoCurriculum) {
		ICurriculum curriculum = new Curriculum();

		IDisciplinaExecucao executionCourse =
			Cloner.copyInfoExecutionCourse2ExecutionCourse(
				infoCurriculum.getInfoExecutionCourse());

		copyObjectProperties(curriculum, infoCurriculum);
		curriculum.setExecutionCourse(executionCourse);

		return curriculum;
	}
}
