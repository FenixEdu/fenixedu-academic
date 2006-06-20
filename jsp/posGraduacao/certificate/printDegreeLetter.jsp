<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" type="net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan" />
<bean:define id="infoThesisDataVersion" name="<%= SessionConstants.MASTER_DEGREE_THESIS_DATA_VERSION%>" />
 
<html>
<head>
	<title></title>
</head>

<body>

<style type="text/css" media="screen, print">
body {
font-family: Times New Roman, Times, serif;
font-weight: bold;
text-transform: uppercase;
font-style: italic;
font-size: 14px;
}
#container {
line-height: 171%;  /* -------- */
margin-top: 14.01cm;  /* -------- */
margin-left: 4.75cm;  /* -------- */ 
width: 22.5cm;
}
#container p { margin: 0; padding: 0; }
#container span.hide {
color: #aaa;
text-transform: none;
font-size: 18px;
	visibility: hidden;
}
</style>

<%
	String parish = infoStudentCurricularPlan.getInfoStudent().getInfoPerson().getFreguesiaNaturalidade();
	String subDivision = infoStudentCurricularPlan.getInfoStudent().getInfoPerson().getConcelhoNaturalidade();
	String birth = parish.equals(subDivision) ? parish : parish + " - " + subDivision;
%>

<div id="container">
<span class="hide">Faço saber que ao licenciado</span> <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/><br/>

<span class="hide">Filho de</span> <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nomePai"/><br/>
<span class="hide">e de</span> <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nomeMae"/><br/>
<span class="hide">natural de</span> <%= birth %> <span class="hide">tendo frequentado com aproveitamento</span><br/>
<span class="hide">o curso de mestrado em</span> <bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/><br/>

<span class="hide">no Instituto Superior Técnico desta Universidade e defendido, perante um júri legalmente constituído, a disserta-</span><br/>
<p style="margin-bottom: 0.61cm;"><span class="hide">ção com o título</span> <bean:write name="infoThesisDataVersion" property="dissertationTitle"/></p>

<span style="margin-left: 4.7cm;" class="hide">lhe foi concebido em,</span> <span><bean:write name="<%= SessionConstants.CONCLUSION_DATE%>" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="hide">o grau de mestre em</span> <bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/> <span></span><span class="hide"> com a qualificação de &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span> <bean:message name="<%= SessionConstants.FINAL_RESULT %>" bundle="ENUMERATION_RESOURCES"/></span><br/>

<span class="hide">que, em conformidade com as disposições legais em vigor, lhe mandei passar a presente carta de curso</span>
<p style="margin-top: 0.55cm; margin-left: 3.8cm;"><span class="hide">Universidade Técnica de Lisboa, em</span>&nbsp;&nbsp; <%= java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG, new java.util.Locale("pt", "PT")).format(new java.util.Date()) %><br/></p>
<p style="visibility: hidden;">cutting problem solved</p>
</div>

</body>
</html>
