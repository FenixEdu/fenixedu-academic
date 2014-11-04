<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>

<h2><bean:message key="title.unitSite.manage.sites" bundle="MANAGER_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
	<p class="error0">
		<bean:write name="messages" />
	</p>
	</html:messages>
</logic:messagesPresent>

<bean:define id="siteOid" name="siteOid"></bean:define>
<fr:form action="<%="/unitSiteManagement.do?method=createEntryPoint&oid="+siteOid%>"> 
	
	<fr:edit id="multiLanguageStringBean" name="multiLanguageStringBean"> 
	 	<fr:schema bundle="SITE_RESOURCES" type="org.fenixedu.academic.dto.VariantBean"> 
			<fr:slot name="MLString" key="label.name"></fr:slot>
	 	</fr:schema>
	</fr:edit>
	
	<html:submit> 
		<bean:message key="label.submit"/>
	</html:submit>
</fr:form>
