/*
 * Created on Nov 29, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Filtro.credits;

import DataBeans.teacher.professorship.InfoSupportLesson;
import Dominio.IProfessorship;
import Dominio.Professorship;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.ISuportePersistente;

/**
 * @author jpvl
 */
public class EditSupportLessonAuthorization extends AbstractTeacherDepartmentAuthorization
{
	public final static EditSupportLessonAuthorization filter = new EditSupportLessonAuthorization();

	public static EditSupportLessonAuthorization getInstance()
	{
		return filter;
	}

    
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
	 */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws FenixServiceException
    {
        InfoSupportLesson supportLesson = (InfoSupportLesson) arguments[1];
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        
        IProfessorship professorship = (IProfessorship) professorshipDAO.readByOId(new Professorship(
                supportLesson.getInfoProfessorship().getIdInternal()),
                false);
        
        return professorship != null ? professorship.getTeacher().getIdInternal() : null;
    }

}