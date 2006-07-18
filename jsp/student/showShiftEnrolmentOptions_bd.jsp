<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<table cellpadding="0" cellspacing="0" border="0">
	<%--	<tr>
		<td>
		    <html:link page="/studentShiftEnrolmentManager.do?method=initializeEnrolment&divideMethod=courses"> Inscrição por disciplinas</html:link>
		</td>
	</tr> --%>
	<tr>
		<td>
			<html:link page="/studentShiftEnrolmentManager.do?method=initializeEnrolment&divideMethod=classes">
				Inscrição por Turmas
			</html:link>
		</td>
	</tr>
</table>