/*
 * Created on 10/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree;

import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public abstract class EnrolmentTemporarilyEnrol {

	public static void apply(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {
			throw new ExcepcaoPersistencia();
	}
}
