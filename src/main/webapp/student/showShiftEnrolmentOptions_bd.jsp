<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
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