/*
 * Created on 12/Mai/2003
 *
 */
package ServidorPersistente;
import java.util.List;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.ITurno;

/**
 * @author asnr and scpo
 *
 */
public interface IPersistentStudentGroup extends IPersistentObject{
	public void delete(IStudentGroup studentGroup) throws ExcepcaoPersistencia;
	
	public List readAll() throws ExcepcaoPersistencia;
	
	public IStudentGroup readStudentGroupByGroupPropertiesAndGroupNumber(IGroupProperties groupProperties,Integer studentGroupNumber) throws ExcepcaoPersistencia;
	public List readAllStudentGroupByGroupProperties(IGroupProperties groupProperties) throws ExcepcaoPersistencia;
	public List readAllStudentGroupByGroupPropertiesAndShift(IGroupProperties groupProperties,ITurno shift) throws ExcepcaoPersistencia;
}
