<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<br/>
<h2><bean:message key="title.student.equivalency.plan"/></h2>

<fr:edit id="net.sourceforge.fenixedu.domain.util.search.StudentSearchBean"
		name="studentSearchBean"
		type="net.sourceforge.fenixedu.domain.util.search.StudentSearchBean"
		schema="net.sourceforge.fenixedu.domain.util.search.StudentSearchBean">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thright thlight thmiddle"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
</fr:edit>
<br/>
<br/>
<logic:present name="studentSearchBean" property="studentNumber">
	<logic:notPresent name="student">
		<bean:message key="message.student.does.not.exist"/>
	</logic:notPresent>
	<logic:present name="student">
		<logic:notPresent name="studentCurricularPlanEquivalencePlan">
			<bean:message key="message.student.does.not.have.equivalence.plan"/>
		</logic:notPresent>
		<logic:present name="studentCurricularPlanEquivalencePlan">
			<bean:define id="curriculumModule" name="studentCurricularPlanEquivalencePlan" property="oldStudentCurricularPlan.root" toScope="request"/>
			<bean:define id="indentLevel" type="java.lang.String" value="0" toScope="request"/>
			<bean:define id="width" type="java.lang.String" value="70" toScope="request"/>
			<jsp:include page="showStudentEquivalencyPlanForCurriculumModule.jsp"/>
		</logic:present>
	</logic:present>
</logic:present>
