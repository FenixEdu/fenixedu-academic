/*
 * Created on 17/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import org.apache.commons.beanutils.PropertyUtils;

import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoWeeklyOcupation;
import DataBeans.util.Cloner;
import Dominio.teacher.IServiceProviderRegime;
import Dominio.teacher.IWeeklyOcupation;
import Dominio.teacher.ServiceProviderRegime;
import Dominio.teacher.WeeklyOcupation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentServiceProviderRegime;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditTeacherInformation implements IServico
{
    private static EditTeacherInformation service = new EditTeacherInformation();

    public static EditTeacherInformation getService()
    {
        return service;
    }

    /**
	 *  
	 */
    private EditTeacherInformation()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "EditTeacherInformation";
    }

    /**
	 * Executes the service.
	 */
    // TODO: faltam os cargos de gestão
    public Boolean run(InfoServiceProviderRegime infoServiceProviderRegime, InfoWeeklyOcupation infoWeeklyOcupation) throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            
            IPersistentServiceProviderRegime persistentServiceProviderRegime = sp.getIPersistentServiceProviderRegime();
            IServiceProviderRegime oldServiceProviderRegime = Cloner.copyInfoServiceProviderRegime2IServiceProviderRegime(infoServiceProviderRegime);
            
            IServiceProviderRegime newServiceProviderRegime = new ServiceProviderRegime();
            newServiceProviderRegime.setIdInternal(oldServiceProviderRegime.getIdInternal());
            persistentServiceProviderRegime.simpleLockWrite(newServiceProviderRegime);
            PropertyUtils.copyProperties(newServiceProviderRegime, oldServiceProviderRegime);
            
            IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
            IWeeklyOcupation oldWeeklyOcupation = Cloner.copyInfoWeeklyOcupation2IWeeklyOcupation(infoWeeklyOcupation);
            
            IWeeklyOcupation newWeeklyOcupation = new WeeklyOcupation();
            newWeeklyOcupation.setIdInternal(oldWeeklyOcupation.getIdInternal());
            persistentWeeklyOcupation.simpleLockWrite(newWeeklyOcupation);
            PropertyUtils.copyProperties(newWeeklyOcupation, oldWeeklyOcupation);
            
            // TODO: faltam os cargos de gestão
            
            return new Boolean(true);
        } catch (Exception e)
        {
            throw new FenixServiceException(e);
        }
    }
}