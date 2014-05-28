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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<html:xhtml/>

<h2><bean:message key="label.publicRelationOffice" bundle="APPLICATION_RESOURCES"/></h2>


<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
	<p>
		<span class="error0"><bean:write name="messages" /></span>
	</p>
	</html:messages>
</logic:messagesPresent>
<br>
<bean:message key="label.insertUsername" bundle="APPLICATION_RESOURCES"/>

<fr:form action="/managePublicRelationsPeople.do?method=addPersonManager">
	<fr:edit id="addPerson" name="bean"> 
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice.PersonBean">
			<fr:slot name="username" key="label.identificationNumber">
			</fr:slot>
		</fr:schema> 
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="label.submit"/></html:submit>
</fr:form>
<br>
<br>
<fr:view name="persons" schema="unitSite.manager">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 thlight mtop05"/>
            <fr:property name="link(delete)" value="/managePublicRelationsPeople.do?method=removeManager"/>
            <fr:property name="param(delete)" value="externalId/managerID"/>
            <fr:property name="key(delete)" value="link.unitSite.managers.remove"/>
            <fr:property name="bundle(delete)" value="SITE_RESOURCES"/>
        </fr:layout>
</fr:view>
