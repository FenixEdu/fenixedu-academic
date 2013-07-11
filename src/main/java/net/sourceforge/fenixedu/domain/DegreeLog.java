package net.sourceforge.fenixedu.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.util.BundleUtil;
import pt.utl.ist.fenix.tools.util.StringAppender;

public class DegreeLog extends DegreeLog_Base {

    public enum DegreeLogTypes {

        CANDIDACIES, COORDINATION_TEAM, PROGRAM_TUTORED_PARTICIPATION, QUC_RESULTS, SCIENTIFIC_COMISSION;
        public String getQualifiedName() {
            return StringAppender.append(DegreeLogTypes.class.getSimpleName(), ".", name());
        }

        private static final Collection<DegreeLogTypes> typesAsList = Collections.unmodifiableList(Arrays.asList(values()));

        public static Collection<DegreeLogTypes> valuesAsList() {
            return typesAsList;
        }
    }

    public DegreeLog() {
        super();
    }

    public DegreeLog(Degree degree, ExecutionYear executionYear, String description) {
        super();
        if (getDegree() == null) {
            setDegree(degree);
        }
        if (getExecutionYear() == null) {
            setExecutionYear(executionYear);
        }
        setDescription(description);
    }

    protected static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getStringFromResourceBundle(bundle, key, args);
    }

    public DegreeLogTypes getDegreeLogType() {
        return null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
