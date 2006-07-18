<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2>Gestão de Exames</h2>
<p>Seleccione a opção pretendida para criar, editar ou visualisar a calendarização dos exames. <br />
DevNote: Deveria de haver uma descrição do que se pretende fazer, e o que está envolvido, na marcação de exames.</p>
<span class="error"><html:errors /></span>
<html:form action="/mainExams">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" width="125"><bean:message key="property.executionPeriod"/>:</td>
    <td nowrap="nowrap"><jsp:include page="selectExecutionPeriodList.jsp"/></td>
  </tr>
</table>
<br />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
<bean:message key="label.choose"/>
</html:submit>
</html:form>