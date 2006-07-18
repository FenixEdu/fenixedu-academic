<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="infoMasterDegreeThesisDataVersion" name="<%= SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION%>" />
<bean:define id="conclusiondate" name="<%= SessionConstants.CONCLUSION_DATE %>" />
<bean:define id="finalResult" name="<%= SessionConstants.FINAL_RESULT%>" />
<bean:define id="infoFinalResult" name="<%= SessionConstants.INFO_FINAL_RESULT%>" />
tendo concluido a parte curricular do Programa de Mestrado em 
<b><bean:write name="infoMasterDegreeThesisDataVersion"  property="infoMasterDegreeThesis.infoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/> 
( MBA em <bean:write name="infoMasterDegreeThesisDataVersion"  property="infoMasterDegreeThesis.infoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/> )</b> 
em <bean:write name="conclusiondate" />,
 com a defesa da disserta��o intitulada "
<b><bean:write name="infoMasterDegreeThesisDataVersion" property="dissertationTitle" />"</b>.
</p>
<p>
A parte escolar do curso � constituida pelas seguintes disciplinas, na qual obteve a m�dia de <bean:write name="infoFinalResult" property="finalAverage"/> valores.
</p>
