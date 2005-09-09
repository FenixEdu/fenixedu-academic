package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IStudentGroup;

public class InfoStudentGroupWithAttends extends InfoStudentGroup {

    public void copyFromDomain(final IStudentGroup studentGroup) {
        super.copyFromDomain(studentGroup);

        if(studentGroup != null) {
            final List<IAttends> attends = studentGroup.getAttends();
            final List<InfoFrequenta> infoAttends = new ArrayList<InfoFrequenta>(attends.size());
            setInfoAttends(infoAttends);
            for (final IAttends attend : attends) {
                infoAttends.add(InfoFrequenta.newInfoFromDomain(attend));
            }
        }
    }
    
    public static InfoStudentGroupWithAttends newInfoFromDomain(final IStudentGroup studentGroup) {
        final InfoStudentGroupWithAttends infoStudentGroup;

        if(studentGroup != null) {
            infoStudentGroup = new InfoStudentGroupWithAttends();
            infoStudentGroup.copyFromDomain(studentGroup);
        } else {
            infoStudentGroup = null;
        }
        
        return infoStudentGroup;
    }

}