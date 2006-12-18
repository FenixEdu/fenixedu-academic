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

<html:xhtml/>
<h2><bean:message key="label.search.scorm"/></h2>

<logic:equal name="searchType" value="simple">

 	<fr:form action="<%= "/searchScormContent.do?method=searchScormContents&amp;searchType=" + request.getAttribute("searchType") + "&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() %>">
		<fr:hasMessages for="search" type="validation">
			<p>
			<span class="error0"><bean:message key="label.requiredFieldsNotPresent"/></span>
			</p>
		</fr:hasMessages>
	
		<fr:edit id="search" name="bean" visible="false"/>
		
		<table class="tstyle5 thright thlight">
		<tr>
			<td><bean:message key="label.searchField"/> 
			<fr:edit id="valueField" name="bean" slot="value" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator">
				<fr:layout>
					<fr:property name="size" value="40"/>
				</fr:layout>
			</fr:edit>
			</td>
			<td><fr:edit id="searchTypeField" name="bean" slot="searchField" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator"/></td>
			<td> <html:link page="<%="/searchScormContent.do?method=prepareSearchScormContents&amp;searchType=advanced" + "&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() %>"><bean:message key="label.search.advanced" /></html:link></td>
		</tr>
		</table>
			<html:submit><bean:message key="label.search" /></html:submit>
	</fr:form>

</logic:equal>

<logic:equal name="searchType" value="advanced">

	<logic:messagesPresent message="true">
		<html:messages id="messages" message="true" >
		<p>
			<span class="error0"><bean:write name="messages" /></span>
		</p>
		</html:messages>
	</logic:messagesPresent>
	
	<fr:hasMessages for="editf1" type="validation">
		<span class="error0"><bean:message key="required.searchField" /></span>
	</fr:hasMessages>
	<fr:hasMessages for="editv1" type="validation">
		<span class="error0"><bean:message key="required.value" /></span>
	</fr:hasMessages>
		

 	<fr:form action="<%= "/searchScormContent.do?method=searchScormContent&amp;searchType=" + request.getAttribute("searchType") + "&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() %>">
		<fr:edit id="search" name="bean" nested="true" visible="false" />
		<table class="tstyle5">
		<tr>
		<td></td>
		<td>
			<fr:edit id="editf1" name="bean" slot="field1" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator">
				<fr:layout>
					<fr:property name="defaultText" value=""/>
				</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editv1" name="bean" slot="value1" validator="net.sourceforge.fenixedu.renderers.validators.RequiredValidator">
				<fr:layout>
					<fr:property name="size" value="40"/>
				</fr:layout>
			</fr:edit>
		</td>
		<td rowspan="3" style="vertical-align: bottom;">
			<html:link page="<%="/searchScormContent.do?method=prepareSearchScormContents&amp;searchType=simple" + "&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + "&amp;itemID=" + item.getIdInternal() %>"><bean:message key="label.search.simple" /></html:link>
		</td>
		</tr>
		<tr>
		<td>
			<fr:edit id="c1" name="bean" slot="firstConjunction">
			<fr:layout>
				<fr:property name="defaultText" value=""/>
			</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editf2" name="bean" slot="field2">
			<fr:layout>
				<fr:property name="defaultText" value=""/>
			</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editv2" name="bean" slot="value2">
			<fr:layout>
				<fr:property name="size" value="40"/>
			</fr:layout>
			</fr:edit>
		</td>
		</tr>
		<tr>
		<td>
			<fr:edit id="c2" name="bean" slot="secondConjunction">
			<fr:layout>
				<fr:property name="defaultText" value=""/>
			</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editf3" name="bean" slot="field3">
			<fr:layout>
				<fr:property name="defaultText" value=""/>
			</fr:layout>
			</fr:edit>
		</td>
		<td>
			<fr:edit id="editv3" name="bean" slot="value3">
			<fr:layout>
				<fr:property name="size" value="40"/>
			</fr:layout>
			</fr:edit>
		</td>
		</tr>
		</table>
			<html:submit><bean:message key="label.search" /></html:submit>
		</fr:form>


</logic:equal>

<logic:present name="searchResult">
<logic:notEmpty name="searchResult">

<p><bean:message key="label.hitCount" />: <strong><fr:view name="totalItems"/></strong></p>


<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 
<cp:collectionPages url="<%= 
	"/teacher/searchScormContent.do?method=moveIndex&amp;searchType=" + request.getAttribute("searchType")  
	+ "&amp;field1=" + request.getAttribute("field1") + "&amp;value1=" + request.getAttribute("value1") 
	+ "&amp;field2=" + request.getAttribute("field2") + "&amp;value1=" + request.getAttribute("value2") 
	+ "&amp;field3=" + request.getAttribute("field3") + "&amp;value1=" + request.getAttribute("value3") 
	+ "&amp;c1=" + request.getAttribute("c1") + "&amp;c2=" + request.getAttribute("c2") + 
	"&amp;sectionID=" + section.getIdInternal() + "&amp;executionCourseID=" + request.getParameter("executionCourseID") + 
	"&amp;itemID=" + item.getIdInternal()%>" 
	pageNumberAttributeName="page" numberOfPagesAttributeName="numberOfPages"/>
</p>
</logic:notEqual>


<ul>
<logic:iterate id="file" name="searchResult" type="net.sourceforge.fenixedu.domain.File">

		 <li class="mtop1">
		 	<logic:present name="file">
		 	<%--<fr:view name="file"/>--%>
		 	 <fr:view name="file" property="item.section.site.executionCourse.nome"/> (<fr:view name="file" property="item.section.site.executionCourse.executionYear.year"/>) <br/> 
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />  <fr:view name="file" property="displayName"></fr:view> (<a href="<fr:view name="file" property="downloadUrl"/>"><fr:view name="file" property="filename"/></a>)
			</logic:present>
    	 </li>

</logic:iterate>
</ul>

<logic:notEqual name="numberOfPages" value="1">
<p>
<bean:message key="label.page" bundle="SITE_RESOURCES"/>: 
<cp:collectionPages url="<%= 
	"/teacher/searchScormContent.do?method=moveIndex&amp;searchType=" + request.getAttribute("searchType")  
	+ "&amp;field1=" + request.getAttribute("field1") + "&amp;value1=" + request.getAttribute("value1") 
	+ "&amp;field2=" + request.getAttribute("field2") + "&amp;value1=" + request.getAttribute("value2") 
	+ "&amp;field3=" + request.getAttribute("field3") + "&amp;value1=" + request.getAttribute("value3") 
	+ "&amp;c1=" + request.getAttribute("c1") + "&amp;c2=" + request.getAttribute("c2") + 
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