/*
 * Created on 28/Mai/2003
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IFrequenta;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;

/**
 * @author asnr and scpo
 *  
 */
public interface IPersistentStudentGroupAttend extends IPersistentObject {
    public List readByStudentGroupId(Integer ID) throws ExcepcaoPersistencia;

    public void delete(IStudentGroupAttend studentGroupAttend) throws ExcepcaoPersistencia;

    public IStudentGroupAttend readBy(IStudentGroup studentGroup, IFrequenta attend)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    public List readAllByStudentGroup(IStudentGroup studentGroup) throws ExcepcaoPersistencia;

    public IStudentGroupAttend readBy(IFrequenta attend) throws ExcepcaoPersistencia;
}