package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerTurmas
 *
 * @author tfc130
 * @version
 **/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoClass;
import DataBeans.InfoDegree;
import Dominio.ITurma;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurmas implements IServico {

	private static LerTurmas _servico = new LerTurmas();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerTurmas getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerTurmas() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerTurmas";
	}

	public Object run(CurricularYearAndSemesterAndInfoExecutionDegree aCSiLE) {

		List turmas = null;
		List infoTurmas = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			turmas =
				sp.getITurmaPersistente().readBySemestreAndAnoCurricularAndSiglaLicenciatura(
					aCSiLE.getSemestre(),
					aCSiLE.getAnoCurricular(),
					aCSiLE
						.getInfoLicenciaturaExecucao()
						.getInfoLicenciatura()
						.getSigla());

			Iterator iterator = turmas.iterator();
			infoTurmas = new ArrayList();
			while (iterator.hasNext()) {
				ITurma elem = (ITurma) iterator.next();
				InfoDegree infoLicenciatura =
					new InfoDegree(
						elem.getLicenciatura().getSigla(),
						elem.getLicenciatura().getNome());
				infoTurmas.add(
					new InfoClass(
						elem.getNome(),
						elem.getSemestre(),
						elem.getAnoCurricular(),
						infoLicenciatura));
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return infoTurmas;
	}

}