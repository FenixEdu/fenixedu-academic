/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.degree.finalProject;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;
import DataBeans.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import DataBeans.degree.finalProject.TeacherDegreeFinalProjectStudentsDTO;
import DataBeans.util.Cloner;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.degree.finalProject.IPersistentTeacherDegreeFinalProjectStudent;

/**
 * @author jpvl
 */
public class ReadTeacherDFPStudentsService implements IServico
{
    private static ReadTeacherDFPStudentsService service = new ReadTeacherDFPStudentsService();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadTeacherDFPStudentsService getService()
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
        // TODO Auto-generated method stub
        return "ReadTeacherDFPStudents";
    }

    public TeacherDegreeFinalProjectStudentsDTO run(InfoTeacher infoTeacher)
        throws FenixServiceException
    {
        TeacherDegreeFinalProjectStudentsDTO teacherDfpStudentsDTO =
            new TeacherDegreeFinalProjectStudentsDTO();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

            IExecutionYear executionYear = executionYearDAO.readCurrentExecutionYear();
            InfoExecutionYear infoExecutionYear =
                (InfoExecutionYear) Cloner.get(executionYear);

            ITeacher teacher =
                (ITeacher) teacherDAO.readByOId(new Teacher(infoTeacher.getIdInternal()), false);
            InfoTeacher infoTeacher2 = Cloner.copyITeacher2InfoTeacher(teacher);

            IPersistentTeacherDegreeFinalProjectStudent teacherDfpStudentDAO =
                sp.getIPersistentTeacherDegreeFinalProjectStudent();

            List teacherDFPStudentList =
                teacherDfpStudentDAO.readByTeacherAndExecutionYear(teacher, executionYear);

            List infoteacherDFPStudentList =
                (List) CollectionUtils.collect(teacherDFPStudentList, new Transformer()
            {

                public Object transform(Object input)
                {
                    ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
                        (ITeacherDegreeFinalProjectStudent) input;
                    InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent =
                        Cloner
                            .copyITeacherDegreeFinalProjectStudent2InfoTeacherDegreeFinalProjectStudent(
                            teacherDegreeFinalProjectStudent);
                    return infoTeacherDegreeFinalProjectStudent;
                }
            });

            teacherDfpStudentsDTO.setInfoTeacher(infoTeacher2);
            teacherDfpStudentsDTO.setInfoExecutionYear(infoExecutionYear);
            teacherDfpStudentsDTO.setInfoTeacherDegreeFinalProjectStudentList(infoteacherDFPStudentList);
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!");
        }

        return teacherDfpStudentsDTO;

    }
}
