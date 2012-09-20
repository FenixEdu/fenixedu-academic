<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
<bean:define id="conclusiondate" name="<%= PresentationConstants.CONCLUSION_DATE %>" />
	<logic:present name="<%= PresentationConstants.DISCRIMINATED_WITH_AVERAGE%>">
		<bean:define id="infoFinalResult" name="<%= PresentationConstants.INFO_FINAL_RESULT%>" />
	</logic:present>
	do curso de <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em 
    	<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/> ministrado neste Instituto, 
    	obteve aproveitamento nas disciplinas abaixo discriminadas, com as quais concluíu o curso especializado conducente à obtenção do grau de mestre,  
    	em <bean:write name="conclusiondate" />
    		<logic:present name="<%= PresentationConstants.DISCRIMINATED_WITH_AVERAGE%>">
     			, com a média de <bean:write name="infoFinalResult" property="finalAverage"/> valores
			</logic:present>.<br />