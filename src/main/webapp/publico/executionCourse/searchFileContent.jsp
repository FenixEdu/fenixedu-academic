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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/collection-pager" prefix="cp" %>

<bean:define id="executionCourseID" name="executionCourse" property="externalId"/>
<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceCoursesBean"/>

<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/checkall.js"></script>

<html:xhtml/>
<h2><bean:message key="label.file.content.search"/></h2>

	<div class="infoop2">
		<bean:message key="label.information.about.search.line1"/>
		<bean:message key="label.information.about.search.line2"/>
	</div>

 	<fr:form id="searchForm" action="<%= "/searchFileContent.do?method=searchFileContent" %>">
		<fr:hasMessages for="search" type="validation">
			<p>
			<span class="error0"><bean:message key="label.requiredFieldsNotPresent"/></span>
			</p>
		</fr:hasMessages>
	
		<fr:edit id="search" name="bean" visible="false"/>

		<table class="tstyle5 thright thlight">
		<tr>
			<th>
				<bean:message key="label.executionYear" bundle="ADMIN_OFFICE_RESOURCES"/>:
			</th>
			<td>
				<fr:edit id="executionYearField" name="bean" slot="executionYear">
				<fr:layout name="menu-select-postback">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OpenExecutionYearsProvider"/>
					<fr:property name="format" value="${year}"/>
					<fr:property name="bundle" value="APPLICATION_RESOURCES"/>
					<fr:property name="defaultText" value="label.masterDegree.administrativeOffice.allExecutionYears"/>
					<fr:property name="key" value="true"/>
				</fr:layout>
				<fr:destination name="postback" path="<%="/searchFileContent.do?method=changeTimeStamp" %>"/>
				</fr:edit>	
	
				<bean:message key="label.executionPeriod" bundle="APPLICATION_RESOURCES"/>:
				<fr:edit id="executionPeriodField" name="bean" slot="executionPeriod">
				<fr:layout name="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionPeriodsForExecutionYear"/>
					<fr:property name="format" value="${name}"/>
					<fr:property name="bundle" value="APPLICATION_RESOURCES"/>
					<fr:property name="defaultText" value="label.masterDegree.administrativeOffice.allExecutionYears"/>
					<fr:property name="key" value="true"/>
				</fr:layout>
				<fr:destination name="postback" path="<%="/searchFileContent.do?method=changeTimeStamp" %>"/>
				</fr:edit>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.educationalLearningResourceType" bundle="SITE_RESOURCES"/>:
			</th>
			<td>
				<div id="noType" class="dinline">
					<logic:empty name="bean" property="educationalResourceTypes">  
					<input id="noTypeCheckbox" type="checkbox" checked="checked" onclick="javascript:uncheckall('typesCheckBoxes')"/>
					</logic:empty>
					<logic:notEmpty name="bean" property="educationalResourceTypes">  
					<input id="noTypeCheckbox" type="checkbox" onclick="javascript:uncheckall('typesCheckBoxes')"/>
					</logic:notEmpty>
					<label for="noTypeCheckbox"><bean:message key="label.notClassified" bundle="SITE_RESOURCES"/></label>
				</div>
				<div id="typesCheckBoxes" class="dinline"> 
				<fr:edit id="educationalType" name="bean" slot="educationalResourceTypes">
					<fr:layout name="option-select">
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.EducationalResourceProvider"/>
						<fr:property name="classes" value="nobullet liinline dinline"/>
					</fr:layout>
				</fr:edit>
				</div>
	
				<script type="text/javascript" language="javascript">
					var checkboxes = document.getElementById('typesCheckBoxes').getElementsByTagName("INPUT"); 
					for(var i=0;i<checkboxes.length;i++) {
						checkboxes[i].onclick = function() {  uncheckall('noType'); }
						checkboxes[i].ondblclick = function() { uncheckall('noType'); }
					}
				</script>
			</td>
		</tr>
		
		<tr>
			<th>
				<bean:message key="label.searchField" />:
			</th>
			<td>
				<fr:edit id="searchTextField" name="bean" slot="searchText" />
			</td>
		</tr>

		</table>
			<html:submit><bean:message key="label.search" /></html:submit>
	</fr:form>

<logic:present name="bean" property="results">
<logic:notEmpty name="bean" property="results">

<p><bean:message key="label.hitCount" />: <strong><fr:view name="bean" property="totalItems"/></strong></p>


<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 
<cp:collectionPages url="<%= 
	"/publico/searchFileContent.do?method=moveIndex" + bean.getSearchElementsAsParameters()  %>" 
	pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
</p>
</logic:notEqual>


<ul>
<logic:iterate id="file" name="bean" property="results" type="net.sourceforge.fenixedu.domain.File">

	 	<logic:present name="file">
		 <li class="mtop1">
		 	<logic:equal name="file" property="class.simpleName" value="FileContent">
			<bean:define id="site" name="file" property="site"/>
				<logic:equal name="site" property="class.simpleName" value="ExecutionCourseSite">
				<bean:define id="executionCourse" name="site" property="executionCourse"/>
			 	 <fr:view name="executionCourse" property="nome"/> (<fr:view name="executionCourse" property="executionPeriod.name"/> - <fr:view name="executionCourse" property="executionYear.year"/>) <br/> 
			     <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />  <fr:view name="file" property="displayName"></fr:view> (<a href="<fr:view name="file" property="downloadUrl"/>"><fr:view name="file" property="filename"/></a>)
				</logic:equal>
			</logic:equal>
		 	<logic:notEqual name="file" property="class.simpleName" value="FileContent">
		 		<bean:write name="file" property="class.simpleName"/>
		 	</logic:notEqual>
    	 </li>
		</logic:present>
</logic:iterate>
</ul>

<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 
<cp:collectionPages url="<%= 
    "/publico/searchFileContent.do?method=moveIndex" + bean.getSearchElementsAsParameters() %>" 
	pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages" numberOfVisualizedPages="10"/>
</p>
</logic:notEqual>


</logic:notEmpty>

<logic:empty name="bean" property="results">
	<p>
		<em><bean:message key="label.search.noContentsFound" /></em>.
	</p>
</logic:empty>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>