<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN %>" />
<bean:define id="infoExecutionYear" name="<%= PresentationConstants.INFO_EXECUTION_YEAR %>"  />
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