package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.elections.DelegateElectionCandidacyPeriod;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionPeriod;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlLink.Target;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.PropertyUtils;

public class DelegateElectionPeriodLinkRenderer extends OutputRenderer {

	private static final String DEFAULT_FORMAT = "dd/MM/yyyy";

    private String linkFormat;

    private boolean contextRelative;
    
    private boolean moduleRelative;

    private String destination;

    private String bundle;
    
    private String pastPeriodClasses;
    
    private String currentPeriodClasses;
    
    private String dateHtmlSeparator;
    
    private String dateFormat;
    
    private String periodCreateLabel;
    
    private boolean isPeriodCreateLabelKey;
    
    private String pastPeriodCreateLabel;
    
    private boolean isPastPeriodCreateLabelKey;
    
    private String pastPeriodLabel;
    
    private boolean isPastPeriodLabelKey;
	
    private String votingPeriodPostLabel;
    
    private boolean isVotingPeriodPostLabelKey;

    private String candidacyPeriodPreLabel;
    
    private boolean isCandidacyPeriodPreLabelKey;
    
    private String candidacyPeriodPostLabel;
    
    private boolean isCandidacyPeriodPostLabelKey;
    
    private String votingPeriodPreLabel;
    
    private boolean isVotingPeriodPreLabelKey;

        
    public String getDateHtmlSeparator() {
		return dateHtmlSeparator;
	}

	public void setDateHtmlSeparator(String dateHtmlSeparator) {
		this.dateHtmlSeparator = dateHtmlSeparator;
	}
	
