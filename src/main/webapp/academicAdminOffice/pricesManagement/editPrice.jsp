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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.pricesManagement.edit" /></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet">
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:hasMessages type="conversion">
	<ul class="nobullet">
	<fr:messages>
		<li><span class="error0"><fr:message/></span></li>
	</fr:messages>
	</ul>
</fr:hasMessages>


<p class="mtop2 mbottom05">
<strong><bean:message name="postingRule" property="eventType.qualifiedName" bundle="ENUMERATION_RESOURCES"/></strong>
</p>
<bean:define id="postingRuleClassName" name="postingRule" property="class.simpleName" />

<logic:equal value="PartialRegistrationRegimeRequestPR" name="postingRuleClassName">
	<fr:form>
		<fr:edit name="executionYearBean" id="executionYearBean" >
			<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.pricesManagement.PricesManagementDispatchAction$ExecutionYearBean">
				<fr:slot name="executionYear" key="label.net.sourceforge.fenixedu.domain.accounting.postingRules.PartialRegistrationRegimeRequestPR.executionYear" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsForAcademicServiceRequestProvider" />
					<fr:property name="format" value="${year}" />
					<fr:property name="postback" value="postback" />
				</fr:slot>		
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thmiddle thright thlight mtop05" />
				<fr:property name="columnClasses" value=",,tdclear" />
			</fr:layout>
			
			<fr:destination name="postback" path="/pricesManagement.do?method=changeExecutionYearPostback" />
		</fr:edit>
	</fr:form>
</logic:equal>

<fr:edit name="postingRule" 
		 schema="<%=postingRuleClassName + ".edit"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thmiddle thright thlight mtop05" />
		<fr:property name="columnClasses" value=",,tdclear" />
	</fr:layout>
	<fr:destination name="cancel" path="/pricesManagement.do?method=viewPrices"/>
	<fr:destination name="success" path="/pricesManagement.do?method=viewPrices"/>
</fr:edit>
