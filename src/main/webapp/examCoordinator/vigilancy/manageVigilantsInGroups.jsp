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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.vigilancy.manageVigilantsInGroups.title" bundle="VIGILANCY_RESOURCES"/></h2>

<ul>
	<li><html:link  page="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.back"/></html:link></li>
</ul>

<logic:notEmpty name="vigilants">
<div class="warning0">
	<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.unableToRemoveVigilantsDueToConvokes"/>
	<fr:view name="vigilants">
		<fr:layout>
			<fr:property name="eachLayout" value="values"/>
			<fr:property name="eachSchema" value="presentVigilant"/>
			<fr:property name="classes" value="mbottom05"/>
		</fr:layout>
	</fr:view>
</div>

</logic:notEmpty>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>

<fr:form action="/vigilancy/vigilantGroupManagement.do?method=addVigilantsToGroup">

<p class="mbottom05"><strong><bean:message key="label.vigilancy.manageDepartmentVigilants" bundle="VIGILANCY_RESOURCES"/></strong>:</p>
<fr:edit id="bounds" name="bounds">
<fr:layout name="vigilantsInGroup-render">
	<fr:property name="personSchema" value="presentPersonWithCatAndNumber"/>
	<fr:property name="classes" value="tstyle1 thlight"/>
</fr:layout>
<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"/>
</fr:edit>

<strong><bean:message key="label.vigilancy.manageExternalVigilants" bundle="VIGILANCY_RESOURCES"/></strong>:
<fr:edit id="externalBounds" name="externalBounds">
<fr:layout name="vigilantsInGroup-render">
	<fr:property name="personSchema" value="presentPersonWithCatAndNumber"/>
	<fr:property name="classes" value="tstyle1"/>

</fr:layout>

</fr:edit>
<html:submit><bean:message key="label.submit" bundle="VIGILANCY_RESOURCES"/></html:submit>
<html:cancel><bean:message key="label.cancel" bundle="VIGILANCY_RESOURCES"/></html:cancel>

</fr:form>

<p class="mbottom0"><em><bean:message key="label.vigilancy.label" bundle="VIGILANCY_RESOURCES"/>:</em></p>
<p class="mvert0"><em><bean:message key="label.vigilancy.convokable.abbr" bundle="VIGILANCY_RESOURCES"/> - <bean:message key="label.vigilancy.convokable" bundle="VIGILANCY_RESOURCES"/></em></p>
<p class="mtop0"><em><bean:message key="label.vigilancy.notConvokable.abbr" bundle="VIGILANCY_RESOURCES"/> - <bean:message key="label.vigilancy.notConvokable" bundle="VIGILANCY_RESOURCES"/></em></p>


<p class="mtop3 mbottom05"><strong><bean:message key="label.vigilancy.externalPersonToGroup" bundle="VIGILANCY_RESOURCES"/>:</strong></p>
<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="VIGILANCY_RESOURCES">
		<p class="mtop1 mvert05">
			<span class="error0"><bean:write name="messages" /></span>
		</p>
	</html:messages>
</logic:messagesPresent>


<fr:form id="addSingleVigilant" action="/vigilancy/vigilantGroupManagement.do?method=addVigilantToGroupByUsername">
<fr:edit name="bean" id="addExternalPersonToGroup" schema="addExternalPerson"
	nested="true">
	<fr:destination name="cancel" path="/vigilancy/vigilantGroupManagement.do?method=prepareVigilantGroupManagement&show=groups"/>
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight mtop05 thright"/>
		<fr:property name="columnClasses" value=",,tdclear"/>
	</fr:layout>
</fr:edit>
<p class="mtop0">
	<span class="switchInline"><a href="javascript:document.getElementById('addSingleVigilant').submit()"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.add"/></a></span>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="switchNone"><bean:message key="label.vigilancy.add" bundle="VIGILANCY_RESOURCES"/></html:submit>
</p>
</fr:form>

