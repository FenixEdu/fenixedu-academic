/*
 * Created on 27/Mai/2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoProfessorship;
import DataBeans.util.Cloner;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadProfessorships implements IServico
{

    private static ReadProfessorships service = new ReadProfessorships();

    /**
	 * The singleton access method of this class.
	 */

    public static ReadProfessorships getService()
    {
        return service;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadProfessorships";
    }
    public List run(IUserView userView) throws FenixServiceException
    {

        List infoProfessorshipsList = new ArrayList();

        try
        {
            ISuportePersistente persistentSuport;
            persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentProfessorship persistentProfessorship =
                persistentSuport.getIPersistentProfessorship();
            IPersistentTeacher teacherDAO = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = teacherDAO.readTeacherByUsername(userView.getUtilizador());

            List professorships = persistentProfessorship.readByTeacher(teacher);

            Iterator iter = professorships.iterator();

            while (iter.hasNext())
            {

                IProfessorship professorship = (IProfessorship) iter.next();
                InfoProfessorship infoProfessorShip =
                    Cloner.copyIProfessorship2InfoProfessorship(professorship);

                infoProfessorshipsList.add(infoProfessorShip);
            }

            return infoProfessorshipsList;
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

}
