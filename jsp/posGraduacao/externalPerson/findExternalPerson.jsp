<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson" %>


<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.externalPersons.find"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>

<html:form action="/findExternalPerson.do?method=find">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<table border="0" width="100%" cellspacing="3" cellpadding="10">
		
		<tr>
			<td class="infoop" >
				<span class="emphasis-box">info</span>
			</td>
			<td class="infoop">
				<strong>Nota:</strong> Na indicaçãodo nome pode ser fornecido apenas parte do nome da pessoa externa.<br/>
				Exemplo 1: Para selecionar todas as pessoas externas que começam com a letra "A" escreva <strong>A%</strong><br/>
				Exemplo 2: Para selecionar todas as pessoas externas que começam com a letra "A" e que tenham um segundo nome que começam com a letra "M" escreva <strong>A% M%</strong>
			</td>
	
		</tr>
	</table>
	<p/>
	<table>
		<tr>
			<td align="left" >
				<bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/>:
				<input alt="input.name" type="text" name="name" size="25" value=""/>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbuttonSmall">
					<bean:message key="button.submit.masterDegree.externalPerson.find"/>
				</html:submit>
			</td>
		</tr>

	</table>
	
</html:form>


</center>