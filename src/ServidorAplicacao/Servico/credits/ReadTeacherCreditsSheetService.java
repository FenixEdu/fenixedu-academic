/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoProfessorship;
import DataBeans.InfoTeacher;
import DataBeans.credits.TeacherCreditsSheetDTO;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IProfessorship;
import Dominio.IShiftProfessorship;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentShiftProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class ReadTeacherCreditsSheetService implements IServico
{

    private static ReadTeacherCreditsSheetService instance = new ReadTeacherCreditsSheetService();

    public static ReadTeacherCreditsSheetService getService()
    {
        return instance;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadTeacherCreditsSheetService";
    }
    private IExecutionPeriod readExecutionPeriod(
        InfoExecutionPeriod infoExecutionPeriod,
        IPersistentExecutionPeriod executionPeriodDAO)
        throws FenixServiceException
    {
        IExecutionPeriod executionPeriod;
        try
        {
            if (infoExecutionPeriod == null)
            {
                executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            } else
            {
                executionPeriod =
                    (IExecutionPeriod) executionPeriodDAO.readByOId(
                        new ExecutionPeriod(infoExecutionPeriod.getIdInternal()),
                        false);
            }
        } catch (ExcepcaoPersistencia e1)
        {
            e1.printStackTrace(System.out);
            throw new FenixServiceException("Error getting execution period!", e1);
        }
        return executionPeriod;
    }
    /**
	 * @param sp
	 * @param infoTeacher
	 * @param infoExecutionPeriod
	 * @return
	 */
    private List readInfoCreditList(
        ISuportePersistente sp,
        ITeacher teacher,
        IExecutionPeriod executionPeriod)
    {
        // TODO Read InfoCreditList
        return null;
    }
    /**
	 * @param sp
	 * @param infoTeacher
	 * @param infoExecutionPeriod
	 * @return
	 */
    private List readInfoMasterDegreeProfessorships(
        ISuportePersistente sp,
        ITeacher teacher,
        IExecutionPeriod executionPeriod)
        throws FenixServiceException
    {
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        List masterDegreeProfessorshipList;
        try
        {
            masterDegreeProfessorshipList =
                professorshipDAO.readByTeacherAndTypeOfDegree(teacher, TipoCurso.MESTRADO_OBJ);
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems getting master degree professorships!", e);
        }
        List infoMasterDegreeProfessorships =
            (List) CollectionUtils.collect(masterDegreeProfessorshipList, new Transformer()
        {

            public Object transform(Object input)
            {
                IProfessorship professorship = (IProfessorship) input;
                InfoProfessorship infoProfessorship =
                    Cloner.copyIProfessorship2InfoProfessorship(professorship);
                return infoProfessorship;
            }
        });

        return infoMasterDegreeProfessorships;
    }
    /**
	 * @param sp
	 * @param infoTeacher
	 * @param infoExecutionPeriod
	 * @return
	 */
    private List readInfoShiftProfessorships(
        ISuportePersistente sp,
        ITeacher teacher,
        IExecutionPeriod executionPeriod)
        throws FenixServiceException
    {
        IPersistentShiftProfessorship shiftProfessorshipDAO = sp.getIPersistentShiftProfessorship();

        List shiftProfessorships = null;
//        try
//        {
//            shiftProfessorships =
//                shiftProfessorshipDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
//        } catch (ExcepcaoPersistencia e)
//        {
//            e.printStackTrace(System.out);
//            throw new FenixServiceException("Problems getting shiftProfessorships!", e);
//        }
        List infoShiftProfessorships =
            (List) CollectionUtils.collect(shiftProfessorships, new Transformer()
        {

            public Object transform(Object input)
            {
                IShiftProfessorship shiftProfessorship = (IShiftProfessorship) input;
                InfoShiftProfessorship infoShiftProfessorship =
                    Cloner.copyIShiftProfessorship2InfoShiftProfessorship(shiftProfessorship);
                return infoShiftProfessorship;
            }
        });

        return infoShiftProfessorships;
    }

    private ITeacher readTeacher(InfoTeacher infoTeacher, IPersistentTeacher teacherDAO)
        throws FenixServiceException
    {
        ITeacher teacher;
        try
        {
            teacher = teacherDAO.readByNumber(infoTeacher.getTeacherNumber());
        } catch (ExcepcaoPersistencia e2)
        {
            e2.printStackTrace(System.out);
            throw new FenixServiceException("Error getting teacher!", e2);
        }
        return teacher;
    }

    public TeacherCreditsSheetDTO run(InfoTeacher infoTeacher, InfoExecutionPeriod infoExecutionPeriod)
        throws FenixServiceException
    {

        TeacherCreditsSheetDTO teacherCreditsSheetDTO = new TeacherCreditsSheetDTO();

        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems with database!", e);
        }

        IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
        IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
        IExecutionPeriod executionPeriod = readExecutionPeriod(infoExecutionPeriod, executionPeriodDAO);
        ITeacher teacher = readTeacher(infoTeacher, teacherDAO);

        List masterDegreeProfessorships =
            readInfoMasterDegreeProfessorships(sp, teacher, executionPeriod);

        List infoShiftProfessorships = readInfoShiftProfessorships(sp, teacher, executionPeriod);

        List infoCreditLine = readInfoCreditList(sp, teacher, executionPeriod);

        teacherCreditsSheetDTO.setInfoTeacher(infoTeacher);
        teacherCreditsSheetDTO.setInfoExecutionPeriod(infoExecutionPeriod);

        teacherCreditsSheetDTO.setInfoCreditLineList(infoCreditLine);
        teacherCreditsSheetDTO.setInfoMasterDegreeProfessorships(masterDegreeProfessorships);
        teacherCreditsSheetDTO.setInfoShiftProfessorshipList(infoShiftProfessorships);

        return teacherCreditsSheetDTO;
    }
}
