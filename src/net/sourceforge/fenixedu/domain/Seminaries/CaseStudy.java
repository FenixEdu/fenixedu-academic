/*
 * Created on 28/Jul/2003, 15:05:18
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 15:05:18
 *  
 */
public class CaseStudy extends DomainObject implements ICaseStudy {
    private Integer idInternal;

    private ITheme seminaryTheme;

    private List seminaryCandidacies;

    private String code;

    private String description;

    private String name;

    private Integer seminaryThemeIdInternal;

    public CaseStudy() {
    }

    public CaseStudy(Integer idInternal) {
        super(idInternal);
    }

    public CaseStudy(String name, String description, String code) {
        this.setName(name);
        this.setDescription(description);
        this.setCode(code);
    }

    /**
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public Integer getIdInternal() {
        return idInternal;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public ITheme getSeminaryTheme() {
        return seminaryTheme;
    }

    /**
     * @param string
     */
    public void setCode(String string) {
        code = string;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param integer
     */
    public void setIdInternal(Integer integer) {
        idInternal = integer;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param seminary
     */
    public void setSeminaryTheme(ITheme seminary) {
        this.seminaryTheme = seminary;
    }

    /**
     * @return
     */
    public Integer getSeminaryThemeIdInternal() {
        return seminaryThemeIdInternal;
    }

    /**
     * @param integer
     */
    public void setSeminaryThemeIdInternal(Integer integer) {
        seminaryThemeIdInternal = integer;
    }

    /**
     * @return
     */
    public List getSeminaryCandidacies() {
        return seminaryCandidacies;
    }

    /**
     * @param list
     */
    public void setSeminaryCandidacies(List list) {
        seminaryCandidacies = list;
    }

}