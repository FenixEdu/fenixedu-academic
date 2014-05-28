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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="label.payments.searchPerson" bundle="MANAGER_RESOURCES"/></strong></h2>

<logic:messagesPresent message="true">
	<p>
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</p>
</logic:messagesPresent>

<fr:form action="/paymentsManagement.do?method=searchPerson">
	<fr:edit id="searchPersonBean" name="searchPersonBean" schema="SimpleSearchPersonWithStudentBean.edit">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thright thlight"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			<fr:destination name="invalid" path="/paymentsManagement.do?method=prepareSearchPersonInvalid"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.search">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.search"/>
	</html:submit>
</fr:form>

<logic:present name="persons">
	<logic:empty name="persons">
		<p>		
			<em><bean:message key="label.payments.noPersonsFound" bundle="MANAGER_RESOURCES"/></em>
		</p>
			
	</logic:empty>
	<logic:notEmpty name="persons">
		<fr:view name="persons" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
	        	<fr:property name="columnClasses" value="listClasses,,"/>
				<fr:property name="linkFormat(view)" value="/paymentsManagement.do?method=showOperations&personId=${externalId}" />
				<fr:property name="key(view)" value="label.view"/>
				<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
				<fr:property name="contextRelative(view)" value="true"/>	
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>


