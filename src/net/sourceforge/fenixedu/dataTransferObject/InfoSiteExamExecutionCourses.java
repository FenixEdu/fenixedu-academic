/*
 * Created on Nov 24, 2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 *  
 */
public class InfoSiteExamExecutionCourses extends InfoSiteEvaluationExecutionCourses implements
        ISiteComponent {
    protected InfoExam infoExam;

    /**
     * @return Returns the infoExam.
     */
    public InfoExam getInfoExam() {
        return infoExam;
    }

    /**
     * @param infoExam
     *            The infoExam to set.
     */
    public void setInfoExam(InfoExam infoExam) {
        this.infoExam = infoExam;
    }

}