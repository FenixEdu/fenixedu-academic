/*
 * Created on 17/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import DataBeans.teacher.InfoOrientation;
import DataBeans.teacher.InfoPublicationsNumber;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoWeeklyOcupation;
import DataBeans.util.Cloner;
import Dominio.teacher.IOrientation;
import Dominio.teacher.IPublicationsNumber;
import Dominio.teacher.IServiceProviderRegime;
import Dominio.teacher.IWeeklyOcupation;
import Dominio.teacher.Orientation;
import Dominio.teacher.PublicationsNumber;
import Dominio.teacher.ServiceProviderRegime;
import Dominio.teacher.WeeklyOcupation;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentOrientation;
import ServidorPersistente.teacher.IPersistentPublicationsNumber;
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
    public Boolean run(
        InfoServiceProviderRegime infoServiceProviderRegime,
        InfoWeeklyOcupation infoWeeklyOcupation,
        List infoOrientations,
        List infoPublicationsNumbers)
        throws FenixServiceException
    {
        try
        {
            Date date = Calendar.getInstance().getTime();
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentServiceProviderRegime persistentServiceProviderRegime =
                sp.getIPersistentServiceProviderRegime();
            IServiceProviderRegime oldServiceProviderRegime =
                Cloner.copyInfoServiceProviderRegime2IServiceProviderRegime(infoServiceProviderRegime);

            IServiceProviderRegime newServiceProviderRegime = new ServiceProviderRegime();
            newServiceProviderRegime.setIdInternal(oldServiceProviderRegime.getIdInternal());
            persistentServiceProviderRegime.simpleLockWrite(newServiceProviderRegime);
            PropertyUtils.copyProperties(newServiceProviderRegime, oldServiceProviderRegime);
            newServiceProviderRegime.setLastModificationDate(date);

            IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
            IWeeklyOcupation oldWeeklyOcupation =
                Cloner.copyInfoWeeklyOcupation2IWeeklyOcupation(infoWeeklyOcupation);

            IWeeklyOcupation newWeeklyOcupation = new WeeklyOcupation();
            newWeeklyOcupation.setIdInternal(oldWeeklyOcupation.getIdInternal());
            persistentWeeklyOcupation.simpleLockWrite(newWeeklyOcupation);
            PropertyUtils.copyProperties(newWeeklyOcupation, oldWeeklyOcupation);
            newWeeklyOcupation.setLastModificationDate(date);

            IPersistentOrientation persistentOrientation = sp.getIPersistentOrientation();
            Iterator iter = infoOrientations.iterator();
            while(iter.hasNext())
            {
                InfoOrientation infoOrientation = (InfoOrientation) iter.next();
                IOrientation oldOrientation = Cloner.copyInfoOrientation2IOrientation(infoOrientation);
                IOrientation newOrientation = new Orientation();
                newOrientation.setIdInternal(oldOrientation.getIdInternal());
                persistentOrientation.simpleLockWrite(newOrientation);
                PropertyUtils.copyProperties(newOrientation, oldOrientation);
                newOrientation.setLastModificationDate(date);
            }
            
            IPersistentPublicationsNumber persistentPublicationsNumber = sp.getIPersistentPublicationsNumber();
            iter = infoPublicationsNumbers.iterator();
            while(iter.hasNext())
            {
                InfoPublicationsNumber infoPublicationsNumber = (InfoPublicationsNumber) iter.next();
                IPublicationsNumber oldPublicationsNumber = Cloner.copyInfoPublicationsNumber2IPublicationsNumber(infoPublicationsNumber);
                IPublicationsNumber newPublicationsNumber = new PublicationsNumber();
                newPublicationsNumber.setIdInternal(oldPublicationsNumber.getIdInternal());
                persistentPublicationsNumber.simpleLockWrite(newPublicationsNumber);
                PropertyUtils.copyProperties(newPublicationsNumber, oldPublicationsNumber);
                newPublicationsNumber.setLastModificationDate(date);
            }
            // TODO: faltam os cargos de gestão

            return new Boolean(true);
        } catch (Exception e)
        {
            throw new FenixServiceException(e);
        }
    }
}