<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
		do curso de  <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em 
		<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/> 
			<logic:equal name="infoStudentCurricularPlan" property="specialization.name" value='<%= Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE.toString()%>'>
    	 		ministrado neste instituto, obteve aproveitamento nas disciplinas abaixo discriminadas, como extra-curriculares ao curso especializado conducente 
    			à obtenção do grau de mestre:
			</logic:equal>
			<logic:equal name="infoStudentCurricularPlan" property="specialization" value='<%= Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION.toString()%>'>
 				ministrado neste instituto, obteve aproveitamento nas disciplinas abaixo discriminada, como extra-curriculares.
			</logic:equal>