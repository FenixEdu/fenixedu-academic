/*
 * Created on 18/Nov/2003
 * 
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherEndDateBeforeBeginDateException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherPeriodConflictException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherPeriodNotWithinContractPeriodException;
import ServidorAplicacao.Servico.exceptions.grant.GrantResponsibleTeacherEndDateBeforeBeginDateException;
import ServidorAplicacao.Servico.exceptions.grant.GrantResponsibleTeacherNotFoundException;
import ServidorAplicacao.Servico.exceptions.grant.GrantResponsibleTeacherPeriodConflictException;
import ServidorAplicacao.Servico.exceptions.grant.GrantResponsibleTeacherPeriodNotWithinContractPeriodException;
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

    protected void checkIfGrantTeacherPeriodConflict(
        IGrantContract grantContract,
        InfoGrantContract infoGrantContract,
        ISuportePersistente sp)
        throws FenixServiceException
    {
        Date beginResponsibleDate = infoGrantContract.getGrantResponsibleTeacherInfo().getBeginDate();
        Date beginOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getBeginDate();
        try
        {
            //check that new RESPONSIBILITY period does NOT CONFLICT with any other
            IPersistentGrantResponsibleTeacher grt = sp.getIPersistentGrantResponsibleTeacher();
            List responsibles =
                grt.readActualGrantResponsibleTeacherByContract(
                    grantContract,
                    infoGrantContract.getGrantResponsibleTeacherInfo().getIdInternal());
            Iterator respIter = responsibles.iterator();
            IGrantResponsibleTeacher responsibleTeacher = null;
            while (respIter.hasNext())
            {
                responsibleTeacher = (IGrantResponsibleTeacher) respIter.next();
                if ((responsibleTeacher != null)
                    && (beginResponsibleDate.before(responsibleTeacher.getEndDate())))
                    throw new GrantResponsibleTeacherPeriodConflictException();
            }

            //check that new ORIENTATION period does NOT CONFLICT with any other
            IPersistentGrantOrientationTeacher got = sp.getIPersistentGrantOrientationTeacher();
            List orientators =
                got.readActualGrantOrientationTeacherByContract(
                    grantContract,
                    infoGrantContract.getGrantOrientationTeacherInfo().getIdInternal());
            Iterator orienIter = orientators.iterator();
            IGrantOrientationTeacher orientationTeacher = null;
            while (orienIter.hasNext())
            {
                orientationTeacher = (IGrantOrientationTeacher) orienIter.next();
                if ((orientationTeacher != null)
                    && (beginOrientationDate.before(orientationTeacher.getEndDate())))
                    throw new GrantOrientationTeacherPeriodConflictException();
            }
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException();
        }
    }

    protected void checkIfGrantTeacherPeriodWithinContractPeriod(InfoGrantContract infoGrantContract)
        throws FenixServiceException
    {
        Date beginContractDate = infoGrantContract.getDateBeginContract();
        Date endContractDate = infoGrantContract.getDateEndContract();
        Date beginResponsibleDate = infoGrantContract.getGrantResponsibleTeacherInfo().getBeginDate();
        Date endResponsibleDate = infoGrantContract.getGrantResponsibleTeacherInfo().getEndDate();
        Date beginOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getBeginDate();
        Date endOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getEndDate();

        //check if RESPONSIBILITY period is WITHIN contract period
        if (!((beginResponsibleDate.after(beginContractDate)
            || beginContractDate.equals(beginResponsibleDate))
            && (endResponsibleDate.before(endContractDate) || endContractDate.equals(endResponsibleDate))))
            throw new GrantResponsibleTeacherPeriodNotWithinContractPeriodException();

        //check if ORIENTATION period is WITHIN contract period
        if (!((beginOrientationDate.after(beginContractDate)
            || beginContractDate.equals(beginOrientationDate))
            && (endOrientationDate.before(endContractDate) || endContractDate.equals(endOrientationDate))))
            throw new GrantOrientationTeacherPeriodNotWithinContractPeriodException();
    }

    protected void doBeforeLock(
        IDomainObject domainObjectToLock,
        InfoObject infoObject,
        ISuportePersistente sp)
        throws FenixServiceException
    {
        IGrantContract grantContract = (IGrantContract) domainObjectToLock;
        InfoGrantContract infoGrantContract = (InfoGrantContract) infoObject;

        Date beginResponsibleDate = infoGrantContract.getGrantResponsibleTeacherInfo().getBeginDate();
        Date endResponsibleDate = infoGrantContract.getGrantResponsibleTeacherInfo().getEndDate();
        Date beginOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getBeginDate();
        Date endOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getEndDate();

        try
        {
            //check that endDate is after beginDate (GrantResponsibleTeacher)
            if (endResponsibleDate.before(beginResponsibleDate))
                throw new GrantResponsibleTeacherEndDateBeforeBeginDateException();
            //check that endDate is after beginDate (GrantOrientationTeacher)
            if (endOrientationDate.before(beginOrientationDate))
                throw new GrantOrientationTeacherEndDateBeforeBeginDateException();

            if (!isNew(grantContract))
                checkIfGrantTeacherPeriodConflict(grantContract, infoGrantContract, sp);

            checkIfGrantTeacherPeriodWithinContractPeriod(infoGrantContract);
        } catch (Exception e)
        {
            if (e instanceof FenixServiceException)
                throw (FenixServiceException) e;
            throw new FenixServiceException();
        }
    }

    protected void checkIfGrantTeacherRelationExists(
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

            //check if the GrantResponsible relation exists
            Integer responsibleId = infoGrantContract.getGrantResponsibleTeacherInfo().getIdInternal();
            if ((responsibleId != null) && !(responsibleId.equals(new Integer(0))))
            {
                //lock the existent object to write (EDIT)
                newGrantResponsibleTeacher =
                    (IGrantResponsibleTeacher) rt.readByOId(
                        Cloner.copyInfoGrantResponsibleTeacher2IGrantResponsibleTeacher(
                            infoGrantContract.getGrantResponsibleTeacherInfo()),
                        true);
            } else
                rt.simpleLockWrite(newGrantResponsibleTeacher);
            oldGrantResponsibleTeacher =
                Cloner.copyInfoGrantResponsibleTeacher2IGrantResponsibleTeacher(
                    infoGrantContract.getGrantResponsibleTeacherInfo());
            PropertyUtils.copyProperties(newGrantResponsibleTeacher, oldGrantResponsibleTeacher);
            newGrantResponsibleTeacher.setGrantContract((IGrantContract) newDomainObject);

            //check if the GrantOrientation relation exists
            Integer orientationId = infoGrantContract.getGrantOrientationTeacherInfo().getIdInternal();
            if ((orientationId != null) && !(orientationId.equals(new Integer(0))))
            {
                //lock the existent object to write (EDIT)
                newGrantOrientationTeacher =
                    (IGrantOrientationTeacher) rt.readByOId(
                        Cloner.copyInfoGrantOrientationTeacher2IGrantOrientationTeacher(
                            infoGrantContract.getGrantOrientationTeacherInfo()),
                        true);
            } else
                ot.simpleLockWrite(newGrantOrientationTeacher);
            oldGrantOrientationTeacher =
                Cloner.copyInfoGrantOrientationTeacher2IGrantOrientationTeacher(
                    infoGrantContract.getGrantOrientationTeacherInfo());
            PropertyUtils.copyProperties(newGrantOrientationTeacher, oldGrantOrientationTeacher);
            newGrantOrientationTeacher.setGrantContract((IGrantContract) newDomainObject);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        } catch (Exception e)
        {
            throw new FenixServiceException(e);
        }
    }

    protected void doAfterLock(
        IDomainObject newDomainObject,
        InfoObject infoObject,
        ISuportePersistente sp)
        throws FenixServiceException
    {
        checkIfGrantTeacherRelationExists(newDomainObject, infoObject, sp);
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
        InfoTeacher infoTeacher = null;
        ITeacher teacher = null;
        try
        {
            teacher = pt.readByNumber(teacherNumber);
            if (teacher == null)
                throw new GrantResponsibleTeacherNotFoundException();
            infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
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
            if (teacher == null)
                throw new GrantOrientationTeacherNotFoundException();
            infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
        } catch (ExcepcaoPersistencia persistentException)
        {
            throw new FenixServiceException(persistentException.getMessage());
        }
        return infoTeacher;
    } 
    
    /**
     * Executes the service.
     * 
     */
    public boolean run(InfoGrantContract infoGrantContract) throws FenixServiceException
    {
        Boolean result = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher pTeacher = sp.getIPersistentTeacher();
            IPersistentGrantType pGrantType = sp.getIPersistentGrantType();
            IPersistentGrantContract pGrantContract = sp.getIPersistentGrantContract();

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

            // set the contract number!
            Integer maxNumber =
                pGrantContract.readMaxGrantContractNumberByGrantOwner(
                    infoGrantContract.getGrantOwnerInfo().getIdInternal());
            int aux = maxNumber.intValue() + 1;
            Integer newContractNumber = new Integer(aux);
            infoGrantContract.setContractNumber(newContractNumber);

            result = super.run(new Integer(0), infoGrantContract);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        } catch (Exception e)
        {
            if (e instanceof FenixServiceException)
            {
                throw (FenixServiceException) e;
            }
            throw new FenixServiceException(e);
        }
        return result.booleanValue();
    }
}