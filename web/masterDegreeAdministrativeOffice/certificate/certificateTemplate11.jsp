<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
<bean:define id="conclusiondate" name="<%= PresentationConstants.CONCLUSION_DATE %>" />
<bean:define id="infoFinalResult" name="<%= PresentationConstants.INFO_FINAL_RESULT%>" />

<div style="text-align: center !important;">
	<h2 class='diploma_posGrad'>DIPLOMA</h2>
	<h3 class='diploma_posGrad'>O <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%> certifica que</h3>
	<p class="diplomado">
		<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/>
	</p>
	<h3 class='diploma_posGrad'>concluiu a parte curricular do Curso de </h3>
	<h3 class="diplomado"><bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em 
	<bean:write name="infoStudentCurricularPlan"  property="infoDegreeCurricularPlan.infoDegree.nome"/></h3>
   	<h3 class='diploma_posGrad'>com a média de <bean:write name="infoFinalResult" property="finalAverage"/> valores.</h3>

	<div class="dipl_signature">    	
		<h4  class='diploma_posGrad'><bean:write name="<%= PresentationConstants.DATE %>" /></h4>
		<h4 class='diploma_posGrad'>O Presidente do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%></h4>
	</div>
</div>