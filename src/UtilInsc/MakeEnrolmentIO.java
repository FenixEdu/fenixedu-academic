package UtilInsc;

import java.util.List;

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;

/**
 * @author David Santos
 */

public abstract class MakeEnrolmentIO
{

    public static void showFinalSpan(InfoEnrolmentContext infoEnrolmentContext)
    {
        //        System.out.println();
        //
        //        System.out.println("TO CHOOSE (FINAL SPAN):");
        //        Iterator iterator =
        //            infoEnrolmentContext.getInfoFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
        //        int i = 0;
        //        while (iterator.hasNext())
        //        {
        //            InfoCurricularCourseScope infoCurricularCourseScope =
        //                (InfoCurricularCourseScope) iterator.next();
        //            System.out.println(
        //                i
        //                    + " - YEAR: "
        //                    + infoCurricularCourseScope
        //                        .getInfoCurricularSemester()
        //                        .getInfoCurricularYear()
        //                        .getYear()
        //                    + "; SEMESTER: "
        //                    +
		// infoCurricularCourseScope.getInfoCurricularSemester().getSemester()
        //                    + "; CURRICULAR COURSE: "
        //                    + infoCurricularCourseScope.getInfoCurricularCourse().getName());
        //            i++;
        //        }
        //
        //        System.out.println();
        //
        //        System.out.println("AUTOMATIC CHOOSEN (ANUALS + ALTERNATIVES):");
        //        Iterator iterator2 =
        //            infoEnrolmentContext.getInfoCurricularCoursesScopesAutomaticalyEnroled().iterator();
        //        int j = 0;
        //        while (iterator2.hasNext())
        //        {
        //            InfoCurricularCourseScope infoCurricularCourseScope =
        //                (InfoCurricularCourseScope) iterator2.next();
        //            System.out.println(
        //                j
        //                    + " - YEAR: "
        //                    + infoCurricularCourseScope
        //                        .getInfoCurricularSemester()
        //                        .getInfoCurricularYear()
        //                        .getYear()
        //                    + "; SEMESTER: "
        //                    +
		// infoCurricularCourseScope.getInfoCurricularSemester().getSemester()
        //                    + "; CURRICULAR COURSE: "
        //                    + infoCurricularCourseScope.getInfoCurricularCourse().getName());
        //            j++;
        //        }
    }

    public static void showAvailableDegreesForOption(InfoEnrolmentContext infoEnrolmentContext)
    {
        //        System.out.println();
        //        System.out.println(
        //            "AVAILABLE DEGREES FOR OPTION: ["
        //                + infoEnrolmentContext
        //                    .getInfoChosenOptionalCurricularCourseScope()
        //                    .getInfoCurricularCourse()
        //                    .getName()
        //                + "]");
        //        Iterator iterator =
		// infoEnrolmentContext.getInfoDegreesForOptionalCurricularCourses().iterator();
        //        int i = 0;
        //        while (iterator.hasNext())
        //        {
        //            InfoDegree infoDegree = (InfoDegree) iterator.next();
        //            System.out.println(
        //                i + " - NAME: " + infoDegree.getNome() + "; CODE: " +
		// infoDegree.getSigla());
        //            i++;
        //        }
    }

    public static void showAvailableCurricularCoursesForOption(InfoEnrolmentContext infoEnrolmentContext)
    {
        //        System.out.println();
        //        System.out.println(
        //            "AVAILABLE COURSES FOR OPTION: ["
        //                + infoEnrolmentContext
        //                    .getInfoChosenOptionalCurricularCourseScope()
        //                    .getInfoCurricularCourse()
        //                    .getName()
        //                + "]");
        //        Iterator iterator =
        //            infoEnrolmentContext.getOptionalInfoCurricularCoursesToChooseFromDegree().iterator();
        //        int i = 0;
        //        while (iterator.hasNext())
        //        {
        //            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse)
		// iterator.next();
        //            System.out.println(
        //                i
        //                    + " - NAME: "
        //                    + infoCurricularCourse.getName()
        //                    + "; CODE: "
        //                    + infoCurricularCourse.getCode());
        //            i++;
        //        }
    }

    public static void showChosenCurricularCoursesForOptionalCurricularCourses(InfoEnrolmentContext infoEnrolmentContext)
    {
        //        System.out.println();
        //        System.out.println("CHOSEN COURSE AND OPTIONAL COURSE:");
        //        Iterator iterator =
		// infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments().iterator();
        //        while (iterator.hasNext())
        //        {
        //            InfoEnrolmentInOptionalCurricularCourse
		// infoEnrolmentInOptionalCurricularCourse =
        //                (InfoEnrolmentInOptionalCurricularCourse) iterator.next();
        //            // System.out.println("OPTIONAL CURRICULAR COURSE NAME: " +
        //			//
		// infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseScope().getInfoCurricularCourse().getName()
        //			// + "; CHOSEN CURRICULAR COURSE: " +
        //			//
		// infoEnrolmentInOptionalCurricularCourse.getInfoCurricularCourseForOption().getName());
        //        }
    }

