<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.gratuity.studentSituation"/></h2>
<center>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<br/>
<br/>
<html:form action="/studentSituation.do" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readStudent"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" value="MASTER_DEGREE"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<table border="0">
		<tr>
			<td><bean:message key="label.choose.student"/>&nbsp;</td>
			<td>
				<input alt="input.studentNumber" type="text" name="studentNumber" size="5" value=""/>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.submit.degree.type.and.student"/>
	</html:submit>
</html:form>

</center>
