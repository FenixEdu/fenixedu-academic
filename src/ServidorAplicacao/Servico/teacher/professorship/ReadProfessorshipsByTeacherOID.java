/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoProfessorship;
import DataBeans.util.Cloner;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class ReadProfessorshipsByTeacherOID implements IServico
{

    private static ReadProfessorshipsByTeacherOID service = new ReadProfessorshipsByTeacherOID();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadProfessorshipsByTeacherOID getService()
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
        return "ReadProfessorshipsByTeacherOID";
    }

    public List run(Integer teacherOID) throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();

            ITeacher teacher = (ITeacher) teacherDAO.readByOId(new Teacher(teacherOID), false);

            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();

            List professorships = professorshipDAO.readByTeacher(teacher);

            List infoProfessorshipList =
                (List) CollectionUtils.collect(professorships, new Transformer()
            {

                public Object transform(Object input)
                {
                    IProfessorship professorship = (IProfessorship) input;
                    InfoProfessorship infoProfessorShip =
                        Cloner.copyIProfessorship2InfoProfessorship(professorship);
                    return infoProfessorShip;
                }
            });

            return infoProfessorshipList;
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }

    }
}
