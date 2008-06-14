<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.viewDelegates" bundle="DELEGATES_RESOURCES" /></h2>
	
<!-- DEGREE DELEGATES -->
<logic:present name="searchBean" >
	<fr:view name="searchBean" layout="tabular" schema="delegates.selectedDegreeTypeAndDegree">
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thlight thright tdleft mbottom05"/>
			<fr:property name="rowClasses" value="bold,bold,"/>
			<fr:property name="columnClasses" value="nowrap, width200px nowrap,"/>
		</fr:layout>
	</fr:view>
	
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