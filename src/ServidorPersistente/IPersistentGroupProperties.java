/*
 * Created on 12/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;
import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IPersistentGroupProperties extends IPersistentObject{
	public void delete(IGroupProperties groupProperties) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void lockWrite(IGroupProperties groupProperties) throws ExcepcaoPersistencia;
	public IGroupProperties readBy(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia;
}
