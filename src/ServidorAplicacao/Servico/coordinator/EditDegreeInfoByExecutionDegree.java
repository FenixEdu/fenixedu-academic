package ServidorAplicacao.Servico.coordinator;

import java.sql.Timestamp;
import java.util.Calendar;

import DataBeans.InfoDegreeInfo;
import Dominio.CursoExecucao;
import Dominio.DegreeInfo;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeInfo;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeInfo;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Created on 5/Nov/2003
 */
public class EditDegreeInfoByExecutionDegree implements IServico {
	private static EditDegreeInfoByExecutionDegree service = new EditDegreeInfoByExecutionDegree();

	public static EditDegreeInfoByExecutionDegree getService() {
		return service;
	}

	public EditDegreeInfoByExecutionDegree() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public final String getNome() {
		return "EditDegreeInfoByExecutionDegree";
	}

	public boolean run(Integer infoExecutionDegreeId, Integer infoDegreeInfoId, InfoDegreeInfo infoDegreeInfo)
		throws FenixServiceException {
		System.out.println("--->A processar EditDegreeInfoByExecutionDegree...");
		try {
			ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();

			//Execution Course
			ICursoExecucaoPersistente cursoExecucaoPersistente = suportePersistente.getICursoExecucaoPersistente();
			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(infoExecutionDegreeId);
			executionDegree = (ICursoExecucao) cursoExecucaoPersistente.readByOId(executionDegree, false);

			//Degree
			ICurso degree = null;
			if (executionDegree != null && executionDegree.getCurricularPlan() != null) {
				degree = executionDegree.getCurricularPlan().getDegree();
			}

			//DegreeInfo
			IPersistentDegreeInfo persistentDegreeInfo = suportePersistente.getIPersistentDegreeInfo();
			IDegreeInfo degreeInfo = new DegreeInfo();
			
			//edit the information
			degreeInfo.setIdInternal(infoDegreeInfoId);
			
			//associate the degree
			degreeInfo.setDegree(degree);
						
			//update information that it will be displayed in degree site.
			degreeInfo.setObjectives(infoDegreeInfo.getObjectives());
			degreeInfo.setHistory(infoDegreeInfo.getHistory());
			degreeInfo.setProfessionalExits(infoDegreeInfo.getProfessionalExits());
			degreeInfo.setAdditionalInfo(infoDegreeInfo.getAdditionalInfo());
			degreeInfo.setLinks(infoDegreeInfo.getLinks());
			degreeInfo.setTestIngression(infoDegreeInfo.getTestIngression());
			degreeInfo.setDriftsInitial(infoDegreeInfo.getDriftsInitial());
			degreeInfo.setDriftsFirst(infoDegreeInfo.getDriftsFirst());
			degreeInfo.setDriftsSecond(infoDegreeInfo.getDriftsSecond());
			degreeInfo.setClassifications(infoDegreeInfo.getClassifications());
			degreeInfo.setMarkMin(infoDegreeInfo.getMarkMin());
			degreeInfo.setMarkMax(infoDegreeInfo.getMarkMax());
			degreeInfo.setMarkAverage(infoDegreeInfo.getMarkAverage());
			
			//update last modification date
			degreeInfo.setLastModificationDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
			persistentDegreeInfo.lockWrite(degreeInfo);
			
			System.out.println("--->Escreveu: " + degreeInfo);			
			System.out.println("--->Terminou EditDegreeInfoByExecutionDegree...");			
		} catch (ExcepcaoPersistencia e) {
			System.out.println("--->EXCEPCAO: Ocorreu EditDegreeInfoByExecutionDegree...");
			e.printStackTrace();
			throw new FenixServiceException(e);
		}		
		return true;
	}
}
