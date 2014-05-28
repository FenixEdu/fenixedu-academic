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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.delegatesManagement" bundle="PEDAGOGICAL_COUNCIL" /></h2>

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

<p>
	<html:link page="/delegatesManagement.do?method=exportToXLS">
		<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
		<bean:message key="link.delegates.exportToXLS" bundle="PEDAGOGICAL_COUNCIL"/>
	</html:link>
</p>

<logic:present name="delegateBean" >
		<p class="mtop15 mbottom05"><b><bean:message key="label.delegates.createEditDelegates.selectDegree" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
			
		<fr:form action="/delegatesManagement.do?method=prepareSelectDegree">
			<fr:edit id="delegateBean" name="delegateBean" layout="tabular-editable" schema="delegates.selectDegreeTypeAndDegree">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft mtop05 mbottom0"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/delegatesManagement.do?method=prepare" />
				<fr:destination name="post-back-degree-type" path="/delegatesManagement.do?method=prepare" />
				<fr:destination name="post-back-degree" path="/delegatesManagement.do?method=prepareSelectDegree" />
			</fr:edit>
		</fr:form>
</logic:present>

<!-- DELEGATES PRESENTATION  -->

<logic:present name="delegates">
	<p class="mtop2 mbottom1">
		<b><bean:message key="label.delegates.createEditDelegates.delegatesFromDegree" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
	
	<fr:view name="delegates" schema="delegates.delegatesFromDegree">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight tdcenter mtop0"/>
			<fr:property name="columnClasses" value="aleft,,,width200px nowrap aleft,width200px nowrap aleft,aleft"/>
			<%-- 
			<fr:property name="link(addDelegate)" value="/delegatesManagement.do?method=prepareAddDelegate" />
			<fr:property name="param(addDelegate)" value="degree.externalId/selectedDegree,delegateType.name/delegateType"/>
			<fr:property name="key(addDelegate)" value="link.delegates.addDelegate"/>
			<fr:property name="bundle(addDelegate)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(addDelegate)" value="emptyDelegateBean"/>
--%>
			<fr:property name="link(addYearDelegate)" value="/delegatesManagement.do?method=prepareAddDelegate" />
			<fr:property name="param(addYearDelegate)" value="degree.externalId/selectedDegree,curricularYear.year/selectedYear,delegateType.name/delegateType"/>
			<fr:property name="key(addYearDelegate)" value="link.delegates.addDelegate"/>
			<fr:property name="bundle(addYearDelegate)" value="PEDAGOGICAL_COUNCIL"/>
			<%--<fr:property name="visibleIfNot(addYearDelegate)" value="emptyYearDelegateBeanWithElection"/>
			 --%>

			<fr:property name="link(finishRole)" value="/delegatesManagement.do?method=prepareFinishRole" />
			<fr:property name="param(finishRole)" value="personFunction.externalId/delegateOID"/>
			<fr:property name="key(finishRole)" value="link.delegates.finishRole"/>
			<fr:property name="bundle(finishRole)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(finishRole)" value="hasDelegate"/>
			
			<fr:property name="link(viewResults)" value="/delegatesManagement.do?method=prepareViewResults" />
			<fr:property name="param(viewResults)" value="degree.externalId/selectedDegree,curricularYear.year/selectedYear"/>
			<fr:property name="key(viewResults)" value="link.delegates.viewResults"/>
			<fr:property name="bundle(viewResults)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(viewResults)" value="emptyYearDelegateBeanWithElection"/>
			<%--
			<fr:property name="link(change)" value="/delegatesManagement.do?method=prepareChangeDelegate"/>
			<fr:property name="param(change)" value="delegate.externalId/selectedDelegate"/>
			<fr:property name="key(change)" value="link.delegates.changeDelegate"/>
			<fr:property name="bundle(change)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(change)" value="yearDelegateBeanWithElectedElection"/>

			<fr:property name="link(remove)" value="/delegatesManagement.do?method=removeDelegate" />
			<fr:property name="param(remove)" value="personFunction.externalId/delegateOID"/>
			<fr:property name="key(remove)" value="link.delegates.removeDelegate"/>
			<fr:property name="bundle(remove)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(remove)" value="hasDelegate"/>
			 --%>
		</fr:layout>
	</fr:view>
</logic:present>

<logic:present name="editDelegateBean">
	<fr:edit id="editDelegateBean" name="editDelegateBean" action="/delegatesManagement.do?method=finishRole">
		<fr:schema bundle="PEDAGOGICAL_COUNCIL" type="net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.delegates.DelegateBean">
			<fr:slot name="delegateType" key="label.functionType" readOnly="true" />
			<logic:equal name="editDelegateBean" property="delegateType" value="<%= net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType.DELEGATE_OF_YEAR.toString()%>">
				<fr:slot name="curricularYear.year"  key="label.curricularYear" layout="null-as-label" readOnly="true" />
			</logic:equal>
			<fr:slot name="studentNumber" key="label.studentNumber" layout="null-as-label"  readOnly="true"/>
			<fr:slot name="studentName" key="label.name" layout="null-as-label"  readOnly="true"/>
			<fr:slot name="personFunction.beginDate" key="label.startDate" readOnly="true">
				<fr:property name="format" value="${dayOfMonth,02d}/${monthOfYear,02d}/${year}"></fr:property>
			</fr:slot>
			<fr:slot name="personFunctionNewEndDate" key="label.endDate"/>
		</fr:schema>
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thleft mtop05 mbottom0"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</logic:present>

<logic:present name="newDelegateBean">
	<fr:form action="/delegatesManagement.do?method=addDelegate">
		<fr:edit id="delegateBean" name="delegateBean" visible="false" />
		<fr:edit id="newDelegateBean" name="newDelegateBean" schema="delegate.insertNewDelegate">
			<fr:destination name="invalid" path="/delegatesManagement.do?method=prepareViewDelegates" />
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom0" />
				<fr:property name="columnClasses" value="width100px,width150px,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width150px">
					<html:submit><bean:message key="button.delegates.addDelegate" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>

<logic:present name="newYearDelegateBean">
	<fr:form action="/delegatesManagement.do?method=addYearDelegate">
		<fr:edit id="delegateBean" name="delegateBean" visible="false" />
		<fr:edit id="newYearDelegateBean" name="newYearDelegateBean" schema="delegate.insertNewYearDelegate">
			<fr:destination name="invalid" path="/delegatesManagement.do?method=prepareViewDelegates" />
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop0 mbottom0" />
				<fr:property name="columnClasses" value="width100px,width150px,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<table class="tstyle5 gluetop mtop0">
			<tr>
				<td class="width100px"></td>
				<td class="width150px">
					<html:submit><bean:message key="button.delegates.addDelegate" bundle="PEDAGOGICAL_COUNCIL"/></html:submit>
				</td>
			</tr>
		</table>
	</fr:form>
</logic:present>