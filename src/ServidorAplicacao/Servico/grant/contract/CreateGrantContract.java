/*
 * Created on 18/Nov/2003
 * 
 */
package ServidorAplicacao.Servico.grant.contract;

import org.apache.commons.beanutils.PropertyUtils;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantType;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.grant.contract.GrantOrientationTeacher;
import Dominio.grant.contract.GrantResponsibleTeacher;
import Dominio.grant.contract.GrantType;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantOrientationTeacher;
import Dominio.grant.contract.IGrantResponsibleTeacher;
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
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;
import ServidorPersistente.grant.IPersistentGrantResponsibleTeacher;
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
        throws FenixServiceException
    {
        try
        {
            IPersistentGrantResponsibleTeacher rt = sp.getIPersistentGrantResponsibleTeacher();
            IPersistentGrantOrientationTeacher ot = sp.getIPersistentGrantOrientationTeacher();
            InfoGrantContract infoGrantContract = (InfoGrantContract) infoObject;
            IGrantResponsibleTeacher oldGrantResponsibleTeacher = null;
			IGrantResponsibleTeacher newGrantResponsibleTeacher = new GrantResponsibleTeacher();
            IGrantOrientationTeacher oldGrantOrientationTeacher = null;
			IGrantOrientationTeacher newGrantOrientationTeacher = new GrantOrientationTeacher();

            rt.simpleLockWrite(newGrantResponsibleTeacher);
            oldGrantResponsibleTeacher =
                Cloner.copyInfoGrantResponsibleTeacher2IGrantResponsibleTeacher(
                    infoGrantContract.getGrantResponsibleTeacherInfo());
			PropertyUtils.copyProperties(newGrantResponsibleTeacher,oldGrantResponsibleTeacher);
			newGrantResponsibleTeacher.setGrantContract((IGrantContract) newDomainObject);
			
            ot.simpleLockWrite(newGrantOrientationTeacher);
            oldGrantOrientationTeacher =
                Cloner.copyInfoGrantOrientationTeacher2IGrantOrientationTeacher(
                    infoGrantContract.getGrantOrientationTeacherInfo());
			PropertyUtils.copyProperties(newGrantOrientationTeacher,oldGrantOrientationTeacher);
			newGrantOrientationTeacher.setGrantContract((IGrantContract) newDomainObject);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        } catch (Exception e)
        {
            throw new FenixServiceException(e);
        }
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

    private InfoTeacher checkIfGrantResponsibleTeacherExists(
        Integer teacherNumber,
        IPersistentTeacher pt)
        throws FenixServiceException
    {
        InfoTeacher infoTeacher = new InfoTeacher();
        ITeacher teacher = new Teacher();
        try
        {
            teacher = pt.readByNumber(teacherNumber);
            infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            if (infoTeacher == null)
                throw new GrantResponsibleTeacherNotFoundException();
        } catch (ExcepcaoPersistencia persistentException)
        {
            throw new FenixServiceException(persistentException.getMessage());
        }
        return infoTeacher;
    }

    private InfoTeacher checkIfGrantOrientationTeacherExists(
        Integer teacherNumber,
        IPersistentTeacher pt)
        throws FenixServiceException
    {
        InfoTeacher infoTeacher = new InfoTeacher();
        ITeacher teacher = new Teacher();
        try
        {
            teacher = pt.readByNumber(teacherNumber);
            infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
            if (pt.readByNumber(teacherNumber) == null)
                throw new GrantOrientationTeacherNotFoundException();
        } catch (ExcepcaoPersistencia persistentException)
        {
            throw new FenixServiceException(persistentException.getMessage());
        }
        return infoTeacher;
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

            infoGrantContract.getGrantResponsibleTeacherInfo().setResponsibleTeacherInfo(
                checkIfGrantResponsibleTeacherExists(
                    infoGrantContract
                        .getGrantResponsibleTeacherInfo()
                        .getResponsibleTeacherInfo()
                        .getTeacherNumber(),
                    pTeacher));

            infoGrantContract.getGrantOrientationTeacherInfo().setOrientationTeacherInfo(
                checkIfGrantOrientationTeacherExists(
                    infoGrantContract
                        .getGrantOrientationTeacherInfo()
                        .getOrientationTeacherInfo()
                        .getTeacherNumber(),
                    pTeacher));

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