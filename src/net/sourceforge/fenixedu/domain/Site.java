/*
 * Site.java
 * Mar 10, 2003
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Ivo Brandão
 */
public class Site extends Site_Base {
    private IExecutionCourse executionCourse;

    /**
     * @return IDisciplinaExecucao
     */
    public IExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    /**
     * Sets the executionCourse.
     * 
     * @param executionCourse
     *            The executionCourse to set
     */
    public void setExecutionCourse(IExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[SITE";
        result += ", codInt=" + getIdInternal();
        result += ", executionCourse=" + getExecutionCourse();
        result += ", initialStatement=" + getInitialStatement();
        result += ", introduction=" + getIntroduction();
        result += ", mail =" + getMail();
        result += ", alternativeSite=" + getAlternativeSite();
        result += "]";
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof ISite) {
            result = (getExecutionCourse().equals(((ISite) arg0).getExecutionCourse()));
        }
        return result;
    }

}
