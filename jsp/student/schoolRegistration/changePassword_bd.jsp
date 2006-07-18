<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p><strong>P�gina 1 de 6</strong></p>

<div style="width: 50%; margin: 0 24%;">
	
	<div style="text-align: center;">
	<h2>Altera��o da Password</h2>
	<p><strong>Por raz�es de seguran�a deve mudar a sua password.</strong></p>
	<p style="color: #770; background-color: #ffe;">
	<strong>Aten��o:</strong> A altera��o da password s� ter� efeito quando terminar o processo de matr�cula.
	Se o processo for cancelado a nova password n�o ficar� guardada.
	</p>
	</div>
<div class="infoop">
	<p style="text-align: center"><span class="error"><html:errors/></span></p>
	
	<html:form action="/viewPersonalInfo?method=visualizeFirstTimeStudentPersonalInfoAction">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
    
    <!-- Old Password -->
	<table align="center">
       <tr>
         <td><bean:message key="label.candidate.oldPassword" bundle="DEFAULT"/>:</td>
         <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.oldPassword" property="oldPassword"/></td>
       </tr>
	   <div></div>
       <!-- new password -->
       <tr>
         <td><bean:message key="label.candidate.newPassword" bundle="DEFAULT"/>:</td>
         <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.newPassword" property="newPassword"/></td>
         </td>
       </tr>
       <!-- retype new password -->
       <tr>
         <td><bean:message key="label.candidate.reTypePassword" bundle="DEFAULT"/>:</td>
         <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.reTypeNewPassword" property="reTypeNewPassword"/></td>
         </td>
       </tr>
	</table>
</div>

	<br />
	<p align="center"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Confirmar" styleClass="inputbutton" property="ok" /></p>
	
</html:form>

</div>