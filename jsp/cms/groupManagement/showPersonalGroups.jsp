<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<%@ page import="net.sourceforge.fenixedu.domain.PersonalGroup" %>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.GroupTypes"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<logic:present name="person">
	
    <bean:define id="groupsIterator" type="java.util.Iterator" scope="request" property="personalGroupsIterator" name="person"/>
    <bean:define id="numberOfGroups" type="java.lang.Integer" property="personalGroupsCount" name="person"/>    
    
	<h3><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.title.label" /></h3>
	<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.foundGroups.label" arg0="<%= numberOfGroups.toString() %>"/>

    <logic:greaterThan name="numberOfGroups" value="0">	
        	<table width="100%">
        		<tr>
        			<th class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
        			</th>
        			<th class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.description.label"/>
        			</th>
        			<th class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.type.label"/>
        			</th>
        			<th class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.groupCardinality.label"/>
        			</th>
        			<th class="listClasses-header">&nbsp;
        			</th>
        			<th class="listClasses-header">&nbsp;
        			</th>
        		</tr>
        		<logic:iterate id="personalGroup" name="groupsIterator" type="net.sourceforge.fenixedu.domain.PersonalGroup">
        			<bean:define id="readableGroupType" value="<%= GroupTypes.userGroupTypeByClass(personalGroup.getGroup().getClass()).toString() %>"/>
                <bean:define id="groupSize" name="personalGroup" property="group.elementsCount" type="java.lang.Integer"/>
                    
        			<tr>
        				<td class="listClasses"><bean:write name="personalGroup" property="name"/></td>
        				<td class="listClasses"><bean:write name="personalGroup" property="description"/></td>
        				<td class="listClasses"><bean:message bundle="CMS_RESOURCES" name="readableGroupType" bundle="ENUMERATION_RESOURCES"/></td>
        				<td class="listClasses"><bean:write name="groupSize"/> 
                        <bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.elements.label"/>
                    </td>
                    <%
                        Map params = new HashMap();
                        params.put("method", "viewElements");
                        params.put("groupId", personalGroup.getIdInternal());
                    
                        request.setAttribute("params", params);
                    
                        Integer size = new Integer(personalGroup.getGroup().getElementsCount());
                        request.setAttribute("groupSize", size);
                     %>
        				<td class="listClasses">
                        <html:link  name="params" module="/cms" action="/personalGroupsManagement" target="_blank" ><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.viewElements.link"/></html:link>
                    </td>
                    <%
                        params.put("method", "deleteGroup");
                     %>
        				<td class="listClasses">
                        <html:link name="params" module="/cms" action="/personalGroupsManagement" ><bean:message  bundle="CMS_RESOURCES" key="cms.delete.label"/></html:link>
                    </td>
        			</tr>
        		</logic:iterate>
        	</table>
    </logic:greaterThan>
    
	<h3><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.createNewGroup.label" /></h3>
    
	<html:form action="/personalGroupsManagement" method="get">
        <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectUserGroupTypeToAdd"/>
        
		<table>
		  <tr>
			<td width="10%"">
				<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.type.label" />
			</td>
            
			<td width="90%"><e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.accessControl.GroupTypes" bundle="ENUMERATION_RESOURCES"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.userGroupType" property="userGroupType">
    	    		         <html:options collection="values" property="value" labelProperty="label" />
				</html:select>
                
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.create.label"/>
				</html:submit>        		
			</td>	
		  </tr>
		</table>
	</html:form>
</logic:present>
