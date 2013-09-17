<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" %>
<bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
do curso de  <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em 
		<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/> 
			<logic:equal name="infoStudentCurricularPlan" property="specialization.name" value='<%= Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE.toString()%>'>
     			ministrado neste Instituto, obteve aproveitamento nas disciplinas abaixo discriminadas, que fazem parte do curso especializado conducente à obtenção do grau de mestre, ao abrigo do D.L. 216/92 de 13 de Outubro:
     			<br />
			</logic:equal>
			<logic:equal name="infoStudentCurricularPlan" property="specialization.name" value='<%= Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION.toString()%>'>
 				ministrado neste instituto,obteve aproveitamento nas disciplinas abaixo discriminada.
			</logic:equal>
			<br />
