package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixWebFramework.security.UserView;

public class VigilantBean implements Serializable {

    private ExecutionYear executionYear;

    private List<VigilantGroup> vigilantGroups = new ArrayList<VigilantGroup>();

    private VigilantGroup selectedVigilantGroup;

    private boolean showIncompatibilities = Boolean.FALSE;

    private boolean showUnavailables = Boolean.FALSE;

    private boolean showAllVigilancyInfo = Boolean.FALSE;

    private boolean showInformationByVigilant = Boolean.TRUE;

    private boolean showBoundsJustification = Boolean.FALSE;

    private boolean showStartPoints = Boolean.FALSE;

    private boolean showNotActiveConvokes = Boolean.FALSE;

    private boolean showPointsWeight = Boolean.FALSE;

    private boolean showOwnVigilancies = Boolean.FALSE;

    public List<Vigilancy> activeOtherCourseVigilancies;

    private final HashMap<String, String> schemas = new HashMap<String, String>();

    public boolean isShowStartPoints() {
        return showStartPoints;
    }

    public void setShowStartPoints(boolean showStartPoints) {
        this.showStartPoints = showStartPoints;
    }

    public boolean isShowBoundsJustification() {
        return showBoundsJustification;
    }

    public void setShowBoundsJustification(boolean showBoundsJustification) {
        this.showBoundsJustification = showBoundsJustification;
    }

    public boolean isShowAllVigilancyInfo() {
        return showAllVigilancyInfo;
    }

    public void setShowAllVigilancyInfo(boolean showAllVigilancyInfo) {
        this.showAllVigilancyInfo = showAllVigilancyInfo;
    }

    public boolean isShowIncompatibilities() {
        return showIncompatibilities;
    }

    public void setShowIncompatibilities(boolean showIncompatibilities) {
        this.showIncompatibilities = showIncompatibilities;
    }

    public boolean isShowUnavailables() {
        return showUnavailables;
    }

    public void setShowUnavailables(boolean showUnavailables) {
        this.showUnavailables = showUnavailables;
    }

    public boolean isShowInformationByVigilant() {
        return showInformationByVigilant;
    }

    public void setShowInformationByVigilant(boolean showInformationByVigilant) {
        this.showInformationByVigilant = showInformationByVigilant;
    }

    public String getWhatSchemaToUse() {
        if (this.showAllVigilancyInfo) {
            if (this.showIncompatibilities) {
                if (this.showUnavailables) {
                    if (this.showBoundsJustification) {
                        return "convokesByVigilant.allInformation";
                    } else {
                        return "convokesByVigilant.exceptBounds";
                    }
                } else {
                    if (this.showBoundsJustification) {
                        return "convokesByVigilant.exceptUnavailablesAndBounds";
                    } else {
                        return "convokesByVigilant.exceptUnavailables";
                    }
                }
            } else {
                if (this.showUnavailables) {
                    if (this.showBoundsJustification) {
                        return "convokesByVigilant.incompatibilitiesAndbounds";
                    } else {
                        return "convokesByVigilant.exceptIncompatibilities";
                    }
                } else {
                    if (this.showBoundsJustification) {
                        return "convokesByVigilant.vigilancyInformationAndBounds";
                    } else {
                        return "convokesByVigilant.onlyVigilancyInformation";
                    }
                }
            }
        } else {
            if (this.showIncompatibilities) {
                if (this.showUnavailables) {
                    if (this.showBoundsJustification) {
                        return "convokesByVigilant.exceptVigilancyInformationAndBounds";
                    } else {
                        return "convokesByVigilant.exceptVigilancyInformation";
                    }
                } else {
                    if (this.showBoundsJustification) {
                        return "convokesByVigilant.incompatibilitiesAndBounds";
                    } else {
                        return "convokesByVigilant.onlyIncompatibilities";
                    }
                }
            } else {
                if (this.showUnavailables) {
                    if (this.showUnavailables) {
                        return "convokesByVigilant.unavailablesAndBounds";
                    } else {
                        return "convokesByVigilant.onlyUnavailables";
                    }
                } else {
                    if (this.showBoundsJustification) {
                        return "convokesByVigilant.showOnlyBounds";
                    } else {
                        return "convokesByVigilant.simpleInformation";
                    }
                }
            }
        }
    }

