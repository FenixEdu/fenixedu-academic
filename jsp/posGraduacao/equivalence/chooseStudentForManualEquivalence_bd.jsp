<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="Util.TipoCurso" %>

<h2><bean:message key="tilte.manual.equivalence"/></h2>
<span class="error"><html:errors/></span>
<br/>
<br/>
<br/>
<html:form action="/prepareStudentDataForManualEquivalence.do">
	<html:hidden property="method" value="getStudentAndDegreeTypeForManualEquivalence"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="degreeType" value="<%= (new Integer(TipoCurso.MESTRADO)).toString() %>"/>
	<table border="0">
		<tr>
			<td align="left"><bean:message key="label.choose.student"/>&nbsp;</td>
			<td align="left">
				<input type="text" name="studentNumber" size="5" value=""/>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit"/>
	</html:submit>
</html:form>
