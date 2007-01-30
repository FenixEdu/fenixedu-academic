<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean"/>

<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/checkall.js"></script>

<html:xhtml/>
<h2><bean:message key="label.search.scorm"/></h2>

<style>
@import url(<%= request.getContextPath() %>/CSS/transitional.css);
/* @import url(<%= request.getContextPath() %>/CSS/dotist.css); */
</style>

	<div class="infoop2">
		<bean:message key="label.information.about.search.line1"/>
		<bean:message key="label.information.about.search.line2"/>
	</div>

 	<fr:form id="searchForm" action="<%= "/searchScormContent.do?method=searchScormContents&amp;executionCourseID=" + request.getParameter("executionCourseID") %>">
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
				<fr:destination name="postback" path="<%="/searchScormContent.do?method=changeTimeStamp&amp;executionCourseID=" + request.getParameter("executionCourseID") %>"/>
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
				<fr:destination name="postback" path="<%="/searchScormContent.do?method=changeTimeStamp&amp;executionCourseID=" + request.getParameter("executionCourseID") %>"/>
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

		<logic:iterate id="searchElement" indexId="index" name="bean" property="searchElements">
		<tr>
			<th>
				<logic:equal name="index" value="0">
					<bean:message key="label.searchField"/>:
				</logic:equal>
				
				<logic:notEqual name="index" value="0">
					<fr:edit id="<%="conjunctionType" + index%>" name="searchElement" slot="conjunction">
					<fr:layout>
						<fr:property name="defaultText" value=""/>
					</fr:layout>
					</fr:edit>
				</logic:notEqual>

			</th>

			<td>
				<fr:edit id="<%="valueField" + index%>" name="searchElement" slot="queryValue" >
					<fr:layout>
						<fr:property name="size" value="40"/>
					</fr:layout>
				</fr:edit>

				<bean:message key="label.in" bundle="APPLICATION_RESOURCES"/>
				<fr:edit id="<%= "searchTypeField" + index%>" name="searchElement" slot="searchField">
				<fr:layout>
					<fr:property name="excludedValues" value="TYPE, DATE, UNIT"/> 
					<fr:property name="sort" value="true"/>
				</fr:layout>
				</fr:edit>

 
				<logic:equal name="index" value="0">
					<div class="switchNone">
					<html:link page="<%="/searchScormContent.do?method=addNewSearchCriteria&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean.getSearchElementsAsParameters() %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:link>			
					<logic:greaterThan name="bean" property="searchElementsSize" value="1">
					 , 
					<html:link page="<%="/searchScormContent.do?method=removeSearchCriteria&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean.getSearchElementsAsParameters() %>"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES"/></html:link>								
					</logic:greaterThan>
					</div>
					<div class="switchInline">
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='searchScormContent.do?method=addNewSearchCriteria&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean.getSearchElementsAsParameters() + "&amp;addIndex=" + (index+1) + "'; getElementById('searchForm').submit();" %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></a>
					<logic:greaterThan name="bean" property="searchElementsSize" value="1">
					 , 
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='searchScormContent.do?method=removeSearchCriteria&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index + "'; getElementById('searchForm').submit();" %>"><bean:message key="label.remove" bundle="APPLICATION_RESOURCES"/></a>
					</logic:greaterThan>
					</div>
				</logic:equal>
				
				<logic:notEqual name="index" value="0">
					<div class="switchNone">
					<html:link page="<%="/searchScormContent.do?method=addNewSearchCriteria&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean.getSearchElementsAsParameters() %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:link> , 			
					<html:link page="<%="/searchScormContent.do?method=removeSearchCriteria&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index%>"><bean:message key="link.remove" bundle="APPLICATION_RESOURCES"/></html:link>
					</div>
					<div class="switchInline">
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='searchScormContent.do?method=addNewSearchCriteria&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean.getSearchElementsAsParameters() + "&amp;addIndex=" + (index+1) +  "'; getElementById('searchForm').submit();" %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></a> , 
					<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='searchScormContent.do?method=removeSearchCriteria&amp;executionCourseID=" + request.getParameter("executionCourseID") + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index + "'; getElementById('searchForm').submit();"%>"><bean:message key="link.remove" bundle="APPLICATION_RESOURCES"/></a>
					</div>
				</logic:notEqual>
			</td>
		</tr>
		</logic:iterate>


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
	"/publico/searchScormContent.do?method=moveIndex&amp" + bean.getSearchElementsAsParameters() + 
	"&amp;executionCourseID=" + request.getParameter("executionCourseID") %>" 
	pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>
</p>
</logic:notEqual>


<ul>
<logic:iterate id="file" name="bean" property="results" type="net.sourceforge.fenixedu.domain.File">

	 	<logic:present name="file">
		 <li class="mtop1">
		 	<logic:equal name="file" property="class.simpleName" value="FileItem">
		 	 <fr:view name="file" property="item.section.site.executionCourse.nome"/> (<fr:view name="file" property="item.section.site.executionCourse.executionPeriod.name"/> - <fr:view name="file" property="item.section.site.executionCourse.executionYear.year"/>) <br/> 
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />  <fr:view name="file" property="displayName"></fr:view> (<a href="<fr:view name="file" property="downloadUrl"/>"><fr:view name="file" property="filename"/></a>)
			</logic:equal>
		 	<logic:notEqual name="file" property="class.simpleName" value="FileItem">
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
    "/publico/searchScormContent.do?method=moveIndex" + bean.getSearchElementsAsParameters() + 
	"&amp;executionCourseID=" + request.getParameter("executionCourseID") %>" 
	pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>
</p>
</logic:notEqual>


</logic:notEmpty>

<logic:empty name="bean" property="results">
	<p>
		<em><bean:message key="label.search.noResultsFound" /></em>.
	</p>
</logic:empty>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>