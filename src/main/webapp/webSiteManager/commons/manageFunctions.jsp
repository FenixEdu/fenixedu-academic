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
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<h2>
    <bean:message key="title.site.manage.functions" bundle="SITE_RESOURCES"/>
</h2>

<ul>
	<li>
		<html:link page="<%= String.format("%s?method=manageExistingFunctions&amp;%s", actionName, context) %>">
			<bean:message key="link.site.manage.functions" bundle="SITE_RESOURCES"/>
		</html:link>
	</li>
</ul>

<logic:notEmpty name="people">
	<fr:view name="people" schema="site.functions.person.table">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 tdtop thleft mtop2"/>
	
			<fr:property name="link(manage)" value="<%= String.format("%s?method=managePersonFunctions&amp;%s", actionName, context) %>"/>
			<fr:property name="param(manage)" value="person.externalId/personID"/>
			<fr:property name="key(manage)" value="site.functions.person.edit"/>
			<fr:property name="bundle(manage)" value="SITE_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="people">
	<em><bean:message key="message.site.manage.functions.people.empty" bundle="SITE_RESOURCES"/></em>
</logic:empty>

<div class="mtop2">
	<p class="mbottom05">
	    <strong><bean:message key="title.site.functions.person.add" bundle="SITE_RESOURCES"/></strong>
    </p>
    
    <p class="mvert05">
        <em><bean:message key="message.site.functions.person.add" bundle="SITE_RESOURCES"/></em>
    </p>

	<logic:messagesPresent message="true" property="addPersonError">
	    <html:messages id="message" message="true" property="addPersonError" bundle="SITE_RESOURCES">
	        <p><span class="error0"><bean:write name="message"/></span></p>
	    </html:messages>
	</logic:messagesPresent>
	
    <fr:form action="<%= String.format("%s?method=managePersonFunctions&amp;%s", actionName, context) %>">
        <fr:edit id="addUserBean" name="addUserBean" schema="site.functions.addPerson">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdmiddle thlight thright thmiddle mbottom0"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="<%= String.format("%s?method=manageFunctions&amp;%s", actionName, context) %>"/>
        </fr:edit>

        <html:submit styleClass="mtop05">
            <bean:message key="button.add" bundle="SITE_RESOURCES"/> 
        </html:submit>
    </fr:form>
</div>
