package net.sourceforge.fenixedu.renderers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMenu;
import net.sourceforge.fenixedu.renderers.components.HtmlMenuOption;
import net.sourceforge.fenixedu.renderers.converters.EnumConverter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.log4j.Logger;

import pt.ist.utl.fenix.utils.Pair;

/**
 * This renderer presents an html menu with one option for each possible enum value.
 * Each option's label is searched in the bundle <tt>ENUMERATION_RESOURCES</tt> using the
 * enum's name.
 * 
 * <p>
 * Example:<br/>
 * Choose a {@link java.lang.annotation.ElementType element type}:
 * <select>
 *  <option>Type</option>
 *  <option>Field</option>
 *  <option>Parameter</option>
 *  <option>Constructor</option>
 *  <option>Local Variable</option>
 *  <option>Annotation</option>
 *  <option>Package</option>
 * </select>
 * 
 * @author cfgi
 */
public class EnumInputRenderer extends InputRenderer {

    private static Logger logger = Logger.getLogger(EnumInputRenderer.class);

    private String defaultText;
    private String bundle;
    private boolean key;
    private String excludedValues;
    private String includedValues;
    
    public String getBundle() {
        return this.bundle;
    }

    /**
     * The bundle used if <code>key</code> is <code>true</code>
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getDefaultText() {
        return this.defaultText;
    }

    /**
     * The text or key of the default menu title.
     * 
     * @property
     */
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getExcludedValues() {
        return excludedValues;
    }


    /**
     * Excluded Values.
     * 
     * @property
     */
    public void setExcludedValues(String excludedValues) {
        this.excludedValues = excludedValues;
    }

    public String getIncludedValues() {
        return includedValues;
    }

    /**
     * Excluded Values.
     * 
     * @property
     */
    public void setIncludedValues(String includedValues) {
        this.includedValues = includedValues;
    }

    public boolean isKey() {
        return this.key;
    }

    /**
     * Indicates the the default text is a key to a resource bundle.
     *  
     * @property
     */
    public void setKey(boolean key) {
        this.key = key;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object targetObject, Class type) {
                Enum enumerate = (Enum) targetObject;
                
                HtmlMenu menu = new HtmlMenu();
                
                String defaultOptionTitle = getDefaultTitle();
                menu.createDefaultOption(defaultOptionTitle).setSelected(enumerate == null);
                
                Collection<Object> constants = getIncludedEnumValues(type);
                Collection<Object> excludedValues = getExcludedEnumValues(type);
                List<Pair<Enum,String>> pairList = new ArrayList<Pair<Enum,String>>();
                
                for(Object object : constants) {
                	Enum oneEnum = (Enum) object;
                	pairList.add(new Pair<Enum,String>(oneEnum,RenderUtils.getEnumString(oneEnum, getBundle())));
                }
                
                Collections.sort(pairList, new Comparator<Pair<Enum,String>>() {
					public int compare(Pair<Enum, String> o1, Pair<Enum, String> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
                });
                
                for (Pair<Enum,String> pair : pairList) {
                    
                	Enum oneEnum = pair.getKey();
                	String description = pair.getValue();

                    if (excludedValues.contains(oneEnum)) {
                    	continue;
                    }

                    HtmlMenuOption option = menu.createOption(description);
                    option.setValue(oneEnum.toString());

                    if (enumerate != null && oneEnum.equals(enumerate)) {
                	option.setSelected(true);
                    }
                }
                
                menu.setConverter(new EnumConverter());
                menu.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
                
                return menu;
            }

            // TODO: refactor this, probably mode to HtmlMenu, duplicate id=menu.getDefaultTitle
            private String getDefaultTitle() {
                if (getDefaultText() == null) {
                    return RenderUtils.getResourceString("renderers.menu.default.title");
                }
                else {
                    if (isKey()) {
                        return RenderUtils.getResourceString(getBundle(), getDefaultText());
                    }
                    else {
                        return getDefaultText();
                    }
                }
            }
            
        };
    }
    
    private Collection<Object> getIncludedEnumValues(Class type) {
	final String valuesString = getIncludedValues();
	
	if (valuesString == null || valuesString.length() == 0) {
	    Object[] constants = type.getEnumConstants();
	    if (constants == null) {
                constants = type.getDeclaringClass().getEnumConstants();
            }
	    
	    return Arrays.asList(constants);
	}
	else {
	    return getEnumValues(type, valuesString);
	}
    }
    
    private Collection<Object> getExcludedEnumValues(Class type) {
	final String valuesString = getExcludedValues();
	
	if (valuesString == null || valuesString.length() == 0) {
	    return Collections.emptyList();
	}
	else {
	    return getEnumValues(type, valuesString);
	}
    }
    
    private Collection<Object> getEnumValues(Class type, String valuesString) {
	ArrayList<Object> result = new ArrayList<Object>();
	for (String part : valuesString.split(",")) {
	    result.add(Enum.valueOf(type, part.trim()));
	}

	return result;
    }
}
