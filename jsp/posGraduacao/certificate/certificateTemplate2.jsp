<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

	

<tr><td>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
<bean:define id="certificateType" name="<%= SessionConstants.CERTIFICATE_TYPE%>" />
tem <bean:write name="certificateType"/> no ano lectivo de **2002/2003**, no curso de 
    <bean:write name="infoStudentCurricularPlan" property="specialization"/> em 
    <bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/> 
</td></tr>
