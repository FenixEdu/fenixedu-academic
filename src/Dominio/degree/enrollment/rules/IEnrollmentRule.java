package Dominio.degree.enrollment.rules;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IEnrollmentRule
{
	public List apply(List curricularCoursesToBeEnrolledIn) throws ExcepcaoPersistencia;
}