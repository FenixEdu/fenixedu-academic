/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class EditProfessorship extends EditDomainObjectService {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#canCreate(Dominio.IDomainObject)
     */
    protected boolean canCreate(IDomainObject domainObject) throws FenixServiceException {

        IProfessorship professorship = (IProfessorship) domainObject;
        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
            IProfessorship professorshipFromDB = professorshipDAO.readByTeacherIDAndExecutionCourseID(
                    professorship.getTeacher(), professorship.getExecutionCourse());

            boolean isNewProfessorship = professorship.getIdInternal() == null
                    || professorship.getIdInternal().intValue() == 0;
            Integer wantedId = professorship.getIdInternal();
            return ((!isNewProfessorship && (professorshipFromDB != null) && (professorshipFromDB
                    .getIdInternal().equals(wantedId))) || (isNewProfessorship && (professorshipFromDB == null)));

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject) {
        return Cloner.copyInfoProfessorship2IProfessorship((InfoProfessorship) infoObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        return sp.getIPersistentProfessorship();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "EditProfessorShip";
    }

}