<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/istLayout.jsp" >
     <put name="title" value="Instituto Superior Técnico" /> 
	 <put name="symbols_row" value="/publico/degreeSite/symbolsRow.jsp" />
     <put name="profile_navigation" value="/publico/degreeSite/profileNavigation.jsp" />
	 <put name="main_navigation" value="/publico/degreeSite/degreeSiteMainNavigation.jsp" />
	 <put name="body" value="/publico/viewClasses_bd.jsp" />
  	 <put name="footer" value="/publico/degreeSite/footer.jsp" />
</tiles:insert>
