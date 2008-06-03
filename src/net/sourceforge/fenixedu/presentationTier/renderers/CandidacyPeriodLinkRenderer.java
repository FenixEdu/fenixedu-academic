//package net.sourceforge.fenixedu.presentationTier.renderers;
//
//import org.apache.commons.beanutils.PropertyUtils;
//import org.joda.time.format.DateTimeFormat;
//import org.joda.time.format.DateTimeFormatter;
//
//import net.sourceforge.fenixedu.domain.elections.DelegateElectionCandidacyPeriod;
//import net.sourceforge.fenixedu.domain.elections.DelegateElectionPeriod;
//import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
//import pt.ist.fenixWebFramework.renderers.OutputRenderer;
//import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
//import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
//import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
//import pt.ist.fenixWebFramework.renderers.components.HtmlText;
//import pt.ist.fenixWebFramework.renderers.components.HtmlLink.Target;
//import pt.ist.fenixWebFramework.renderers.components.state.ViewDestination;
//import pt.ist.fenixWebFramework.renderers.layouts.Layout;
//import pt.ist.fenixWebFramework.renderers.schemas.Schema;
//import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
//import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
//
//public class CandidacyPeriodLinkRenderer extends OutputRenderer {
//
//	private static final String DEFAULT_FORMAT = "dd/MM/yyyy";
//	
//	private boolean useParent;
//
//    private String linkFormat;
//
//    private boolean contextRelative;
//    
//    private boolean moduleRelative;
//
//    private String destination;
//
//    private String subSchema;
//
//    private String subLayout;
//
//    private String key;
//
//    private String bundle;
//
//    private String text;
//
//    private String linkIf;
//    
//    private boolean blankTarget = false;
//
//    private boolean indentation = false;
//    
//    private String pastPeriodClasses;
//    
//    private String currentPeriodClasses;
//    
//    private String dateHtmlSeparator;
//    
//    private String periodPostLabel;
//    
//    private boolean isPeriodPostLabelKey;
//    
//    private String periodPreLabel;
//    
//    private boolean isPeriodPreLabelKey;
//    
//    private String dateFormat;
//    
//    private String periodCreateLabel;
//    
//    private String pastPeriodCreateLabel;
//    
//    private boolean isPeriodCreateLabelKey;
//    
//    private boolean isPastPeriodCreateLabelKey;
//    
//	public String getDateHtmlSeparator() {
//		return dateHtmlSeparator;
//	}
//
//	public void setDateHtmlSeparator(String dateHtmlSeparator) {
//		this.dateHtmlSeparator = dateHtmlSeparator;
//	}
//
//	public String getPeriodPostLabel() {
//		return periodPostLabel;
//	}
//
//	public void setPeriodPostLabel(String periodPostLabel) {
//		this.periodPostLabel = periodPostLabel;
//	}
//	
//	public String getDateFormat() {
//		return (dateFormat != null ? dateFormat : DEFAULT_FORMAT);
//	}
//
//	public void setDateFormat(String dateFormat) {
//		this.dateFormat = dateFormat;
//	}
//
//	public boolean isPeriodPostLabelKey() {
//		return isPeriodPostLabelKey;
//	}
//
//	public void setIsPeriodPostLabelKey(boolean isPeriodPostLabelKey) {
//		this.isPeriodPostLabelKey = isPeriodPostLabelKey;
//	}
//	
//	public String getPeriodPreLabel() {
//		return periodPreLabel;
//	}
//
//	public void setPeriodPreLabel(String periodPreLabel) {
//		this.periodPreLabel = periodPreLabel;
//	}
//	
//	public boolean isPeriodPreLabelKey() {
//		return isPeriodPreLabelKey;
//	}
//
//	public void setIsPeriodPreLabelKey(boolean isPeriodPreLabelKey) {
//		this.isPeriodPreLabelKey = isPeriodPreLabelKey;
//	}
//	
//	public boolean isPastPeriodCreateLabelKey() {
//		return isPastPeriodCreateLabelKey;
//	}
//
//	public void setIsPastPeriodCreateLabelKey(boolean isPastPeriodCreateLabelKey) {
//		this.isPastPeriodCreateLabelKey = isPastPeriodCreateLabelKey;
//	}
//
//	public boolean isPeriodCreateLabelKey() {
//		return isPeriodCreateLabelKey;
//	}
//
//	public void setIsPeriodCreateLabelKey(boolean isPeriodCreateLabelKey) {
//		this.isPeriodCreateLabelKey = isPeriodCreateLabelKey;
//	}
//
//	public String getPastPeriodCreateLabel() {
//		return pastPeriodCreateLabel;
//	}
//
//	public void setPastPeriodCreateLabel(String pastPeriodCreateLabel) {
//		this.pastPeriodCreateLabel = pastPeriodCreateLabel;
//	}
//
//	public String getPeriodCreateLabel() {
//		return periodCreateLabel;
//	}
//
//	public void setPeriodCreateLabel(String periodCreateLabel) {
//		this.periodCreateLabel = periodCreateLabel;
//	}
//
//	public boolean isBlankTarget() {
//		return blankTarget;
//	}
//
//    /**
//     * This property allows you to specify if the link opens in a new
//     * window or not. Defaults to false.
//     *
//     * @property
//     */
//	public void setBlankTarget(boolean blankTarget) {
//		this.blankTarget = blankTarget;
//	}
//    
//    public String getLinkFormat() {
//        return this.linkFormat;
//    }
//
//    /**
//     * This property allows you to specify the format of the final link. In this
//     * format you can use properties of the object being presented. For example:
//     * 
//     * <code>
//     *  format="/some/action.do?oid=${id}"
//     * </code>
//     * 
//     * @see RenderUtils#getFormattedProperties(String, Object)
//     * @property
//     */
//    public void setLinkFormat(String linkFormat) {
//        this.linkFormat = linkFormat;
//    }
//
//    public boolean isContextRelative() {
//        return this.contextRelative;
//    }
//
//    /**
//     * Indicates that the link specified should be relative to the context of the
//     * application and not to the current module. This also overrides the module
//     * if a destination is specified.
//     * 
//     * @property
//     */
//    public void setContextRelative(boolean contextRelative) {
//        this.contextRelative = contextRelative;
//    }
//
//    public boolean isModuleRelative() {
//        return this.moduleRelative;
//    }
//
//    /**
//     * Allows you to choose if the generated link is relative to the current module. Note that
//     * if the link is not context relative then it also isn't module relative.
//     * 
//     * @property
//     */
//    public void setModuleRelative(boolean moduleRelative) {
//        this.moduleRelative = moduleRelative;
//    }
//
//    public boolean isUseParent() {
//        return this.useParent;
//    }
//
//    /**
//     * This property can be used when presenting an object's slot. If this
//     * property is true the object that will be considered when replacing the
//     * properties in the link will be the parent object, that is, the object
//     * that contains the slot being presented.
//     * 
//     * <p>
//     * Off course, if this property is false (the default) the object that will
//     * be considered is the object initialy being presented.
//     * 
//     * @property
//     */
//    public void setUseParent(boolean useParent) {
//        this.useParent = useParent;
//    }
//
//    public String getDestination() {
//        return this.destination;
//    }
//
//    /**
//     * This property is an alternative to the use of the
//     * {@link #setLinkFormat(String) linkFormat}. With this property you can
//     * specify the name of the view state destination that will be used. This
//     * property allows you to select the concrete destination in each context
//     * were this configuration is used.
//     * 
//     * @property
//     */
//    public void setDestination(String destination) {
//        this.destination = destination;
//    }
//
//    public String getText() {
//        return this.text;
//    }
//
//    /**
//     * The text to appear as the link text. This is a simple alternative to the
//     * full presentation of the object.
//     * 
//     * @property
//     */
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public String getKey() {
//        return this.key;
//    }
//
//    /**
//     * Instead of specifying thr {@link #setText(String) text} property you can
//     * specify a key, with this property, and a bundle with the
//     * {@link #setBundle(String) bundle}.
//     * 
//     * @property
//     */
//    public void setKey(String key) {
//        this.key = key;
//    }
//
//    public String getBundle() {
//        return this.bundle;
//    }
//
//    /**
//     * The bundle were the {@link #setKey(String) key} will be fetched.
//     * 
//     * @property
//     */
//    public void setBundle(String bundle) {
//        this.bundle = bundle;
//    }
//
//    public String getSubLayout() {
//        return this.subLayout;
//    }
//
//    /**
//     * Specifies the sub layout that will be used for the body of the link, that
//     * is, the object will be presented using the layout specified and the
//     * result of that presentation will be the body of the link.
//     * 
//     * @property
//     */
//    public void setSubLayout(String subLayout) {
//        this.subLayout = subLayout;
//    }
//
//    public String getSubSchema() {
//        return this.subSchema;
//    }
//
//    /**
//     * The name of the schema to use in the presentation of the object for the
//     * body of the link.
//     * 
//     * @property
//     */
//    public void setSubSchema(String subSchema) {
//        this.subSchema = subSchema;
//    }
//
//    public String getCurrentPeriodClasses() {
//		return currentPeriodClasses;
//	}
//
//	public void setCurrentPeriodClasses(String currentPeriodClasses) {
//		this.currentPeriodClasses = currentPeriodClasses;
//	}
//
//	public String getPastPeriodClasses() {
//		return pastPeriodClasses;
//	}
//
//	public void setPastPeriodClasses(String pastPeriodClasses) {
//		this.pastPeriodClasses = pastPeriodClasses;
//	}
//
//	public String getLinkIf() {
//        return this.linkIf;
//    }
//
//    /**
//     * Name of the property to use when determining if we should really do a link or not.
//     * 
//     * @property
//     */
//    public void setLinkIf(String linkIf) {
//        this.linkIf = linkIf;
//    }
//    
//    /**
//     * Chooses if the generated elements should be indented or not. This can be
//     * usefull when you want to introduce a separator but need to remove extra
//     * spaces.
//     * 
//     * @property
//     */
//    public void setIndentation(boolean indentation) {
//    	this.indentation  = indentation;
//    }
//    
//    public boolean isIndentation() {
//    	return this.indentation;
//    }
//
//    @Override
//    protected Layout getLayout(Object object, Class type) {
//        return new Layout() {
//        	
//        	public String getPeriodResume(DelegateElectionPeriod electionPeriod, String preLabel, String postLabel, boolean isLongResume) {
//        		preLabel = (preLabel != null ? RenderUtils.getResourceString(getBundle(), preLabel) + "&nbsp;" : "");
//        		
//        		postLabel = (postLabel != null ? postLabel : "");
//        		
//        		String shortResume = electionPeriod.getStartDate().toString(getDateFormat()) + getDateHtmlSeparator() + 
//					electionPeriod.getEndDate().toString(getDateFormat());
//        		
//        		String longResume = null;
//				if(electionPeriod instanceof DelegateElectionCandidacyPeriod)
//					longResume = "(" + ((DelegateElectionCandidacyPeriod)electionPeriod).getElection().getCandidatesCount() + postLabel + ")";
//				else
//					longResume = "(" + ((DelegateElectionVotingPeriod)electionPeriod).getElection().getVotesCount() + postLabel + ")";
//        		
//        		if(isLongResume) {
//        			return preLabel + shortResume + "<br/>" + longResume;
//        		}
//        		else {
//        			return preLabel + shortResume;
//        		}
//        	}
//
//            @Override
//            public HtmlComponent createComponent(Object object, Class type) {
//                Object usedObject = getTargetObject(object);
//
//                if (usedObject == null) {
//                    return new HtmlText();
//                }
//                
//                if (isAllowedToLink(usedObject)) {
//                	
//                    HtmlLink link = getLink(usedObject);
//                    
//                    link.setIndented(isIndentation());
//    
//                    String text = getLinkText();
//                    if (text != null) {
//                        link.setText(text);
//                    } else {
//                    	HtmlBlockContainer container = new HtmlBlockContainer();
//                    	
//                    	if(isUseParent()) {
//                        	DelegateElectionPeriod electionPeriod = (DelegateElectionPeriod) object;
//
//                        	if (electionPeriod.isPastPeriod()) {
//                        		String createLabel = (isPastPeriodCreateLabelKey() ? RenderUtils.getResourceString(getBundle(), 
//                        				getPastPeriodCreateLabel()) : getPastPeriodCreateLabel());
////                				String createLabel = RenderUtils.getResourceString(getBundle(), "label.createAnotherPeriod");                 				
//                				HtmlBlockContainer linkContainer = new HtmlBlockContainer();
//                				link.setBody(new HtmlText(createLabel));
//                				linkContainer.addChild(link);
//                				container.addChild(linkContainer);                				
//                				
//                				String preLabel = (isPeriodPreLabelKey() ? RenderUtils.getResourceString(getBundle(), getPeriodPreLabel()) : getPeriodPreLabel());
//                				String postLabel = (isPeriodPostLabelKey() ? RenderUtils.getResourceString(getBundle(), getPeriodPostLabel()) : getPeriodPostLabel());                				
//                				HtmlText periodResume = new HtmlText(getPeriodResume(electionPeriod, preLabel, postLabel, false), false);                				                				
//                				periodResume.setClasses(getPastPeriodClasses()); //Classes for past periods                				
//                				container.addChild(periodResume);                				
//                				return container;
//                        	}
//                        	else if (electionPeriod.isCurrentPeriod()){
//                        		String postLabel = (isPeriodPostLabelKey() ? RenderUtils.getResourceString(getBundle(), getPeriodPostLabel()) : getPeriodPostLabel());                        		
//                        		String periodResume = getPeriodResume(electionPeriod, null, postLabel, true);                        		                        		
//                        		link.setBody(new HtmlText(periodResume, false));                        		
//                        		link.setClasses(getCurrentPeriodClasses()); //Classes for current periods                        		
//                        		container.addChild(link);                        		
//                        		return container;
//                        	}
//                        	else {
//                        		String periodResume = getPeriodResume(electionPeriod, null, null, false);
//                        		link.setBody(new HtmlText(periodResume, false));
//                        		link.setClasses(getClasses()); //Default classes
//                        		container.addChild(link);
//                        		return container;
//                        	}
//                        }
//                        else {
//                        	link.setBody(getLinkBody(object));
//                        }
//                    }
//    
//                    if (isBlankTarget()) {
//                	        link.setTarget(Target.BLANK);
//                    }
//    
//                    return link;
//                }
//                else {
//                    return getLinkBody(object);
//                }
//            }
//
//            private boolean isAllowedToLink(Object usedObject) {
//                if (getLinkIf() == null) {
//                    return true;
//                }
//                else {
//                    try {
//                        Object object = PropertyUtils.getProperty(usedObject, getLinkIf());
//                        if (object == null) {
//                            return true;
//                        }
//                        else {
//                            return (Boolean) object;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return false;
//                    } 
//                }
//            }
//
//            public HtmlComponent getLinkBody(Object object) {
//                Schema findSchema = RenderKit.getInstance().findSchema(getSubSchema());
//                return renderValue(object, findSchema, getSubLayout());
//            }
//
//            private String getLinkText() {
//                if (getText() != null) {
//                    return getText();
//                }
//
//                if (getKey() == null) {
//                    return null;
//                }
//
//                return RenderUtils.getResourceString(getBundle(), getKey());
//            }
//
//            private HtmlLink getLink(Object usedObject) {
//                HtmlLink link = new HtmlLink();
//
//                String url;
//
//                if (getDestination() != null) {
//                    ViewDestination destination = getContext().getViewState().getDestination(
//                            getDestination());
//
//                    if (destination != null) {
//                        link.setModule(destination.getModule());
//                        url = destination.getPath();
//                    } else {
//                        url = "#";
//                    }
//                } else {
//                    if (getLinkFormat() != null) {
//                        url = getLinkFormat();
//                    } else {
//                        url = "#";
//                    }
//                }
//
//                link.setUrl(RenderUtils.getFormattedProperties(url, usedObject));
//
//                link.setModuleRelative(isModuleRelative());
//                link.setContextRelative(isContextRelative());
//                
//                return link;
//            }
//
//        };
//    }
//
//    protected Object getTargetObject(Object object) {
//        if (isUseParent()) {
//            if (getContext().getParentContext() != null) {
//                return getContext().getParentContext().getMetaObject().getObject();
//            } else {
//                return null;
//            }
//        } else {
//            return object;
//        }
//    }
//
//	
//}
