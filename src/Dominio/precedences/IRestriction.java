package Dominio.precedences;

import Dominio.IDomainObject;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IRestriction extends IDomainObject {
    public IPrecedence getPrecedence();

    public void setPrecedence(IPrecedence precedence);

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext);
}