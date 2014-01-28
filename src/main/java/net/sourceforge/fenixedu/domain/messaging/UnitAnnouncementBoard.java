package net.sourceforge.fenixedu.domain.messaging;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class UnitAnnouncementBoard extends UnitAnnouncementBoard_Base {

    public static final Comparator<UnitAnnouncementBoard> BY_UNIT_DEPTH_AND_NAME = new Comparator<UnitAnnouncementBoard>() {
        @Override
        public int compare(UnitAnnouncementBoard o1, UnitAnnouncementBoard o2) {
            Unit unit1 = o1.getUnit();
            Unit unit2 = o2.getUnit();

            int result = unit1.getUnitDepth() - unit2.getUnitDepth();

            if (result == 0) {
                result = o1.getName().compareTo(o2.getName());
                return (result == 0) ? o1.getExternalId().compareTo(o2.getExternalId()) : result;
            } else {
                return (result <= 0) ? -1 : 1;
            }
        }
    };

    public UnitAnnouncementBoard(Unit unit) {
        super();

        setUnit(unit);
    }

    @Override
    public String getFullName() {
        return getUnit().getName();
    }

    @Override
    public String getQualifiedName() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.getName()).append(" - ").append(this.getUnit().getName());
        return buffer.toString();
    }

    @Override
    public void delete() {
        setUnit(null);
        super.delete();
    }

    @Override
    public Site getSite() {
        return getUnit().getSite();
    }

    @Override
    public Boolean getInitialAnnouncementsApprovedState() {
        return isCurrentUserApprover();
    }

    @Deprecated
    public boolean hasUnitPermittedManagementGroupType() {
        return getUnitPermittedManagementGroupType() != null;
    }

    @Deprecated
    public boolean hasUnitPermittedReadGroupType() {
        return getUnitPermittedReadGroupType() != null;
    }

    @Deprecated
    public boolean hasUnitPermittedWriteGroupType() {
        return getUnitPermittedWriteGroupType() != null;
    }

}
