/*
 * Created on 18/Nov/2003
 * 
 */
package ServidorAplicacao.Servico.grant.contract;

import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantType;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantType;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import ServidorAplicacao.Servico.exceptions.grant.GrantResponsibleTeacherNotFoundException;
import ServidorAplicacao.Servico.exceptions.grant.GrantTypeNotFoundException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantType;

/**
 * @author  Barbosa
 * @author  Pica
 *  
 */
public class CreateGrantContract extends ServidorAplicacao.Servico.framework.EditDomainObjectService
{

    private static CreateGrantContract service = new CreateGrantContract();
    /**
     * The singleton access method of this class.
     */
    public static CreateGrantContract getService()
    {
        return service;
    }
    /**
     * The constructor of this class.
     */
    private CreateGrantContract()
    {
    }
    /**
     * The name of the service
     */
    public final String getNome()
    {
        return "CreateGrantContract";
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#clone2DomainObject(DataBeans.InfoObject)
     */
    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        return Cloner.copyInfoGrantContract2IGrantContract((InfoGrantContract) infoObject);
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        return sp.getIPersistentGrantContract();
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.EditDomainObjectService#readObjectByUnique(IDomainObject,ServidorPersistente.ISuportePersistente)
     */
    protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentGrantContract pgc = sp.getIPersistentGrantContract();
        IGrantContract grantContract = (IGrantContract) domainObject;

        return pgc.readGrantContractByNumberAndGrantOwner(
            grantContract.getContractNumber(),
            grantContract.getGrantOwner().getIdInternal());
    }

    protected void doAfterLock(
        IDomainObject newDomainObject,
        InfoObject infoObject,
        ISuportePersistente sp)
    {
    	
    }

    protected InfoGrantType checkIfGrantTypeExists(String sigla, IPersistentGrantType pt)
        throws FenixServiceException
    {
        InfoGrantType infoGrantType = new InfoGrantType();
        IGrantType grantType = new GrantType();
        try
        {
            grantType = pt.readGrantTypeBySigla(sigla);
            infoGrantType = Cloner.copyIGrantType2InfoGrantType(grantType);
            if (infoGrantType == null)
                throw new GrantTypeNotFoundException();
        } catch (ExcepcaoPersistencia persistentException)
        {
            throw new FenixServiceException(persistentException.getMessage());
        }
        return infoGrantType;
    }

    private void checkIfGrantResponsibleTeacherExists(Integer teacherNumber, IPersistentTeacher pt)
        throws FenixServiceException
    {
        try
        {
            if (pt.readByNumber(teacherNumber) == null)
                throw new GrantResponsibleTeacherNotFoundException();
        } catch (ExcepcaoPersistencia persistentException)
        {
            throw new FenixServiceException(persistentException.getMessage());
        }
    }

    private void checkIfGrantOrientationTeacherExists(Integer teacherNumber, IPersistentTeacher pt)
        throws FenixServiceException
    {
        try
        {
            if (pt.readByNumber(teacherNumber) == null)
                throw new GrantOrientationTeacherNotFoundException();
        } catch (ExcepcaoPersistencia persistentException)
        {
            throw new FenixServiceException(persistentException.getMessage());
        }
    }

    /**
     * Executes the service.
     */
    public boolean run(InfoGrantContract infoGrantContract) throws FenixServiceException
    {
        Boolean result = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher pTeacher = sp.getIPersistentTeacher();
            IPersistentGrantType pGrantType = sp.getIPersistentGrantType();

            infoGrantContract.setGrantTypeInfo(
                checkIfGrantTypeExists(infoGrantContract.getGrantTypeInfo().getSigla(), pGrantType));

            checkIfGrantResponsibleTeacherExists(
                infoGrantContract
                    .getGrantResponsibleTeacherInfo()
                    .getResponsibleTeacherInfo()
                    .getTeacherNumber(),
                pTeacher);

            checkIfGrantOrientationTeacherExists(
                infoGrantContract
                    .getGrantOrientationTeacherInfo()
                    .getOrientationTeacherInfo()
                    .getTeacherNumber(),
                pTeacher);

            result = super.run(infoGrantContract);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        } catch (Exception e)
        {
            throw new FenixServiceException(e);
        }
        return result.booleanValue();
    }
}