<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
 <bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
 <bean:define id="nationality" name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nacionalidade" type="java.lang.String"/>
            
 <b><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></b>, filho(a) de
 <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nomePai"/> e de
 <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nomeMae"/>, natural de
 <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.freguesiaNaturalidade"/>, de nacionalidade

	<%
		if(nationality.startsWith("PORTUGUESA")){
	%>
		<bean:message key="label.person.portugueseNationalityFormated" />,
	<% }else{ %>
		<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nacionalidade"/>,
	<% } %>
