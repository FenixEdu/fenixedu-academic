<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
<bean:define id="infoExecutionYear" name="<%= SessionConstants.INFO_EXECUTION_YEAR %>"  />
<p>A pedido do(a) interessado(a), <b><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></b>, 
aluno(a) do curso de <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em <bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>,
portador(a) do Documento de Identificação nº <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.numeroDocumentoIdentificacao"/>,
emitido em <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.dataEmissaoDocumentoIdentificacao"/>, morador(a) na 
<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.morada"/>, &nbsp;<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.codigoPostal"/>
&nbsp; <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.localidade"/>. 
</p>
<p>
Declaro que está matriculado(a) neste Instituto desde o ano lectivo de <bean:write name="anoLectivo"/>.
</p>