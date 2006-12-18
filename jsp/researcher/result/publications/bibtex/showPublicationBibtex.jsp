<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<logic:present role="RESEARCHER">

<script language="JavaScript">
 window.resizeTo(800,390);
</script>

	<logic:present name="bibtex">
	<bean:define id="bibtex" name="bibtex" type="java.lang.String"/>
	<pre>
	<%= bibtex %>	
	</pre>
	</logic:present>
		
	<logic:notEmpty name="bibtexList">
		<logic:iterate id="bibtex" name="bibtexList" type="java.lang.String">
			<pre>
			<%= bibtex %>
			</pre>
		</logic:iterate>
	</logic:notEmpty>
</logic:present>



	
	 
	 
	
