<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.editBibligraphicReference"/></h2>

<logic:present name="siteView"> 
<bean:define id="bibliographicReference" name="siteView" property="component"/>

<html:form action="/createBibliographicReference">
<html:hidden property="page" value="1"/>
<bean:message key="message.insertBibliographyData"/>
<br />
<br />		    	
<strong><bean:message key="message.bibliographicReferenceTitle"/></strong>
	<span class="error"><html:errors property="title"/></span>
    <p><html:textarea rows="4" cols="56" property="title"/></p>
    
<strong><bean:message key="message.bibliographicReferenceAuthors"/></strong>
   	<span class="error"><html:errors property="authors"/></span>
    <p><html:textarea rows="4" cols="56" property="authors"/></p>

<strong><bean:message key="message.bibliographicReferenceReference"/></strong>
	<span class="error"><html:errors property="reference"/></span>
    <p><html:textarea rows="2" cols="56" property="reference"/></p>

<strong><bean:message key="message.bibliographicReferenceYear"/></strong>
    <span class="error"><html:errors property="year"/></span>
    <p><html:text property="year"/></p>
    
<strong><bean:message key="message.bibliographicReferenceOptional"/></strong>            
		<p>
			<html:select property="optional">
	    		<html:option key="option.bibliographicReference.optional" value="true"/>
	    		<html:option key="option.bibliographicReference.recommended" value="false"/>
	    	</html:select>
		</p>    	
<br />
<br />
<br />
<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
</html:submit>       
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  

<html:hidden property="method" value="createBibliographicReference"/>
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

</html:form>
</logic:present>