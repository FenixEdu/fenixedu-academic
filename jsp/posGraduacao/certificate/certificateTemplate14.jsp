<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoMasterDegreeThesisDataVersion" name="<%= SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION%>" />
<bean:define id="conclusiondate" name="<%= SessionConstants.CONCLUSION_DATE %>" />
<bean:define id="finalResult" name="<%= SessionConstants.FINAL_RESULT%>" />
<bean:define id="infoFinalResult" name="<%= SessionConstants.INFO_FINAL_RESULT%>" />
concluiu o curso de mestrado em 
<b><bean:write name="infoMasterDegreeThesisDataVersion"  property="infoMasterDegreeThesis.infoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/></b> em <bean:write name="conclusiondate" />,
 com a defesa da dissertação intitulada "
<b><bean:write name="infoMasterDegreeThesisDataVersion" property="dissertationTitle" />"</b>.
</p>
<p>
A parte escolar do curso é constituida pelas seguintes disciplinas, na qual obteve a média de <bean:write name="infoFinalResult" property="finalAverage"/> valores.
</p>
