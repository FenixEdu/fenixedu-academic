package middleware.almeida.dcsrjao;

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

	public LoadCurramToFenix() {
	}

	public static void main(String[] args) {

		if (loader == null) {
			loader = new LoadCurramToFenix();
		}

		loader.migrationStart("LoadCurramToFenix");
				
		List almeida_currams = loader.persistentObjectOJB.readAllAlmeidaCurrams();
		Iterator iterator = almeida_currams.iterator();
		while (iterator.hasNext()) {
			Almeida_curram almeida_curram = (Almeida_curram) iterator.next();
			loader.processCurram(almeida_curram);
		}

		loader.migrationEnd("LoadCurramToFenix", logString);
	}

	private void processCurram(Almeida_curram almeida_curram) {
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(new Integer("" + almeida_curram.getCodcur()), DegreeCurricularPlanState.CONCLUDED_OBJ);

		IBranch branch = new Branch();
		if (almeida_curram.getCodram() == 0) {
			branch.setCode("");
			branch.setDegreeCurricularPlan(degreeCurricularPlan);
			branch.setName("");
		} else {
			branch.setCode(getBranchCode(almeida_curram));
			branch.setDegreeCurricularPlan(degreeCurricularPlan);
			branch.setName(almeida_curram.getDescri());
		}
		branch.setScopes(null);
		writeElement(branch);
	}

	private String getBranchCode(Almeida_curram almeida_curram) {
		return "" + almeida_curram.getCodcur() + almeida_curram.getCodram() + almeida_curram.getCodorien();
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadCurramToFenix.txt";
	}
}