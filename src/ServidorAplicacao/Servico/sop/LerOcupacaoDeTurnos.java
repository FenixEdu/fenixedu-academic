/*
 * LerAlunosDeTurno.java
 *
 * Created on 27 de Outubro de 2002, 21:41
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço LerAlunosDeTurno.
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerOcupacaoDeTurnos implements IServico {

	private static LerOcupacaoDeTurnos _servico = new LerOcupacaoDeTurnos();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerOcupacaoDeTurnos getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerOcupacaoDeTurnos() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerOcupacaoDeTurnos";
	}

	public Object run(List infoShiftList) {

		List alunos = new ArrayList();
		
 		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			InfoShift infoShift = new InfoShift();
			Iterator infoShiftsIterator = infoShiftList.iterator();

			while (infoShiftsIterator.hasNext()) {
				infoShift = (InfoShift) infoShiftsIterator.next();

				ITurno shift = new Turno();
				shift = Cloner.copyInfoShift2IShift(infoShift);

				alunos = sp.getITurnoAlunoPersistente().readByShift(shift);
				
				Integer ocupation = new Integer(alunos.size());
				Double percentage = new Double(alunos.size()*100/shift.getLotacao().intValue());
			    
			    infoShift.setOcupation(ocupation);  
			    infoShift.setPercentage(percentage);
	
			}
			
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return infoShiftList;
	}

}