/*
 * ReadExamsByOID.java
 *
 * Created on 12/11/2003
 */

package ServidorAplicacao.Servico.sop.exams;

/**
  * @author Ana e Ricardo
  * 
 **/
import DataBeans.InfoExam;
import DataBeans.util.Cloner;
import Dominio.Exam;
import Dominio.IExam;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExamByOID implements IServico
{

    private static ReadExamByOID _servico = new ReadExamByOID();
    /**
     * The singleton access method of this class.
     **/
    public static ReadExamByOID getService()
    {
        return _servico;
    }

    /**
     * The actor of this class.
     **/
    private ReadExamByOID()
    {
    }

    /**
     * Devolve o nome do servico
     **/
    public final String getNome()
    {
        return "ReadExamByOID";
    }

    public InfoExam run(Integer oid) throws FenixServiceException
    {
        InfoExam infoExam = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExam exam = (IExam) sp.getIPersistentExam().readByOID(Exam.class, oid);
            if (exam == null)
            {
                throw new FenixServiceException("The exam does not exist");
            }
            infoExam = Cloner.copyIExam2InfoExam(exam);

        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }
        return infoExam;
    }
}