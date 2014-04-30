package net.sourceforge.fenixedu.domain.careerWorkshop;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class CareerWorkshopConfirmationSpreadsheet extends CareerWorkshopConfirmationSpreadsheet_Base {
    public CareerWorkshopConfirmationSpreadsheet(String filename, byte[] content) {
        super();
        init(filename, filename, content, RoleGroup.get(RoleType.DIRECTIVE_COUNCIL));
    }

    @Override
    public void delete() {
        setCareerWorkshopConfirmationEvent(null);
        super.delete();
    }

    @Deprecated
    public boolean hasCareerWorkshopConfirmationEvent() {
        return getCareerWorkshopConfirmationEvent() != null;
    }

}
