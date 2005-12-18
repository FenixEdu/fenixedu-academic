package net.sourceforge.fenixedu.applicationTier.Servico;

/**
 * ObterSeccao service. It is used to get all information about a seccao. A
 * seccao is represented by its name, the sitio it is associated with and its
 * parent seccao.
 * 
 * @author Ricardo Nortadas
 * @version
 */

import java.util.Calendar;

import net.sourceforge.fenixedu.util.Mes;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ObterData implements IService {

	/**
	 * Executes the service. Returns the all information about the desired Data.
	 * 
	 * @param sitioName
	 *            is the name of the sitio.
	 * @param name
	 *            is the name of the desired Data.
	 * 
	 * @throws ExcepcaoInexistente
	 *             if there is none Data with the desired name, parent and
	 *             sitio.
	 */
	public DataView run() {

		Calendar calendar = Calendar.getInstance();
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int ano = calendar.get(Calendar.YEAR);
		int intMes = calendar.get(Calendar.MONTH);

		Mes objMes = new Mes(intMes + 1);

		String mes = objMes.toString();

		/*
		 * Object argumentos[] = {};
		 * 
		 * DataView data = (DataView) gestor.executar(null, "ObterData",
		 * argumentos); request.setAttribute("Data", data);
		 */
		return new DataView(dia, mes, ano);
	}

}