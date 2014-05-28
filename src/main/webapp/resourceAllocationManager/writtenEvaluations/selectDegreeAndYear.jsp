<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="link.exams.searchWrittenEvaluationsByDegreeAndYear"/></h2>

<html:form action="/searchWrittenEvaluationsByDegreeAndYear" focus="executionDegreeID">

	<fr:edit name="bean">
		<fr:schema type="org.fenixedu.bennu.core.util.VariantBean" bundle="SOP_RESOURCES">
			<fr:slot name="object" layout="menu-select-postback" key="property.academicInterval">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.AcademicIntervalProvider" />
				<fr:property name="format" value="\${pathName}" />
				<fr:property name="nullOptionHidden" value="true" />
			</fr:slot>
		</fr:schema>
		<fr:destination name="postback" path="/searchWrittenEvaluationsByDegreeAndYear.do?method=prepare" />
		<fr:layout name="flow" />
	</fr:edit>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
	<%--<bean:define id="academicInterval" name="academicInterval"/>--%>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.academicInterval" property="academicInterval" value="<%= (String) request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL) %>"/>

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