    public static void showActualEnrolments(InfoEnrolmentContext infoEnrolmentContext)
    {
        //        System.out.println();
        //        System.out.println("ACTUAL ENROLMENTS:");
        //        Iterator iterator =
		// infoEnrolmentContext.getActualEnrolment().iterator();
        //        while (iterator.hasNext())
        //        {
        //            InfoCurricularCourseScope infoCurricularCourseScope =
        //                (InfoCurricularCourseScope) iterator.next();
        //            System.out.println(
        //                "YEAR: "
        //                    + infoCurricularCourseScope
        //                        .getInfoCurricularSemester()
        //                        .getInfoCurricularYear()
        //                        .getYear()
        //                    + "; SEMESTER: "
        //                    +
		// infoCurricularCourseScope.getInfoCurricularSemester().getSemester()
        //                    + "; CURRICULAR COURSE: "
        //                    + infoCurricularCourseScope.getInfoCurricularCourse().getName());
        //        }
    }

    public static void showEnrolmentValidationResultMessages(InfoEnrolmentContext infoEnrolmentContext)
    {
        //        System.out.println();
        //        System.out.println("ENROLMENT VALIDATION MESSAGES:");
        //        Set keySet =
		// infoEnrolmentContext.getEnrolmentValidationResult().getMessage().keySet();
        //
        //        Iterator iterator = keySet.iterator();
        //        int i = 1;
        //        while (iterator.hasNext())
        //        {
        //            String msg = (String) iterator.next();
        //            System.out.println(
        //                i
        //                    + " - MESSAGE: "
        //                    + selectApropriateMessageFromAppResource(
        //                        msg,
        //                        (List)
		// infoEnrolmentContext.getEnrolmentValidationResult().getMessage().get(
        //                            msg)));
        //            i++;
        //        }
    }

    public static void selectNormalCurricularCoursesToEnroll(InfoEnrolmentContext infoEnrolmentContext)
    {
        //        List indexes = new ArrayList();
        //        BufferedReader in = new BufferedReader(new
		// InputStreamReader(System.in));
        //        String str = new String(" ");
        //        String endStr = new String("");
        //
        //        while (!str.equals(endStr))
        //        {
        //            System.out.print("> ");
        //            try
        //            {
        //                str = in.readLine();
        //                if (!str.equals(endStr))
        //                {
        //                    Integer i = new Integer(str);
        //                    indexes.add(i);
        //                }
        //            }
        //            catch (NumberFormatException e)
        //            {
        //                continue;
        //            }
        //            catch (Exception e)
        //            {
        //                e.printStackTrace(System.out);
        //            }
        //        }
        //
        //        Iterator iterator = indexes.iterator();
        //        while (iterator.hasNext())
        //        {
        //            int i = ((Integer) iterator.next()).intValue();
        //            InfoCurricularCourseScope infoCurricularCourseScope;
        //            try
        //            {
        //                infoCurricularCourseScope =
        //                    (InfoCurricularCourseScope) infoEnrolmentContext
        //                        .getInfoFinalCurricularCoursesScopesSpanToBeEnrolled()
        //                        .get(i);
        //                if
		// (!infoEnrolmentContext.getActualEnrolment().contains(infoCurricularCourseScope))
        //                {
        //                    infoEnrolmentContext.getActualEnrolment().add(infoCurricularCourseScope);
        //                }
        //                else if (
        //                    !infoCurricularCourseScope.getInfoCurricularCourse().getType().equals(
        //                        new CurricularCourseType(CurricularCourseType.OPTIONAL_COURSE)))
        //                {
        //                    infoEnrolmentContext.getActualEnrolment().remove(infoCurricularCourseScope);
        //                }
        //            }
        //            catch (IndexOutOfBoundsException e)
        //            {
        //                continue;
        //            }
        //        }
    }

    public static void selectDegreeForOptionalCurricularCourseToEnroll(InfoEnrolmentContext infoEnrolmentContext)
    {
        //        BufferedReader in = new BufferedReader(new
		// InputStreamReader(System.in));
        //        String str = new String(" ");
        //        String endStr = new String("");
        //        boolean flag = true;
        //
        //        while (flag)
        //        {
        //            System.out.print("> ");
        //            try
        //            {
        //                str = in.readLine();
        //                if (!str.equals(endStr))
        //                {
        //                    Integer i = new Integer(str);
        //                    infoEnrolmentContext.setChosenOptionalInfoDegree(
        //                        (InfoDegree) infoEnrolmentContext
        //                            .getInfoDegreesForOptionalCurricularCourses()
        //                            .get(
        //                            i.intValue()));
        //                    flag = false;
        //                }
        //                else
        //                {
        //                    infoEnrolmentContext.setChosenOptionalInfoDegree(null);
        //                    flag = false;
        //                }
        //            }
        //            catch (NumberFormatException e)
        //            {
        //                continue;
        //            }
        //            catch (IndexOutOfBoundsException e)
        //            {
        //                continue;
        //            }
        //            catch (Exception e)
        //            {
        //                e.printStackTrace(System.out);
        //            }
        //        }
    }

