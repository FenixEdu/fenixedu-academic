package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DegreeInfo;
import Dominio.ICurso;
import Dominio.IDegreeInfo;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeInfo;

/**
 * @author Tânia Pousão Created on 30/Out/2003
 */
public class DegreeInfoOJB extends ObjectFenixOJB implements IPersistentDegreeInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentDegreeInfo#readDegreeInfoByDegree(Dominio.ICurso)
	 */
	public List readDegreeInfoByDegree(ICurso degree) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("degreeKey", degree.getIdInternal());

		return (List) queryList(DegreeInfo.class, criteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentDegreeInfo#lockWrite(Dominio.ICurso)
	 */
	public void lockWrite(Object obj) throws ExcepcaoPersistencia {
		//if there is nothing to write, simply return.
		if (obj == null) {
			System.out.println("if there is nothing to write, simply return.");
			return;
		}
		
		if (obj instanceof IDegreeInfo) {
			IDegreeInfo degreeInfoToWrite = (IDegreeInfo) obj;

			if (degreeInfoToWrite.getIdInternal() == null || degreeInfoToWrite.getIdInternal().equals(new Integer(0))) {
				//if degreeInfo hasn't a id internal then insert it in database.
				System.out.println("if degreeInfo hasn't a id internal then insert it in database.");
				super.lockWrite(degreeInfoToWrite);
			} else {
				//degreeInfo has a id internal.
				IDegreeInfo degreeInfoFromBD = new DegreeInfo();
				degreeInfoFromBD.setIdInternal(degreeInfoToWrite.getIdInternal());
				
				degreeInfoFromBD = (IDegreeInfo) readByOId(degreeInfoFromBD, true);
				if (degreeInfoFromBD == null) {
					System.out.println("if degreeInfo isn't in database, then write it.");
					//if degreeInfo isn't in database, then write it.
					//super.lockWrite(degreeInfoToWrite);
					simpleLockWrite(degreeInfoToWrite);
				} else if (degreeInfoFromBD.getIdInternal().equals(degreeInfoToWrite.getIdInternal())) {
					//else if the degreeInfo is mapped to the database, then write any existing change.
					System.out.println("else if the degreeInfo is mapped to the database, then write any existing change.");					
					//super.lockWrite(degreeInfoToWrite);
					simpleLockWrite(degreeInfoToWrite);
				} else { 
					//else throw an already existing exception.
					System.out.println("else throw an already existing exception.");					
					throw new ExcepcaoPersistencia();
				}
			}
		}
	}
}
