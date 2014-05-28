/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.util.struts;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleUtils;

import pt.utl.ist.fenix.tools.resources.AbstractMessageResourceProvider;

public class StrutsMessageResourceProvider extends AbstractMessageResourceProvider {

    private Locale locale;

    private HttpServletRequest request;

    private ServletContext servletContext;

    public StrutsMessageResourceProvider(Properties properties, Locale locale, ServletContext servletContext,
            HttpServletRequest request) {
        super(properties);
        this.locale = locale;
        this.servletContext = servletContext;
        this.request = request;
    }

    public StrutsMessageResourceProvider(Locale locale, ServletContext servletContext, HttpServletRequest request) {
        super();
        this.locale = locale;
        this.servletContext = servletContext;
        this.request = request;
    }

    private MessageResources getMessageResources(String bundleName) {
        // Identify the current module
        ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(this.request, this.servletContext);

        return (bundleName != null && bundleName.length() > 0) ? (MessageResources) this.servletContext.getAttribute(bundleName
                + moduleConfig.getPrefix()) : (MessageResources) this.servletContext.getAttribute(Globals.MESSAGES_KEY
                + moduleConfig.getPrefix());

    }

    @Override
    public String getMessage(String bundle, String key, String... args) {
        return format(getMessageResources(getBundleMapping(bundle)).getMessage(this.locale, key), args);
    }
}
