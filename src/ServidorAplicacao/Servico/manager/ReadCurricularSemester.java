/*
 * Created on 24/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoCurricularSemester;
import DataBeans.util.Cloner;
import Dominio.CurricularSemester;
import Dominio.ICurricularSemester;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadCurricularSemester implements IServico {

  private static ReadCurricularSemester service = new ReadCurricularSemester();

  /**
   * The singleton access method of this class.
   */
  public static ReadCurricularSemester getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadCurricularSemester() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadCurricularSemester";
  }

  /**
   * Executes the service. Returns the current InfoCurricularSemester.
   */
  public InfoCurricularSemester run(Integer idInternal) throws FenixServiceException {
	ICurricularSemester curricularSemester;
	ICurricularSemester newCurricularSemester;
	try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			newCurricularSemester=new CurricularSemester();
			newCurricularSemester.setIdInternal(idInternal);
		curricularSemester = (ICurricularSemester) sp.getIPersistentCurricularSemester().readByOId(newCurricularSemester, false);
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}
     
	if (curricularSemester == null) {
		return null;
	}

	InfoCurricularSemester infoCurricularSemester = Cloner.copyCurricularSemester2InfoCurricularSemester(curricularSemester); 
	return infoCurricularSemester;
  }
}
