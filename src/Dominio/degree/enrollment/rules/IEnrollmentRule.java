package Dominio.degree.enrollment.rules;

import java.util.List;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IEnrollmentRule {
    public List apply(List curricularCoursesToBeEnrolledIn);
}