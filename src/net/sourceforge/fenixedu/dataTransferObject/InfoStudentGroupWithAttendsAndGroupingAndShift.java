package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentGroup;

public class InfoStudentGroupWithAttendsAndGroupingAndShift extends InfoStudentGroupWithAttendsAndGrouping {

    public void copyFromDomain(final StudentGroup studentGroup) {
        super.copyFromDomain(studentGroup);

        if(studentGroup != null) {
            setInfoShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
        }
    }
    
    public static InfoStudentGroupWithAttendsAndGroupingAndShift newInfoFromDomain(final StudentGroup studentGroup) {
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