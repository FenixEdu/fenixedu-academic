<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
    <h2><bean:message key="title.createTurno"/></h2>
        <span class="error"><!-- Error messages go here --><html:errors /></span>
        <html:form action="/criarTurnoForm">
            <table align="left" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="formTD">
                        <bean:message key="property.turno.name"/>:
                    </td>
                    <td class="formTD">
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.nome" property="nome" size="11" maxlength="20"/>
                    </td>
                </tr>
                <tr>
                    <td class="formTD">
                        <bean:message key="property.turno.disciplina"/>:
                    </td>
                    <td  class="formTD">
                    	<jsp:include page="selectExecutionCourseList.jsp"/>
                    
                    <%--    <html:select bundle="HTMLALT_RESOURCES" altKey="select.indexDisciplinaExecucao" property="indexDisciplinaExecucao" size="1">
                            <html:options collection="disciplinasExecucao" property="value" labelProperty="label"/>
                        </html:select> --%>
                    </td>
                </tr>
                <tr>
                    <td class="formTD">
                        <bean:message key="property.turno.type"/>:
                    </td>
                    <td class="formTD">
                        <html:select bundle="HTMLALT_RESOURCES" altKey="select.tipoAula" property="tipoAula" size="1">
                            <html:options collection="tiposAula" property="value" labelProperty="label"/>
                        </html:select>
                  </td>
                </tr>
                <tr>
                    <td class="formTD">
                        <bean:message key="property.turno.capacity"/>:
                    </td>
                    <td class="formTD">
                        <html:text bundle="HTMLALT_RESOURCES" altKey="text.lotacao" property="lotacao" size="11" maxlength="20"/>
                    </td>
                </tr>
          </table>
        <p>&nbsp;</p>
        <p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
          <table align='left' cellpadding="0" cellspacing="0">
              <tr align="center">
                  <td>
                      <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Guardar" styleClass="inputbutton">
                          <bean:message key="label.save"/>
                      </html:submit>
                  </td>
                  <td width="10"></td>
                  <td>
                      <html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton">
                          <bean:message key="label.clear"/>
                      </html:reset>
                  </td>
              </tr>
          </table>
        </html:form>