    public static InfoCurricularCourse selectOptionalCurricularCourseToEnroll(InfoEnrolmentContext infoEnrolmentContext)
    {
        //        BufferedReader in = new BufferedReader(new
		// InputStreamReader(System.in));
        //        String str = new String(" ");
        //        String endStr = new String("");
        //        boolean flag = true;
        //        InfoCurricularCourse infoCurricularCourse = null;
        //
        //        while (flag)
        //        {
        //            System.out.print("> ");
        //            try
        //            {
        //                str = in.readLine();
        //                if (!str.equals(endStr))
        //                {
        //                    Integer i = new Integer(str);
        //                    infoCurricularCourse =
        //                        (InfoCurricularCourse) infoEnrolmentContext
        //                            .getOptionalInfoCurricularCoursesToChooseFromDegree()
        //                            .get(
        //                            i.intValue());
        //                    flag = false;
        //                }
        //            }
        //            catch (NumberFormatException e)
        //            {
        //                continue;
        //            }
        //            catch (IndexOutOfBoundsException e)
        //            {
        //                continue;
        //            }
        //            catch (Exception e)
        //            {
        //                e.printStackTrace(System.out);
        //            }
        //        }
        //        return infoCurricularCourse;
        return null;
    }

    public static String selectApropriateMessageFromAppResource(String appResourceKey, List values)
    {
        //        BufferedReader in = null;
        //        String str = null;
        //        String strFinal = null;
        //
        //        try
        //        {
        //            in =
        //                new BufferedReader(
        //                    new
		// FileReader("config/ServidorApresentacao/StudentResources.properties"));
        //        }
        //        catch (FileNotFoundException e)
        //        {
        //            e.printStackTrace(System.out);
        //            return str;
        //        }
        //
        //        try
        //        {
        //            while (in.ready())
        //            {
        //                str = in.readLine();
        //                String aux1 = new String(appResourceKey + "=");
        //                String aux2 = null;
        //                CharSequence aux3 = null;
        //                CharSequence aux4 = null;
        //                if (str.startsWith(aux1))
        //                {
        //                    int j = str.indexOf("{0}");
        //                    if (j == -1)
        //                    {
        //                        strFinal = str.substring(aux1.length());
        //                    }
        //                    else
        //                    {
        //                        aux2 = str.substring(aux1.length());
        //                        Iterator iterator = values.iterator();
        //                        while (iterator.hasNext())
        //                        {
        //                            String val = (String) iterator.next();
        //                            int i = aux2.indexOf("{0}");
        //                            aux3 = aux2.subSequence(0, i);
        //                            aux4 = aux2.subSequence((i + 3), aux2.length());
        //                            strFinal = aux3.toString() + val + aux4.toString();
        //                        }
        //                    }
        //                    break;
        //                }
        //            }
        //            in.close();
        //        }
        //        catch (IOException e)
        //        {
        //            e.printStackTrace(System.out);
        //        }
        //        catch (Exception e)
        //        {
        //            e.printStackTrace(System.out);
        //        }
        //        return strFinal;
        return null;

    }

    public static IUserView interactiveAutentication()
    {
        //        IUserView userView = null;
        //        BufferedReader in = new BufferedReader(new
		// InputStreamReader(System.in));
        //        String str = new String(" ");
        //        String argsAutenticacao[] = new String[3];
        //
        //        try
        //        {
        //            System.out.println("AUTENTICATING USER:");
        //            System.out.print("LOGIN: ");
        //            str = in.readLine();
        //            argsAutenticacao[0] = new String(str);
        //            System.out.print("PASSWORD: ");
        //            str = in.readLine();
        //            argsAutenticacao[1] = new String(str);
        //            argsAutenticacao[2] = Autenticacao.EXTRANET;
        //            System.out.println();
        //        }
        //        catch (IOException e1)
        //        {
        //            e1.printStackTrace();
        //        }
        //
        //        try
        //        {
        //            userView =
        //                (IUserView) ServiceManagerServiceFactory.executeService(
        //                    null,
        //                    "Autenticacao",
        //                    argsAutenticacao);
        //        }
        //        catch (FenixServiceException e)
        //        {
        //            e.printStackTrace(System.out);
        //        }
        //        return userView;
        return null;
    }
}