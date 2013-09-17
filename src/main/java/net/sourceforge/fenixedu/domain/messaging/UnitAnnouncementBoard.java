package net.sourceforge.fenixedu.domain.messaging;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter;

public class UnitAnnouncementBoard extends UnitAnnouncementBoard_Base {

    public static final Comparator<UnitAnnouncementBoard> BY_UNIT_DEPTH_AND_NAME = new Comparator<UnitAnnouncementBoard>() {
        @Override
        public int compare(UnitAnnouncementBoard o1, UnitAnnouncementBoard o2) {
            Unit unit1 = o1.getParty();
            Unit unit2 = o2.getParty();

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
    public void setParty(Party party) {
        super.setParty(party);
    }

    @Override
    public Unit getParty() {
        return (Unit) super.getParty();
    }

    public void setUnit(Unit unit) {
        this.setParty(unit);
    }

    public Unit getUnit() {
        return getParty();
    }

    @Override
    public Boolean getInitialAnnouncementsApprovedState() {
        return isCurrentUserApprover();
    }

    @Override
    public String getSiteParamForAnnouncementBoard(Announcement announcement) {
        String base = super.getSiteParamForAnnouncementBoard(announcement);

        StringBuffer actionPath = new StringBuffer();
        actionPath.append("&");
        actionPath.append(ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME);
        actionPath.append("=");
        actionPath.append(this.getUnit().getSite().getReversePath());

        return base + actionPath.toString();
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