    public VigilantBean() {
        setExecutionYear(null);
        setSelectedVigilantGroup(null);
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;

    }

    public VigilantGroup getSelectedVigilantGroup() {
        return selectedVigilantGroup;
    }

    public void setSelectedVigilantGroup(VigilantGroup group) {
        this.selectedVigilantGroup = group;
    }

    public List getVigilantGroups() {
        List groups = new ArrayList<VigilantGroup>();
        for (VigilantGroup vigilantGroup : this.vigilantGroups) {
            if (vigilantGroup != null) {
                groups.add(vigilantGroup);
            }
        }
        return groups;
    }

    public List<VigilantGroup> getUserViewVigilantGroups() {
        Person person = ((IUserView) UserView.getUser()).getPerson();
        return person.getVigilantGroupsForExecutionYear(executionYear);
    }

    public void setVigilantGroups(Collection<VigilantGroup> groups) {
        this.vigilantGroups = new ArrayList<VigilantGroup>();
        for (VigilantGroup group : groups) {
            if (group != null) {
                this.vigilantGroups.add(group);
            }
        }
    }

    public String getWhatSchemaToUseInVigilants() {
        boolean unavailables = this.isShowUnavailables();
        boolean incompatibilities = this.isShowIncompatibilities();
        boolean bounds = this.isShowBoundsJustification();

        if (unavailables) {
            if (incompatibilities) {
                if (bounds) {
                    return "vigilantsWithAllInformation";
                } else {
                    return "vigilantsWithoutBounds";
                }
            } else {
                if (bounds) {
                    return "vigilantsWithoutIncompatibilities";
                } else {
                    return "vigilantsOnlyWithUnavailables";
                }
            }
        } else {
            if (incompatibilities) {
                if (bounds) {
                    return "vigilantsWithOutUnavailables";
                } else {
                    return "vigilantsOnlyWithIncompatibilities";
                }
            } else {
                if (bounds) {
                    return "vigilantsOnlyWithBounds";
                } else {
                    return "simpleVigilants";
                }

            }
        }

    }

    public boolean isShowNotActiveConvokes() {
        return showNotActiveConvokes;
    }

    public void setShowNotActiveConvokes(boolean showNotActiveConvokes) {
        this.showNotActiveConvokes = showNotActiveConvokes;
    }

    public boolean isShowPointsWeight() {
        return showPointsWeight;
    }

    public void setShowPointsWeight(boolean showPointsWeight) {
        this.showPointsWeight = showPointsWeight;
    }

    public boolean isShowOwnVigilancies() {
        return showOwnVigilancies;
    }

    public void setShowOwnVigilancies(boolean showOwnVigilancies) {
        this.showOwnVigilancies = showOwnVigilancies;
    }

    public void setActiveOtherCourseVigilancies(List<Vigilancy> activeOtherCourseVigilancies) {
        this.activeOtherCourseVigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy activeOtherCourseVigilancy : activeOtherCourseVigilancies) {
            this.activeOtherCourseVigilancies.add(activeOtherCourseVigilancy);
        }
    }

    public List<Vigilancy> getActiveOtherCourseVigilancies() {
        List<Vigilancy> activeOtherCourseVigilancies = new ArrayList<Vigilancy>();
        for (Vigilancy activeOtherCourseVigilancy : this.activeOtherCourseVigilancies) {
            activeOtherCourseVigilancies.add(activeOtherCourseVigilancy);
        }
        return activeOtherCourseVigilancies;
    }

}
