/*
 * Created on 28/Jul/2003, 15:11:34
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 15:11:34
 *  
 */
public interface ICaseStudy extends IDomainObject {
    /**
     * @return
     */
    public abstract String getCode();

    /**
     * @return
     */
    public abstract String getDescription();

    /**
     * @return
     */
    public abstract String getName();

    /**
     * @return
     */
    public abstract ITheme getSeminaryTheme();

    /**
     * @param string
     */
    public abstract void setCode(String string);

    /**
     * @param string
     */
    public abstract void setDescription(String string);

    /**
     * @param string
     */
    public abstract void setName(String string);

    /**
     * @param seminary
     */
    public abstract void setSeminaryTheme(ITheme seminary);

    public abstract Integer getSeminaryThemeIdInternal();

    public abstract void setSeminaryThemeIdInternal(Integer id);

    public List getSeminaryCandidacies();

    public void setSeminaryCandidacies(List list);

}