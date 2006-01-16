<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants"%>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.Group" %>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.UserGroupTypes"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<h2>FIME: Groups are no longer domain objects so interfaces must be changed.</h2>

<%--
<logic:present name="person">
	<bean:define id="groupsIterator" type="java.util.Iterator" scope="request" property="userGroupsIterator" name="person"/>
	<bean:define id="numberOfGroups" type="java.lang.Integer" property="userGroupsCount" name="person"/>	
	
	<h2><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.title.label" /></h2>
	<bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.foundGroups.label" arg0="<%=numberOfGroups.toString()%>"/>
	
<logic:greaterThan name="numberOfGroups" value="0">	
	<table width="100%">
		<tr>
			<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
			</td>
			<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.description.label"/>
			</td>
			<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.type.label"/>
			</td>
			<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.groupCardinality.label"/>
			</td>			
			<td class="listClasses-header">&nbsp;
			</td>
			<td class="listClasses-header">&nbsp;
			</td>
			<td class="listClasses-header">&nbsp;
			</td>			
		</tr>			
		<logic:iterate id="group" name="groupsIterator" type="net.sourceforge.fenixedu.domain.accessControl.Group">
			<bean:define id="readableGroupType" value="<%=UserGroupTypes.userGroupTypeByClass(group.getClass()).toString()%>"/>
			<tr>
				<td class="listClasses"><bean:write name="group" property="name"/></td>
				<td class="listClasses"><bean:write name="group" property="description"/></td>
				<td class="listClasses"><bean:message  bundle="CMS_RESOURCES" name="readableGroupType" bundle="ENUMERATION_RESOURCES"/></td>
				<%
				Map params = new HashMap();
				params.put("method","viewElements");
				//params.put("groupId",group.getIdInternal());
				request.setAttribute("params",params);
				Integer size = new Integer(group.getElementsCount());
				request.setAttribute("groupSize",size);
				 %>
				<td class="listClasses"><bean:write name="groupSize"/> <bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.elements.label"/></td>
				<td class="listClasses"><html:link  name="params" module="/cms" action="/userGroupsManagement" target="_blank" ><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.viewElements.link"/></html:link></td>
				<td class="listClasses"><html:link  paramId="groupId" paramName="group" paramProperty="idInternal" module="/cms" action="/deleteUserGroup" ><bean:message  bundle="CMS_RESOURCES" key="cms.delete.label"/></html:link></td>
				<logic:empty name="group" property="mailingList">
					<%
					params.clear();
					params.put("method","prepareCreate");
					//params.put("groupId",group.getIdInternal());
					request.setAttribute("params",params);
					 %>				
					<td width="0%" class="listClasses"><html:link  name="params" module="/cms" action="/mailingListManagement" ><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.createMailingList.label"/></html:link></td>
				 </logic:empty>
				 <logic:notEmpty name="group" property="mailingList">
					<%
					params.clear();
					params.put("method","viewMailingList");
					//params.put("mailingListID",group.getKeyMailingList());
					request.setAttribute("params",params);
					 %>				 
 					<td width="0%" class="listClasses"><html:link  name="params" module="/cms" action="/mailingListManagement" ><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.viewMailingList.label"/></html:link></td>
				 </logic:notEmpty>
			</tr>			
		</logic:iterate>
	</table>
</logic:greaterThan>
	<h3><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.createNewGroup.label" /></h2>
	<html:form action="/userGroupsManagement" method="get">
		<table>
		<tr>
			<td width="10%"">
				<bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.type.label" />
			</td>
			<td width="90%"><e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.accessControl.UserGroupTypes" bundle="ENUMERATION_RESOURCES"/>
				<html:select property="userGroupType">
    	    		<html:options collection="values" property="value" labelProperty="label" />
				</html:select>        		
				<html:hidden property="method" value="selectUserGroupTypeToAdd"/>
				<html:submit styleClass="inputbutton">
					<bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.create.label"/>
				</html:submit>        		
			</td>	
		</tr>
		</table>
	</html:form>
</logic:present>
--%>