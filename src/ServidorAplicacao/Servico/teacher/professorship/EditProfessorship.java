/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import DataBeans.InfoObject;
import DataBeans.InfoProfessorship;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.IProfessorship;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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