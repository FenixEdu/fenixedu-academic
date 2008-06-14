<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
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

<logic:present name="delegateBean" >
		<p class="mtop15 mbottom05">
			<b><bean:message key="label.delegates.createEditDelegates.selectDegree" bundle="PEDAGOGICAL_COUNCIL" /></b></p>
			
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
			
			<fr:property name="link(addDelegate)" value="/delegatesManagement.do?method=prepareAddDelegate" />
			<fr:property name="param(addDelegate)" value="degree.idInternal/selectedDegree,delegateType.name/delegateType"/>
			<fr:property name="key(addDelegate)" value="link.delegates.addDelegate"/>
			<fr:property name="bundle(addDelegate)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(addDelegate)" value="emptyDelegateBean"/>

			<fr:property name="link(addYearDelegate)" value="/delegatesManagement.do?method=prepareAddDelegate" />
			<fr:property name="param(addYearDelegate)" value="degree.idInternal/selectedDegree,curricularYear.year/selectedYear,delegateType.name/delegateType"/>
			<fr:property name="key(addYearDelegate)" value="link.delegates.addDelegate"/>
			<fr:property name="bundle(addYearDelegate)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(addYearDelegate)" value="emptyYearDelegateBean"/>
			
			<fr:property name="link(viewResults)" value="/delegatesManagement.do?method=prepareViewResults" />
			<fr:property name="param(viewResults)" value="degree.idInternal/selectedDegree,curricularYear.year/selectedYear"/>
			<fr:property name="key(viewResults)" value="link.delegates.viewResults"/>
			<fr:property name="bundle(viewResults)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(viewResults)" value="emptyYearDelegateBeanWithElection"/>
			
			<fr:property name="link(change)" value="/delegatesManagement.do?method=prepareChangeDelegate"/>
			<fr:property name="param(change)" value="delegate.idInternal/selectedDelegate"/>
			<fr:property name="key(change)" value="link.delegates.changeDelegate"/>
			<fr:property name="bundle(change)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(change)" value="yearDelegateBeanWithElectedElection"/>
			
			<fr:property name="link(remove)" value="/delegatesManagement.do?method=removeDelegate" />
			<fr:property name="param(remove)" value="delegate.idInternal/selectedDelegate,delegateType.name/delegateType"/>
			<fr:property name="key(remove)" value="link.delegates.removeDelegate"/>
			<fr:property name="bundle(remove)" value="PEDAGOGICAL_COUNCIL"/>
			<fr:property name="visibleIf(remove)" value="hasDelegate"/>
		</fr:layout>
	</fr:view>
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