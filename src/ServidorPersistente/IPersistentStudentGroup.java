/*
 * Created on 12/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;
import java.util.List;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IPersistentStudentGroup extends IPersistentObject{
	public void delete(IStudentGroup studentGroup) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void lockWrite(IStudentGroup studentGroup) throws ExcepcaoPersistencia;
	public IStudentGroup readStudentGroupByGroupPropertiesAndGroupNumber(IGroupProperties groupProperties,Integer studentGroupNumber) throws ExcepcaoPersistencia;
	public List readAllStudentGroupByGroupProperties(IGroupProperties groupProperties) throws ExcepcaoPersistencia;
	public List readAllStudentGroupByGroupPropertiesAndShift(IGroupProperties groupProperties,ITurno shift) throws ExcepcaoPersistencia;
}
