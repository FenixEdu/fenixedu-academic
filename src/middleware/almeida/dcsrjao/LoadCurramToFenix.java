package middleware.almeida.dcsrjao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import middleware.almeida.Almeida_curram;
import Dominio.Branch;
import Dominio.IBranch;
import Dominio.IDegreeCurricularPlan;
import Util.DegreeCurricularPlanState;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class LoadCurramToFenix extends LoadDataToFenix {

	private static LoadCurramToFenix loader = null;
	private static String logString = "";
	private static HashMap error = new HashMap();
	private static String errorMessage = "";
	private static String errorDBID = "";
	private int branchID = 25; // 25 porque já existem 24 na BD

	public LoadCurramToFenix() {
	}

	public static void main(String[] args) {

		if (loader == null) {
			loader = new LoadCurramToFenix();
		}

		loader.migrationStart("LoadCurramToFenix");
		loader.setupDAO();
		List almeida_currams = loader.persistentObjectOJB.readAllAlmeidaCurrams();
		loader.shutdownDAO();
		Iterator iterator = almeida_currams.iterator();
		Almeida_curram almeida_curram = null;
		while (iterator.hasNext()) {
			almeida_curram = (Almeida_curram) iterator.next();
			loader.printIteration(loader.getClassName(), almeida_curram.getCodint());
			loader.setupDAO();
			loader.processCurram(almeida_curram);
			loader.shutdownDAO();
		}
		logString += error.toString();
		loader.migrationEnd("LoadCurramToFenix", logString);
	}

	private void processCurram(Almeida_curram almeida_curram) {

		IDegreeCurricularPlan degreeCurricularPlan = processOldDegreeCurricularPlan(almeida_curram);
		if (degreeCurricularPlan == null) {
			numberUntreatableElements++;
			return;
		} else {
			String code = "";
			String name = "";
			if (almeida_curram.getCodram() != 0) {
				code = getBranchCode(almeida_curram);
				name = almeida_curram.getDescri();
			}
			if (persistentObjectOJB.readBranchByUnique(code, degreeCurricularPlan) == null) {
				IBranch branch = new Branch();
				branch.setCode(code);
				branch.setDegreeCurricularPlan(degreeCurricularPlan);
				branch.setName(name);
				branch.setScopes(null);
				branch.setIdInternal(new Integer(this.branchID));
				this.branchID++;
				writeElement(branch);
			} else {
				loader.numberUntreatableElements++;
				errorMessage = "\n O ramo com o code " + code + " no plano curricular " + degreeCurricularPlan.getName() + "já existe! Registos: ";
				errorDBID = almeida_curram.getCodint() + ",";
				error = loader.setErrorMessage(errorMessage, errorDBID, error);
			}
		}
	}

	private IDegreeCurricularPlan processOldDegreeCurricularPlan(Almeida_curram almeida_curram) {
		Integer keyDegree;
		try {
			keyDegree = new Integer("" + almeida_curram.getCodcur());
		} catch (NumberFormatException e) {
			errorMessage = "\n O curso " + almeida_curram.getCodcur() + "é inválido! Registos: ";
			errorDBID = almeida_curram.getCodint() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		//		ICurso degree = persistentObjectOJB.readDegreeByID(keyDegree);
		//		if (degree == null) {
		//			errorMessage = "\n O curso " + almeida_curram.getCodcur() + " não existe! Registos: ";
		//			errorDBID = almeida_curram.getCodint() + ", ";
		//			error = loader.setErrorMessage(errorMessage, errorDBID, error);
		//			return null;
		//		}

		//		String degreeName = degree.getNome() + " OLD";
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(keyDegree, DegreeCurricularPlanState.CONCLUDED_OBJ);
		if (degreeCurricularPlan == null) {
			errorMessage = "\n O degree Curricular Plan do curso " + almeida_curram.getCodcur() + " não existe! Registos: ";
			errorDBID = almeida_curram.getCodint() + ", ";
			error = loader.setErrorMessage(errorMessage, errorDBID, error);
			return null;
		}

		return degreeCurricularPlan;
	}

	private String getBranchCode(Almeida_curram almeida_curram) {
		return "" + almeida_curram.getCodcur() + almeida_curram.getCodram() + almeida_curram.getCodorien();
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadCurramToFenix.txt";
	}

	protected String getClassName() {
		return "LoadCurramToFenix";
	}
}