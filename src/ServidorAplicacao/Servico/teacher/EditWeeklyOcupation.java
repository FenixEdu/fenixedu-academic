/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import DataBeans.InfoObject;
import DataBeans.teacher.InfoWeeklyOcupation;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.teacher.IWeeklyOcupation;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditWeeklyOcupation extends EditDomainObjectService
{

    private static EditWeeklyOcupation service = new EditWeeklyOcupation();

    public static EditWeeklyOcupation getService()
    {
        return service;
    }

    /**
     *  
     */
    private EditWeeklyOcupation()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome()
    {
        return "EditWeeklyOcupation";
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
    {
        IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
        return persistentWeeklyOcupation;
    }

    protected IDomainObject clone2DomainObject(InfoObject infoObject)
    {
        IWeeklyOcupation weeklyOcupation =
            Cloner.copyInfoWeeklyOcupation2IWeeklyOcupation((InfoWeeklyOcupation) infoObject);
        return weeklyOcupation;
    }

    protected boolean canCreate(IDomainObject domainObject, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IWeeklyOcupation oldWeeklyOcupation = (IWeeklyOcupation) domainObject;
        IPersistentWeeklyOcupation persistentWeeklyOcupation = sp.getIPersistentWeeklyOcupation();
        IWeeklyOcupation newWeeklyOcupation =
            persistentWeeklyOcupation.readByTeacher(oldWeeklyOcupation.getTeacher());

        Integer oldIdInternal = oldWeeklyOcupation.getIdInternal();
        boolean newObject = (oldIdInternal == null) || (oldIdInternal.equals(new Integer(0)));

        return (
            !newObject
                && (newWeeklyOcupation != null)
                && oldIdInternal.equals(newWeeklyOcupation.getIdInternal()))
            || (newObject && (newWeeklyOcupation == null));
    }
}
