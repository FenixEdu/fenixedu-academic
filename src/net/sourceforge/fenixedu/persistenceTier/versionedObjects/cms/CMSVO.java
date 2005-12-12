/**
 * 
 */


package net.sourceforge.fenixedu.persistenceTier.versionedObjects.cms;


import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.cms.Cms;
import net.sourceforge.fenixedu.domain.cms.ICms;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.cms.IPersistentCMS;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 12:39:28,25/Out/2005
 * @version $Id$
 */
public class CMSVO extends VersionedObjectsBase implements IPersistentCMS
{

	private final static String propertiesFilename = "/cms.properties";

	private final static String defaultFenixCMSName = "FenixCMS";

	private static Properties properties;

	public ICms readFenixCMS() throws ExcepcaoPersistencia
	{
		ICms result = null;
		String fenixCMSName = CMSVO.defaultFenixCMSName;
		if (CMSVO.properties == null)
		{
			CMSVO.properties = new Properties();
			try
			{
				InputStream inputStream = getClass().getResourceAsStream(CMSVO.propertiesFilename);
				properties.load(inputStream);
				fenixCMSName = CMSVO.properties.getProperty("cms.instance.name");
				if (fenixCMSName == null)
				{
					fenixCMSName = CMSVO.defaultFenixCMSName;
				}
			}
			catch (Exception e)
			{
				// the default server
				fenixCMSName = CMSVO.defaultFenixCMSName;
			}
		}
		Collection<ICms> CMSs = readAll(Cms.class);
		for (ICms cms : CMSs)
		{
			if (cms.getName().equals(fenixCMSName))
			{
				result = cms;
				break;
			}
		}

		return result;
	}

	public ICms readCmsByName(String name) throws ExcepcaoPersistencia
	{
		ICms result = null;
		Collection<ICms> CMSs = readAll(Cms.class);
		for (ICms cms : CMSs)
		{
			if (cms.getName().equals(name))
			{
				result = cms;
				break;
			}
		}
		
		return result;
	}
}
