/*
 * Created on 30/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDegreeCurricularPlan;
import Dominio.IDegreeEnrolmentInfo;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IPersistentDegreeEnrolmentInfo {
	public abstract void deleteAll() throws ExcepcaoPersistencia;
	public abstract void delete(IDegreeEnrolmentInfo degreeEnrolmentInfo)
		throws ExcepcaoPersistencia;
	public abstract void lockWrite(IDegreeEnrolmentInfo degreeEnrolmentInfoToWrite)
		throws ExcepcaoPersistencia, ExistingPersistentException;
	public abstract List readAll() throws ExcepcaoPersistencia;
	public abstract IDegreeEnrolmentInfo readDegreeEnrolmentInfoByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
		throws ExcepcaoPersistencia;
}