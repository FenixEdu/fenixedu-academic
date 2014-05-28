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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="label.delegatesManagement.GGAE" bundle="PEDAGOGICAL_COUNCIL" /></h2>
<h3><bean:message key="label.delegatesManagement.GGAE.subTitle" bundle="PEDAGOGICAL_COUNCIL" /></h3>

<logic:present name="currentExecutionYear">
	<p class="mtop1 mbottom1"><b><bean:message key="label.executionYear" bundle="PEDAGOGICAL_COUNCIL" />:</b>
		<bean:write name="currentExecutionYear" property="year" /></p>
</logic:present>

<!-- AVISOS E ERROS -->
<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="PEDAGOGICAL_COUNCIL">
		<p><span class="error"><bean:write name="message" /></span></p>
	</html:messages>
</logic:messagesPresent>

<!-- DELEGATES PRESENTATION  -->

<logic:present name="ggaeDelegates">
	<p class="mtop2 mbottom1">
			<b><bean:message key="label.delegates.ggaeDelegates" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	
	<fr:view name="ggaeDelegates" schema="delegates.ggaeDelegates">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0"/>
			<fr:property name="columnClasses" value="aleft,,width200px nowrap aleft,width200px nowrap aleft,aleft"/>
			<fr:property name="link(add)" value="/delegatesManagement.do?method=prepareAddGGAEDelegate" />
			<fr:property name="param(add)" value="ggaeDelegateFunction.externalId/selectedGgaeFunction"/>
			<fr:property name="key(add)" value="link.delegates.addDelegate"/>
			<fr:property name="bundle(add)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIfNot(add)" value="hasGgaeDelegate"/>
			<fr:property name="link(remove)" value="/delegatesManagement.do?method=removeGGAEDelegate" />
			<fr:property name="param(remove)" value="ggaeDelegate.externalId/selectedDelegate,ggaeDelegateFunction.externalId/selectedGgaeFunction"/>
			<fr:property name="key(remove)" value="link.delegates.removeDelegate"/>
			<fr:property name="bundle(remove)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(remove)" value="hasGgaeDelegate"/>
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="choosePersonBean" >
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.delegates.createEditGGAEDelegates.person" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	<p class="color888 mvert05">
		<bean:message key="label.delegates.createEditGGAEDelegates.person.help" bundle="PEDAGOGICAL_COUNCIL" /></p>
		
	<fr:form action="/delegatesManagement.do?method=prepareAddGGAEDelegate">
		<fr:edit id="delegateBean" name="choosePersonBean" layout="tabular-editable" schema="delegates.createEditGGAEDelegate.insertPersonUsername">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright mtop0 mbottom0"/>
				<fr:property name="columnClasses" value="width100px,width200px,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/delegatesManagement.do?method=prepareAddGGAEDelegate" />
			<fr:destination name="cancel" path="/delegatesManagement.do?method=prepareViewGGAEDelegates" />
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width200px">
					<html:submit><bean:message key="button.delegates.submit" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
					<html:cancel><bean:message key="button.delegates.cancel" bundle="PEDAGOGICAL_COUNCIL"/></html:cancel>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>

<logic:present name="confirmPersonBean" >
	<p class="mtop15 mbottom05">
		<span class="warning0"><bean:message key="label.delegates.createEditGGAEDelegates.confirmPerson" bundle="PEDAGOGICAL_COUNCIL" /></span></p>
	
	<fr:form action="/delegatesManagement.do?method=addGGAEDelegate">
		<fr:edit id="delegateBean" name="confirmPersonBean" layout="tabular-editable" schema="delegates.createEditGGAEDelegate.confirmPerson">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright mtop0 mbottom0"/>
				<fr:property name="columnClasses" value="width100px,width250px,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="cancel" path="/delegatesManagement.do?method=prepareViewGGAEDelegates" />
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width250px">
					<html:submit><bean:message key="button.delegates.addDelegate" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
					<html:cancel><bean:message key="button.delegates.cancel" bundle="PEDAGOGICAL_COUNCIL"/></html:cancel>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>

<p class="mtop2">
		<b><bean:message key="label.delegate.ggae.groupA" bundle="DELEGATES_RESOURCES" />:</b>
		<bean:message key="label.delegate.ggae.groupA.description" bundle="DELEGATES_RESOURCES" />
	</p>
	<p class="mtop05">
		<b><bean:message key="label.delegate.ggae.groupB" bundle="DELEGATES_RESOURCES" />:</b>
		<bean:message key="label.delegate.ggae.groupB.description" bundle="DELEGATES_RESOURCES" />
	</p>
	<p class="mtop05">
		<b><bean:message key="label.delegate.ggae.groupC" bundle="DELEGATES_RESOURCES" />:</b>
		<bean:message key="label.delegate.ggae.groupC.description" bundle="DELEGATES_RESOURCES" />
	</p>
	<p class="mtop05">
		<b><bean:message key="label.delegate.ggae.groupD" bundle="DELEGATES_RESOURCES" />:</b>
		<bean:message key="label.delegate.ggae.groupD.description" bundle="DELEGATES_RESOURCES" />
	</p>
	<p class="mtop05">
		<b><bean:message key="label.delegate.ggae.groupE" bundle="DELEGATES_RESOURCES" />:</b>
		<bean:message key="label.delegate.ggae.groupE.description" bundle="DELEGATES_RESOURCES" />
	</p>
	<p class="mtop05">
		<b><bean:message key="label.delegate.ggae.groupF" bundle="DELEGATES_RESOURCES" />:</b>
		<bean:message key="label.delegate.ggae.groupF.description" bundle="DELEGATES_RESOURCES" />
	</p>



