/*
 * Created on 28/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IFrequenta;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IPersistentStudentGroupAttend extends IPersistentObject{

	public void delete(IStudentGroupAttend studentGroupAttend) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public void lockWrite(IStudentGroupAttend studentGroupAttend) throws ExcepcaoPersistencia;
	public IStudentGroupAttend readBy(IStudentGroup studentGroup,IFrequenta attend) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public List readAllByStudentGroup(IStudentGroup studentGroup) throws ExcepcaoPersistencia;
}

