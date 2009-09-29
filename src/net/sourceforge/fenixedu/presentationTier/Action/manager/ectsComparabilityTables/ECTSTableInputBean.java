package net.sourceforge.fenixedu.presentationTier.Action.manager.ectsComparabilityTables;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.AcademicIntervalConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class ECTSTableInputBean implements Serializable {
    public static enum ECTSTableType {
	COMPETENCE_COURSE, COMPETENCE_COURSE_BY_DEGREE, COMPETENCE_COURSE_BY_CURRICULAR_YEAR, GRADUATED_BY_DEGREE, GRADUATED_BY_CYCLE;
    }

    public static class ExecutionIntervalProvider implements DataProvider {

	@Override
	public Converter getConverter() {
	    return new AcademicIntervalConverter();
	}

	@Override
	public Object provide(Object source, Object current) {
	    List<AcademicInterval> result = new ArrayList<AcademicInterval>();
	    ExecutionYear year = ExecutionYear.readFirstBolonhaExecutionYear();
	    while (year != null) {
		result.add(year.getAcademicInterval());
		year = year.getNextExecutionYear();
	    }
	    return result;
	}
    }

    private static final long serialVersionUID = -5348156905650195855L;

    private AcademicInterval executionInterval;

    private ECTSTableType type;

    private transient InputStream inputStream;

    private String filename;

    public void setExecutionInterval(AcademicInterval executionInterval) {
	this.executionInterval = executionInterval;
    }

    public AcademicInterval getExecutionInterval() {
	return executionInterval;
    }

    public void setType(ECTSTableType type) {
	this.type = type;
    }

    public ECTSTableType getType() {
	return type;
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = StringNormalizer.normalize(filename);
    }
}