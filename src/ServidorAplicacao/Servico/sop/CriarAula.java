/*
 * CriarAula.java
 *
 * Created on 26 de Outubro de 2002, 15:09
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o CriarAula.
 *
 * @author tfc130
 **/

import DataBeans.InfoLesson;
import DataBeans.InfoLessonServiceResult;
import Dominio.Aula;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.NotExecutedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CriarAula implements IServico {

	private static CriarAula _servico = new CriarAula();
	/**
	 * The singleton access method of this class.
	 **/
	public static CriarAula getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CriarAula() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CriarAula";
	}

	public Object run(InfoLesson infoAula) throws NotExecutedException {

		InfoLessonServiceResult result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ISala sala = sp.getISalaPersistente().readByNome( infoAula.getInfoSala().getNome() );
			IDisciplinaExecucao disciplinaExecucao = sp.getIDisciplinaExecucaoPersistente()
					.readBySiglaAndAnoLectivAndSiglaLicenciatura(
						infoAula.getInfoDisciplinaExecucao().getSigla(),
						infoAula
							.getInfoDisciplinaExecucao()
							.getInfoLicenciaturaExecucao()
							.getAnoLectivo(),
						infoAula
							.getInfoDisciplinaExecucao()
							.getInfoLicenciaturaExecucao()
							.getInfoLicenciatura()
							.getSigla());
			IAula aula = new Aula(infoAula.getDiaSemana(),
								  infoAula.getInicio(),
								  infoAula.getFim(),
								  infoAula.getTipo(),
								  sala,
								  disciplinaExecucao);

			result = valid(aula);

			if ( result.isSUCESS() )
				sp.getIAulaPersistente().lockWrite(aula);

		} catch (ExcepcaoPersistencia ex) {
			throw new NotExecutedException(ex.getMessage());
		}

		return result;
	}

	private InfoLessonServiceResult valid(IAula lesson) {
		InfoLessonServiceResult result = new InfoLessonServiceResult();

		if ( lesson.getInicio().getTime().getTime() >= lesson.getFim().getTime().getTime() ) {
			result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
		}

		return result;
	}

}