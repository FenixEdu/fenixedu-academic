<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<jsp:include page="../teacherCreditsStyles.jsp"/>


<em><bean:message key="label.teacherService.credits"/></em>
<h3><bean:message key="label.credits.creditsReduction.definition" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h3>
<%net.sourceforge.fenixedu.domain.ExecutionSemester executionSemester = net.sourceforge.fenixedu.domain.ExecutionSemester.readActualExecutionSemester(); 
Boolean canChangeCredits = executionSemester.isInValidCreditsPeriod(net.sourceforge.fenixedu.domain.person.RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);

%>
<bean:define id="executionSemesterName" value="<%= executionSemester.getQualifiedName()%>"/>
<bean:define id="canChange" value="<%= canChangeCredits.toString()%>"/>
<h3><bean:write name="executionSemesterName"/></h3>
<%--
<logic:equal name="canChange" value="true">
	<p><html:link page="/creditsReductions.do?method=selectTeacher">
		<bean:message key="label.reductionService.insert" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</html:link></p>
</logic:equal>
 --%>
<logic:present name="creditsReductions">
	<fr:view name="creditsReductions">
		<fr:schema bundle="TEACHER_CREDITS_SHEET_RESOURCES" type="net.sourceforge.fenixedu.domain.teacher.ReductionService">
			<fr:slot name="teacherService.teacher.person" key="label.empty" layout="view-as-image">
				<fr:property name="classes" value="column3" />
				<fr:property name="moduleRelative" value="false" />
				<fr:property name="contextRelative" value="true" />
				<fr:property name="imageFormat" value="/person/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=${teacher.person.istUsername}" />
			</fr:slot>
			<fr:slot name="teacherService.teacher.person.presentationName" key="label.name"/>
			<fr:slot name="creditsReduction" key="label.requestedReductionCredits" layout="null-as-label"/>
			<fr:slot name="creditsReductionAttributed" key="label.attributedReductionCredits" layout="null-as-label"/>
			<fr:slot name="attributionDate" key="label.attributionDate" layout="null-as-label"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thleft mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="headerTable,,"/>
			<logic:equal name="canChange" value="true">
				<fr:property name="link(edit)" value="/creditsReductions.do?method=aproveReductionService" />
				<fr:property name="key(edit)" value="label.edit" />
				<fr:property name="param(edit)" value="externalId/reductionServiceOID" />
				<fr:property name="bundle(edit)" value="TEACHER_CREDITS_SHEET_RESOURCES" />
			</logic:equal>
		</fr:layout>
	</fr:view>
</logic:present>