	public String getDateFormat() {
		return (dateFormat != null ? dateFormat : DEFAULT_FORMAT);
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	/**
     * This property allows you to specify the candidacy period
     * post label. (Ex.: candidatos)
     *
     * @property
     */
	public String getCandidacyPeriodPostLabel() {
		return candidacyPeriodPostLabel;
	}

	public void setCandidacyPeriodPostLabel(String candidacyPeriodPostLabel) {
		this.candidacyPeriodPostLabel = candidacyPeriodPostLabel;
	}
	
	public boolean isCandidacyPeriodPostLabelKey() {
		return isCandidacyPeriodPostLabelKey;
	}

	public void setIsCandidacyPeriodPostLabelKey(boolean isCandidacyPeriodPostLabelKey) {
		this.isCandidacyPeriodPostLabelKey = isCandidacyPeriodPostLabelKey;
	}
	
	/**
     * This property allows you to specify the voting period
     * post label. (Ex.: votos)
     *
     * @property
     */
	public String getVotingPeriodPostLabel() {
		return votingPeriodPostLabel;
	}

	public void setVotingPeriodPostLabel(String votingPeriodPostLabel) {
		this.votingPeriodPostLabel = votingPeriodPostLabel;
	}
	
	public boolean isVotingPeriodPostLabelKey() {
		return isVotingPeriodPostLabelKey;
	}

	public void setIsVotingPeriodPostLabelKey(boolean isVotingPeriodPostLabelKey) {
		this.isVotingPeriodPostLabelKey = isVotingPeriodPostLabelKey;
	}
	
	/**
     * This property allows you to specify the candidacy period
     * pre label. (Ex.: Candidaturas:)
     *
     * @property
     */
	public String getCandidacyPeriodPreLabel() {
		return candidacyPeriodPreLabel;
	}

	public void setCandidacyPeriodPreLabel(String candidacyPeriodPreLabel) {
		this.candidacyPeriodPreLabel = candidacyPeriodPreLabel;
	}
	
	public boolean isCandidacyPeriodPreLabelKey() {
		return isCandidacyPeriodPreLabelKey;
	}

	public void setIsCandidacyPeriodPreLabelKey(boolean isCandidacyPeriodPreLabelKey) {
		this.isCandidacyPeriodPreLabelKey = isCandidacyPeriodPreLabelKey;
	}
	
	/**
     * This property allows you to specify the voting period
     * pre label. (Ex.: Período de votação:)
     *
     * @property
     */
	public String getVotingPeriodPreLabel() {
		return votingPeriodPreLabel;
	}

	public void setVotingPeriodPreLabel(String votingPeriodPreLabel) {
		this.votingPeriodPreLabel = votingPeriodPreLabel;
	}
	
	public boolean isVotingPeriodPreLabelKey() {
		return isVotingPeriodPreLabelKey;
	}

	public void setIsVotingPeriodPreLabelKey(boolean isVotingPeriodPreLabelKey) {
		this.isVotingPeriodPreLabelKey = isVotingPeriodPreLabelKey;
	}
	
	/**
     * This property allows you to specify a past period
     * label. (Ex.: Período terminado:)
     *
     * @property
     */
	public String getPastPeriodLabel() {
		return pastPeriodLabel;
	}

	public void setPastPeriodLabel(String pastPeriodLabel) {
		this.pastPeriodLabel = pastPeriodLabel;
	}
	
	public boolean isPastPeriodLabelKey() {
		return isPastPeriodLabelKey;
	}

	public void setIsPastPeriodLabelKey(boolean isPastPeriodLabelKey) {
		this.isPastPeriodLabelKey = isPastPeriodLabelKey;
	}
	
	/**
     * This property allows you to specify a period
     * create link label. (Ex.: Definir período)
     *
     * @property
     */
	public String getPeriodCreateLabel() {
		return periodCreateLabel;
	}

	public void setPeriodCreateLabel(String periodCreateLabel) {
		this.periodCreateLabel = periodCreateLabel;
	}
	
	public boolean isPeriodCreateLabelKey() {
		return isPeriodCreateLabelKey;
	}

	public void setIsPeriodCreateLabelKey(boolean isPeriodCreateLabelKey) {
		this.isPeriodCreateLabelKey = isPeriodCreateLabelKey;
	}
	
	/**
     * This property allows you to specify the past period
     * create link label. (Ex.: Definir outro período)
     *
     * @property
     */
	public String getPastPeriodCreateLabel() {
		return pastPeriodCreateLabel;
	}

	public void setPastPeriodCreateLabel(String pastPeriodCreateLabel) {
		this.pastPeriodCreateLabel = pastPeriodCreateLabel;
	}
	
	public boolean isPastPeriodCreateLabelKey() {
		return isPastPeriodCreateLabelKey;
	}

	public void setIsPastPeriodCreateLabelKey(boolean isPastPeriodCreateLabelKey) {
		this.isPastPeriodCreateLabelKey = isPastPeriodCreateLabelKey;
	}
    
    public String getLinkFormat() {
        return this.linkFormat;
    }

    /**
     * This property allows you to specify the format of the final link. In this
     * format you can use properties of the object being presented. For example:
     * 
     * <code>
     *  format="/some/action.do?oid=${id}"
     * </code>
     * 
     * @see RenderUtils#getFormattedProperties(String, Object)
     * @property
     */
    public void setLinkFormat(String linkFormat) {
        this.linkFormat = linkFormat;
    }

    public boolean isContextRelative() {
        return this.contextRelative;
    }

    /**
     * Indicates that the link specified should be relative to the context of the
     * application and not to the current module. This also overrides the module
     * if a destination is specified.
     * 
     * @property
     */
    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    public boolean isModuleRelative() {
        return this.moduleRelative;
    }

    /**
     * Allows you to choose if the generated link is relative to the current module. Note that
     * if the link is not context relative then it also isn't module relative.
     * 
     * @property
     */
    public void setModuleRelative(boolean moduleRelative) {
        this.moduleRelative = moduleRelative;
    }

    public String getDestination() {
        return this.destination;
    }

    /**
     * This property is an alternative to the use of the
     * {@link #setLinkFormat(String) linkFormat}. With this property you can
     * specify the name of the view state destination that will be used. This
     * property allows you to select the concrete destination in each context
     * were this configuration is used.
     * 
     * @property
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBundle() {
        return this.bundle;
    }
    
    /**
     * The bundle were the {@link #setKey(String) key} will be fetched.
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getCurrentPeriodClasses() {
		return currentPeriodClasses;
	}

	public void setCurrentPeriodClasses(String currentPeriodClasses) {
		this.currentPeriodClasses = currentPeriodClasses;
	}

	public String getPastPeriodClasses() {
		return pastPeriodClasses;
	}

	public void setPastPeriodClasses(String pastPeriodClasses) {
		this.pastPeriodClasses = pastPeriodClasses;
	}

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {
        	
            @Override
            public HtmlComponent createComponent(Object object, Class type) {
            	if (getContext().getParentContext() != null) {
            		Object targetObject = getContext().getParentContext().getMetaObject().getObject(); //Election
                    
            		HtmlLink link = getLink(targetObject);                    
            		
        			HtmlBlockContainer container = new HtmlBlockContainer();
        			
    				DelegateElectionPeriod electionPeriod = (DelegateElectionPeriod) object;                        	
    				
    				if(electionPeriod.isCandidacyPeriod() && getVotingPeriodPostLabel() != null) { 
    					//Showing voting periods but no voting period created. Show candidacy period.
    					
    					String createLabel = (isPeriodCreateLabelKey() ? RenderUtils.getResourceString(getBundle(), 
    							getPeriodCreateLabel()) : getPeriodCreateLabel());                				
    					HtmlBlockContainer linkContainer = new HtmlBlockContainer();
    					link.setBody(new HtmlText(createLabel));
    					linkContainer.addChild(link);                				
    					container.addChild(linkContainer);
    					
    					String preLabel = getElectionPeriodPreLabel(electionPeriod);
    					String postLabel = getElectionPeriodPostLabel(electionPeriod);
    					
    					HtmlText periodResume = new HtmlText(getPeriodResume(electionPeriod, preLabel, postLabel, true), false);
    					periodResume.setClasses(getPastPeriodClasses()); //Classes for candidacy period                				
    					container.addChild(periodResume);                				
    					return container;
    				}
    				else {
    					if (electionPeriod.isPastPeriod()) {
    						if(electionPeriod.isCandidacyPeriod()) {
	    						String createLabel = (isPastPeriodCreateLabelKey() ? RenderUtils.getResourceString(getBundle(), 
	    								getPastPeriodCreateLabel()) : getPastPeriodCreateLabel());                 				
	    						HtmlBlockContainer linkContainer = new HtmlBlockContainer();
	    						link.setBody(new HtmlText(createLabel));
	    						linkContainer.addChild(link);
	    						container.addChild(linkContainer);      
    						}
    						
    						String pastPeriodLabel = (isPastPeriodLabelKey() ? RenderUtils.getResourceString(getBundle(), getPastPeriodLabel()) : getPastPeriodLabel());	                				
    						String postLabel = getElectionPeriodPostLabel(electionPeriod);
    						
    						HtmlText periodResume = new HtmlText(getPeriodResume(electionPeriod, pastPeriodLabel, postLabel, true), false);                				                				
    						periodResume.setClasses(getPastPeriodClasses()); //Classes for past periods                				
    						container.addChild(periodResume);                				
    						return container;
    					}
    					else if (electionPeriod.isCurrentPeriod()){
    						String postLabel = getElectionPeriodPostLabel(electionPeriod);                        		
    						String periodResume = getPeriodResume(electionPeriod, null, postLabel, true);                        		                        		
    						link.setBody(new HtmlText(periodResume, false));                        		
    						link.setClasses(getCurrentPeriodClasses()); //Classes for current periods                        		
    						container.addChild(link);                        		
    						return container;
    					}
    					else {
    						String periodResume = getPeriodResume(electionPeriod, null, null, false);
    						link.setBody(new HtmlText(periodResume, false));
    						link.setClasses(getClasses()); //Default classes
    						container.addChild(link);
    						return container;
    					}
    				}
                }
            	return new HtmlText();
            }

            private HtmlLink getLink(Object usedObject) {
                HtmlLink link = new HtmlLink();

                String url;

                if (getDestination() != null) {
                    ViewDestination destination = getContext().getViewState().getDestination(
                            getDestination());

                    if (destination != null) {
                        link.setModule(destination.getModule());
                        url = destination.getPath();
                    } else {
                        url = "#";
                    }
                } else {
                    if (getLinkFormat() != null) {
                        url = getLinkFormat();
                    } else {
                        url = "#";
                    }
                }

                link.setUrl(RenderUtils.getFormattedProperties(url, usedObject));

                link.setModuleRelative(isModuleRelative());
                link.setContextRelative(isContextRelative());
                
                return link;
            }
            
            private String getPeriodResume(DelegateElectionPeriod electionPeriod, String preLabel, String postLabel, boolean isLongResume) {        		
        		preLabel = (preLabel != null ? preLabel + ":&nbsp;" : "");        		
        		postLabel = (postLabel != null ? "&nbsp;" + postLabel : "");
        		
        		String shortResume = electionPeriod.getStartDate().toString(getDateFormat()) + getDateHtmlSeparator() + 
					electionPeriod.getEndDate().toString(getDateFormat());
        		
        		String longResume = null;
				if(electionPeriod instanceof DelegateElectionCandidacyPeriod){
					longResume = "(" + ((DelegateElectionCandidacyPeriod)electionPeriod).getDelegateElection().getCandidatesCount() + postLabel + ")";
				}
				else{
					longResume = "(" + ((DelegateElectionVotingPeriod)electionPeriod).getDelegateElection().getVotesCount() + postLabel + ")";
				}
        		
        		return (isLongResume ? preLabel + shortResume + "<br/>" + longResume : preLabel + shortResume);
        	}
        	
        	private String getElectionPeriodPostLabel(DelegateElectionPeriod electionPeriod){
        		String postLabel;
        		if(electionPeriod.isCandidacyPeriod()) {
        			postLabel = (isCandidacyPeriodPostLabelKey() ? RenderUtils.getResourceString(getBundle(), getCandidacyPeriodPostLabel()) : getCandidacyPeriodPostLabel());
        		}
        		else {
        			postLabel = (isVotingPeriodPostLabelKey() ? RenderUtils.getResourceString(getBundle(), getVotingPeriodPostLabel()) : getVotingPeriodPostLabel());
        		}       
        		return postLabel;
        	}
        	
        	private String getElectionPeriodPreLabel(DelegateElectionPeriod electionPeriod){
        		String preLabel;
        		if(electionPeriod.isCandidacyPeriod()) {
        			preLabel = (isCandidacyPeriodPreLabelKey() ? RenderUtils.getResourceString(getBundle(), getCandidacyPeriodPreLabel()) : getCandidacyPeriodPreLabel());
        		}
        		else {
        			preLabel = (isVotingPeriodPreLabelKey() ? RenderUtils.getResourceString(getBundle(), getVotingPeriodPreLabel()) : getVotingPeriodPreLabel());
        		}        		
        		return preLabel;
        	}

        };
    }
}
