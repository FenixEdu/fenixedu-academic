package middleware.almeida;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import Dominio.Branch;
import Dominio.IBranch;
import Dominio.IDegreeCurricularPlan;
import Util.DegreeCurricularPlanState;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */

public class LoadCurram extends LoadDataFile {

	private static LoadCurram loader = null;
	private static String logString = "";

	public LoadCurram() {
	}

	public static void main(String[] args) {
		loader = new LoadCurram();
		loader.load();
	}

	public void run() {
		if (loader == null) {
			loader = new LoadCurram();
		}
//		loader.load();
		loader.copyAlmeidaCurramToFenixBranch();
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer = new StringTokenizer(line, getFieldSeparator());

		int numberTokens = stringTokenizer.countTokens();

		String curso = stringTokenizer.nextToken();
		String ramo = null;
		String orientacao = null;
		if (numberTokens > 2) {
			ramo = stringTokenizer.nextToken();
		}
		if (numberTokens > 3) {
			orientacao = stringTokenizer.nextToken();
		}
		String descricao = stringTokenizer.nextToken();

		Almeida_curram almeida_curram = new Almeida_curram();
		almeida_curram.setCodint(loader.numberElementsWritten + 1);
		almeida_curram.setCodcur((new Integer(curso)).longValue());
		if (ramo != null) {
			almeida_curram.setCodram((new Integer(ramo)).longValue());
		}
		if (orientacao != null) {
			almeida_curram.setCodorien((new Integer(orientacao)).longValue());
		}
		almeida_curram.setDescri(descricao);

		writeElement(almeida_curram);

	}

	private void copyAlmeidaCurramToFenixBranch() {
		loader.setupDAO();
		System.out.println("Migrating Almeida_curram to Fenix Branch.\n");
		super.startTime = Calendar.getInstance();

		List almeidaCurramsList = persistentObjectOJB.readAllAlmeidaCurrams();
		Iterator iterator = almeidaCurramsList.iterator();
		while(iterator.hasNext()) {
			Almeida_curram almeida_curram = (Almeida_curram) iterator.next();
			IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(new Integer("" + almeida_curram.getCodcur()), DegreeCurricularPlanState.CONCLUDED_OBJ);

//			if(almeida_curram.getCodorien() == 0) {
			IBranch branch = new Branch();
			if(almeida_curram.getCodram() == 0) {
				branch.setCode("");
				branch.setDegreeCurricularPlan(degreeCurricularPlan);
				branch.setName("");
			} else {
//				branch.setCode(getBranchCode(almeida_curram.getDescri()));
				branch.setCode(getBranchCode(almeida_curram));
				branch.setDegreeCurricularPlan(degreeCurricularPlan);
				branch.setName(almeida_curram.getDescri());
			}
			branch.setScopes(null);
			writeElement(branch);
//			} else {
//				LoadCurram.logString += "ERRO: Existem varios perfis dentro do mesmo ramo [" + almeida_curram.getDescri() + "]\n";
//			}
		}
		loader.shutdownDAO();
		super.endTime = Calendar.getInstance();
		System.out.println("Done.");
		super.report();
		loader.writeToFile(logString);
	}

	private String getBranchCode(Almeida_curram almeida_curram) {
		return "" + almeida_curram.getCodcur() + almeida_curram.getCodram() + almeida_curram.getCodorien();
	}

	protected String getFilename() {
		return "etc/migration/dcs-rjao/almeidaCommonData/CURRAM.TXT";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/branchMigrationLog.txt";
	}
}