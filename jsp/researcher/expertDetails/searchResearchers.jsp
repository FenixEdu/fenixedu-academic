<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.find.an.expert"/></h2>

<bean:define id="action" value="/searchResearchers.do?method=searchByKeyword"/>
<logic:present name="searchByName">
	<bean:define id="action" value="/searchResearchers.do?method=searchByName"/>
</logic:present>


		<bean:message key="link.Search" bundle="RESEARCHER_RESOURCES"/> 
		| <html:link page="/searchResearchers.do?method=browseByDepartment"><bean:message key="label.browser.by.department" bundle="RESEARCHER_RESOURCES"/></html:link>


<fr:form action="/searchResearchers.do?method=searchByName">
	<table class="tstyle5 mbottom0">
		<tr>
			<td style="width: 100px;">
					<bean:message key="label.name" bundle="RESEARCHER_RESOURCES"/>:
			</td>
			<td>
			<fr:edit id="nameSearch" name="nameBean" slot="string">
				<fr:layout>
					<fr:property name="classes" value="mvert0"/>
					<fr:property name="size" value=""/>
				</fr:layout>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>	
			</fr:edit>
			</td>
			<td>
			<html:submit><bean:message key="label.search" bundle="RESEARCHER_RESOURCES"/></html:submit>
			</td>
			<td>
			<fr:hasMessages for="nameSearch" type="validation">
						<fr:messages>
							<span class="error0"><fr:message /></span>
						</fr:messages>
			</fr:hasMessages>
			</td>
		</tr>
	</table>
</fr:form>

<fr:form action="/searchResearchers.do?method=searchByKeyword">
	<table class="tstyle5 mtop0">
		<tr>
			<td style="width: 100px;">
					<bean:message key="label.keywords" bundle="RESEARCHER_RESOURCES"/>:
			</td>
			<td>
			<fr:edit id="keywordSearch" name="keywordBean" slot="string">
				<fr:layout>
					<fr:property name="classes" value="mvert0"/>
					<fr:property name="size" value=""/>
				</fr:layout>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>	
			</fr:edit>
			</td>
			<td>
			<html:submit><bean:message key="label.search" bundle="RESEARCHER_RESOURCES"/></html:submit>
			</td>
			<td>
			<fr:hasMessages for="keywordSearch" type="validation">
						<fr:messages>
							<span class="error0"><fr:message /></span>
						</fr:messages>
			</fr:hasMessages>
			</td>
		</tr>
	</table>
</fr:form>

					

<logic:notEmpty name="researchers">
	
	   <bean:size id="listSize" name="researchers"/>
	   
	   <p><bean:message key="label.hit.count" bundle="RESEARCHER_RESOURCES" arg0="<%= listSize.toString() %>"/></p>
	    
		<logic:iterate name="researchers" id="researcherIterator">
			<bean:define id="researcher" name="researcherIterator" toScope="request"/>
			<jsp:include page="showResearcher.jsp"/>
		</logic:iterate>
	
</logic:notEmpty>

<logic:empty name="researchers">
	<logic:present name="nameBean" property="string">
			<bean:message key="label.no.results.found" bundle="RESEARCHER_RESOURCES"/>
	</logic:present>
	
	<logic:present name="keywordBean" property="string">
			<bean:message key="label.no.results.found" bundle="RESEARCHER_RESOURCES"/>
	</logic:present>
</logic:empty>