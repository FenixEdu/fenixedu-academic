<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
<bean:define id="conclusiondate" name="<%= SessionConstants.CONCLUSION_DATE %>" />
	<logic:present name="<%= SessionConstants.DISCRIMINATED_WITH_AVERAGE%>">
		<bean:define id="infoFinalResult" name="<%= SessionConstants.INFO_FINAL_RESULT%>" />
	</logic:present>
	do curso de <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em 
    	<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/> ministrado neste Instituto, 
    	obteve aproveitamento nas disciplinas abaixo discriminadas, com as quais conclu�u o curso especializado conducente � obten��o do grau de mestre,  
    	em <bean:write name="conclusiondate" />
    		<logic:present name="<%= SessionConstants.DISCRIMINATED_WITH_AVERAGE%>">
     			, com a m�dia de <bean:write name="infoFinalResult" property="finalAverage"/> valores
			</logic:present>.<br />