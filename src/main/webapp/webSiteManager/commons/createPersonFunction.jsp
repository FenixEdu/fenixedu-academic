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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<html:xhtml/>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="unit" name="site" property="unit" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="personId" name="person" property="externalId"/>
<bean:define id="functionId" name="function" property="externalId"/>

<h2>
    <bean:message key="title.site.manage.functions" bundle="SITE_RESOURCES"/>
</h2>

<div>
	<strong>
		<bean:message key="title.site.manage.functions.createPersonFunction" bundle="SITE_RESOURCES"/>
		<fr:view name="function" property="name"/>
	</strong>
</div>

<logic:messagesPresent message="true">
    <div class="mvert15">
        <span class="error0">
            <html:messages id="error" message="true" bundle="SITE_RESOURCES"> 
                <bean:write name="error"/>
            </html:messages>
        </span>
    </div>
</logic:messagesPresent>

<%
	org.joda.time.YearMonthDay today = new org.joda.time.YearMonthDay();
	request.setAttribute("today", today);
%>

<fr:form action="<%= String.format("%s?method=managePersonFunctions&%s&personID=%s", actionName, context, personId) %>">
	<fr:create id="createPersonFunction" type="net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction" schema="site.functions.person.personFunction.create">
		<fr:layout name="tabular">
		    <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
		    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>

		<fr:hidden slot="parentParty" name="function" property="unit"/>
		<fr:hidden slot="childParty" name="person"/>
		<fr:hidden slot="accountabilityType" name="function"/>
		
		<fr:default slot="beginDate" name="today"/>
		
		<fr:destination name="cancel" path="<%= String.format("%s?method=managePersonFunctions&%s&personID=%s", actionName, context, personId) %>"/>
		<fr:destination name="invalid" path="<%= String.format("%s?method=addPersonFunction&%s&personID=%s&functionID=%s", actionName, context, personId, functionId) %>"/>
		<fr:destination name="exception" path="<%= String.format("%s?method=addPersonFunction&%s&personID=%s&functionID=%s", actionName, context, personId, functionId) %>"/>
	</fr:create>
	
	<html:submit>
		<bean:message key="button.submit" bundle="SITE_RESOURCES"/>
	</html:submit>
	<html:cancel>
		<bean:message key="button.cancel" bundle="SITE_RESOURCES"/>
	</html:cancel>
</fr:form>
