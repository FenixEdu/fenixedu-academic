package middleware.almeida.dcsrjao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.almeida.Almeida_escola;
import Dominio.IUniversity;
import Dominio.University;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class LoadEscolasToFenix extends LoadDataToFenix {

	private static LoadEscolasToFenix loader = null;
	private static String logString = "";
	private static HashMap error = new HashMap();
	private static String errorMessage = "";
	private static String errorDBID = "";

	public LoadEscolasToFenix() {
	}

	public static void main(String[] args) {

		if (loader == null) {
			loader = new LoadEscolasToFenix();
		}

		loader.migrationStart("LoadEscolasToFenix");
		loader.setupDAO();
		List almeida_currams = loader.persistentObjectOJB.readAllAlmeidaEscolas();
		loader.shutdownDAO();
		Iterator iterator = almeida_currams.iterator();
		Almeida_escola almeida_escola = null;
		while (iterator.hasNext()) {
			almeida_escola = (Almeida_escola) iterator.next();
			loader.printIteration(loader.getClassName(),almeida_escola.getId_internal());
			loader.setupDAO();
			loader.processEscola(almeida_escola);
			loader.shutdownDAO();
		}
		logString += error.toString();
		loader.migrationEnd("LoadEscolasToFenix", logString);
	}

	private void processEscola(Almeida_escola almeida_escola) {
		IUniversity university = persistentObjectOJB.readUniversityByUnique(almeida_escola.getCode(), almeida_escola.getName());
		if (university == null) {
			university = new University();
			university.setIdInternal(new Integer(numberElementsWritten + 1));
			university.setCode(almeida_escola.getCode());
			university.setName(almeida_escola.getName());
			writeElement(university);
		} else {
			errorMessage = "\n A universidade com o codigo = " + almeida_escola.getCode() + "] e nome = " + almeida_escola.getName() + " já existe! Registos: ";
			errorDBID = almeida_escola.getId_internal() + ",";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		}

	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadEscolasToFenix.txt";
	}
	
	protected String getClassName() {
		return "LoadEscolasToFenix";
	}
}