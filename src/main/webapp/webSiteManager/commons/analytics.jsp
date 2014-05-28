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
<%@ page import="org.apache.struts.util.RequestUtils" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.unitSite.analytics" bundle="SITE_RESOURCES"/>
</h2>

<div class="infoop2">
    <p class="mvert0">
    	<bean:message key="message.site.analytics" bundle="SITE_RESOURCES" arg0="${site.fullPath}"/>
   	</p>
</div>

<logic:present name="codeAccepted">
	<p>
		<span class="success0">
			<bean:message key="message.site.analytics.code.accepted" bundle="SITE_RESOURCES"/>.
		</span>
	</p>
</logic:present>

<logic:present name="codeRemoved">
	<p>
		<span class="success0">
			<bean:message key="message.site.analytics.code.removed" bundle="SITE_RESOURCES"/>.
		</span>
	</p>
</logic:present>

<fr:form action="<%= String.format("%s?method=analytics&amp;%s", actionName, context) %>">
	<fr:edit id="siteAnalytics" name="site" schema="unitSite.analytics.code">
		<fr:layout>
		    <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
		    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:edit>
	
	<html:submit>
		<bean:message key="button.submit"/>
	</html:submit>
</fr:form>
