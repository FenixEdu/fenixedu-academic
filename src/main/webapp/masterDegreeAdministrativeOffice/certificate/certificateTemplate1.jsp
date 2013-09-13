<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
 <bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
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
