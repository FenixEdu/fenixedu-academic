<%@ page language="java" %> 
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="publications" name="LOGGED_USER_ATTRIBUTE" property="person.researchResultPublications"/>
<bean:define id="personId" name="person" property="externalId"/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.management.title"/></h2>

<logic:messagesPresent name="messages" message="true">
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></strong></p>
	</html:messages>
</logic:messagesPresent>

<logic:notPresent name="preferredSetting">
	<ul class="list5 mbottom2">
<!--
		<li>
			<html:link page="/resultPublications/prepareCreate.do">
				<bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.ResultPublication.insert.link"/>
			</html:link>
		</li>
		<li>
			<html:link page="/publications/bibtexManagement.do?method=prepareOpenBibtexFile">
				<bean:message bundle="RESEARCHER_RESOURCES"	key="researcher.result.publication.importBibtex"/>
			</html:link>
		</li>
 -->
		<li>
			<html:link target="_blank" page="<%="/publications/bibtexManagement.do?method=exportAllPublicationsToBibtex&amp;personOID=" + personId %>">
					<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportAllToBibTeX" />
			</html:link>
		</li>
<!-- 
		<bean:define id="firstOID" name="executionYearIntervalBean" property="firstExecutionYear.externalId"/>
		<bean:define id="lastOID" name="executionYearIntervalBean" property="finalExecutionYear.externalId"/>
        <li>
        	<html:link page="<%= "/publications/management.do?method=prepareSetPreferredPublications&firstOID=" + firstOID + "&lastOID=" + lastOID %>">
                <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.setPreferredPublications"/>
            </html:link>
        </li>
        <li>
        	<html:link page="<%= "/publications/management.do?method=setUnitToAll" %>">
                <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.associate.unit.all.publications"/>
            </html:link>
        </li>
 -->
	</ul>

	<div class="error3">
		<p>
			<bean:message bundle="RESEARCHER_RESOURCES" key="message.moved.to.sotis"/>
		</p>
	</div>

<!-- 
	<div class="error3">
		<p>
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.moved.to.sotis" arg0="<a href='https://id.ist.utl.pt/cas/login?service=https%3A%2F%2Fbarra.tecnico.ulisboa.pt%2Flogin%2F%3Fnext%3Dhttps%253A%252F%252Fsotis.ist.utl.pt%252Fsotis-ui%252F%2523researcher'>sotis</a>"/>
		</p>
	</div>
 -->
	
	<fr:form action="/resultPublications/listPublications.do">
	<fr:edit id="executionYearIntervalBean" name="executionYearIntervalBean" visible="false"/>
	
	<p class="mbottom025"><bean:message key="label.choosen.interval" bundle="RESEARCHER_RESOURCES"/>:</p>
	
	<table class="tstyle5 mtop025">
	<tr>
		<td>
			<bean:message key="label.start" bundle="RESEARCHER_RESOURCES"/>:	  
			<fr:edit id="firstYear" name="executionYearIntervalBean" slot="firstExecutionYear">
				<fr:layout name="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
				<fr:property name="format" value="${year}"/>
				<fr:property name="defaultText" value="label.undefined"/>
				<fr:property name="key" value="true"/>
				<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
			</fr:layout>
			</fr:edit>
		</td>						  
		<td>
			<bean:message key="label.end" bundle="RESEARCHER_RESOURCES"/>:

			<fr:edit id="finalYear" name="executionYearIntervalBean" slot="finalExecutionYear">
				<fr:layout name="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
				<fr:property name="format" value="${year}"/>
				<fr:property name="defaultText" value="label.undefined"/>
				<fr:property name="key" value="true"/>
				<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
			</fr:layout>
			</fr:edit>

			<html:submit><bean:message key="label.filter" bundle="RESEARCHER_RESOURCES"/></html:submit>
		</td>
	</tr>
	</table>
	</fr:form>
</logic:notPresent>

<logic:present name="preferredSetting">
    <h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.preferredPublications.title"/></h3>
	<div class="infoop2">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.preferredPublications.help" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/>
	</div>
	
	<p>
		<strong><bean:message key="label.selectedInterval" bundle="RESEARCHER_RESOURCES"/></strong>: <fr:view name="first" property="year"/> - <fr:view name="last" property="year"/>
	</p>
	
	<p>
		<html:link action="/publications/management.do?method=listPublications"><bean:message bundle="RESEARCHER_RESOURCES" key="label.back"/></html:link>
	</p>
</logic:present>

<logic:empty name="publications">
	<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.emptyList"/></em></p>
</logic:empty>


	
	
<logic:notEmpty name="publications">

<logic:present name="preferredSetting">
       <jsp:include page="publicationsCategories.jsp" />
</logic:present>

<logic:notPresent name="preferredSetting">
    <jsp:include page="publicationsCategories.jsp" />
    <p class="mtop15">
    	<bean:define id="firstOID" name="executionYearIntervalBean" property="firstExecutionYear.externalId"/>
		<bean:define id="lastOID" name="executionYearIntervalBean" property="finalExecutionYear.externalId"/>
    
    	<em>
	        <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.preferredPublications.help.management"/>
			<html:link page="<%= "/publications/management.do?method=prepareSetPreferredPublications&firstOID=" + firstOID + "&lastOID=" + lastOID %>">
                <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.setPreferredPublications"/>
            </html:link>
    	</em>
    </p>
</logic:notPresent>

</logic:notEmpty>
