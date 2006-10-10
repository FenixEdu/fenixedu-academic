/*
 * Created on 5/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoSiteFirstPage extends DataTranferObject implements ISiteComponent {

    private String alternativeSite;

    private String initialStatement;

    private String introduction;

    private List lastFiveAnnouncements;

    private List responsibleTeachers;

    private List lecturingTeachers;

    private Integer siteIdInternal;

    /**
     * @return
     */
    public String getAlternativeSite() {
        return alternativeSite;
    }

    /**
     * @return
     */
    public String getInitialStatement() {
        return initialStatement;
    }

    /**
     * @return
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * @return
     */
    public List getLecturingTeachers() {
        return lecturingTeachers;
    }

    /**
     * @return
     */
    public List getResponsibleTeachers() {
        return responsibleTeachers;
    }

    /**
     * @param string
     */
    public void setAlternativeSite(String string) {
        alternativeSite = string;
    }

    /**
     * @param string
     */
    public void setInitialStatement(String string) {
        initialStatement = string;
    }

    /**
     * @param string
     */
    public void setIntroduction(String string) {
        introduction = string;
    }

    /**
     * @param list
     */
    public void setLecturingTeachers(List list) {
        lecturingTeachers = list;
    }

    /**
     * @param list
     */
    public void setResponsibleTeachers(List list) {
        responsibleTeachers = list;
    }

    /**
     * @return
     */
    public Integer getSiteIdInternal() {
        return siteIdInternal;
    }

    /**
     * @param integer
     */
    public void setSiteIdInternal(Integer integer) {
        siteIdInternal = integer;
    }

    /**
     * @param infoAnnouncements
     */
    public void setLastFiveAnnouncements(List infoAnnouncements) {
        lastFiveAnnouncements = infoAnnouncements;
    }

    public List getLastFiveAnnouncements() {
        return lastFiveAnnouncements;
    }

}