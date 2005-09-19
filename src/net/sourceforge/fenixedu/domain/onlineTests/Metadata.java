package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.utilTests.ParseMetadata;

import org.apache.struts.upload.FormFile;

/**
 * @author Susana Fernandes
 */

public class Metadata extends Metadata_Base {

    

    public Metadata(final IExecutionCourse executionCourse, final FormFile metadataFile, final String path) {
        super();
        setVisibility(Boolean.TRUE);
        setExecutionCourse(executionCourse);

        try {
            if (metadataFile != null && metadataFile.getFileData().length != 0) {
                ParseMetadata parseMetadata = new ParseMetadata();
                setMetadataFile(new String(metadataFile.getFileData(), "ISO-8859-1"));
                parseMetadata.parseMetadata(this, path);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            throw new DomainException("failled.metadata.file.parse");
        }

    }

    public Calendar getLearningTime() {
        if (getLearningTimeDate() != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(getLearningTimeDate());
            return calendar;
        }

        return null;
    }

    public void setLearningTime(Calendar calendar) {
        final Date date = (calendar != null) ? calendar.getTime() : null;
        setLearningTimeDate(date);
    }

    public List<IQuestion> getVisibleQuestions() {
        final List<IQuestion> visibleQuestions = new ArrayList<IQuestion>();
        for (final IQuestion question : getQuestions()) {
            if (question.getVisibility()) {
                visibleQuestions.add(question);
            }
        }
        return visibleQuestions;
    }


}