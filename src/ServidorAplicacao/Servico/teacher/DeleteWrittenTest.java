/*
 * Created on 18/Fev/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.WrittenTest;
import ServidorAplicacao.Servico.framework.DeleteDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt
 * @author Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public class DeleteWrittenTest extends DeleteDomainObjectService
{
    public DeleteWrittenTest()
    {

    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getDomainObjectClass()
     */
    protected Class getDomainObjectClass()
    {
        return WrittenTest.class;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servico.framework.DeleteDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
     */
    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia
    {
        return sp.getIPersistentWrittenTest();
    }
}
