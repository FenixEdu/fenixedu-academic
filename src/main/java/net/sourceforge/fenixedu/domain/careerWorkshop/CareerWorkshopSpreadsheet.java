package net.sourceforge.fenixedu.domain.careerWorkshop;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class CareerWorkshopSpreadsheet extends CareerWorkshopSpreadsheet_Base {
    public CareerWorkshopSpreadsheet(String filename, byte[] content) {
        super();
        init(filename, filename, content, RoleGroup.get(RoleType.DIRECTIVE_COUNCIL));
    }

    @Override
    public void delete() {
        setCareerWorkshopApplicationEvent(null);
        super.delete();
    }

    @Deprecated
    public boolean hasCareerWorkshopApplicationEvent() {
        return getCareerWorkshopApplicationEvent() != null;
    }

}
