<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.research.result.publication.PreferredPublicationPriority"%>
<html:xhtml/>

<bean:define id="currentSchema" name="currentSchema" scope="request" type="java.lang.String"/>
<table class="publications mtop1">
<logic:iterate id="result" name="results" scope="request">
        <bean:define id="resultId" name="result" property="externalId"/>
	<tr>
	    <td class="priority">
	    <logic:present name="preferredSetting">
	    	<bean:define id="firstId" name="first" property="externalId"></bean:define>
	    	<bean:define id="lastId" name="last" property="externalId"></bean:define>
	    	<fr:form action="<%= "/publications/management.do?method=prepareSetPreferredPublications&firstOID=" + firstId + "&lastOID=" + lastId  %>">
				<fr:edit name="result" slot="preferredLevel" nested="true">
				    <fr:layout name="menu-postback">
	    			    <fr:property name="defaultOptionHidden" value="true" />
				    </fr:layout>
				</fr:edit>
			</fr:form>
	    </logic:present>
	    <logic:notPresent name="preferredSetting">
	        <bean:define id="level" name="result" property="preferredLevel.name" type="java.lang.String" />
	        <span title='<bean:message bundle="ENUMERATION_RESOURCES" key="<%= PreferredPublicationPriority.class.getSimpleName() + "." + level + ".description"  %>"/>'>
	           <fr:view name="result" property="preferredLevel" />
	        </span>
	    </logic:notPresent>
	    </td>
	    <td>
            <fr:view name="result" layout="nonNullValues" schema="<%=currentSchema%>">
                <fr:layout>
                    <fr:property name="htmlSeparator" value=", "/>
                    <fr:property name="indentation" value="false"/>
                </fr:layout>
                <fr:destination name="view.publication" path="<%= "/resultPublications/showPublication.do?resultId=" + resultId %>"/>
            </fr:view>
            (<html:link target="_blank" page="<%="/publications/bibtexManagement.do?method=exportPublicationToBibtex&publicationId="+ resultId%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.publication.exportToBibTeX" /></html:link>)
	    </td>
	</tr>
</logic:iterate>
</table>