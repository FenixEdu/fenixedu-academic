<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<tr><td>
 <bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
 <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/> filho de
 <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nomePai"/> e de
 <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nomeMae"/> natural de
 <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.freguesiaNaturalidade"/> de nacionalidade
 <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nacionalidade"/>
</td></tr>
