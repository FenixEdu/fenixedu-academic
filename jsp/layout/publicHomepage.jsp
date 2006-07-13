<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<%--
<link rel="stylesheet" type="text/css" media="screen" href="http://www.ist.utl.pt/css/iststyle.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />
--%>
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/dotist_print.css" />
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/print.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/homepage_screen.css" />

<title><tiles:getAsString name="title" ignore="true" /></title>

</head>
<body>

<%--
<tiles:insert attribute="symbols_row" ignore="true"/>
<tiles:insert attribute="profile_navigation" ignore="true"/>
<tiles:insert attribute="main_navigation" ignore="true"/>
<tiles:getAsString name="body-inline" ignore="true"/>
<tiles:insert attribute="footer" ignore="true"/>
--%>



<!-- START HEADER -->
<div id="logoist"><img src="../images/homepage/wwwist.gif" width="234" height="51" alt="<bean:message key="wwwist" bundle="IMAGE_RESOURCES" />" /></div>
<!-- END HEADER -->

<p class="skip"><a href="#re">Skip to the content</a></p>

<div id="container">


<!-- BEGIN NAV -->
<ul id="nav">
	<li style="list-style: none; padding-top: 3em; "><b>Geral</b></li>
	<tiles:insert attribute="profile_navigation" ignore="true"/>
	<li style="list-style: none; padding-top: 2em; "><b>Página Pessoal</b></li>
	<tiles:insert attribute="main_navigation" ignore="true"/>
</ul>
<!-- END NAV -->

<!--START MAIN CONTENT -->
<div id="content">

	 <div id="version">
		<html:form action="/changeLocaleTo.do">
			<html:hidden property="windowLocation" value=""/>
			<html:hidden property="newLanguage" value=""/>
			<html:hidden property="newCountry" value=""/>

			<logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
				<input type="image" src="<%= request.getContextPath() %>/images/flags/pt.gif" alt="<bean:message key="pt" bundle="IMAGE_RESOURCES" />" title="Português" value="PT"
				 onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.windowLocation.value=window.location;this.form.submit();" />
				<input class="activeflag" type="image" src="<%= request.getContextPath() %>/images/flags/en.gif" alt="<bean:message key="en" bundle="IMAGE_RESOURCES" />" title="English" value="EN" 
				onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.windowLocation.value=window.location;this.form.submit();"/>
			</logic:notEqual>
			
			<logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">			
				<input class="activeflag" type="image" src="<%= request.getContextPath() %>/images/flags/pt.gif" alt="<bean:message key="pt" bundle="IMAGE_RESOURCES" />" title="Português" value="PT"
				 onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.windowLocation.value=window.location;this.form.submit();" />
				<input type="image" src="<%= request.getContextPath() %>/images/flags/en.gif" alt="<bean:message key="en" bundle="IMAGE_RESOURCES" />" title="English" value="EN" 
				onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.windowLocation.value=window.location;this.form.submit();"/>
			</logic:notEqual>
		</html:form>
	</div> 
		
	<%--
	<div id="photo_holder">
		<img src="img/pasantos.jpg" alt="[Foto] Pedro Santos at IST Mathematical Department" width="150" height="100" />
	</div>
	
	<div id="bio_info">
		<h1 id="no">Pedro Alexandre Simões dos Santos</h1>
		<p>Unidade: Departamento de Informática</p>
		<p>Categoria: Professor Associado</p>
		<p>E-mail: something@ist.utl.pt</p>
		<p>Telefone: 213872828</p>
		<p>Telefone móvel: 916756453</p>
		<p>Página alternativa: <a href="">http://www.something.com</a></p>
    </div>

	--%>

	<div id="bio_info">
	<tiles:insert attribute="body" ignore="true"/>
	</div>

        
</div>
<!--END MAIN CONTENT -->

</div>

</body>
</html:html>