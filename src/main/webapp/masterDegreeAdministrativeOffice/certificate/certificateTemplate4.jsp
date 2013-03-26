<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
	<logic:present name="<%= PresentationConstants.ENROLMENT_LIST%>">
		<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= PresentationConstants.ENROLMENT_LIST%>" />
	</logic:present>
	<logic:present name="<%= PresentationConstants.EXTRA_ENROLMENT_LIST%>">
		<bean:define id="infoEnrolmentExtraCurricularPlan" name="<%= PresentationConstants.EXTRA_ENROLMENT_LIST%>" />
	</logic:present>
	<logic:present name="<%= PresentationConstants.ENROLMENT%>">
	<logic:present name="<%= PresentationConstants.ENROLMENT_LIST%>">
	nas seguintes disciplinas:<br />
	<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
	<br />
		<bean:write name="itr" property="infoCurricularCourse.name" />
	</logic:iterate>
	</logic:present>
	<logic:present name="<%= PresentationConstants.EXTRA_ENROLMENT_LIST%>">
	nas seguintes disciplinas Extra-Curriculares:<br />
	<logic:iterate id="itr" name="infoEnrolmentExtraCurricularPlan">
	<br />
	<bean:write name="itr" property="infoCurricularCourse.name" />
	</logic:iterate>
		</logic:present>
	</logic:present>
		<logic:present name="<%= PresentationConstants.APROVMENT%>">
	<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
	<br />
		<bean:write name="itr" property="infoCurricularCourse.name" />
 		<bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 		com 
  		<bean:write name="itr" property="infoEnrolmentEvaluation.gradeValue" />
 		valores
 		<logic:equal name="itr" property="infoCurricularCourse.type.name" value="P_TYPE_COURSE">
	 		 (<bean:message key="label.curricularCourse.pType" />)
 		</logic:equal>
	</logic:iterate>
		</logic:present>
		<logic:present name="<%= PresentationConstants.EXTRA_CURRICULAR_APROVMENT%>">
			<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= PresentationConstants.EXTRA_ENROLMENT_LIST%>" />
	<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
<br />
 		<bean:write name="itr" property="infoCurricularCourse.name" />
 		<bean:write name="itr" property="infoExecutionPeriod.infoExecutionYear.year" />
 		com 
 		<bean:write name="itr" property="infoEnrolmentEvaluation.gradeValue" />
		 valores
	</logic:iterate>
		</logic:present>
	<bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN%>"/>
	<logic:notEmpty name="infoStudentCurricularPlan"  property="givenCredits" >
		<br/>
		Atribuição de Créditos:  <bean:write name="infoStudentCurricularPlan"  property="givenCredits" /> Créditos
	</logic:notEmpty>