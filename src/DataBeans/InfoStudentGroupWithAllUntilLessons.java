/*
 * Created on 18/Jun/2004
 *
 */
package DataBeans;

import Dominio.IStudentGroup;

/**
 * @author Tânia Pousão 18/Jun/2004
 */
public class InfoStudentGroupWithAllUntilLessons extends InfoStudentGroup {
    public void copyFromDomain(IStudentGroup studentGroup) {
        super.copyFromDomain(studentGroup);
        if (studentGroup != null) {
            setInfoGroupProperties(InfoGroupPropertiesWithInfoExecutionCourse
                    .newInfoFromDomain(studentGroup.getGroupProperties()));
            setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(studentGroup.getShift()));
        }
    }

    public static InfoStudentGroup newInfoFromDomain(IStudentGroup studentGroup) {
        InfoStudentGroupWithAllUntilLessons infoStudentGroup = null;
        if (studentGroup != null) {
            infoStudentGroup = new InfoStudentGroupWithAllUntilLessons();
            infoStudentGroup.copyFromDomain(studentGroup);
        }

        return infoStudentGroup;
    }
}