package UtilInsc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import framework.factory.ServiceManagerServiceFactory;

import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.Autenticacao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import Util.EnrolmentState;

/**
 * @author David Santos
 */

public abstract class SetEnrolmentStateIO {

	public static void showTemporarilyEnrolmentList(List enrolmentList) {
		System.out.println();

		IEnrolment firstEnrolment = (IEnrolment) enrolmentList.get(0);
		String studentName = firstEnrolment.getStudentCurricularPlan().getStudent().getPerson().getNome() + " - " + firstEnrolment.getStudentCurricularPlan().getStudent().getPerson().getUsername();
		String executionPeriodName = firstEnrolment.getExecutionPeriod().getExecutionYear().getYear() + " - " + firstEnrolment.getExecutionPeriod().getName();

		System.out.println("LIST OF TEMPORARILY ENROLMENTS FOR STUDENT [" + studentName + "] IN EXECUTION PERIOD [" + executionPeriodName + "]:");
		Iterator iterator = enrolmentList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if(enrolment instanceof IEnrolmentInOptionalCurricularCourse) {
				IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) enrolment;
				System.out.println(i + " - CURRICULAR COURSE: " + enrolmentInOptionalCurricularCourse.getCurricularCourse().getName() + " -> " + enrolmentInOptionalCurricularCourse.getCurricularCourseForOption().getName());
			} else {
				System.out.println(i + " - CURRICULAR COURSE: " + enrolment.getCurricularCourse().getName());
			}
			i++;
		}
	}

	public static void showExecutionPeriods(List executionPeriodList) {
		System.out.println();
		System.out.println("LIST OF EXECUTION PERIODS:");
		Iterator iterator = executionPeriodList.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			IExecutionPeriod executionPeriod = (IExecutionPeriod) iterator.next();
			System.out.println(i + " - " + executionPeriod.getExecutionYear().getYear() + " " + executionPeriod.getName());
			i++;
		}
	}

	public static String showEnrolmentState(EnrolmentState enrolmentState) {
		return enrolmentState.toString();
	}

	public static void selectNewStatesForListOfTemporarilyEnrolments(List enrolmentList, boolean chooseExecutionPeriodsMode) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = new String(" ");
		String endStr = new String("");
		Iterator iterator = enrolmentList.iterator();

		System.out.println();
		while(iterator.hasNext()) {
			IEnrolment enrolment = (IEnrolment) iterator.next();
			if(enrolment instanceof IEnrolmentInOptionalCurricularCourse) {
				IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) enrolment;
				System.out.println("SELECT NEW STATE FOR ENROLMENT IN CURRICULAR COURSE [" + enrolmentInOptionalCurricularCourse.getCurricularCourse().getName() + " -> " + enrolmentInOptionalCurricularCourse.getCurricularCourseForOption().getName() + "]:");
			} else {
				System.out.println("SELECT NEW STATE FOR ENROLMENT IN CURRICULAR COURSE [" + enrolment.getCurricularCourse().getName() + "]:");
			}
			if(chooseExecutionPeriodsMode) {
				System.out.println("(APROVED = 1; NOT_APROVED = 2; ENROLED = 3; TEMPORARILY_ENROLED = 4; ANNULED = 5)");
			} else {
				System.out.println("(APROVED = 1; NOT_APROVED = 2)");
			}
			while(!str.equals(endStr)) {
				System.out.print("> ");
				try {
					str = in.readLine();
					if(!str.equals(endStr)) {
						int i = (new Integer(str)).intValue();
						if( (i < 6) && (i > 0) ) {
							enrolment.setEnrolmentState(EnrolmentState.getEnum(i));
							break;
						}
					}
				} catch (NumberFormatException e) {
					continue;
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
			}
		}
	}

	public static IExecutionPeriod selectExecutionPeriod(List executionPeriodList) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = new String(" ");
		String endStr = new String("");
		boolean flag = true;
		IExecutionPeriod executionPeriod = null;

		while(flag) {
			System.out.print("> ");
			try {
				str = in.readLine();
				if(!str.equals(endStr)) {
					Integer i = new Integer(str);
					executionPeriod = (IExecutionPeriod) executionPeriodList.get(i.intValue());
					flag = false;
				}
			} catch (NumberFormatException e) {
				continue;
			} catch (IndexOutOfBoundsException e) {
				continue;
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
		return executionPeriod;
	}

	public static boolean selectMode() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = new String(" ");
		String endStr = new String("");
		boolean result = false;

		System.out.println("SELECT THE MODE TO OPERATE [CHOOSE_EXECUTION_PERIOD = 1; ACTUAL_ENROLMENT_PERIOD = 2]:");
		while(!str.equals(endStr)) {
			System.out.print("> ");
			try {
				str = in.readLine();
				if(!str.equals(endStr)) {
					int i = (new Integer(str)).intValue();
					if(i == 1) {
						result = true;
						break;
					} else if(i == 2) {
						result = false;
						break;
					}
				}
			} catch (NumberFormatException e) {
				continue;
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
		return result;
	}

	public static IUserView interactiveAutentication() {
		IUserView userView = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = new String(" ");
		String argsAutenticacao[] = new String[3];

		try {
			System.out.println("AUTENTICATING USER:");
			System.out.print("LOGIN: ");
			str = in.readLine();
			argsAutenticacao[0] = new String(str);
			System.out.print("PASSWORD: ");
			str = in.readLine();
			argsAutenticacao[1] = new String(str);
			argsAutenticacao[2] = Autenticacao.EXTRANET;
			System.out.println();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			userView = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsAutenticacao);
		} catch (FenixServiceException e) {
			e.printStackTrace(System.out);
		}
		return userView;
	}
}