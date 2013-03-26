<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp" %>

<bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
<bean:define id="executionCourseId" name="executionCourse" property="idInternal"/>
<bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>
<bean:define id="section" name="item" property="section" type="net.sourceforge.fenixedu.domain.Section"/>
<bean:define id="bean" name="bean" type="net.sourceforge.fenixedu.dataTransferObject.SearchDSpaceBean"/>

<html:xhtml/>
<h2><bean:message key="label.search.scorm"/></h2>

 	<fr:form id="searchForm" action="<%= "/searchScormContent.do?method=searchScormContents&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() %>">
		<fr:hasMessages for="search" type="validation">
			<p>
			<span class="error0"><bean:message key="label.requiredFieldsNotPresent"/></span>
			</p>
		</fr:hasMessages>
	
		<fr:edit id="search" name="bean" visible="false"/>
	
		<table class="tstyle5 thright thlight">
		<tr>
		<td colspan="4">
			<bean:message key="label.educationalLearningResourceType" bundle="SITE_RESOURCES"/>: <fr:edit id="educationalType" name="bean" slot="educationalResourceTypes">
				<fr:layout name="option-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.EducationalResourceProvider"/>
					<fr:property name="classes" value="nobullet liinline"/>
				</fr:layout>
			</fr:edit>	
		</td>
		</tr>
		<tr>
		<td>
			<bean:message key="label.executionYear" bundle="ADMIN_OFFICE_RESOURCES"/>: 
			<fr:edit id="executionYearField" name="bean" slot="executionYear">
			<fr:layout name="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OpenExecutionYearsProvider"/>
				<fr:property name="format" value="${year}"/>
				<fr:property name="bundle" value="APPLICATION_RESOURCES"/>
				<fr:property name="defaultText" value="label.masterDegree.administrativeOffice.allExecutionYears"/>
				<fr:property name="key" value="true"/>
			</fr:layout>
			<fr:destination name="postback" path="<%="/searchScormContent.do?method=changeYear&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() %>"/>
		</fr:edit>	
		</td>
		<td>
			<bean:message key="label.executionPeriod" bundle="APPLICATION_RESOURCES"/>:
			<fr:edit id="executionPeriodField" name="bean" slot="executionPeriod">
			<fr:layout name="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionPeriodsForExecutionYear"/>
				<fr:property name="format" value="${name}"/>
				<fr:property name="bundle" value="APPLICATION_RESOURCES"/>
				<fr:property name="defaultText" value="label.masterDegree.administrativeOffice.allExecutionYears"/>
				<fr:property name="key" value="true"/>
			</fr:layout>
			</fr:edit>
		</td>
		<td colspan="2">
		</td>
		</tr>

		<logic:iterate id="searchElement" indexId="index" name="bean" property="searchElements">
		<tr>
			<td>
			<logic:notEqual name="index" value="0">
			<fr:edit id="<%="conjunctionType" + index%>" name="searchElement" slot="conjunction"/>
			</logic:notEqual>
			</td>
			<td><bean:message key="label.searchField"/> 
			<fr:edit id="<%="valueField" + index%>" name="searchElement" slot="queryValue" >
				<fr:layout>
					<fr:property name="size" value="40"/>
				</fr:layout>
			</fr:edit>
			</td>
			<td><fr:edit id="<%= "searchTypeField" + index%>" name="searchElement" slot="searchField"/></td>
			<td> 
			<logic:equal name="index" value="0">
				<div class="switchNone">
				<html:link page="<%="/searchScormContent.do?method=addNewSearchCriteria&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() + bean.getSearchElementsAsParameters() %>"><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:link>			
				</div>
				<div class="switchInline">
				<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='searchScormContent.do?method=addNewSearchCriteria&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() + bean.getSearchElementsAsParameters() + "'; getElementById('searchForm').submit();" %>">
				<bean:message key="label.add" bundle="APPLICATION_RESOURCES"/>
				</a>
				</div>
			</logic:equal>
			<logic:notEqual name="index" value="0">
				<div class="switchNone">
				<html:link page="<%="/searchScormContent.do?method=removeSearchCriteria&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index%>"><bean:message key="link.remove" bundle="APPLICATION_RESOURCES"/></html:link>
				</div>
				<div class="switchInline">
				<a href="#" onclick="<%= "javascript:getElementById('searchForm').action='searchScormContent.do?method=removeSearchCriteria&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() + bean.getSearchElementsAsParameters() + "&amp;removeIndex=" + index + "'; getElementById('searchForm').submit();"%>">
				<bean:message key="link.remove" bundle="APPLICATION_RESOURCES"/>
				</a>
				</div>
			</logic:notEqual>
		
			</td>
		</tr>
		</logic:iterate>


		</table>
			<html:submit><bean:message key="label.search" /></html:submit>
	</fr:form>

<logic:present name="searchResult">
<logic:notEmpty name="searchResult">

<p><bean:message key="label.hitCount" />: <strong><fr:view name="totalItems"/></strong></p>


<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 
<cp:collectionPages url="<%= 
	"/teacher/searchScormContent.do?method=moveIndex" + bean.getSearchElementsAsParameters() + 
	"&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + 
	"&amp;itemID=" + item.getIdInternal()%>" 
	pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>
</p>
</logic:notEqual>


<ul>
<logic:iterate id="file" name="searchResult" type="net.sourceforge.fenixedu.domain.File">

		 <li class="mtop1">
		 	<logic:present name="file">
		 	 <fr:view name="file" property="item.section.site.executionCourse.nome"/> (<fr:view name="file" property="item.section.site.executionCourse.executionPeriod.name"/> - <fr:view name="file" property="item.section.site.executionCourse.executionYear.year"/>) <br/> 
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />  <fr:view name="file" property="displayName"></fr:view> (<a href="<fr:view name="file" property="downloadUrl"/>"><fr:view name="file" property="filename"/></a>)
			</logic:present>
    	 </li>

</logic:iterate>
</ul>

<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 
<cp:collectionPages url="<%= 
	"/teacher/searchScormContent.do?method=moveIndex" + bean.getSearchElementsAsParameters() + 
	"&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + 
	"&amp;itemID=" + item.getIdInternal()%>" 
	pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>
</p>
</logic:notEqual>


</logic:notEmpty>

<logic:empty name="searchResult">
	<bean:message key="label.search.noResultsFound" /> 
</logic:empty>
</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>