<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h3><bean:message key="title.editBibligraphicReference"/></h3>
<html:form action="/bibliographicReferenceManager">
   	<html:hidden property="page" value="1"/>
    <fieldset style="width: 500; height: 315; padding: 2">
      	
    		<h2><bean:message key="message.insertBibliographyData"/></h2>
    	
      
    <h2><bean:message key="message.bibliographicReferenceTitle"/></h2>
	<span class="error"><html:errors property="title"/></span>
    <p><html:textarea rows="4" cols="56" property="title"/></p>
    
    <h2><bean:message key="message.bibliographicReferenceAuthors"/></h2>
   	<span class="error"><html:errors property="authors"/></span>
    <p><html:textarea rows="4" cols="56" property="authors"/></p>

    <h2><bean:message key="message.bibliographicReferenceReference"/></h2>
	<span class="error"><html:errors property="reference"/></span>
    <p><html:textarea rows="2" cols="56" property="reference"/></p>

    <h2><bean:message key="message.bibliographicReferenceYear"/></h2>
    <span class="error"><html:errors property="year"/></span>
    <p><html:text property="year"/></p>
    
    <h2><bean:message key="message.bibliographicReferenceOptional"/></h2>
    <p><html:checkbox property="optional" value="yes"/></p>
    <p>
    
    <center>
    
    <logic:notPresent name="edit">
    	<html:hidden property="method" value="createBibliographicReference"/>
   	 <h3>	<html:submit styleClass="inputbutton">
    	   	<bean:message key="button.confirm"/>                    		         	
    	</html:submit></h3>       
    </logic:notPresent>
    
    <logic:present name="edit">
       	<html:hidden property="method" value="editBibliographicReference"/>
   	 <h3>	<html:submit styleClass="inputbutton">
    	   	<bean:message key="button.confirm"/>                    		         	
    	</html:submit>  </h3>      
    </logic:present>	
    	
    </center></p>
    </fieldset>
</html:form>
