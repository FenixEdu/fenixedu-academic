/*
 * Created on 23/Jan/2004
 *  
 */

package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantCostCenter;
import DataBeans.grant.contract.InfoGrantProject;
import DataBeans.util.Cloner;
import Dominio.grant.contract.GrantCostCenter;
import Dominio.grant.contract.GrantProject;
import Dominio.grant.contract.IGrantCostCenter;
import Dominio.grant.contract.IGrantProject;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantCostCenter;
import ServidorPersistente.grant.IPersistentGrantProject;
/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantPaymentEntity implements IService
{

	/**
	 * The constructor of this class.
	 */
	public EditGrantPaymentEntity()
	{
	}
	
	public void run(InfoGrantCostCenter infoObject) throws FenixServiceException
	{
		ISuportePersistente sp = null;
		IGrantCostCenter grantCostCenter = null;
		InfoGrantCostCenter infoGrantCostCenter = (InfoGrantCostCenter) infoObject;
		IPersistentGrantCostCenter pgcc = null;
		
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			pgcc = sp.getIPersistentGrantCostCenter();
			IGrantCostCenter costCenterExists = pgcc.readGrantCostCenterByNumber(infoGrantCostCenter.getNumber());
			if((costCenterExists != null) && !costCenterExists.getIdInternal().equals(infoGrantCostCenter.getIdInternal()))
				throw new ExistingServiceException();
			grantCostCenter = (IGrantCostCenter) pgcc.readByOID(GrantCostCenter.class,infoGrantCostCenter.getIdInternal());
			if(grantCostCenter == null)
				grantCostCenter = new GrantCostCenter();
			pgcc.simpleLockWrite(grantCostCenter);
			grantCostCenter.setDesignation(infoGrantCostCenter.getDesignation());
			grantCostCenter.setNumber(infoGrantCostCenter.getNumber());
			grantCostCenter.setResponsibleTeacher(Cloner.copyInfoGrantCostCenter2IGrantCostCenter(infoGrantCostCenter).getResponsibleTeacher());
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("Unable to dao factory!", e);
		}
	}
	
	public void run(InfoGrantProject infoObject) throws FenixServiceException
	{
		ISuportePersistente sp = null;
		IGrantProject grantProject = null;
		InfoGrantProject infoGrantProject = (InfoGrantProject) infoObject;
		IGrantProject iGrantProject = Cloner.copyInfoGrantProject2IGrantProject(infoGrantProject);
		IPersistentGrantProject pgp = null;
		
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			pgp = sp.getIPersistentGrantProject();
			IGrantProject projectExists = pgp.readGrantProjectByNumber(infoGrantProject.getNumber()); 
			if((projectExists != null) && !projectExists.getIdInternal().equals(infoGrantProject.getIdInternal()))
				throw new ExistingServiceException();
			grantProject = (IGrantProject) pgp.readByOID(GrantProject.class,infoGrantProject.getIdInternal());
			if(grantProject == null)
				grantProject = new GrantProject();
			pgp.simpleLockWrite(grantProject);
			Cloner.copyInfoGrantProject2IGrantProject(infoGrantProject);
			grantProject.setDesignation(infoGrantProject.getDesignation());
			grantProject.setNumber(infoGrantProject.getNumber());
			grantProject.setResponsibleTeacher(Cloner.copyInfoGrantProject2IGrantProject(infoGrantProject).getResponsibleTeacher());
			grantProject.setGrantCostCenter(iGrantProject.getGrantCostCenter());
		}
		catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace();
			throw new FenixServiceException("Unable to dao factory!", e);
		}
	}
}
