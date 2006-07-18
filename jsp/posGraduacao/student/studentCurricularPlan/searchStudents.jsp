<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<h2 align="left"><bean:message key="title.masterDegree.administrativeOffice.chooseStudent"/></h2>

<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="infoop" >
			<span class="emphasis-box">info</span>
		</td>
		<td class="infoop">
			<strong>Nota:</strong> Na indica��o do nome pode ser fornecido apenas parte do nome do aluno.<br/>
			Exemplo 1: Para selecionar todos os alunos que come�am com a letra "A" escreva <strong>A%</strong><br/>
			Exemplo 2: Para selecionar todos os alunos que come�am com a letra "A" e que tenham um segundo nome que come�a com a letra "M" escreva <strong>A% M%</strong>
		</td>
	</tr>
</table>
<br/><br/>
<span class="error"><html:errors/></span>

<html:form action="/seeStudentAndCurricularPlans.do" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="read"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<table border="0" cellspacing="0" cellpadding="5">
		<tr>
			<td align="left"><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/>:&nbsp;</td>
			<td align="left">
				<input alt="input.studentNumber" type="text" name="studentNumber" size="5" value=""/>
			</td>
		</tr>	
		<tr>
			<td align="left"><bean:message key="label.masterDegree.administrativeOffice.studentName"/>:&nbsp;</td>
			<td align="left">
				<input alt="input.studentName" type="text" name="studentName" size="20" value=""/>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.masterDegree.administrativeOffice.idNumber"/>:&nbsp;</td>
			<td align="left">
				<input alt="input.idNumber" type="text" name="idNumber" size="20" value=""/>
			</td>
		</tr>
		<tr>
			<td align="left"><bean:message key="label.masterDegree.administrativeOffice.idType"/>:&nbsp;</td>
			<td align="left">
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.idType" property="idType" size="1">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.masterDegree.administrativeOffice.studentSearchSubmit"/>
	</html:submit>
</html:form>
