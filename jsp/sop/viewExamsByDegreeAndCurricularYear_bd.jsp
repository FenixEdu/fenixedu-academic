<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>A licenciatura seleccionada
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>

        <h2><bean:message key="title.exams"/></h2>
        <span class="error"><html:errors/></span>

		Falta por aqui a tabela dos exames de licenciatura e ano curricular
