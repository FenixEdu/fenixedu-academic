/*
 * Created on 14/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ICareer;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.framework.EditDomainObjectAuthorizationFilter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;
import net.sourceforge.fenixedu.util.RoleType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditCareerTeacherAuthorizationFilter extends EditDomainObjectAuthorizationFilter {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.EditDomainObjectTeacherAuthorizationFilter#domainObjectBelongsToTeacher(ServidorAplicacao.IUserView,
     *      net.sourceforge.fenixedu.dataTransferObject.InfoObject)
     */
    protected boolean verifyCondition(IUserView id, InfoObject infoOject) {
        try {
            InfoCareer infoCareer = (InfoCareer) infoOject;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCareer persistentCareer = sp.getIPersistentCareer();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ITeacher teacher = persistentTeacher.readTeacherByUsername(id.getUtilizador());

            boolean isNew = (infoCareer.getIdInternal() == null)
                    || (infoCareer.getIdInternal().equals(new Integer(0)));
            if (isNew)
                return true;

            ICareer career = (ICareer) persistentCareer.readByOID(Career.class, infoCareer
                    .getIdInternal());

            return career.getTeacher().equals(teacher);
        } catch (ExcepcaoPersistencia e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.framework.EditDomainObjectAuthorizationFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }
}