package net.sourceforge.fenixedu.domain.student.importation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import pt.utl.ist.fenix.tools.loaders.DataLoaderFromFile;

/**
 * 
 * @author naat
 * 
 */
public abstract class DgesBaseProcess extends DgesBaseProcess_Base {

    protected static final String ALAMEDA_UNIVERSITY = "A";
    protected static final String TAGUS_UNIVERSITY = "T";
    static Map<String, Ingression> CONTINGENT_TO_INGRESSION_CONVERSION = new HashMap<String, Ingression>();

    static {
	// Contingente Geral
	CONTINGENT_TO_INGRESSION_CONVERSION.put("1", Ingression.CNA01);
	// Contingente Açores
	CONTINGENT_TO_INGRESSION_CONVERSION.put("2", Ingression.CNA02);
	// Contingente Madeira
	CONTINGENT_TO_INGRESSION_CONVERSION.put("3", Ingression.CNA03);
	// Contingente Emigrantes
	CONTINGENT_TO_INGRESSION_CONVERSION.put("5", Ingression.CNA05);
	// Contingente Militar
	CONTINGENT_TO_INGRESSION_CONVERSION.put("6", Ingression.CNA06);
	// Contingente Deficientes
	CONTINGENT_TO_INGRESSION_CONVERSION.put("D", Ingression.CNA07);
    }

    protected DgesBaseProcess() {
	super();
    }

    protected void init(final ExecutionYear executionYear, final EntryPhase entryPhase) {
	check(executionYear, "error.DgesBaseProcess.execution.year.is.null", new String[0]);
	check(entryPhase, "error.DgesBaseProcess.entry.phase.is.null", new String[0]);

	setExecutionYear(executionYear);
	setEntryPhase(entryPhase);
    }

    protected List<DegreeCandidateDTO> parseDgesFile(byte[] contents, String university, EntryPhase entryPhase) {

	final List<DegreeCandidateDTO> result = new ArrayList<DegreeCandidateDTO>();
	result.addAll(new DataLoaderFromFile<DegreeCandidateDTO>().load(DegreeCandidateDTO.class, contents));
	setConstantFields(university, entryPhase, result);
	return (List<DegreeCandidateDTO>) result;

    }

    private void setConstantFields(String university, EntryPhase entryPhase, final Collection<DegreeCandidateDTO> result) {
	for (final DegreeCandidateDTO degreeCandidateDTO : result) {
	    degreeCandidateDTO.setIstUniversity(university);
	    degreeCandidateDTO.setEntryPhase(entryPhase);
	}
    }

}