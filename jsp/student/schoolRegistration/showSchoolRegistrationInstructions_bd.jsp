<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/changePassword?method=start">
<html:hidden property="page" value="1"/>

<table align="center" width="70%">
<tr>
	<td align="center"><h4 class="registration">Ler com Atenção</h4></td>
</tr>	
<tr>
	<td class="infoop" align="justify">
		<h4 class="registration_cell"><bean:message key="label.registration.info" /></h4>
	</td>
</tr>
</table>
<br>
<p align="center"><html:submit value="Continuar" styleClass="inputbutton"/></p>
</html:form>
