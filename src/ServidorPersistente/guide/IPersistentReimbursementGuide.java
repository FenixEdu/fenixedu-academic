/*
 * Created on 17/Nov/2003
 *
 */
package ServidorPersistente.guide;

import java.util.List;

import Dominio.IGuide;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a>
 *17/Nov/2003
 *
 */
public interface IPersistentReimbursementGuide extends IPersistentObject
{
    public List readByGuide(IGuide guide) throws ExcepcaoPersistencia;
    public Integer generateReimbursementGuideNumber() throws ExcepcaoPersistencia;
}