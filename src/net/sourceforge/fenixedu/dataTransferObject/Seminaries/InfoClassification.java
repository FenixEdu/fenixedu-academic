/*
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at Feb 1, 2004 , 6:59:38 PM
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;

/**
 * 
 * Created at Feb 1, 2004 , 6:59:38 PM
 * 
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *  
 */
public class InfoClassification extends DataTranferObject {
    private String aritmeticClassification;

    private String classification;

    private String completedCourses;

    /**
     * @return
     */
    public String getAritmeticClassification() {
        return aritmeticClassification;
    }

    /**
     * @return
     */
    public String getClassification() {
        return classification;
    }

    /**
     * @param f
     */
    public void setAritmeticClassification(String f) {
        aritmeticClassification = f;
    }

    /**
     * @param f
     */
    public void setClassification(String f) {
        classification = f;
    }

    /**
     * @return
     */
    public String getCompletedCourses() {
        return completedCourses;
    }

    /**
     * @param f
     */
    public void setCompletedCourses(String f) {
        completedCourses = f;
    }

}