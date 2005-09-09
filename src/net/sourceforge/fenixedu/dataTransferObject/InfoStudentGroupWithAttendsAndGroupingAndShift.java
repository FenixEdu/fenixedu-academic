package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IStudentGroup;

public class InfoStudentGroupWithAttendsAndGroupingAndShift extends InfoStudentGroupWithAttendsAndGrouping {

    public void copyFromDomain(final IStudentGroup studentGroup) {
        super.copyFromDomain(studentGroup);

        if(studentGroup != null) {
            setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(studentGroup.getShift()));
        }
    }
    
    public static InfoStudentGroupWithAttendsAndGroupingAndShift newInfoFromDomain(final IStudentGroup studentGroup) {
        final InfoStudentGroupWithAttendsAndGroupingAndShift infoStudentGroup;

        if(studentGroup != null) {
            infoStudentGroup = new InfoStudentGroupWithAttendsAndGroupingAndShift();
            infoStudentGroup.copyFromDomain(studentGroup);
        } else {
            infoStudentGroup = null;
        }
        
        return infoStudentGroup;
    }

}