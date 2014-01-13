package net.sourceforge.fenixedu.domain.phd.migration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.MissingPersonalDataException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.ParseException;

import org.fenixedu.bennu.core.domain.Bennu;

public class PhdMigrationProcess extends PhdMigrationProcess_Base {

    private transient Map<String, String> INSTITUTION_MAP = new HashMap<String, String>();
    private transient Map<Integer, PhdMigrationIndividualPersonalData> PERSONAL_DATA_MAP =
            new HashMap<Integer, PhdMigrationIndividualPersonalData>();

    protected PhdMigrationProcess() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected PhdMigrationProcess(String[] institutionEntries, String[] processDataEntries, String[] personalDataEntries,
            String[] guidingEntries) {
        super();
        setRootDomainObject(Bennu.getInstance());

        indexInstitutionEntries(institutionEntries);
        createPersonalDataEntries(personalDataEntries);
        createProcessDataEntries(processDataEntries);
        createGuidingEntries(guidingEntries);
    }

    private void indexInstitutionEntries(String[] institutionEntries) {
        for (String institutionEntry : institutionEntries) {
            String[] compounds = institutionEntry.split("\\t");
            INSTITUTION_MAP.put(compounds[0], compounds[1]);
        }
    }

    static public PhdMigrationProcess createMigrationProcess(String[] institutionEntries, String[] processDataEntries,
            String[] personalDataEntries, String[] guidingEntries) {
        return new PhdMigrationProcess(institutionEntries, processDataEntries, personalDataEntries, guidingEntries);
    }

    private void createGuidingEntries(String[] guidingEntries) {
        for (String entry : guidingEntries) {
            PhdMigrationGuiding guiding = new PhdMigrationGuiding(entry);
            try {
                guiding.parseAndSetNumber(INSTITUTION_MAP);
                guiding.setPhdMigrationProcess(this);
            } catch (ParseException e) {
                guiding.setParseLog(pruneStackTrace(getStackTrace(e)));
            }
        }

    }

    private void createPersonalDataEntries(String[] personalDataEntries) {
        for (String entry : personalDataEntries) {
            PhdMigrationIndividualPersonalData personalData = new PhdMigrationIndividualPersonalData(entry);
            try {
                personalData.parseAndSetNumber();
                PERSONAL_DATA_MAP.put(personalData.getNumber(), personalData);
            } catch (ParseException e) {
                personalData.setParseLog(pruneStackTrace(getStackTrace(e)));
            }
        }
    }

    private String getStackTrace(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            try {
                e.printStackTrace(pw);
                return sw.toString();
            } finally {
                pw.close();
                sw.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String pruneStackTrace(String stackTrace) {
        String firstLine = stackTrace.substring(0, stackTrace.indexOf('\n'));
        String exceptionStr = firstLine.substring(firstLine.indexOf("\" ") + 1);
        return exceptionStr;
    }

    private void createProcessDataEntries(String[] processDataEntries) {
        for (String entry : processDataEntries) {
            PhdMigrationIndividualProcessData processData = new PhdMigrationIndividualProcessData(entry);
            try {
                processData.parseAndSetNumber();
                PhdMigrationIndividualPersonalData personalData = PERSONAL_DATA_MAP.get(processData.getNumber());
                if (personalData == null) {
                    throw new MissingPersonalDataException();
                }

                processData.setPhdMigrationIndividualPersonalData(personalData);
                processData.setPhdMigrationProcess(this);
            } catch (ParseException e) {
                processData.setParseLog(pruneStackTrace(getStackTrace(e)));
            }
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationIndividualProcessData> getPhdMigrationIndividualProcessData() {
        return getPhdMigrationIndividualProcessDataSet();
    }

    @Deprecated
    public boolean hasAnyPhdMigrationIndividualProcessData() {
        return !getPhdMigrationIndividualProcessDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.migration.PhdMigrationGuiding> getPhdMigrationGuiding() {
        return getPhdMigrationGuidingSet();
    }

    @Deprecated
    public boolean hasAnyPhdMigrationGuiding() {
        return !getPhdMigrationGuidingSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasEndTime() {
        return getEndTime() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasStartTime() {
        return getStartTime() != null;
    }

}
