<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="siteId" name="site" property="externalId"/>

<h2>
	<bean:message key="label.section"/>
	<fr:view name="section" property="name" />
</h2>

<logic:notEmpty name="section" property="childrenSections">
	<p class="mtop15">
	    <span class="warning0">
	        <bean:message key="message.section.subSection.count" bundle="SITE_RESOURCES" 
	                      arg0="<%= String.valueOf(section.getChildrenSections().size()) %>"/>
	    </span>
    </p>
</logic:notEmpty>

<logic:notEmpty name="section" property="childrenItems">
	<p class="mtop15">
	    <span class="warning0">
	        <bean:message key="message.section.items.count" bundle="SITE_RESOURCES"
	                      arg0="<%= String.valueOf(section.getChildrenItems().size()) %>"/>
	    </span>
    </p>
</logic:notEmpty>

<fr:form action="<%= String.format("%s?method=confirmSectionDelete&%s&sectionID=%s", actionName, context, section.getExternalId()) %>">
    <p class="mtop15">
        <bean:message key="message.section.delete.confirm" bundle="SITE_RESOURCES"/>
    </p>
    
    <p class="mtop1">
	    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
	        <bean:message key="button.confirm" bundle="SITE_RESOURCES"/>
	    </html:submit>
	    
	    <html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" property="cancel">
	        <bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
	    </html:cancel>
    </p>
</fr:form>
