/*
 * Created on 6/Ago/2003
 */
package DataBeans;

/**
 * @author Susana Fernandes
 */
public class InfoSiteTestQuestion extends DataTranferObject implements ISiteComponent
{

    private InfoTestQuestion infoTestQuestion;

    private InfoExecutionCourse executionCourse;

    public InfoSiteTestQuestion()
    {
    }

    public InfoExecutionCourse getExecutionCourse()
    {
        return executionCourse;
    }

    public InfoTestQuestion getInfoTestQuestion()
    {
        return infoTestQuestion;
    }

    public void setExecutionCourse(InfoExecutionCourse course)
    {
        executionCourse = course;
    }

    public void setInfoTestQuestion(InfoTestQuestion question)
    {
        infoTestQuestion = question;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof InfoSiteTestQuestion)
        {
            InfoSiteTestQuestion infoSiteTestQuestion = (InfoSiteTestQuestion) obj;
            result = getExecutionCourse().equals(infoSiteTestQuestion.getExecutionCourse())
                    && getInfoTestQuestion().equals(infoSiteTestQuestion.getInfoTestQuestion());
        }
        return result;
    }
}