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

<h2><bean:message key="label.findDelegates" bundle="DELEGATES_RESOURCES" /></h2>

<logic:present name="currentExecutionYear">
	<p class="mtop1 mbottom1"><b><bean:message key="label.executionYear" bundle="APPLICATION_RESOURCES" />:</b>
		<bean:write name="currentExecutionYear" property="year" /></p>
</logic:present>

<logic:present name="searchMethod">
	<ul>
		<li>
			<bean:define id="searchMethod" name="searchMethod" type="java.lang.String"/>
			<html:link page="<%="/findDelegates.do?method=prepare&amp;" + searchMethod + "=true" %>">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.back" />
			</html:link>
		</li>
	</ul>
</logic:present>

<p class="mtop2 mbottom05">
	<b><bean:message key="label.delegates.selectedCriteria" bundle="DELEGATES_RESOURCES" /></b></p>
	
<!-- DELEGATES SEARCHED BY DEGREE  -->
<logic:present name="searchByDegreeBean" >
	<fr:view name="searchByDegreeBean" layout="tabular" schema="delegates.selectedDegreeTypeAndDegree">
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright tdleft mtop0 mbottom05"/>
			<fr:property name="rowClasses" value="bold,bold,"/>
			<fr:property name="columnClasses" value="nowrap, width200px nowrap,"/>
		</fr:layout>
	</fr:view>
	
	<logic:present name="integratedMasterDegreeDelegate" >
		<p class="mtop2 mbottom1">
			<b><bean:message key="label.delegates.integratedMasterDegreeDelegate" bundle="DELEGATES_RESOURCES" /></b></p>	
		<bean:define id="delegateBean" name="integratedMasterDegreeDelegate" toScope="request" />
		<bean:define id="schema" value="delegates.searchDelegates.searchByDegree.delegateResume" type="java.lang.String" toScope="request" />
		<jsp:include page="delegatePhoto.jsp"/>
		<jsp:include page="searchedByDegreeDelegate.jsp"/>
	</logic:present>

	<logic:present name="masterDegreeDelegate" >
		<p class="mtop2 mbottom1">
			<b><bean:message key="label.delegates.masterDegreeDelegate" bundle="DELEGATES_RESOURCES" /></b></p>
		<bean:define id="delegateBean" name="masterDegreeDelegate" toScope="request" />
		<bean:define id="schema" value="delegates.searchDelegates.searchByDegree.delegateResume" type="java.lang.String" toScope="request" />
		<jsp:include page="delegatePhoto.jsp"/>
		<jsp:include page="searchedByDegreeDelegate.jsp"/>
	</logic:present>
	
	<logic:present name="degreeDelegate" >
		<p class="mtop2 mbottom1">
			<b><bean:message key="label.delegates.degreeDelegate" bundle="DELEGATES_RESOURCES" /></b></p>			
		<bean:define id="delegateBean" name="degreeDelegate" toScope="request" />
		<bean:define id="schema" value="delegates.searchDelegates.searchByDegree.delegateResume" type="java.lang.String" toScope="request" />
		<jsp:include page="delegatePhoto.jsp"/>
		<jsp:include page="searchedByDegreeDelegate.jsp"/>
	</logic:present>
	
	<logic:present name="yearDelegates" >
		<logic:notEmpty name="yearDelegates">
			<logic:iterate id="delegate" name="yearDelegates">
				<p class="mtop2 mbottom1 bold">
					<bean:message key="label.yearDelegatePreffixe" bundle="DELEGATES_RESOURCES" />
					<bean:write name="delegate" property="curricularYear.year" />
					<bean:message key="label.yearDelegateSuffixe" bundle="DELEGATES_RESOURCES" /></p>
				<bean:define id="delegateBean" name="delegate" toScope="request" />
				<bean:define id="schema" value="delegates.searchDelegates.searchByDegree.delegateResume" type="java.lang.String" toScope="request" />
				<jsp:include page="delegatePhoto.jsp"/>
				<jsp:include page="searchedByDegreeDelegate.jsp"/>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>
</logic:present>


<!-- DELEGATES SEARCHED BY NAME -->
<logic:present name="searchByNameBean">
	<fr:view name="searchByNameBean" layout="tabular" schema="delegates.selectedDelegate.name">
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright tdleft mtop0 mbottom05"/>
			<fr:property name="rowClasses" value="bold"/>
			<fr:property name="columnClasses" value="nowrap, width200px nowrap,"/>
		</fr:layout>
	</fr:view>

	<logic:present name="delegatesFound" >
		<logic:notEmpty name="delegatesFound">
			<p class="mtop2 mbottom1">
				<b><bean:message key="label.delegates.foundDelegates" bundle="DELEGATES_RESOURCES" /></b></p>	
			<logic:iterate id="delegate" name="delegatesFound">
				<bean:define id="delegateBean" name="delegate" toScope="request" />
				<bean:define id="schema" value="delegates.searchDelegates.searchByName.delegateResume" type="java.lang.String" toScope="request" />
				<jsp:include page="delegatePhoto.jsp"/>
				<jsp:include page="searchedByNameDelegate.jsp"/>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="delegatesFound">
			<p class="mtop2"><span class="error0">
				<b><bean:message key="error.delegates.notFoundDelegates" bundle="DELEGATES_RESOURCES" /></b></span></p>
		</logic:empty>
	</logic:present>
