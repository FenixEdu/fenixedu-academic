/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import DataBeans.InfoProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class CreditsServiceWithInfoProfessorshipArgumentAuthorization extends
        AbstractTeacherDepartmentAuthorization {

    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoProfessorship infoProfessorship = (InfoProfessorship) arguments[0];

        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        ITeacher teacher = teacherDAO
                .readByNumber(infoProfessorship.getInfoTeacher().getTeacherNumber());

        return teacher == null ? null : teacher.getIdInternal();
    }

}