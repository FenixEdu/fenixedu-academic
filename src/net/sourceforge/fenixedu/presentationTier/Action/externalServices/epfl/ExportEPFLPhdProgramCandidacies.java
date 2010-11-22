package net.sourceforge.fenixedu.presentationTier.Action.externalServices.epfl;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import pt.utl.ist.fenix.tools.predicates.Predicate;

public class ExportEPFLPhdProgramCandidacies {

    public static byte[] run() throws Exception {
	final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	PrintWriter writer = new PrintWriter(outputStream);

	try {
	    writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
	    writer.println("<data>");
	    List<PhdIndividualProgramProcess> list = PhdIndividualProgramProcess.search(ExecutionYear.readCurrentExecutionYear(),
		    new Predicate<PhdIndividualProgramProcess>() {

			@Override
			public boolean eval(PhdIndividualProgramProcess t) {
			    if (!PhdIndividualProgramCollaborationType.EPFL.equals(t.getCollaborationType())) {
				return false;
			    }

			    if (!PhdIndividualProgramProcessState.CANDIDACY.equals(t.getActiveState())) {
				return false;
			    }

			    return PhdProgramCandidacyProcessState.PRE_CANDIDATE.equals(t.getCandidacyProcess().getActiveState());
			}

		    });
	    for (PhdIndividualProgramProcess process : list) {
		if (!"202/2010".equals(process.getProcessNumber())) {
		    continue;
		}

		writePersonInfo(process, writer);
		break;
	    }

	    writer.println("</data>");
	} finally {
	    writer.close();
	}

	return outputStream.toByteArray();
    }

    /**
     * Write each person's personal and process information
     * 
     * @param process
     * @param writer
     */
    private static void writePersonInfo(PhdIndividualProgramProcess process, PrintWriter writer) {
	writer.println(addTabs(1) + "<personne action=\"AUTO\">");
	Person person = process.getPerson();
	String[] names = person.getName().split(",");

	writer.println(addTabs(2) + String.format("<nom>%s</nom>", names[0].trim()));
	writer.println(addTabs(2) + String.format("<prenom>%s</prenom>", names[1].trim()));
	writer.println(addTabs(2)
		+ String.format("<sexe>%s</sexe>", (Gender.MALE.compareTo(person.getGender()) == 0 ? "SEXH" : "SEXF")));
	writer.println(addTabs(2)
		+ String.format("<naissance>%s</naissance>", person.getDateOfBirthYearMonthDay().toString("dd.MM.yyyy")));

	writer.println(addTabs(2) + "<detailPersonne action=\"AUTO\">");

	writer.println(addTabs(3) + "<domaine>DOMAINEACADEMIQUE</domaine>");
	writer.println(addTabs(3)
		+ String.format("<datePersonne action=\"AUTO\" type=\"TYPE_DATE_ENTREE\">%s</datePersonne>", process
			.getCandidacyProcess().getCandidacyDate().toString("dd.MM.yyyy")));

	writer.println(addTabs(3)
		+ String.format("<lieuPersonne action=\"AUTO\" " + "type=\"LIEUNAIETRA\" identificationLieu=\"ofs\" "
			+ "ofs=\"\" typeLieu=\"LOCETRNONCON\">%s</lieuPersonne>", person.getDistrictSubdivisionOfBirth()));

	writer.println(addTabs(3)
		+ String.format("<lieuPersonne action=\"AUTO\" " + "type=\"LIEUORI\" identificationLieu=\"ofs\" "
			+ "ofs=\"\" typeLieu=\"COMMUNE;PAYS\">%s</lieuPersonne>", person.getCountry().getCode()));

	writer.println(addTabs(3) + "<adresse type=\"ADR_ECH\" action=\"AUTO\">");

	writer.println(addTabs(4) + String.format("<ligne n=\"1\">%s</ligne>", person.getAddress()));
	writer.println(addTabs(4)
		+ String.format("<localite typeLieu=\"LOCALITE;LOCETRNONCON\">%s  %s</localite>", person.getAreaCode(), person
			.getArea()));
	writer.println(addTabs(4)
		+ String.format("<pays>%s</pays>", (person.getCountryOfResidence() != null ? person.getCountryOfResidence()
			.getCode() : "")));
	writer.println(addTabs(4) + String.format("<moyen action=\"AUTO\" type=\"EMAIL\">%s</moyen>", person.getEmail()));
	writer.println(addTabs(4) + String.format("<moyen action=\"AUTO\" type=\"PORTABLE\">%s</moyen>", person.getMobile()));

	writer.println(addTabs(3) + "</adresse>");

	writer.println(addTabs(2) + "</detailPersonne>");
	writer.println(addTabs(2) + "<inscription action=\"AUTO\">");

	writer.println(addTabs(3) + "<gps domaine=\"DOMAINEACADEMIQUE\">");

	writer.println(addTabs(4) + "<modelegps>CDOC</modelegps>");
	writer.println(addTabs(4) + "<unité type=\"ACAD\" format=\"LIBELLE\">IST-EPFL</unité>");
	writer.println(addTabs(4) + "<periode type=\"PEDAGO\" format=\"LIBCOU\">Eval sep</periode>");
	writer.println(addTabs(4) + "<periode type=\"ACAD\">2010</periode>");

	writer.println(addTabs(3) + "</gps>");

	writer.println(addTabs(3) + String.format(" <detail type=\"URL_IST-EPFL\"></detail>"));

	writer.println(addTabs(3)
		+ String.format("<detail type=\"PRG_DOCT_EPFL\" format=\"COURTU\">%s</detail>", process.getExternalPhdProgram()
			.getAcronym()));

	writer.println(addTabs(3)
		+ String.format(" <detail type=\"GPSDOMFOCUS\">%s</detail> ", process.getPhdProgramFocusArea().getName()));

	writer.println(addTabs(2) + "</inscription>");

	writer.println(addTabs(1) + "</personne>");
    }

    private static String addTabs(int level) {
	String returnString = "";
	for (int i = 1; i <= level; i++) {
	    returnString += '\t';
	}
	return returnString;
    }

}
