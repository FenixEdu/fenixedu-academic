<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
    	<h2><bean:message key="title.manage.schedule"/></h2>
    	<table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
              </td>
          </tr>
        </table>
        <p><br/>
        Se pretende realizar alguma opera&ccedil;&atilde;o deve utilizar o men&uacute; lateral se
        a ac&ccedil;&atilde;o desejada est&aacute; relacionada com cria&ccedil;&atilde;o e/ou gest&atilde;o de turmas e
          turnos. Proceda com precau&ccedil;&atilde;o.</p>