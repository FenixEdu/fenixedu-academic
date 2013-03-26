<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem, java.sql.Timestamp" %>

<bean:define id="sectionsList" name="infoWebSite" property="sections" />
<h2><bean:message key="label.sections.configuration"/></h2>
<span class="error"><html:errors/></span>

<html:form action="/sectionsConfiguration">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="configureSections"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=pageContext.findAttribute("objectCode").toString()%>"/>
	<table>
		<tr><td colspan="6" class="infoop"><bean:message key="message.sectionsConfiguration.instructions"/></td></tr>
		<tr><td colspan="6"><br/></td></tr>
		<tr>
			<th class="listClasses-header"><bean:message key="label.websiteSection"/></th>
			<th class="listClasses-header"><bean:message key="label.ftp"/></th>
			<th class="listClasses-header"><bean:message key="label.sorting.field"/></th>
			<th class="listClasses-header"><bean:message key="label.sorting.order"/></th>
			<th class="listClasses-header"><bean:message key="label.posts.number"/></th>
			<th class="listClasses-header"><bean:message key="label.excerpt.size"/></th>
		</tr>
		<logic:notEmpty name="sectionsList" >
			<bean:size id="sectionsSize" name="sectionsList" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.sectionsSize" property="sectionsSize" value="<%= sectionsSize.toString() %>" />
			<logic:iterate id="section" name="sectionsList">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" name="section" property="idInternal" indexed="true"/>
				<tr>
					<td class="listClasses">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" name="section" property="name" size="20" indexed="true"/>
					</td>
					<td class="listClasses">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.ftpName" name="section" property="ftpName" size="10" indexed="true"/>
					</td>
					<td class="listClasses">
						<html:select name="section" property="whatToSort" indexed="true">
							<html:option key="label.initial" value="ITEM_BEGIN_DAY"/>
							<html:option key="label.final" value="ITEM_END_DAY"/>
							<html:option key="label.creation.date" value="CREATION_DATE"/>
						</html:select>
					</td>
					<td class="listClasses">
						<html:select name="section" property="sortingOrder" indexed="true">
							<html:option key="message.ascendent" value="ascendent"/>
							<html:option key="message.descendent" value="descendent"/>
						</html:select>
					</td>
					<td class="listClasses">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.size" name="section" property="size" size="2" indexed="true"/>
					</td>
					<td class="listClasses">
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.excerptSize" name="section" property="excerptSize" size="2" indexed="true"/>
						<bean:message key="label.words"/>
					</td>
				</tr>
			</logic:iterate> 
		</logic:notEmpty>
	</table>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>                    		         	
	</html:submit> 
</html:form>
