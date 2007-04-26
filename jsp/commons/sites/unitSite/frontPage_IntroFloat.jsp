<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<logic:equal name="site" property="showBanner" value="true">

<bean:define id="style" type="java.lang.String" value="width: 100%; float: left;" toScope="request"/>
 
<bean:define id="banner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner" name="site" property="currentBanner" toScope="request"/>

<logic:notEmpty name="site" property="banners">
 <bean:define id="banner" name="site" property="currentBanner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner" toScope="request"/>
 <bean:define id="style" type="java.lang.String" value="<%= "width: 100%; float: left; " +  ((banner.getColor()!=null) ? "background-color: " + banner.getColor() + ";" : "") + (banner.hasBackgroundImage() ? " background-image: url('" + banner.getBackgroundImage().getDownloadUrl() +"'); background-repeat: repeat-x;" : "") %>" toScope="request"/>
</logic:notEmpty>

<div class="usitebanner" style="<%=  style %>">
	<div id="version" style="width: 100px; position: absolute; top: 0; right: 0;">
		<html:form action="/changeLocaleTo.do">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.windowLocation" property="windowLocation" value=""/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newLanguage" property="newLanguage" value=""/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newCountry" property="newCountry" value=""/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newVariant" property="newVariant" value=""/>
			 
	            <logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
					<input type="image" src="<%= request.getContextPath() %>/images/flags/pt.gif" alt="<bean:message key="pt" bundle="IMAGE_RESOURCES" />" title="Português" value="PT"
					 onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();" />
					<input class="activeflag" type="image" src="<%= request.getContextPath() %>/images/flags/en.gif" alt="English" title="English" value="EN" 
					onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();"/>
				</logic:notEqual>
					
	            <logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">			
					<input class="activeflag" type="image" src="<%= request.getContextPath() %>/images/flags/pt.gif" alt="Português" title="Português" value="PT"
					 onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();" />
					<input type="image" src="<%= request.getContextPath() %>/images/flags/en.gif" alt="English" title="English" value="EN" 
					onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();"/>
				</logic:notEqual>
		
			</html:form>
	</div>
	<logic:equal name="site" property="showIntroduction" value="true">
		<div class="usiteintrofloated">
			<fr:view name="site"  property="description" layout="html"/>
		</div>
	</logic:equal>
	<logic:present name="banner" >
		<img src="<%= banner.getMainImage().getDownloadUrl() %>"/>
	</logic:present>
</div>
<div style="clear: both;"></div>

</logic:equal>

<logic:notEqual name="site" property="showBanner" value="true">
	<logic:equal name="site" property="showIntroduction" value="true">
		<div class="usiteintro">
			<fr:view name="site"  property="description" layout="html"/>
		</div>
	</logic:equal>
</logic:notEqual>

<jsp:include flush="true" page="mainBody.jsp"/>