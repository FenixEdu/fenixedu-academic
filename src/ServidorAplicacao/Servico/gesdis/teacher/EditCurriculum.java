/*
 * Created on 18/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoCurriculum;
import DataBeans.util.Cloner;
import Dominio.ICurriculum;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */
public class EditCurriculum implements IServico {

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "EditCurriculum";
	}

	private static EditCurriculum service = new EditCurriculum();
	public static EditCurriculum getService() {
		return service;
	}
	public Boolean run(InfoCurriculum oldInfoCurriculum, InfoCurriculum newInfoCurriculum)
		throws FenixServiceException {
		try {
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
            ICurriculum oldCurriculum = Cloner.copyInfoCurriculum2ICurriculum(oldInfoCurriculum);
			ICurriculum newCurriculum = Cloner.copyInfoCurriculum2ICurriculum(newInfoCurriculum);
            
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            ICurriculum curriculum=persistentCurriculum.readCurriculumByExecutionCourse(oldCurriculum.getExecutionCourse());
			if (curriculum!=null){
				curriculum.setGeneralObjectives(newCurriculum.getGeneralObjectives());
							curriculum.setOperacionalObjectives(newCurriculum.getOperacionalObjectives());
							curriculum.setProgram(newCurriculum.getProgram());
							persistentCurriculum.lockWrite(curriculum);
			}
			else throw new InvalidArgumentsServiceException();	
            	
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		} 
		return new Boolean(true);
	}
}