</logic:present>

<!-- DELEGATES SEARCHED BY NUMBER -->
<logic:present name="searchByNumberBean">
	<fr:view name="searchByNumberBean" layout="tabular" schema="delegates.selectedStudent.number">
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright tdleft mtop0 mbottom05"/>
			<fr:property name="rowClasses" value="bold"/>
			<fr:property name="columnClasses" value="nowrap, width200px nowrap,"/>
		</fr:layout>
	</fr:view>

	<logic:present name="delegatesFound" >
		<logic:notEmpty name="delegatesFound">
			<p class="mtop2 mbottom1">
				<b><bean:message key="label.delegates.foundStudent" bundle="DELEGATES_RESOURCES" /></b></p>	
			<bean:define id="delegateBean" name="searchByNumberBean" toScope="request" />
			<bean:define id="delegates" name="delegatesFound" toScope="request" />
			<bean:define id="delegateSchema" value="delegates.selectedStudent.studentInfo" type="java.lang.String" toScope="request" />
			<bean:define id="roleSchema" value="delegates.searchDelegates.searchByNumber.delegateResume" type="java.lang.String" toScope="request" />
			<jsp:include page="delegatePhoto.jsp"/>
			<jsp:include page="searchedByNumberDelegate.jsp"/>
		</logic:notEmpty>
		<logic:empty name="delegatesFound">
			<p class="mtop2"><span class="error0">
				<b><bean:message key="error.delegates.notFoundDelegates" bundle="DELEGATES_RESOURCES" /></b></span></p>
		</logic:empty>
	</logic:present>
</logic:present>

<!-- DELEGATES SEARCHED BY DELEGATE TYPE  -->
<logic:present name="searchByDelegateTypeBean">
	<% String selectedDelegateSchema = ""; %>
	<logic:present name="delegates">
		<% selectedDelegateSchema += "delegates.selectedDelegateType"; %>
	</logic:present>
	<logic:present name="yearDelegatesFound">
		<% selectedDelegateSchema += "delegates.selectedDelegateTypeAndYear"; %>
	</logic:present>
	<fr:view name="searchByDelegateTypeBean" layout="tabular" schema="<%= selectedDelegateSchema %>">
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright tdleft mtop0 mbottom05"/>
			<fr:property name="rowClasses" value="bold"/>
			<fr:property name="columnClasses" value="nowrap, width200px nowrap,"/>
		</fr:layout>
	</fr:view>
	
	<logic:present name="delegates" >
		<logic:notEmpty name="delegates">
			<p class="bold">
				<bean:message key="label.delegates.foundDelegates" bundle="DELEGATES_RESOURCES" /></p>
			<logic:iterate id="delegate" name="delegates">
				<bean:define id="delegateBean" name="delegate" toScope="request" />
				<bean:define id="schema" value="delegates.searchDelegates.searchByDelegateType.delegateResume" type="java.lang.String" toScope="request" />
				<jsp:include page="delegatePhoto.jsp"/>
				<jsp:include page="searchedByTypeDelegate.jsp"/>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="delegates">
			<p class="mtop2"><span class="error0">
				<b><bean:message key="error.delegates.notFoundDelegates" bundle="DELEGATES_RESOURCES" /></b></span></p>
		</logic:empty>
	</logic:present>

	<!-- YEAR DELEGATES  -->
	<logic:present name="yearDelegatesFound" >
		<logic:notEmpty name="yearDelegatesFound">
			<p class="bold mtop2 mbottom1">
				<bean:message key="label.yearDelegatesFoundPreffixe" bundle="DELEGATES_RESOURCES" />
				<bean:write name="searchByDelegateTypeBean" property="curricularYear.year" />
				<bean:message key="label.yearDelegatesFoundSuffixe" bundle="DELEGATES_RESOURCES" />
			</p>
			<logic:iterate id="delegate" name="yearDelegatesFound">
				<bean:define id="delegateBean" name="delegate" toScope="request" />
				<bean:define id="schema" value="delegates.searchDelegates.searchByDelegateType.delegateResume" type="java.lang.String" toScope="request" />
				<jsp:include page="delegatePhoto.jsp"/>
				<jsp:include page="searchedByTypeDelegate.jsp"/>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="yearDelegatesFound">
			<p class="mtop2"><span class="error0">
				<b><bean:message key="error.delegates.notFoundDelegates" bundle="DELEGATES_RESOURCES" /></b></span></p>
		</logic:empty>
	</logic:present>
</logic:present>