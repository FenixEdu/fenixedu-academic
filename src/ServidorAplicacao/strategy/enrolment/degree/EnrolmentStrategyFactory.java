package ServidorAplicacao.strategy.enrolment.degree;

import ServidorAplicacao.strategy.enrolment.degree.strategys.EnrolmentStrategyLERCI;
import ServidorAplicacao.strategy.enrolment.degree.strategys.IEnrolmentStrategy;

public class EnrolmentStrategyFactory {

	public static final int LERCI = 1;

	private static IEnrolmentStrategy strategyInstance = null;

	public static synchronized IEnrolmentStrategy getInstance(int degree) {

		if (strategyInstance == null) {
			switch(degree) {
				case LERCI:
					strategyInstance = new EnrolmentStrategyLERCI();
					break;
				default:
					break;
			}
		}
		return strategyInstance;
	}

	public static synchronized void resetInstance() {
		if (strategyInstance != null) {
			strategyInstance = null;
		} 
	}

	private EnrolmentStrategyFactory() {
	}

}