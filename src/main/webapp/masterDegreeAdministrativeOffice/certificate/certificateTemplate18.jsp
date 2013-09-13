<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="infoMasterDegreeThesisDataVersion" name="<%= PresentationConstants.MASTER_DEGREE_THESIS_DATA_VERSION%>" />
<bean:define id="conclusiondate" name="<%= PresentationConstants.CONCLUSION_DATE %>" />
<bean:define id="finalResult" name="<%= PresentationConstants.FINAL_RESULT%>" />
<bean:define id="infoFinalResult" name="<%= PresentationConstants.INFO_FINAL_RESULT%>" />
tendo concluido a parte curricular do Programa de Mestrado em 
<b><bean:write name="infoMasterDegreeThesisDataVersion"  property="infoMasterDegreeThesis.infoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/> 
( MBA em <bean:write name="infoMasterDegreeThesisDataVersion"  property="infoMasterDegreeThesis.infoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/> )</b> 
em <bean:write name="conclusiondate" />,
 com a defesa da dissertação intitulada "
<b><bean:write name="infoMasterDegreeThesisDataVersion" property="dissertationTitle" />"</b>.
</p>
<p>
A parte escolar do curso é constituida pelas seguintes disciplinas, na qual obteve a média de <bean:write name="infoFinalResult" property="finalAverage"/> valores.
</p>
