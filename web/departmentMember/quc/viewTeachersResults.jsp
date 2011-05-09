<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<html:xhtml />
<link href="<%= request.getContextPath() %>/CSS/quc_resume_boards.css" rel="stylesheet" media="screen" type="text/css" />


<em><bean:message key="label.departmentMember"/></em>
<h2><bean:message key="title.inquiry.quc.department" bundle="INQUIRIES_RESOURCES"/></h2>

<p class="mtop15"><strong>
	<html:link action="/viewQucResults.do?method=resumeResults" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message key="link.quc.resume" bundle="INQUIRIES_RESOURCES"/>
	</html:link> | 
	<html:link action="/viewQucResults.do?method=competenceResults" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message key="link.quc.curricularUnits" bundle="INQUIRIES_RESOURCES"/>
	</html:link> | 
	<html:link action="/viewQucResults.do?method=teachersResults" paramId="executionSemesterOID" paramName="executionSemester" paramProperty="externalId">
		<bean:message key="link.quc.teachers" bundle="INQUIRIES_RESOURCES"/>
	</html:link>
</strong></p>

<p class="mvert15">
	<fr:form>
		<fr:edit id="executionSemesterBean" name="executionSemesterBean">
			<fr:schema bundle="INQUIRIES_RESOURCES" type="net.sourceforge.fenixedu.dataTransferObject.VariantBean">
				<fr:slot name="domainObject" key="label.inquiries.semester" layout="menu-select-postback">
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

<h3><bean:message key="link.quc.teachers" bundle="INQUIRIES_RESOURCES"/> (<bean:write name="executionSemester" property="executionYear.year"/> - <bean:write name="executionSemester" property="semester"/>º Semestre)</h3>

<p><bean:message key="message.department.teachers" bundle="INQUIRIES_RESOURCES"/></p>

<fr:view name="teachersResume">
	<fr:layout name="department-teachers-resume">
		<fr:property name="extraColumn" value="true"/>
		<fr:property name="classes" value="department-resume"/>
	</fr:layout>
</fr:view>

<ul class="legend-general" style="margin-top: 20px;"> 
	<li><bean:message key="label.inquiry.legend" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-1">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.excelent" bundle="INQUIRIES_RESOURCES"/></li>
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.regular" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.toImprove" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-4">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.indequate" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><bean:message key="label.inquiry.questionsToImprove" bundle="INQUIRIES_RESOURCES"/></li> 
</ul>

<ul class="legend-general" style="margin-top: 0px;"> 
	<li><bean:message key="label.inquiry.workLoad" bundle="INQUIRIES_RESOURCES"/>:</li> 
	<li><span class="legend-bar-2">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.asPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-3">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.higherThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-6">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.lowerThanPredicted" bundle="INQUIRIES_RESOURCES"/></li> 
	<li><span class="legend-bar-5">&nbsp;</span>&nbsp;<bean:message key="label.inquiry.withoutRepresentation" bundle="INQUIRIES_RESOURCES"/></li> 
</ul>