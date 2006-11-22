<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="link.exams.searchWrittenEvaluationsByDegreeAndYear"/></h2>

<html:form action="/searchWrittenEvaluationsByDegreeAndYear" focus="executionDegreeID">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<bean:define id="executionPeriodOID" name="executionPeriodOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodOID" property="executionPeriodOID" value="<%= executionPeriodOID.toString() %>"/>

	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<table>
		<tr>
			<td>
			   	<bean:message key="lable.degree"/>:
			</td>
		</tr>
		<tr>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeID" property="executionDegreeID" size="1">
					<html:option key="label.all" value=""/>
			    	<html:options collection="executionDegreeLabelValueBeans" property="value" labelProperty="label"/>
			    </html:select>
			</td>
		</tr>
	</table>
	<br/>

	<bean:message key="property.context.curricular.year"/>:<br/>
	<table>
		<tr><td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">1</html:multibox></td><td>1</td></tr>
		<tr><td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">2</html:multibox></td><td>2</td></tr>
		<tr><td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">3</html:multibox></td><td>3</td></tr>
		<tr><td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">4</html:multibox></td><td>4</td></tr>
		<tr><td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">5</html:multibox></td><td>5</td></tr>
		<tr><td colspan="2"><html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.selectAllCurricularYears" property="selectAllCurricularYears"><bean:message key="checkbox.show.all.curricular.years"/></html:checkbox></tr>
	</table>
	<br/>

	<bean:message key="property.evaluationType"/>::<br/>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.evaluationType" property="evaluationType" size="1">
		<html:option key="label.all" value=""/>
		<html:option key="label.exams" value="net.sourceforge.fenixedu.domain.Exam"/>
		<html:option key="label.tests" value="net.sourceforge.fenixedu.domain.WrittenTest"/>
    </html:select>

	<br/>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.choose"/>
	</html:submit>
	
	<html:submit styleClass="inputbutton"  onclick="this.form.method.value='print';">
		<bean:message key="label.print"/>
	</html:submit>
	
</html:form>
