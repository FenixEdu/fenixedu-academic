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

<h2><bean:message key="label.viewDelegates" bundle="DELEGATES_RESOURCES" /></h2>
	
<logic:present name="searchByDegreeBean" >
	<fr:form action="/delegatesInfo.do?method=showAll&amp;showBackLink=true">
		<fr:edit id="searchByDegreeBean" name="searchByDegreeBean" layout="tabular-editable" schema="student.searchDelegates.selectDegreeTypeAndDegree">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright"/>
			</fr:layout>
			<fr:destination name="post-back-executionyear" path="/delegatesInfo.do?method=updateExecutionYear" />
			<fr:destination name="post-back-degree" path="/delegatesInfo.do?method=updateDelegates" />
			<fr:destination name="post-back-degree-type" path="/delegatesInfo.do?method=updateDegreeType" />
		</fr:edit>
	</fr:form>
</logic:present>

<!-- DEGREE DELEGATES -->
<logic:present name="searchBean" >
	
	<logic:present name="integratedMasterDegreeDelegate" >
		<p class="mtop15 mbottom1">
			<b><bean:message key="label.delegates.integratedMasterDegreeDelegate" bundle="DELEGATES_RESOURCES" /></b></p>	
		<bean:define id="delegateBean" name="integratedMasterDegreeDelegate" toScope="request" />
		<bean:define id="schema" value="delegates.searchDelegates.searchByDegree.restrictedDelegateResume" type="java.lang.String" toScope="request" />
		<jsp:include page="../../commons/delegates/delegatePhoto.jsp"/>
		<jsp:include page="../../commons/delegates/searchedByDegreeDelegate.jsp"/>
	</logic:present>
	
	<logic:present name="masterDegreeDelegate" >
		<p class="mtop15 mbottom1">
			<b><bean:message key="label.delegates.masterDegreeDelegate" bundle="DELEGATES_RESOURCES" /></b></p>
		<bean:define id="delegateBean" name="masterDegreeDelegate" toScope="request" />
		<bean:define id="schema" value="delegates.searchDelegates.searchByDegree.restrictedDelegateResume" type="java.lang.String" toScope="request" />
		<jsp:include page="../../commons/delegates/delegatePhoto.jsp"/>
		<jsp:include page="../../commons/delegates/searchedByDegreeDelegate.jsp"/>
	</logic:present>
	
	<logic:present name="degreeDelegate" >
		<p class="mtop15 mbottom1">
			<b><bean:message key="label.delegates.degreeDelegate" bundle="DELEGATES_RESOURCES" /></b></p>			
		<bean:define id="delegateBean" name="degreeDelegate" toScope="request" />
		<bean:define id="schema" value="delegates.searchDelegates.searchByDegree.restrictedDelegateResume" type="java.lang.String" toScope="request" />
		<jsp:include page="../../commons/delegates/delegatePhoto.jsp"/>
		<jsp:include page="../../commons/delegates/searchedByDegreeDelegate.jsp"/>
	</logic:present>
	
	<logic:present name="yearDelegates" >
		<logic:notEmpty name="yearDelegates">
			<logic:iterate id="delegate" name="yearDelegates">
				<p class="mtop15 mbottom1 bold">
					<bean:message key="label.yearDelegatePreffixe" bundle="DELEGATES_RESOURCES" />
					<bean:write name="delegate" property="curricularYear.year" />
					<bean:message key="label.yearDelegateSuffixe" bundle="DELEGATES_RESOURCES" /></p>
				<bean:define id="delegateBean" name="delegate" toScope="request" />
				<bean:define id="schema" value="delegates.searchDelegates.searchByDegree.restrictedDelegateResume" type="java.lang.String" toScope="request" />
				<jsp:include page="../../commons/delegates/delegatePhoto.jsp"/>
				<jsp:include page="../../commons/delegates/searchedByDegreeDelegate.jsp"/>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>
</logic:present>

<logic:notPresent name="searchBean">
		<p class="mvert15"><em><bean:message key="message.no.delegates" bundle="DELEGATES_RESOURCES"/></em></p>
</logic:notPresent>

<!-- GGAE DELEGATES -->
<logic:present name="ggaeDelegates" >
	<logic:notEmpty name="ggaeDelegates">
		<p class="mtop2 bold">
			<bean:message key="label.delegates.ggaeDelegates" bundle="DELEGATES_RESOURCES" /></p>
		<logic:iterate id="delegate" name="ggaeDelegates">
			<bean:define id="delegateBean" name="delegate" toScope="request" />
			<bean:define id="schema" value="delegates.searchDelegatesForStudent.delegateResume" type="java.lang.String" toScope="request" />
			<jsp:include page="../../commons/delegates/delegatePhoto.jsp"/>
			<jsp:include page="../../commons/delegates/searchedByTypeDelegate.jsp"/>
		</logic:iterate>
		
		<p class="mtop2">
			<b><bean:message key="label.delegate.ggae.groupA" bundle="DELEGATES_RESOURCES" /></b>:
			<bean:message key="label.delegate.ggae.groupA.description" bundle="DELEGATES_RESOURCES" />
		</p>
		<p class="mtop05">
			<b><bean:message key="label.delegate.ggae.groupB" bundle="DELEGATES_RESOURCES" /></b>:
			<bean:message key="label.delegate.ggae.groupB.description" bundle="DELEGATES_RESOURCES" />
		</p>
		<p class="mtop05">
			<b><bean:message key="label.delegate.ggae.groupC" bundle="DELEGATES_RESOURCES" /></b>:
			<bean:message key="label.delegate.ggae.groupC.description" bundle="DELEGATES_RESOURCES" />
		</p>
		<p class="mtop05">
			<b><bean:message key="label.delegate.ggae.groupD" bundle="DELEGATES_RESOURCES" /></b>:
			<bean:message key="label.delegate.ggae.groupD.description" bundle="DELEGATES_RESOURCES" />
		</p>
		<p class="mtop05">
			<b><bean:message key="label.delegate.ggae.groupE" bundle="DELEGATES_RESOURCES" /></b>:
			<bean:message key="label.delegate.ggae.groupE.description" bundle="DELEGATES_RESOURCES" />
		</p>
		<p class="mtop05">
			<b><bean:message key="label.delegate.ggae.groupF" bundle="DELEGATES_RESOURCES" /></b>:
			<bean:message key="label.delegate.ggae.groupF.description" bundle="DELEGATES_RESOURCES" />
		</p>
	</logic:notEmpty>
</logic:present>

