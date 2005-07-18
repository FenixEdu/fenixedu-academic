/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author jpvl
 */
public class EditProfessorship extends EditDomainObjectService {

    @Override
	protected void copyInformationFromIntoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject) throws ExcepcaoPersistencia {
		InfoProfessorship infoProfessorship = (InfoProfessorship)infoObject;
		IProfessorship professorship = (Professorship)domainObject;
		
		IExecutionCourse executionCourse = new ExecutionCourse();
		IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
		executionCourse = (ExecutionCourse)persistentExecutionCourse.readByOID(ExecutionCourse.class,infoProfessorship.getInfoExecutionCourse().getIdInternal());
		professorship.setExecutionCourse(executionCourse);
		
		professorship.setHours(infoProfessorship.getHours());
		
		professorship.setKeyExecutionCourse(executionCourse.getIdInternal());
		
		IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = persistentTeacher.readByNumber(infoProfessorship.getInfoTeacher().getTeacherNumber());
		professorship.setKeyTeacher(teacher.getIdInternal());
		professorship.setTeacher(teacher);
		
		
		
		
	}

	@Override
	protected IDomainObject createNewDomainObject(InfoObject infoObject) {
		// TODO Auto-generated method stub
		return new Professorship();
	}

	@Override
	protected Class getDomainObjectClass() {
		// TODO Auto-generated method stub
		return Professorship.class;
	}

	protected boolean canCreate(IDomainObject domainObject) throws FenixServiceException,
            ExcepcaoPersistencia {

        IProfessorship professorship = (IProfessorship) domainObject;
        ISuportePersistente sp;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        IProfessorship professorshipFromDB = professorshipDAO.readByTeacherAndExecutionCourse(
                professorship.getTeacher().getIdInternal(), professorship.getExecutionCourse()
                        .getIdInternal());

        boolean isNewProfessorship = professorship.getIdInternal() == null
                || professorship.getIdInternal().intValue() == 0;
        Integer wantedId = professorship.getIdInternal();
        return ((!isNewProfessorship && (professorshipFromDB != null) && (professorshipFromDB
                .getIdInternal().equals(wantedId))) || (isNewProfessorship && (professorshipFromDB == null)));

    }
//
//    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
//        return Cloner.copyInfoProfessorship2IProfessorship((InfoProfessorship) infoObject);
//    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentProfessorship();
    }

    public String getNome() {
        return "EditProfessorShip";
    }

}