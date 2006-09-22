package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

public class VigilantBean implements Serializable {

    private DomainReference<ExecutionYear> executionYear;

    private List<DomainReference<VigilantGroup>> vigilantGroups = new ArrayList<DomainReference<VigilantGroup>>();

    private DomainReference<VigilantGroup> selectedVigilantGroup;

    private boolean showIncompatibilities = Boolean.FALSE;

    private boolean showUnavailables = Boolean.FALSE;

    private boolean showAllVigilancyInfo = Boolean.FALSE;

    private boolean showInformationByVigilant = Boolean.TRUE;
    
    private boolean showBoundsJustification = Boolean.FALSE;

    private boolean showStartPoints = Boolean.FALSE;
    
    private final HashMap<String,String> schemas = new HashMap<String, String>();
    
    
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
                  if(this.showBoundsJustification) { 
                	return "convokesByVigilant.allInformation";
                  }
                  else {
                	  return "convokesByVigilant.exceptBounds";
                  }
                } else {
                	if(this.showBoundsJustification) {
                		return "convokesByVigilant.exceptUnavailablesAndBounds";
                	}
                	else {
                		return "convokesByVigilant.exceptUnavailables";
                	}
                }
            } else {
                if (this.showUnavailables) {
                	if(this.showBoundsJustification) {
                		return "convokesByVigilant.incompatibilitiesAndbounds";
                	}
                	else {
                		return "convokesByVigilant.exceptIncompatibilities";
                	}
                } else {
                	if(this.showBoundsJustification) {
                		return "convokesByVigilant.vigilancyInformationAndBounds";
                	}
                	else {
                		return "convokesByVigilant.onlyVigilancyInformation";
                	}
                }
            }
        } else {
            if (this.showIncompatibilities) {
                if (this.showUnavailables) {
                    if(this.showBoundsJustification) {
                    	return "convokesByVigilant.exceptVigilancyInformationAndBounds";
                    }
                    else {
                    	return "convokesByVigilant.exceptVigilancyInformation";
                    }
                } else {
                	if(this.showBoundsJustification)  {
                		return "convokesByVigilant.incompatibilitiesAndBounds";
                	}
                	else {
                		return "convokesByVigilant.onlyIncompatibilities";
                	}
                }
            } else {
                if (this.showUnavailables) {
                    if(this.showUnavailables) {
                    	return "convokesByVigilant.unavailablesAndBounds";
                    }
                    else {
                    	return "convokesByVigilant.onlyUnavailables";
                    }
                } else {
                	if(this.showBoundsJustification) {
                		return "convokesByVigilant.showOnlyBounds";
                	}
                	else {
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
        return executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = new DomainReference<ExecutionYear>(executionYear);

    }

    public VigilantGroup getSelectedVigilantGroup() {
        return selectedVigilantGroup.getObject();
    }

    public void setSelectedVigilantGroup(VigilantGroup group) {
        this.selectedVigilantGroup = new DomainReference<VigilantGroup>(group);
    }

    public List getVigilantGroups() {
        List groups = new ArrayList<VigilantGroup>();
        for (DomainReference<VigilantGroup> vigilantGroup : this.vigilantGroups) {
            if (vigilantGroup != null)
                groups.add(vigilantGroup.getObject());
        }
        return groups;
    }

    public void setVigilantGroups(List<VigilantGroup> groups) {
        this.vigilantGroups = new ArrayList<DomainReference<VigilantGroup>>();
        for (VigilantGroup group : groups) {
            if (group != null) {
                this.vigilantGroups.add(new DomainReference(group));
            }
        }
    }
    
    public String getWhatSchemaToUseInVigilants() {
        boolean unavailables = this.isShowUnavailables();
        boolean incompatibilities = this.isShowIncompatibilities();
        boolean bounds = this.isShowBoundsJustification();
        
        if (unavailables) {
            if (incompatibilities) {
                if(bounds) {
                	return "vigilantsWithAllInformation";
                }
                else {
                	return "vigilantsWithoutBounds";
                }
            } else {
                if(bounds) {
                	return "vigilantsWithoutIncompatibilities";
                }
                else {
                	return "vigilantsOnlyWithUnavailables";
                }
            }
        } else {
            if (incompatibilities) {
              if(bounds) {
            	return "vigilantsWithOutUnavailables";
              }
              else {
            	 return "vigilantsOnlyWithIncompatibilities"; 
              }
            } else {
                if(bounds) {
                		return "vigilantsOnlyWithBounds";
                }
                else {
                	return "simpleVigilants";
                }
            	
            }
        }

    }

}
