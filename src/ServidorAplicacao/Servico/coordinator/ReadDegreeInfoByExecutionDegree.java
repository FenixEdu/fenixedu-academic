package ServidorAplicacao.Servico.coordinator;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoDegreeInfo;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeInfo;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeInfo;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Created on 4/Nov/2003
 */
public class ReadDegreeInfoByExecutionDegree implements IServico {
	private static ReadDegreeInfoByExecutionDegree service = new ReadDegreeInfoByExecutionDegree();

	public static ReadDegreeInfoByExecutionDegree getService() {
		return service;
	}

	public ReadDegreeInfoByExecutionDegree() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public final String getNome() {
		return "ReadDegreeInfoByExecutionDegree";
	}

	public InfoDegreeInfo run(Integer infoExecutionDegreeId) throws FenixServiceException {
		System.out.println("--->A processar ReadDegreeInfoByExecutionDegree...");
		InfoDegreeInfo infoDegreeInfo = null;

		try {
			ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();

			ICursoExecucaoPersistente cursoExecucaoPersistente = suportePersistente.getICursoExecucaoPersistente();

			ICursoExecucao executionDegree = new CursoExecucao();
			executionDegree.setIdInternal(infoExecutionDegreeId);
			executionDegree = (ICursoExecucao) cursoExecucaoPersistente.readByOId(executionDegree, false);

			if (executionDegree == null || executionDegree.getCurricularPlan() == null) {
				return infoDegreeInfo;
			}

			ICurso degree = null;
			degree = executionDegree.getCurricularPlan().getDegree();

			if (degree == null) {
				return infoDegreeInfo;
			}

			IPersistentDegreeInfo persistentDegreeInfo = suportePersistente.getIPersistentDegreeInfo();
			//Read degree information
			List degreeInfoList = persistentDegreeInfo.readDegreeInfoByDegree(degree);

			//Last information about this degree
			if (degreeInfoList != null && degreeInfoList.size() > 0) {
				System.out.println("==degreeInfoList: " + degreeInfoList.size());
				Collections.sort(degreeInfoList, new BeanComparator("lastModificationDate"));
				IDegreeInfo degreeInfo = (IDegreeInfo) degreeInfoList.get(degreeInfoList.size() - 1);
				infoDegreeInfo = Cloner.copyIDegreeInfo2InfoDegree(degreeInfo);
			} else {
				System.out.println("==degreeInfoList: NULL");
			}

			//verify if the record finded is this execution period
			//IPersistentExecutionPeriod persistentExecutionPeriod = suportePersistente.getIPersistentExecutionPeriod();
			//IExecutionPeriod executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}

		System.out.println("--->Terminou ReadDegreeInfoByExecutionDegree...");
		return infoDegreeInfo;
	}
}
