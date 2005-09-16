<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p><strong>Página 1 de 6</strong></p>

<div style="width: 50%; margin: 0 24%;">
	
	<div style="text-align: center;">
	<h2>Alteração da Password</h2>
	<p><strong>Por razões de segurança deve mudar a sua password.</strong></p>
	<p style="color: #770; background-color: #ffe;">
	<strong>Atenção:</strong> A alteração da password só terá efeito quando terminar o processo de matrícula.
	Se o processo for cancelado a nova password não ficará guardada.
	</p>
	</div>
<div class="infoop">
	<p style="text-align: center"><span class="error"><html:errors/></span></p>
	
	<html:form action="/viewPersonalInfo?method=visualizeFirstTimeStudentPersonalInfoAction">
	<html:hidden property="page" value="2"/>
    
    <!-- Old Password -->
	<table align="center">
       <tr>
         <td><bean:message key="label.candidate.oldPassword" bundle="DEFAULT"/>:</td>
         <td><html:password property="oldPassword"/></td>
       </tr>
	   <div></div>
       <!-- new password -->
       <tr>
         <td><bean:message key="label.candidate.newPassword" bundle="DEFAULT"/>:</td>
         <td><html:password property="newPassword"/></td>
         </td>
       </tr>
       <!-- retype new password -->
       <tr>
         <td><bean:message key="label.candidate.reTypePassword" bundle="DEFAULT"/>:</td>
         <td><html:password property="reTypeNewPassword"/></td>
         </td>
       </tr>
	</table>
</div>

	<br />
	<p align="center"><html:submit value="Confirmar" styleClass="inputbutton" property="ok" /></p>
	
</html:form>

</div>