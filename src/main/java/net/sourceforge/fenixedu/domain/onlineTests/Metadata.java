/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.utilTests.Element;
import net.sourceforge.fenixedu.utilTests.ParseMetadata;

import org.fenixedu.bennu.core.domain.Bennu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Susana Fernandes
 */

public class Metadata extends Metadata_Base {

    private static final Logger logger = LoggerFactory.getLogger(Metadata.class);

    public Metadata(final ExecutionCourse executionCourse, final String author, final String description,
            final String difficulty, final Calendar learningTime, final String mainSubject, final String secondarySubject,
            final String level) {
        super();
        setRootDomainObject(Bennu.getInstance());
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
        setRootDomainObject(Bennu.getInstance());
        setVisibility(Boolean.TRUE);
        setExecutionCourse(executionCourse);
        if (file != null) {
            setMetadataFile(file);
            try {
                ParseMetadata parseMetadata = new ParseMetadata();
                parseMetadata.parseMetadata(vector, this);
            } catch (final Exception ex) {
                logger.error(ex.getMessage(), ex);
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
            if (getQuestionByFileName(newName) == null) {
                return newName;
            }
            newName = original.concat("(" + i + ").xml");
        }
    }

    public Question getQuestionByFileName(String fileName) {
        for (Question question : this.getQuestions()) {
            if (question.getXmlFileName() != null && question.getXmlFileName().equalsIgnoreCase(fileName)) {
                return question;
            }
        }
        return null;
    }

    public void delete() {
        for (; !getQuestions().isEmpty(); getQuestions().iterator().next().delete()) {
            ;
        }
        setExecutionCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public static Set<Metadata> findVisibleMetadataFromExecutionCourseNotOfTest(final ExecutionCourse executionCourse,
            final Test test) {
        final Set<Metadata> testMetadatas = new HashSet<Metadata>();
        for (final TestQuestion testQuestion : test.getTestQuestionsSet()) {
            testMetadatas.add(testQuestion.getQuestion().getMetadata());
        }

        final Set<Metadata> visibleMetadata = new HashSet<Metadata>();
        for (final Metadata metadata : executionCourse.getMetadatasSet()) {
            if (metadata.getVisibility() != null && metadata.getVisibility().booleanValue() && !testMetadatas.contains(metadata)) {
                visibleMetadata.add(metadata);
            }
        }

        return visibleMetadata;
    }

    public static Set<Metadata> findVisibleMetadataFromExecutionCourseNotOfDistributedTest(final ExecutionCourse executionCourse,
            final DistributedTest distributedTest) {
        final Set<Metadata> distributedTestMetadata = new HashSet<Metadata>();
        for (final StudentTestQuestion studentTestQuestion : distributedTest.getDistributedTestQuestionsSet()) {
            distributedTestMetadata.add(studentTestQuestion.getQuestion().getMetadata());
        }

        final Set<Metadata> visibleMetadata = new HashSet<Metadata>();
        for (final Metadata metadata : executionCourse.getMetadatasSet()) {
            if (metadata.getVisibility() != null && metadata.getVisibility().booleanValue()
                    && !distributedTestMetadata.contains(metadata)) {
                visibleMetadata.add(metadata);
            }
        }

        return visibleMetadata;
    }

    public String getLearningTimeFormatted() {
        String result = "";
        Calendar date = getLearningTime();
        if (date == null) {
            return result;
        }
        result += date.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (date.get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        result += date.get(Calendar.MINUTE);
        return result;
    }

    @Deprecated
    public java.util.Date getLearningTimeDate() {
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getLearningTimeDateHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setLearningTimeDate(java.util.Date date) {
        if (date == null) {
            setLearningTimeDateHourMinuteSecond(null);
        } else {
            setLearningTimeDateHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.onlineTests.Question> getQuestions() {
        return getQuestionsSet();
    }

    @Deprecated
    public boolean hasAnyQuestions() {
        return !getQuestionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasSecondarySubject() {
        return getSecondarySubject() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasLearningTimeDateHourMinuteSecond() {
        return getLearningTimeDateHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasDifficulty() {
        return getDifficulty() != null;
    }

    @Deprecated
    public boolean hasAuthor() {
        return getAuthor() != null;
    }

    @Deprecated
    public boolean hasMainSubject() {
        return getMainSubject() != null;
    }

    @Deprecated
    public boolean hasVisibility() {
        return getVisibility() != null;
    }

    @Deprecated
    public boolean hasLevel() {
        return getLevel() != null;
    }

    @Deprecated
    public boolean hasMetadataFile() {
        return getMetadataFile() != null;
    }

}
