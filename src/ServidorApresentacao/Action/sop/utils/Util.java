package ServidorApresentacao.Action.sop.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import Util.DiaSemana;
import Util.TipoSala;

/**
 * Esta classe contem metodos que sao utilizados em varias classes Action.
 *
 * @author joao 
 **/

public class Util {

	/**
	 * Reads the existing buldings of the university and return a list
	 * with them.  This information should be placed inside of the
	 * database, instead of being placed here. The first element of
	 * the list is the selected element when the list is shown. If this
	 * element is null, then it is not added to the list.
	 **/
	public static List readExistingBuldings(String name, String value) {
		ArrayList edificios = new ArrayList();

		if (name != null)
			edificios.add(new LabelValueBean(name, value));

		edificios.add(
			new LabelValueBean("Pavilhão Central", "Pavilhão Central"));
		edificios.add(new LabelValueBean("Pavilhão Civil", "Pavilhão Civil"));
		edificios.add(
			new LabelValueBean("Pavilhão Mecânica II", "Pavilhão Mecânica II"));
		edificios.add(new LabelValueBean("Pavilhão Minas", "Pavilhão Minas"));
		edificios.add(
			new LabelValueBean(
				"Pavilhão Novas Licenciaturas",
				"Pavilhão Novas Licenciaturas"));
		edificios.add(new LabelValueBean("Pavilhão de Pós-Graduação", "Pavilhão de Pós-Graduação"));
		edificios.add(new LabelValueBean("Torre Norte", "Torre Norte"));
		edificios.add(new LabelValueBean("Torre Sul", "Torre Sul"));
		edificios.add(
			new LabelValueBean(
				"TagusPark - Bloco A - Poente",
				"TagusPark - Bloco A - Poente"));
		edificios.add(
			new LabelValueBean(
				"TagusPark - Bloco A - Nascente",
				"TagusPark - Bloco A - Nascente"));
		edificios.add(
			new LabelValueBean(
				"TagusPark - Bloco B - Poente",
				"TagusPark - Bloco B - Poente"));
		edificios.add(
			new LabelValueBean(
				"TagusPark - Bloco B - Nascente",
				"TagusPark - Bloco B - Nascente"));

		return edificios;
	}

	/**
	 * Reads the existing kinds of salas of the university and return a list
	 * with them.  This information should be placed inside of the
	 * database, instead of being placed here. The first element of
	 * the list is the selected element when the list is shown. If this
	 * element is null, then it is not added to the list.
	 **/
	public static List readTypesOfRooms(String name, String value) {
		ArrayList tipos = new ArrayList();

		if (name != null)
			tipos.add(new LabelValueBean(name, value));

		tipos.add(
			new LabelValueBean(
				"Anfiteatro",
				(new Integer(TipoSala.ANFITEATRO)).toString()));
		tipos.add(
			new LabelValueBean(
				"Laboratório",
				(new Integer(TipoSala.LABORATORIO)).toString()));
		tipos.add(
			new LabelValueBean(
				"Plana",
				(new Integer(TipoSala.PLANA)).toString()));

		return tipos;
	}

	/**
	 * Method getDaysOfWeek.
	 * @return ArrayList
	 */
	public static ArrayList getDaysOfWeek() {
		ArrayList weekDays = new ArrayList();
		weekDays.add(
			new LabelValueBean(
				"segunda",
				(new Integer(DiaSemana.SEGUNDA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"terça",
				(new Integer(DiaSemana.TERCA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"quarta",
				(new Integer(DiaSemana.QUARTA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"quinta",
				(new Integer(DiaSemana.QUINTA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"sexta",
				(new Integer(DiaSemana.SEXTA_FEIRA)).toString()));
		weekDays.add(
			new LabelValueBean(
				"sábado",
				(new Integer(DiaSemana.SABADO)).toString()));

		return weekDays;
	}

	/**
	 * Method getHoras.
	 * @return ArrayList
	 */
	public static ArrayList getHours() {
		ArrayList hoursList = new ArrayList();

		hoursList.add("8");
		hoursList.add("9");
		hoursList.add("10");
		hoursList.add("11");
		hoursList.add("12");
		hoursList.add("13");
		hoursList.add("14");
		hoursList.add("15");
		hoursList.add("16");
		hoursList.add("17");
		hoursList.add("18");
		hoursList.add("19");
		hoursList.add("20");
		hoursList.add("21");
		hoursList.add("22");
		hoursList.add("23");
		return hoursList;
	}

	/**
	 * Method getMinutos.
	 * @return ArrayList
	 */
	public static ArrayList getMinutes() {
		ArrayList minutesList = new ArrayList();
		minutesList.add("00");
		minutesList.add("30");
		return minutesList;
	}
}
