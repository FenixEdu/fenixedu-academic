/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.tiles.plugin;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.tiles.DefinitionsFactoryConfig;
import org.apache.struts.tiles.TilesPlugin;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class FenixTilesPlugin extends TilesPlugin {

    @Override
    protected DefinitionsFactoryConfig readFactoryConfig(ActionServlet servlet, ModuleConfig config) throws ServletException {
	final DefinitionsFactoryConfig factoryConfig = super.readFactoryConfig(servlet, config);
	factoryConfig.setAttribute("defaultTileDefinition", this.currentPlugInConfigObject.getProperties().get(
		"defaultTileDefinition"));
	return factoryConfig;
    }

}
