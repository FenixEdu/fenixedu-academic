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
public class CreditsServiceWithInfoProfessorshipArgumentAuthorization
        extends
            AbstractTeacherDepartmentAuthorization
{
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.credits.AbstractTeacherDepartmentAuthorization#getTeacherId(java.lang.Object[])
	 */
    protected Integer getTeacherId(Object[] arguments, ISuportePersistente sp)
            throws FenixServiceException
    {
        InfoProfessorship infoProfessorship = (InfoProfessorship) arguments[0];

        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        ITeacher teacher = null;
        try
        {
            teacher = teacherDAO.readByNumber(infoProfessorship.getInfoTeacher().getTeacherNumber());
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }
        return teacher == null ? null : teacher.getIdInternal();
    }

    /**
	 *  
	 */
    public CreditsServiceWithInfoProfessorshipArgumentAuthorization()
    {
        super();
    }
}