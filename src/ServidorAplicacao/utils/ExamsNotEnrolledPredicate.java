/*
 * Created on 27/Mai/2003
 *
 * 
 */
package ServidorAplicacao.utils;

import java.util.List;

import org.apache.commons.collections.Predicate;

/**
 * @author João Mota
 *
 */
public class ExamsNotEnrolledPredicate implements Predicate
{
    private List examsEnrolled;
    /* (non-Javadoc)
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public ExamsNotEnrolledPredicate(List exams)
    {
        this.examsEnrolled = exams;
    }

    public boolean evaluate(Object arg0)
    {

        return !examsEnrolled.contains(arg0);
    }

}
