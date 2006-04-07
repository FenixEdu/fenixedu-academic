package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.utilTests.Element;
import net.sourceforge.fenixedu.utilTests.ParseMetadata;

/**
 * @author Susana Fernandes
 */

public class Metadata extends Metadata_Base {

    public Metadata(final ExecutionCourse executionCourse, final String author,
            final String description, final String difficulty, final Calendar learningTime,
            final String mainSubject, final String secondarySubject, final String level) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setVisibility(Boolean.TRUE);
        setExecutionCourse(executionCourse);
        setAuthor(author);
        setDescription(description);
        setDifficulty(difficulty);
        setLearningTime(learningTime);
        setMainSubject(mainSubject);
        setSecondarySubject(secondarySubject);
        setLevel(level);
    }

    public Metadata(final ExecutionCourse executionCourse, String file, final Vector<Element> vector) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setVisibility(Boolean.TRUE);
        setExecutionCourse(executionCourse);
        if (file != null) {
            setMetadataFile(file);
            try {
                ParseMetadata parseMetadata = new ParseMetadata();
                parseMetadata.parseMetadata(vector, this);
            } catch (final Exception ex) {
                ex.printStackTrace();
                throw new DomainException("failled.metadata.file.parse");
            }
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

    public List<Question> getVisibleQuestions() {
        final List<Question> visibleQuestions = new ArrayList<Question>();
        for (final Question question : getQuestions()) {
            if (question.getVisibility()) {
                visibleQuestions.add(question);
            }
        }
        return visibleQuestions;
    }
    
    public String correctFileName(String fileName) {
        String original = fileName.replaceAll(".xml", "");
        String newName = fileName;
        for (int i = 1;; i++) {
        	if(getQuestionByFileName(newName) == null) {
        		return newName;
        	}
            newName = original.concat("(" + i + ").xml");
        }    	
    }
    
    public Question getQuestionByFileName(String fileName) {
    	for (Question question : this.getQuestions()) {
			if(question.getXmlFileName().equalsIgnoreCase(fileName)) {
				return question;
			}
		}
    	return null;
    }

    public void delete() {
        for (;!getQuestions().isEmpty();getQuestions().get(0).delete());
        removeExecutionCourse();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
