package Dominio.degree.enrollment.rules;

import java.util.List;

import Dominio.exceptions.EnrolmentRuleDomainException;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IEnrollmentRule {
    public List apply(List curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException;
}