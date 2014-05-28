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

<h2><bean:message key="label.managePeople.title" bundle="WEBSITEMANAGER_RESOURCES"/></h2>
<bean:define id="siteID" name="site" property="externalId"/>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="WEBSITEMANAGER_RESOURCES">
	<p>
		<span class="error0"><bean:write name="messages" /></span>
	</p>
	</html:messages>
</logic:messagesPresent>

<fr:view name="site" property="unit.researchContracts" schema="view.researchContract">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
		<fr:property name="columnClasses" value=",,acenter,,"/>
		<fr:property name="sortBy" value="beginDate"/>
		<fr:property name="key(remove)" value="label.remove" />
		<fr:property name="bundle(remove)" value="WEBSITEMANAGER_RESOURCES" />
		<fr:property name="link(remove)" value="<%= "/manageResearchUnitSite.do?method=removePerson&oid=" + siteID %>"/>
		<fr:property name="param(remove)" value="externalId/cid" />
		<fr:property name="order(remove)" value="2"/>
		<fr:property name="key(edit)" value="label.edit" />
		<fr:property name="bundle(edit)" value="DEFAULT" />
		<fr:property name="link(edit)" value="<%= "/manageResearchUnitSite.do?method=editContract&oid=" + siteID %>"/>
		<fr:property name="param(edit)" value="externalId/cid" />
		<fr:property name="order(edit)" value="1"/>
	</fr:layout>
</fr:view>

<bean:define id="schema" value="create.researchContract.internal" type="java.lang.String"/>

<logic:equal name="bean" property="externalPerson" value="true">
	<bean:define id="schema" value="create.researchContract.external" type="java.lang.String"/>
</logic:equal>

<logic:equal name="bean" property="showMessage" value="true">
	<div class="infoop2">
		<bean:message key="label.informationForCreateUser" bundle="RESEARCHER_RESOURCES"/>
	</div>
</logic:equal>

<fr:form action="<%= "/manageResearchUnitSite.do?method=addPersonWrapper&oid=" + siteID %>">
	<fr:edit id="createPersonContract" name="bean" schema="<%= schema %>" >
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
	<fr:destination name="invalid" path="/manageResearchUnitSite.do?method=managePeople"/>
	<fr:destination name="postBack" path="/manageResearchUnitSite.do?method=managePeoplePostBack"/>
	</fr:edit>
	<html:submit><bean:message key="label.submit" bundle="DEFAULT"/></html:submit>
	<%-- Descomentar quando existir solução para os convites externos
	<logic:equal name="bean" property="showMessage" value="true">
		<html:submit property="createPerson"><bean:message key="label.create" bundle="DEFAULT"/></html:submit>
	</logic:equal> 
	--%>
</fr:form>