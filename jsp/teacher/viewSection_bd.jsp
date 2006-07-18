<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors /></span>

<logic:present name="siteView">
<bean:define id="infoSiteSection" name="siteView" property="component"/>
<bean:define id="section" name="infoSiteSection" property="section"/>
<bean:define id="currentSectionCode" name="section" property="idInternal"/>
	
<h2><bean:message key="label.section"/><bean:write name="section" property="name"/></h2>
<table	 cellpadding="0" border="0">
	<tr> 
		<td>
			<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<html:link page="<%= "/editSection.do?method=prepareEditSection&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;currentSectionCode=" + currentSectionCode %>">		
					<bean:message key="button.editSection"/>
				</html:link>
			</div>
		</td>
		<td>
			<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<bean:define id="sectionName" name="section" property="name"/>
				<logic:notEmpty name="section" property="superiorInfoSection">
					<bean:define id="superiorSection" name="section" property="superiorInfoSection"/>
					<bean:define id="superiorSectionCode" name="superiorSection" property="idInternal"/>
					<html:link page="<%= "/deleteSection.do?method=deleteSection&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;currentSectionCode=" + currentSectionCode + "&amp;superiorSectionCode=" + superiorSectionCode %>" onclick="<%= "return confirm('Tem a certeza que deseja apagar a sec��o " + sectionName + " ?')"%>">		
						<bean:message key="button.deleteSection"/>
					</html:link>
				</logic:notEmpty>
				<logic:empty name="section" property="superiorInfoSection">
					<html:link page="<%= "/deleteSection.do?method=deleteSection&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;currentSectionCode=" + currentSectionCode %>" onclick="<%= "return confirm('Tem a certeza que deseja apagar a sec��o " + sectionName + " ?')"%>">		
						<bean:message key="button.deleteSection"/>
					</html:link>
				</logic:empty>
			</div>
		</td>
											
		<td>
			<div class="gen-button">
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<%-- <bean:define id="sectionCode" name="section" property="idInternal"/> --%>
				<html:link page="<%= "/sectionManagement.do?method=prepareCreateRegularSection&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;currentSectionCode=" + currentSectionCode %>">
					<bean:message key="button.insertSubSection"/>
				</html:link>
			</div>
		</td>
		<td>
			<div class="gen-button">
				<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<html:link page="<%= "/insertItem.do?method=prepareInsertItem&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;currentSectionCode=" + currentSectionCode %>">
					<bean:message key="button.insertItem"/>
				</html:link>
			</div>
		</td>
</tr>
</table>
<br />   
<bean:define id="itemsList" name="infoSiteSection" property="items"/>

<logic:iterate id="item" name="itemsList">
<bean:define id="itemCode" name="item" property="idInternal"/>
<div class="greytxt"><strong><bean:message key="label.item"/>:&nbsp;<bean:write name="item" property="name"/></strong></div>
<br/>
<table>
	<tr>			
		<td>
			<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<html:link page="<%= "/editItem.do?method=prepareEditItem&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;itemCode=" + itemCode %>">
					<bean:message key="button.editItem"/>
				</html:link>
			</div>
		</td>
		<td>
			<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<html:link page="<%= "/deleteItem.do?method=deleteItem&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;itemCode=" + itemCode + "&amp;currentSectionCode=" + currentSectionCode%>" onclick="return confirm('Tem a certeza que deseja apagar este item?')">
					<bean:message key="button.deleteItem"/>
				</html:link>
			</div>
		</td>
		<td>
			<div class="gen-button">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" /> 
				<html:link page="<%="/prepareFileUpload.do?method=prepareFileUpload&objectCode=" + pageContext.findAttribute("objectCode") + "&amp;itemCode=" + itemCode + "&amp;currentSectionCode=" + currentSectionCode%>">
					Inserir Ficheiro
				</html:link>
			</div>
		</td>
	</tr>
</table>  
<br />
<br />
    <logic:equal name="item" property="urgent" value="true"><font color="red"></logic:equal> 		
  	<bean:write name="item" property="information" filter="false" />
  	<logic:equal name="item" property="urgent" value="true"></font></logic:equal>
  	<logic:present name="item" property="infoFileItems">
  	<br/>
  	<br/>  			
  		<table>	
		<logic:iterate id="infoFileItem" name="item" property="infoFileItems">
		<tr>
		<bean:define id="displayName" name="infoFileItem" property="displayName" type="java.lang.String"/>
		<bean:define id="externalStorageIdentification" name="infoFileItem" property="externalStorageIdentification" type="java.lang.String"/>
		<bean:define id="filename" name="infoFileItem" property="filename" type="java.lang.String"/>
		<bean:define id="fileItemId" name="infoFileItem" property="idInternal" type="java.lang.Integer"/>
		<bean:define id="permittedGroupType" name="infoFileItem" property="permittedGroupType" type="net.sourceforge.fenixedu.domain.FileItemPermittedGroupType"/>
		<td>
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		</td>
		<td>
			<html:link href="<%= pageContext.findAttribute("fileDownloadUrlFormat") + "/" + externalStorageIdentification + "/" + filename %>" ><bean:write name="displayName"/>&nbsp;&nbsp;(<bean:message key="<%=permittedGroupType.toString() %>" bundle="ENUMERATION_RESOURCES"/>)</html:link>
		</td>
		<td>&nbsp;&nbsp;<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		</td>
		<td>
			<html:link page="<%= "/fileDelete.do?method=deleteFile&objectCode=" + pageContext.findAttribute("objectCode") + "&amp;itemCode=" + itemCode + "&amp;currentSectionCode=" + currentSectionCode + "&fileItemId=" + fileItemId %>"   onclick="<%= "return confirm('Tem a certeza que deseja apagar o ficheiro "+displayName+"?')"%>" ><bean:message key="label.teacher.siteAdministration.viewSection.deleteItemFile"/></html:link>
		</td>
		<td>
			<html:link page="<%= "/editItemFilePermissions.do?method=prepareEditItemFilePermissions&objectCode=" + pageContext.findAttribute("objectCode") + "&amp;itemCode=" + itemCode + "&amp;currentSectionCode=" + currentSectionCode + "&fileItemId=" + fileItemId %>" ><bean:message key="label.teacher.siteAdministration.viewSection.editItemFilePermissions"/></html:link>
		</td>
		</tr>
		</logic:iterate>
		</table>
		
  	</logic:present>
  	
<br> 
<br> 
</logic:iterate>
</logic:present>