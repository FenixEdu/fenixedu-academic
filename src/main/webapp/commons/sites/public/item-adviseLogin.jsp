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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<logic:present name="section">
   
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>

    <h2>
        <fr:view name="section" property="name" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
        <span class="permalink1">
        	(<app:contentLink name="section"><bean:message key="label.link" bundle="SITE_RESOURCES"/></app:contentLink>)            
        </span>
    </h2>

 	<logic:notEmpty name="section" property="orderedSubSections">
		<fr:view name="section" property="orderedSubSections" layout="list">
		    <fr:layout>
		        <fr:property name="eachLayout" value="values"/>
		        <fr:property name="eachSchema" value="site.section.name"/>
		    </fr:layout>
		    <fr:destination name="section.view" path="<%= actionName + "?method=section&amp;sectionID=${externalId}&amp;" + context %>"/>
		</fr:view>
    </logic:notEmpty>
    
    <bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>
            
	<h3 class="mtop2">
        <a name="<%= "item" + item.getExternalId() %>"></a>
        <fr:view name="item" property="name"/>
        <span class="permalink1">(<app:contentLink name="item"><bean:message key="label.link" bundle="SITE_RESOURCES"/></app:contentLink>)</span>
    </h3>

    <p>
        <em><bean:message key="message.item.view.mustLogin" bundle="SITE_RESOURCES"/></em>
        <%
            if (org.fenixedu.bennu.core.util.CoreConfiguration.casConfig().isCasEnabled()) {
        %>
         		<a href="<%= "https://barra.tecnico.ulisboa.pt/login?next=https://id.ist.utl.pt/cas/login?service=" + section.getFullPath() %>">
            		<bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
       			</a>.
		<%
			} else {
		%>
	            <html:link page="<%= String.format("%s?method=sectionWithLogin&amp;%s&amp;sectionID=%s", actionName, context, section.getExternalId()) %>">
    	            <bean:message key="link.section.view.login" bundle="SITE_RESOURCES"/>
        	   </html:link>.
       	<%
			}
       	%>
    </p>
</logic:present>