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
package net.sourceforge.fenixedu.domain.phd.migration;

import java.io.Serializable;
import java.util.NoSuchElementException;

import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.migration.common.ConversionUtilities;
import net.sourceforge.fenixedu.domain.phd.migration.common.FinalGradeTranslator;
import net.sourceforge.fenixedu.domain.phd.migration.common.PhdProgramTranslator;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IncompleteFieldsException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;

import org.joda.time.LocalDate;

public class PhdMigrationIndividualProcessDataBean implements Serializable {
    private static final long serialVersionUID = 5633491871349165600L;

    private String data;
    private PhdMigrationIndividualProcessData processData;

    private Integer processNumber;
    private PhdProgram phdProgram;
    private String title;
    private String guiderNumber;
    private String assistantGuiderNumber;
    private LocalDate startProcessDate;
    private LocalDate startDevelopmentDate;
    private LocalDate requirementDate;
    private LocalDate meetingDate;
    private LocalDate firstDiscussionDate;
    private LocalDate secondDiscussionDate;
    private LocalDate edictDate;

    private PhdThesisFinalGrade classification;
    private LocalDate ratificationDate;
    private LocalDate annulmentDate;
    private LocalDate limitToFinishDate;

    public PhdMigrationIndividualProcessDataBean(PhdMigrationIndividualProcessData processData) {
        setProcessData(processData);
        setData(processData.getData());
        parse();
    }

    public void parse() {
        try {
            String[] fields = getData().split("\t");

            try {
                processNumber = Integer.valueOf(fields[0].trim());
            } catch (NumberFormatException e) {
                throw new IncompleteFieldsException("processNumber");
            }

            try {
                phdProgram = PhdProgramTranslator.translate(fields[1].trim());
            } catch (NumberFormatException e) {
                throw new IncompleteFieldsException("phdProgram");
            }
            title = fields[2].trim();
            guiderNumber = fields[3].trim();
            assistantGuiderNumber = fields[4].trim();
            startProcessDate = ConversionUtilities.parseDate(fields[5].trim());
            startDevelopmentDate = ConversionUtilities.parseDate(fields[6].trim());
            requirementDate = ConversionUtilities.parseDate(fields[7].trim());
            meetingDate = ConversionUtilities.parseDate(fields[8].trim());
            firstDiscussionDate = ConversionUtilities.parseDate(fields[9].trim());
            secondDiscussionDate = ConversionUtilities.parseDate(fields[10].trim());
            edictDate = ConversionUtilities.parseDate(fields[11].trim());

            classification = FinalGradeTranslator.translate(fields[13].trim());
            ratificationDate = ConversionUtilities.parseDate(fields[14].trim());
            annulmentDate = ConversionUtilities.parseDate(fields[15].trim());
            limitToFinishDate = ConversionUtilities.parseDate(fields[16].trim());

        } catch (NoSuchElementException e) {
            throw new IncompleteFieldsException("Not enough fields");
        }
    }

    public PhdMigrationIndividualProcessData getProcessData() {
        return processData;
    }

    public void setProcessData(PhdMigrationIndividualProcessData processData) {
        this.processData = processData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(Integer processNumber) {
        this.processNumber = processNumber;
    }

    public PhdProgram getPhdProgram() {
        return phdProgram;
    }

    public void setPhdProgram(PhdProgram phdProgram) {
        this.phdProgram = phdProgram;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuiderNumber() {
        return guiderNumber;
    }

    public void setGuiderNumber(String guiderNumber) {
        this.guiderNumber = guiderNumber;
    }

    public String getAssistantGuiderNumber() {
        return assistantGuiderNumber;
    }

    public void setAssistantGuiderNumber(String assistantGuiderNumber) {
        this.assistantGuiderNumber = assistantGuiderNumber;
    }

    public LocalDate getStartProcessDate() {
        return startProcessDate;
    }

    public void setStartProcessDate(LocalDate startProcessDate) {
        this.startProcessDate = startProcessDate;
    }

    public LocalDate getStartDevelopmentDate() {
        return startDevelopmentDate;
    }

    public void setStartDevelopmentDate(LocalDate startDevelopmentDate) {
        this.startDevelopmentDate = startDevelopmentDate;
    }

    public LocalDate getRequirementDate() {
        return requirementDate;
    }

    public void setRequirementDate(LocalDate requirementDate) {
        this.requirementDate = requirementDate;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public LocalDate getFirstDiscussionDate() {
        return firstDiscussionDate;
    }

    public void setFirstDiscussionDate(LocalDate firstDiscussionDate) {
        this.firstDiscussionDate = firstDiscussionDate;
    }

    public LocalDate getSecondDiscussionDate() {
        return secondDiscussionDate;
    }

    public void setSecondDiscussionDate(LocalDate secondDiscussionDate) {
        this.secondDiscussionDate = secondDiscussionDate;
    }

    public LocalDate getEdictDate() {
        return edictDate;
    }

    public void setEdictDate(LocalDate edictDate) {
        this.edictDate = edictDate;
    }

    public PhdThesisFinalGrade getClassification() {
        return classification;
    }

    public void setClassification(PhdThesisFinalGrade classification) {
        this.classification = classification;
    }

    public LocalDate getRatificationDate() {
        return ratificationDate;
    }

    public void setRatificationDate(LocalDate ratificationDate) {
        this.ratificationDate = ratificationDate;
    }

    public LocalDate getAnnulmentDate() {
        return annulmentDate;
    }

    public void setAnnulmentDate(LocalDate annulmentDate) {
        this.annulmentDate = annulmentDate;
    }

    public LocalDate getLimitToFinishDate() {
        return limitToFinishDate;
    }

    public void setLimitToFinishDate(LocalDate limitToFinishDate) {
        this.limitToFinishDate = limitToFinishDate;
    }

    public boolean hasPhdProgram() {
        return phdProgram != null;
    }

}