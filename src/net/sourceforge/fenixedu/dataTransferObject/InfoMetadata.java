/*
 * Created on 23/Jul/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IMetadata;

/**
 * @author Susana Fernandes
 */

public class InfoMetadata extends InfoObject {

    private String metadataFile;

    private InfoExecutionCourse infoExecutionCourse;

    private String description;

    private String difficulty;

    private Calendar learningTime;

    private String level;

    private String mainSubject;

    private String secondarySubject;

    private String author;

    private Integer numberOfMembers;

    private Boolean visibility;

    private List visibleQuestions;

    public InfoMetadata() {
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    public Calendar getLearningTime() {
        return learningTime;
    }

    public String getLevel() {
        return level;
    }

    public String getMainSubject() {
        return mainSubject;
    }

    public String getMetadataFile() {
        return metadataFile;
    }

    public Integer getNumberOfMembers() {
        return numberOfMembers;
    }

    public String getSecondarySubject() {
        return secondarySubject;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setAuthor(String string) {
        author = string;
    }

    public void setDescription(String string) {
        description = string;
    }

    public void setDifficulty(String string) {
        difficulty = string;
    }

    public void setInfoExecutionCourse(InfoExecutionCourse course) {
        infoExecutionCourse = course;
    }

    public void setLearningTime(Calendar calendar) {
        learningTime = calendar;
    }

    public void setLevel(String string) {
        level = string;
    }

    public void setMainSubject(String string) {
        mainSubject = string;
    }

    public void setMetadataFile(String string) {
        metadataFile = string;
    }

    public void setNumberOfMembers(Integer integer) {
        numberOfMembers = integer;
    }

    public void setSecondarySubject(String string) {
        secondarySubject = string;
    }

    public void setVisibility(Boolean boolean1) {
        visibility = boolean1;
    }

    public String getLearningTimeFormatted() {
        String result = "";
        Calendar date = getLearningTime();
        if (date == null)
            return result;
        result += date.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (date.get(Calendar.MINUTE) < 10)
            result += "0";
        result += date.get(Calendar.MINUTE);
        return result;
    }

    public List getVisibleQuestions() {
        return visibleQuestions;
    }

    public void setVisibleQuestions(List list) {
        visibleQuestions = list;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoMetadata) {
            InfoMetadata infoMetadata = (InfoMetadata) obj;
            result = (getIdInternal().equals(infoMetadata.getIdInternal()))
                    && (getInfoExecutionCourse().equals(infoMetadata.getInfoExecutionCourse()))
                    && (equals(getMetadataFile(), infoMetadata.getMetadataFile()))
                    && (equals(getDescription(), infoMetadata.getDescription()))
                    && (equals(getDifficulty(), infoMetadata.getDifficulty()))
                    && (getLearningTime().equals(infoMetadata.getLearningTime()))
                    && (equals(getLevel(), infoMetadata.getLevel()))
                    && (equals(getMainSubject(), infoMetadata.getMainSubject()))
                    && (equals(getSecondarySubject(), infoMetadata.getSecondarySubject()))
                    && (equals(getAuthor(), infoMetadata.getAuthor()))
                    && (getNumberOfMembers().equals(infoMetadata.getNumberOfMembers()))
                    && (getVisibility().equals(infoMetadata.getVisibility()))
                    && (getVisibleQuestions().equals(infoMetadata.getVisibleQuestions()));
        }
        return result;
    }

    private boolean equals(String str1, String str2) {
        if (str1 == null ? str2 == null : str1.equals(str2))
            return true;
        return false;
    }

    public void copyFromDomain(IMetadata metadata) {
        super.copyFromDomain(metadata);
        if (metadata != null) {
            setMetadataFile(metadata.getMetadataFile());
            setDescription(metadata.getDescription());
            setDifficulty(metadata.getDifficulty());
            setLearningTime(metadata.getLearningTime());
            setLevel(metadata.getLevel());
            setMainSubject(metadata.getMainSubject());
            setSecondarySubject(metadata.getSecondarySubject());
            setAuthor(metadata.getAuthor());
            setNumberOfMembers(metadata.getNumberOfMembers());
            setVisibility(metadata.getVisibility());
        }
    }

    public static InfoMetadata newInfoFromDomain(IMetadata metadata) {
        InfoMetadata infoMetadata = null;
        if (metadata != null) {
            infoMetadata = new InfoMetadata();
            infoMetadata.copyFromDomain(metadata);
        }
        return infoMetadata;
    }

}