<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="executionSemesterBean" name="executionSemesterBean" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentExecutionSemester"/>
<bean:define id="departmentParam" value="&departmentUnitOID="/>

<logic:present name="fromPedagogicalCouncil">
	<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
	<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

	<bean:define id="departmentParam" value="<%= "&departmentUnitOID=" + executionSemesterBean.getDepartmentUnitOID() %>"/>	
</logic:present>

<logic:notPresent name="fromPedagogicalCouncil">
	<em><bean:message key="label.departmentMember"/></em>
	<h2><bean:message key="title.inquiry.quc.department" bundle="INQUIRIES_RESOURCES"/></h2>
</logic:notPresent>

<p class="mtop15"><strong>
	<html:link action="<%= "/viewQucResults.do?method=resumeResults" + departmentParam %>" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message key="link.quc.resume" bundle="INQUIRIES_RESOURCES"/>
	</html:link> | 
	<html:link action="<%= "/viewQucResults.do?method=competenceResults" + departmentParam %>" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message key="link.quc.curricularUnits" bundle="INQUIRIES_RESOURCES"/>
	</html:link> | 
	<html:link action="<%= "/viewQucResults.do?method=teachersResults" + departmentParam %>" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message key="link.quc.teachers" bundle="INQUIRIES_RESOURCES"/>
	</html:link>
</strong></p>

<p class="mvert15">
	<fr:form>
		<fr:edit id="executionSemesterBean" name="executionSemesterBean">
			<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentExecutionSemester">
				<fr:slot name="executionSemester" key="label.inquiries.semester" layout="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.departmentMember.ViewQUCResultsDA$ExecutionSemesterQucProvider" />
					<fr:property name="format" value="${executionYear.year} - ${semester}º Semestre" />
					<fr:property name="nullOptionHidden" value="true"/>
					<fr:property name="destination" value="resumePostBack"/>
				</fr:slot>
			</fr:schema>
			<fr:layout>
				<fr:property name="classes" value="thlight thmiddle" />
				<fr:property name="resumePostBack" value="/viewQucResults.do?method=resumeResults"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
</p>

<logic:present name="fromPedagogicalCouncil">
	<h3 style="display: inline;"><bean:write name="departmentName"/></h3> | 
	<html:link page="/viewQucResults.do?method=chooseDepartment">
		<bean:message key="link.inquiry.chooseAnotherDepartment" bundle="INQUIRIES_RESOURCES"/>
    </html:link>
</logic:present>