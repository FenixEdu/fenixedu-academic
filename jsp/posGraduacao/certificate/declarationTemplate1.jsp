<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" scope="session" />
<bean:define id="infoExecutionYear" name="<%= SessionConstants.INFO_EXECUTION_YEAR %>" scope="session" />
<p>A pedido do interessado, <b><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></b>, 
aluno do curso de <bean:write name="infoStudentCurricularPlan" property="specialization"/> em <bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>,
portador do Documento de Identificação nº <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.numeroDocumentoIdentificacao"/>,
emitido em <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.dataEmissaoDocumentoIdentificacao"/>, morador na 
<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.morada"/>, &nbsp;<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.codigoPostal"/>
&nbsp; <bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.localidade"/>, telefone&nbsp;
<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.telefone"/>. 
</p>
<p>
Declaro que está matriculado neste Instituto no ano lectivo de <bean:write name="anoLectivo"/>.
</p>