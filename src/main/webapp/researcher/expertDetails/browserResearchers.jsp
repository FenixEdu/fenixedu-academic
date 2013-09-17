<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.find.an.expert"/></h2>


	   <html:link page="/searchResearchers.do?method=search"><bean:message key="link.Search" bundle="RESEARCHER_RESOURCES"/></html:link> 
	| <bean:message key="label.browser.by.department" bundle="RESEARCHER_RESOURCES"/>

<p class="mtop15"><strong><bean:message key="label.executionCourse.departments" bundle="APPLICATION_RESOURCES"/>:</strong></p>

<style>
li {
padding-bottom: 0.3em; /* é preciso criar uma class para isto <--- */
}
</style>

<ul>
	<logic:iterate id="departmentIterator" name="departments">
		<bean:define id="departmentId" name="departmentIterator" property="externalId"/>
		<li>
		<html:link page="<%= "/searchResearchers.do?method=browseByDepartment&departmentId=" + departmentId %>">
			<fr:view name="departmentIterator" property="realName"/>
		</html:link>
		</li>
	</logic:iterate>
</ul>

	<logic:present name="department">
		
		<em><fr:view name="department" property="realName"/></em>
		<bean:size id="listSize" name="researchers"/>
	   
	   <p><bean:message key="label.hit.count" bundle="RESEARCHER_RESOURCES" arg0="<%= listSize.toString() %>"/></p>
	   
		<logic:notEmpty name="researchers">
			<logic:iterate name="researchers" id="researcherIterator">
				<bean:define id="researcher" name="researcherIterator" toScope="request"/>
				<jsp:include page="showResearcher.jsp"/>
			</logic:iterate>
		</logic:notEmpty>

	</logic:present>