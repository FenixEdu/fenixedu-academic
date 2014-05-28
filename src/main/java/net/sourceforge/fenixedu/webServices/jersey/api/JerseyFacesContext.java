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
package net.sourceforge.fenixedu.webServices.jersey.api;

import java.util.Iterator;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.sun.faces.context.ExternalContextImpl;

public class JerseyFacesContext extends FacesContext {

    private ServletContext context;
    private ServletRequest request;
    private ServletResponse response;
    private UIViewRoot view;

    public JerseyFacesContext(ServletContext context, ServletRequest request, ServletResponse response) {
        super();
        this.context = context;
        this.request = request;
        this.response = response;
        this.view = new UIViewRoot();
        this.view.setLocale(Locale.getDefault());
        FacesContext.setCurrentInstance(this);
    }

    @Override
    public void addMessage(String arg0, FacesMessage arg1) {

    }

    @Override
    public Application getApplication() {
        return null;
    }

    @Override
    public Iterator getClientIdsWithMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExternalContext getExternalContext() {
        return new ExternalContextImpl(context, request, response);
    }

    @Override
    public Severity getMaximumSeverity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator getMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator getMessages(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RenderKit getRenderKit() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getRenderResponse() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getResponseComplete() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ResponseStream getResponseStream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseWriter getResponseWriter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UIViewRoot getViewRoot() {
        return view;
    }

    @Override
    public void release() {
        // TODO Auto-generated method stub

    }

    @Override
    public void renderResponse() {
        // TODO Auto-generated method stub

    }

    @Override
    public void responseComplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResponseStream(ResponseStream arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResponseWriter(ResponseWriter arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setViewRoot(UIViewRoot arg0) {
        // TODO Auto-generated method stub

    }

}
