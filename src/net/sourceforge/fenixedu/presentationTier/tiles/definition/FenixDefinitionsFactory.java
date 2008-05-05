/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.tiles.definition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.apache.struts.tiles.ComponentDefinition;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.NoSuchDefinitionException;
import org.apache.struts.tiles.xmlDefinition.I18nFactorySet;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class FenixDefinitionsFactory extends I18nFactorySet {

    private ComponentDefinition defaultModuleDefinition;

    private String defaultModuleDefinitionName;

    private Map<String, ComponentDefinition> definitionsCache = new HashMap<String, ComponentDefinition>();

    static private Set<String> definitionsToProcessNames = new HashSet<String>();

    static public void addDefinitionName(String name) {
	definitionsToProcessNames.add(name);
    }

    @Override
    public void initFactory(ServletContext servletContext, Map properties) throws DefinitionsFactoryException {
	this.defaultModuleDefinitionName = (String) properties.get("defaultTileDefinition");
	super.initFactory(servletContext, properties);

    }

    @Override
    public ComponentDefinition getDefinition(String name, ServletRequest request, ServletContext servletContext)
	    throws NoSuchDefinitionException, DefinitionsFactoryException {

	if (definitionsToProcessNames.contains(name)) {

	    // init default definition
	    if (defaultModuleDefinition == null) {
		if (defaultModuleDefinitionName != null) {
		    defaultModuleDefinition = super.getDefinition(defaultModuleDefinitionName, request, servletContext);
		} else {
		    return null;
		}
	    }

	    Set<String> processedTiles = (Set<String>) request.getAttribute("__processedTiles");
	    if (processedTiles == null) {
		processedTiles = new HashSet<String>();
		request.setAttribute("__processedTiles", processedTiles);
	    } else if (processedTiles.contains(name)) {
		return null;
	    }
	    processedTiles.add(name);

	    if (definitionsCache.containsKey(name)) {
		return definitionsCache.get(name);
	    }

	    ComponentDefinition componentDefinition = new ComponentDefinition(defaultModuleDefinition);
	    componentDefinition.putAttribute("body", name);
	    definitionsCache.put(name, componentDefinition);
	    return componentDefinition;
	}

	return super.getDefinition(name, request, servletContext);
    }

}
