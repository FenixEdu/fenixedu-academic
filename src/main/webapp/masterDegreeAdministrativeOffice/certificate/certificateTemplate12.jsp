<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="infoMasterDegreeThesisDataVersion" name="<%= PresentationConstants.MASTER_DEGREE_THESIS_DATA_VERSION%>" />
<bean:define id="infoMasterDegreeProofVersion" name="<%= PresentationConstants.MASTER_DEGREE_THESIS_HISTORY %>" />
<bean:define id="conclusiondate" name="<%= PresentationConstants.CONCLUSION_DATE %>" />
<bean:define id="finalResult" name="<%= PresentationConstants.FINAL_RESULT%>" />
prestou provas para obtenção do grau de Mestre em  
<b><bean:write name="infoMasterDegreeThesisDataVersion"  property="infoMasterDegreeThesis.infoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/></b> concluidas em  
<bean:write name="conclusiondate" />, com a defesa da dissertação intitulada "
<b><bean:write name="infoMasterDegreeThesisDataVersion" property="dissertationTitle" />"</b>.
</p>