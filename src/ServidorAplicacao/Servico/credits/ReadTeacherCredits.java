package ServidorAplicacao.Servico.credits;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.teacher.credits.InfoCredits;
import DataBeans.util.Cloner;
import Dominio.ICredits;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.credits.IPersistentCredits;
import ServidorPersistente.credits.IPersistentManagementPositionCreditLine;
import ServidorPersistente.credits.IPersistentServiceExemptionCreditLine;

/**
 * @author jpvl
 *  
 */
public class ReadTeacherCredits implements IService
{

    public List run(Integer teacherOID) throws FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCredits creditsDAO = sp.getIPersistentCredits();
            
            ITeacher teacher =new Teacher(teacherOID);
            
            List credits = creditsDAO.readByTeacher(teacher);
            List infoCredits = (List) CollectionUtils.collect(credits, new Transformer()
            {

                public Object transform(Object input)
                {
                    ICredits credits = (ICredits) input;
                    InfoCredits infoCredits = Cloner.copyICredits2InfoCredits(credits);
                    return infoCredits;
                }
            });
            
            
            return infoCredits;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException("Problems on database!", e);
        }

    }

}
