/*
 * Created on 28/Fev/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.ICursoExecucao;
import ServidorPersistente.middleware.constants.Constants;

/**
 * @author jpvl
 */
public abstract class ClassUtils {

    public static int getCurricularYearByClassName(String className) {
        int anoCurricular = 0;
        try {
            anoCurricular = Integer.parseInt(className.substring(2, 3));
        } catch (java.lang.NumberFormatException e) {
            try {
                anoCurricular = Integer.parseInt(className.substring(className.length() - 3, className
                        .length() - 2));
            } catch (java.lang.NumberFormatException ex) {
                anoCurricular = 5;
                System.out.println("NÃO FOI POSSÍVEL INFERIR O ANO CURRÍCULAR DA TURMA " + className
                        + " PELO QUE FOI DADO O ANO 5");
            }
        }
        return anoCurricular;
    }

    /**
     * @param className
     * @return ICursoExecucao
     */
    public static ICursoExecucao getExecutionDegreeByClassName(String className,
            String[] executionDegreeInitials, HashMap executionDegreeHashMap) {
        int degreeNumber = Integer.parseInt(className.substring(0, 2));
        String degreeInitials = executionDegreeInitials[degreeNumber - 1];
        ICursoExecucao executionDegree = (ICursoExecucao) executionDegreeHashMap.get(degreeInitials);
        if (executionDegree == null)
            System.out.println("Error execution degree not found" + degreeInitials);
        return executionDegree;
    }

    static public List extractDegreeCodeList(List classList) {
        Iterator iterator = classList.iterator();
        List listOfDegrees = new ArrayList();
        while (iterator.hasNext()) {
            String className = (String) iterator.next();

            String degreeCode = className.substring(0, 2);
            if (!listOfDegrees.contains(degreeCode))
                listOfDegrees.add(degreeCode);
        }
        return listOfDegrees;
    }

    static public String getDegreeInitials(String className) {
        int degreeCode = Integer.parseInt(className.substring(0, 2));
        return Constants.siglasLicenciaturas[degreeCode - 1];
    }

    /**
     * @param classNameList
     * @return List
     */
    public static List extractCurricularYearList(List classNameList) {
        Iterator iterator = classNameList.iterator();
        List listOfCurricularYears = new ArrayList();
        while (iterator.hasNext()) {
            String className = (String) iterator.next();

            Integer curricularYear = new Integer(ClassUtils.getCurricularYearByClassName(className));
            if (!listOfCurricularYears.contains(curricularYear))
                listOfCurricularYears.add(curricularYear);
        }
        return listOfCurricularYears;
    }

    /**
     * @param classNameList
     * @return List
     */
    public static List extractInitialsList(List classNameList) {

        List listOfDegreeInitials = new ArrayList();
        Iterator iterator = classNameList.iterator();
        while (iterator.hasNext()) {
            String className = (String) iterator.next();

            String degreeInitials = Constants.siglasLicenciaturas[Integer.parseInt(className.substring(
                    0, 2)) - 1];
            if (!listOfDegreeInitials.contains(degreeInitials))
                listOfDegreeInitials.add(degreeInitials);
        }
        return listOfDegreeInitials;
    }
}