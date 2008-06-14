<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.writtenEvaluationManagement"/></em>
<h2><bean:message key="link.exams.searchWrittenEvaluationsByDegreeAndYear"/></h2>

<html:form action="/searchWrittenEvaluationsByDegreeAndYear" focus="executionDegreeID">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<bean:define id="executionPeriodOID" name="executionPeriodOID"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodOID" property="executionPeriodOID" value="<%= executionPeriodOID.toString() %>"/>

	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<table class="tstyle5 thlight thright thtop">
		<tr>
			<th>
			   	<bean:message key="lable.degree"/>:
			</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeID" property="executionDegreeID" size="1">
					<html:option key="label.all" value=""/>
			    	<html:options collection="executionDegreeLabelValueBeans" property="value" labelProperty="label"/>
			    </html:select>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="property.context.curricular.year"/>:
			</th>
			<td>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">1</html:multibox> 1<br/>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">2</html:multibox> 2<br/>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">3</html:multibox> 3<br/>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">4</html:multibox> 4<br/>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">5</html:multibox> 5<br/>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.selectAllCurricularYears" property="selectAllCurricularYears"><bean:message key="checkbox.show.all.curricular.years"/></html:checkbox></tr>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="property.evaluationType"/>:
			</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.evaluationType" property="evaluationType" size="1">
					<html:option key="label.all" value=""/>
					<html:option key="label.exams" value="net.sourceforge.fenixedu.domain.Exam"/>
					<html:option key="label.tests" value="net.sourceforge.fenixedu.domain.WrittenTest"/>
			    </html:select>
			</td>
		</tr>
	</table>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="label.choose"/>
		</html:submit>
	</p>
	
</html:form>
