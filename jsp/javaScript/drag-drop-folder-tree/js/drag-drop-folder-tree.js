	/************************************************************************************************************
	(C) www.dhtmlgoodies.com, July 2006
	
	Update log:
	
	
	This is a script from www.dhtmlgoodies.com. You will find this and a lot of other scripts at our website.	
	
	Terms of use:
	You are free to use this script as long as the copyright message is kept intact. However, you may not
	redistribute, sell or repost it without our permission.
	
	Thank you!
	
	www.dhtmlgoodies.com
	Alf Magne Kalleland
	
	************************************************************************************************************/
		
	var JSTreeObj;
	var treeUlCounter = 0;
	var nodeId = 1;
		
	/* Constructor */
	function JSDragDropTree()
	{
		var idOfTree;
		var imageFolder;
		var folderImage;
		var leafImage;
		var plusImage;
		var minusImage;
		var blankImage;
		var maximumDepth;
		var dragNode_source;
		var dragNode_parent;
		var dragNode_sourceNextSib;
		var dragNode_noSiblings;
		var dragNode_noChildren;
		
		var dragNode_destination;
		var floatingContainer;
		var dragDropTimer;
		var dropTargetIndicator;
		var insertAsSub;
		var indicator_offsetX;
		var indicator_offsetX_sub;
		var indicator_offsetY;
		
		var onComplete;
		var onError;
		var saveUrl;
		var saveParameter;
		var fieldId;
		var includeImage;
		var movedClass;

		this.saveParameter = 'saveString';

		this.imageFolder = 'images/';
		this.folderImage = 'folder.gif';
		this.leafImage = 'sheet.gif'
		this.plusImage = 'plus.gif';
		this.minusImage = 'minus.gif';
		this.blankImage = 'blank.gif';
		this.maximumDepth = 10000;
		this.includeImage = true;
		var messageMaximumDepthReached;
		var ajaxObjects;
		
		this.floatingContainer = document.createElement('UL');
		this.floatingContainer.style.position = 'absolute';
		this.floatingContainer.style.display='none';
		this.floatingContainer.id = 'floatingContainer';
		this.insertAsSub = false;
		document.body.appendChild(this.floatingContainer);
		this.dragDropTimer = -1;
		this.dragNode_noSiblings = false;
		
		if(document.all){
			this.indicator_offsetX = 2;	// Offset position of small black lines indicating where nodes would be dropped.
			this.indicator_offsetX_sub = 4;
			this.indicator_offsetY = 2;
		}else{
			this.indicator_offsetX = 1;	// Offset position of small black lines indicating where nodes would be dropped.
			this.indicator_offsetX_sub = 3;
			this.indicator_offsetY = 2;			
		}
		if(navigator.userAgent.indexOf('Opera')>=0){
			this.indicator_offsetX = 2;	// Offset position of small black lines indicating where nodes would be dropped.
			this.indicator_offsetX_sub = 3;
			this.indicator_offsetY = -7;				
		}

		this.messageMaximumDepthReached = ''; // Use '' if you don't want to display a message 
		this.ajaxObjects = new Array();
	}
	
	
	/* JSDragDropTree class */
	JSDragDropTree.prototype = {
		
		Get_Cookie : function(name) { 
		   var start = document.cookie.indexOf(name+"="); 
		   var len = start+name.length+1; 
		   if ((!start) && (name != document.cookie.substring(0,name.length))) return null; 
		   if (start == -1) return null; 
		   var end = document.cookie.indexOf(";",len); 
		   if (end == -1) end = document.cookie.length; 
		   return unescape(document.cookie.substring(len,end)); 
		} 
		,
		// This function has been slightly modified
		Set_Cookie : function(name,value,expires,path,domain,secure) { 
			expires = expires * 60*60*24*1000;
			var today = new Date();
			var expires_date = new Date( today.getTime() + (expires) );
		    var cookieString = name + "=" +escape(value) + 
		       ( (expires) ? ";expires=" + expires_date.toGMTString() : "") + 
		       ( (path) ? ";path=" + path : "") + 
		       ( (domain) ? ";domain=" + domain : "") + 
		       ( (secure) ? ";secure" : ""); 
		    document.cookie = cookieString; 
		} 
		,setMaximumDepth : function(maxDepth)
		{
			this.maximumDepth = maxDepth;	
		}
		,setMessageMaximumDepthReached : function(newMessage)
		{
			this.messageMaximumDepthReached = newMessage;
		}
		,	
		setImageFolder : function(path)
		{
			this.imageFolder = path;	
		}
		,
		setFolderImage : function(imagePath)
		{
			this.folderImage = imagePath;			
		}
		,
		setLeafImage : function(imagePath)
		{
			this.leafImage = imagePath;			
		}
		,
		setPlusImage : function(imagePath)
		{
			this.plusImage = imagePath;				
		}
		,
		setMinusImage : function(imagePath)
		{
			this.minusImage = imagePath;			
		}
		,		
		setTreeId : function(idOfTree)
		{
			this.idOfTree = idOfTree;
			this.floatingContainer.id = idOfTree + "-container";	
		}
		,
		setOnComplete : function(f) 
		{
			this.onComplete = f;
		}
		,
		setOnError : function(f) 
		{
			this.onError = f;
		}
		,
		setRequestUrl : function(url) 
		{
			this.requestUrl = url;
		}
		,
		setRequestParameter : function(parameter) 
		{
			this.requestParameter = parameter;
		}
		,
		setFieldId : function(id) 
		{
			this.fieldId = id;
		}
		,
		setIncludeImage : function(include) 
		{
			this.includeImage = include;
		}
		,
		setMovedClass : function(movedClass) 
		{
			this.movedClass = movedClass;
		}
		,
		expandAll : function()
		{
			var menuItems = document.getElementById(this.idOfTree).getElementsByTagName('LI');
			for(var no=0;no<menuItems.length;no++){
				var subItems = menuItems[no].getElementsByTagName('UL');
				if(subItems.length>0 && subItems[0].style.display!='block'){
					JSTreeObj.showHideNode(false,menuItems[no].id.replace(/[^0-9]/g,''));
				}			
			}
		}	
		,
		collapseAll : function()
		{
			var menuItems = document.getElementById(this.idOfTree).getElementsByTagName('LI');
			for(var no=0;no<menuItems.length;no++){
				var subItems = menuItems[no].getElementsByTagName('UL');
				if(subItems.length>0 && subItems[0].style.display=='block'){
					JSTreeObj.showHideNode(false,menuItems[no].id.replace(/[^0-9]/g,''));
				}			
			}		
		}	
		,
		/*
		Find top pos of a tree node
		*/
		getTopPos : function(obj){
			var curtop = 0;
			if (obj.offsetParent)
			{
				while (obj.offsetParent)
				{
					curtop += obj.offsetTop
					obj = obj.offsetParent;
				}
			}
			else if (obj.y)
				curtop += obj.y;

			if (document.all) 
				return curtop/1 + 13;
			else
				return curtop/1 + 13;
		}
		,	
		/*
		Find left pos of a tree node
		*/
		getLeftPos : function(obj){
			var curleft = 0;
			if (obj.offsetParent)
			{
				while (obj.offsetParent)
				{
					curleft += obj.offsetLeft
					obj = obj.offsetParent;
				}
			}
			else if (obj.x)
				curleft += obj.x;

			if (document.all) 
				return curleft/1 - 2;
			else
				return curleft;
		}	
		,
		showHideNode : function(e,inputId)
		{
			if(inputId){
				if(!document.getElementById('dhtmlgoodies_treeNode'+inputId))return;
				thisNode = document.getElementById('dhtmlgoodies_treeNode'+inputId).getElementsByTagName('IMG')[0]; 
			}else {
				thisNode = this;
				if(this.tagName != 'IMG')thisNode = this.parentNode.getElementsByTagName('IMG')[0];	
			}
			
			if(thisNode.style.visibility=='hidden')return;		
			var parentNode = thisNode.parentNode;
			inputId = parentNode.id.replace(/[^0-9]/g,'');
			if(thisNode.src.indexOf(JSTreeObj.plusImage)>=0){
				thisNode.src = thisNode.src.replace(JSTreeObj.plusImage,JSTreeObj.minusImage);
				var ul = parentNode.getElementsByTagName('UL')[0];
				ul.style.display='block';
				if(!initExpandedNodes)initExpandedNodes = ',';
				if(initExpandedNodes.indexOf(',' + inputId + ',')<0) initExpandedNodes = initExpandedNodes + inputId + ',';
			}else{
				thisNode.src = thisNode.src.replace(JSTreeObj.minusImage,JSTreeObj.plusImage);
				parentNode.getElementsByTagName('UL')[0].style.display='none';
				initExpandedNodes = initExpandedNodes.replace(',' + inputId,'');
			}	
			JSTreeObj.Set_Cookie('dhtmlgoodies_expandedNodes_' + JSTreeObj.idOfTree, initExpandedNodes, 500);
			return false;
		}
		,
		/* Initialize drag */
		initDrag : function(e)
		{
			if(document.all)e = event;	
			
			var subs = JSTreeObj.floatingContainer.getElementsByTagName('LI');
			if(subs.length>0){
				if(JSTreeObj.dragNode_sourceNextSib){
					JSTreeObj.dragNode_parent.insertBefore(JSTreeObj.dragNode_source,JSTreeObj.dragNode_sourceNextSib);
				}else{
					JSTreeObj.dragNode_parent.appendChild(JSTreeObj.dragNode_source);
				}					
			}
			
			JSTreeObj.dragNode_source = this.parentNode;
			JSTreeObj.dragNode_parent = this.parentNode.parentNode;
			JSTreeObj.dragNode_sourceNextSib = false;

			
			if(JSTreeObj.dragNode_source.nextSibling)JSTreeObj.dragNode_sourceNextSib = JSTreeObj.dragNode_source.nextSibling;
			JSTreeObj.dragNode_destination = false;
			JSTreeObj.dragDropTimer = 0;
			JSTreeObj.timerDrag();
			return false;
		}
		,
		timerDrag : function()
		{	
			if(this.dragDropTimer>=0 && this.dragDropTimer<10){
				this.dragDropTimer = this.dragDropTimer + 1;
				setTimeout('JSTreeObj.timerDrag()',20);
				return;
			}
			if(this.dragDropTimer==10)
			{
				JSTreeObj.floatingContainer.style.display='block';
				JSTreeObj.floatingContainer.appendChild(JSTreeObj.dragNode_source);	
			}
		}
		,
		moveDragableNodes : function(e)
		{
			if(JSTreeObj.dragDropTimer<10)return;
			if(document.all)e = event;
			dragDrop_x = e.clientX/1 + 5 + document.body.scrollLeft;
			dragDrop_y = e.clientY/1 + 5 + document.documentElement.scrollTop;	
					
			JSTreeObj.floatingContainer.style.left = dragDrop_x + 'px';
			JSTreeObj.floatingContainer.style.top = dragDrop_y + 'px';
			
			var thisObj = this;
	
			if (thisObj != document.documentElement) {
				while ((! thisObj.tagName) || (thisObj.tagName != 'LI')) {
					if (thisObj.parentNode) {
						thisObj = thisObj.parentNode;
					}
					else {
						break;
					}
				}
			}
			
			JSTreeObj.dragNode_noSiblings = false;
			var tmpVar = thisObj.getAttribute('noSiblings');
			if(!tmpVar)tmpVar = thisObj.noSiblings;
			if(tmpVar=='true')JSTreeObj.dragNode_noSiblings=true;
		
			JSTreeObj.dragNode_noChildren = false;
			tmpVar = thisObj.getAttribute('noChildren');
			if(!tmpVar)tmpVar = thisObj.noChildren;
			if(tmpVar=='true')JSTreeObj.dragNode_noChildren=true;

			if (JSTreeObj.dragNode_noSiblings && JSTreeObj.dragNode_noChildren) {
				return false;
			}
		
			if(thisObj && thisObj.id)
			{
				JSTreeObj.dragNode_destination = thisObj;
				var img = thisObj.getElementsByTagName('IMG')[1];
				var tmpObj= JSTreeObj.dropTargetIndicator;
				tmpObj.style.display='block';
				
				var eventSourceObj = this;
				if(JSTreeObj.dragNode_noSiblings && eventSourceObj.tagName=='IMG') {
					eventSourceObj = eventSourceObj.nextSibling;
				}
				if(JSTreeObj.dragNode_noChildren && eventSourceObj.tagName != 'IMG') {
					eventSourceObj = img;
				}
				
				var tmpImg = tmpObj.getElementsByTagName('IMG')[0];
				if((this.tagName != 'IMG' || JSTreeObj.dragNode_noSiblings) && !JSTreeObj.dragNode_noChildren){
					tmpImg.src = tmpImg.src.replace('ind1','ind2');	
					JSTreeObj.insertAsSub = true;
					tmpObj.style.left = (JSTreeObj.getLeftPos(eventSourceObj) + JSTreeObj.indicator_offsetX_sub) + 'px';
				}else{
					tmpImg.src = tmpImg.src.replace('ind2','ind1');
					JSTreeObj.insertAsSub = false;
					tmpObj.style.left = (JSTreeObj.getLeftPos(eventSourceObj) + JSTreeObj.indicator_offsetX) + 'px';
				}
				
				tmpObj.style.top = (JSTreeObj.getTopPos(thisObj) + JSTreeObj.indicator_offsetY) + 'px';
			}
			
			return false;
		}
		,
		dropDragableNodes:function()
		{
			if(JSTreeObj.dragDropTimer<10){				
				JSTreeObj.dragDropTimer = -1;
				return;
			}
			var showMessage = false;
			if(JSTreeObj.dragNode_destination){	// Check depth
				var countUp = JSTreeObj.dragDropCountLevels(JSTreeObj.dragNode_destination,'up');
				var countDown = JSTreeObj.dragDropCountLevels(JSTreeObj.dragNode_source,'down');
				var countLevels = countUp/1 + countDown/1 + (JSTreeObj.insertAsSub?1:0);		
				
				if(countLevels>JSTreeObj.maximumDepth){
					JSTreeObj.dragNode_destination = false;
					showMessage = true; 	// Used later down in this function
				}
			}
			
			
			if(JSTreeObj.dragNode_destination){			
				if(JSTreeObj.insertAsSub){
					var uls = JSTreeObj.dragNode_destination.getElementsByTagName('UL');
					if(uls.length>0){
						ul = uls[0];
						ul.style.display='block';
						
						var lis = ul.getElementsByTagName('LI');

						if(lis.length>0){	// Sub elements exists - drop dragable node before the first one
							ul.insertBefore(JSTreeObj.dragNode_source,lis[0]);	
						}else {	// No sub exists - use the appendChild method - This line should not be executed unless there's something wrong in the HTML, i.e empty <ul>
							ul.appendChild(JSTreeObj.dragNode_source);	
						}
					}else{
						var ul = document.createElement('UL');
						ul.style.display='block';
						JSTreeObj.dragNode_destination.appendChild(ul);
						ul.appendChild(JSTreeObj.dragNode_source);
					}
					var img = JSTreeObj.dragNode_destination.getElementsByTagName('IMG')[0];					
					img.style.visibility='visible';
					img.src = img.src.replace(JSTreeObj.plusImage,JSTreeObj.minusImage);					
					
					
				}else{
					if(JSTreeObj.dragNode_destination.nextSibling){
						var nextSib = JSTreeObj.dragNode_destination.nextSibling;
						nextSib.parentNode.insertBefore(JSTreeObj.dragNode_source,nextSib);
					}else{
						JSTreeObj.dragNode_destination.parentNode.appendChild(JSTreeObj.dragNode_source);
					}
				}	
				/* Clear parent object */
				var tmpObj = JSTreeObj.dragNode_parent;
				var lis = tmpObj.getElementsByTagName('LI');
				if(lis.length==0){
					var img = tmpObj.parentNode.getElementsByTagName('IMG')[0];
					img.style.visibility='hidden';	// Hide [+],[-] icon
					tmpObj.parentNode.removeChild(tmpObj);						
				}
				
				// set class
				if (JSTreeObj.movedClass) {
					var target = JSTreeObj.dragNode_source.getElementsByTagName('SPAN')[0];
				
					if (target.className) {
						if (target.className.indexOf(JSTreeObj.movedClass) < 0) {
							target.className = target.className + ' ' + JSTreeObj.movedClass;
						}
					}
					else {
						target.className = JSTreeObj.movedClass;
					}
				}
			}else{
				// Putting the item back to it's original location
				
				if(JSTreeObj.dragNode_sourceNextSib){
					JSTreeObj.dragNode_parent.insertBefore(JSTreeObj.dragNode_source,JSTreeObj.dragNode_sourceNextSib);
				}else{
					JSTreeObj.dragNode_parent.appendChild(JSTreeObj.dragNode_source);
				}			
					
			}
			JSTreeObj.dropTargetIndicator.style.display='none';		
			JSTreeObj.dragDropTimer = -1;	
			if(showMessage && JSTreeObj.messageMaximumDepthReached)alert(JSTreeObj.messageMaximumDepthReached);
		}
		,
		createDropIndicator : function()
		{
			this.dropTargetIndicator = document.createElement('DIV');
			this.dropTargetIndicator.style.position = 'absolute';
			this.dropTargetIndicator.style.display='none';			
			var img = document.createElement('IMG');
			img.src = this.imageFolder + 'dragDrop_ind1.gif';
			img.id = 'dragDropIndicatorImage';
			this.dropTargetIndicator.appendChild(img);
			document.body.appendChild(this.dropTargetIndicator);
			
		}
		,
		dragDropCountLevels : function(obj,direction,stopAtObject){
			var countLevels = 0;
			if(direction=='up'){
				while(obj.parentNode && obj.parentNode!=stopAtObject){
					obj = obj.parentNode;
					if(obj.tagName=='UL')countLevels = countLevels/1 +1;
				}		
				return countLevels;
			}	
			
			if(direction=='down'){ 
				var subObjects = obj.getElementsByTagName('LI');
				for(var no=0;no<subObjects.length;no++){
					countLevels = Math.max(countLevels,JSTreeObj.dragDropCountLevels(subObjects[no],"up",obj));
				}
				return countLevels;
			}	
		}		
		,
		cancelEvent : function()
		{
			return false;	
		}
		,
		cancelSelectionEvent : function()
		{
			
			if(JSTreeObj.dragDropTimer<10)return true;
			return false;	
		}
		,saveTree : function(initObj,saveString)
		{
			
			if(!saveString)var saveString = '';
			if(!initObj){
				initObj = document.getElementById(this.idOfTree);

			}
			var lis = initObj.getElementsByTagName('LI');

			if(lis.length>0){
				var li = lis[0];
				while(li){
					if(li.id){
						if(saveString.length>0)saveString = saveString + ',';
	
						saveString = saveString + li.id.replace(/[^0-9]/gi,'');
						saveString = saveString + '-';
						if(li.parentNode.id!=this.idOfTree)saveString = saveString + li.parentNode.parentNode.id.replace(/[^0-9]/gi,''); else saveString = saveString + '0';
						
						var ul = li.getElementsByTagName('UL');
						if(ul.length>0){
							saveString = this.saveTree(ul[0],saveString);	
						}	
					}			
					li = li.nextSibling;
				}
			}

			if(initObj.id == this.idOfTree) {
				if (this.fieldId) {
					field = document.getElementById(this.fieldId);
					field.value = saveString;
					field.form.submit();
				}
				else {
					var ajaxIndex = this.ajaxObjects.length;
					this.ajaxObjects[ajaxIndex] = new sack();
	
					var url;
					
					if (this.requestUrl.indexOf('?') >= 0) {
	 					url = this.requestUrl + '&' + this.requestParameter + '=' + saveString;
					}
					else {
						url = this.requestUrl + '?' + this.requestParameter + '=' + saveString;
					}
	
					this.ajaxObjects[ajaxIndex].method = "GET";
					this.ajaxObjects[ajaxIndex].requestFile = url;
					this.ajaxObjects[ajaxIndex].onCompletion = function() { JSTreeObj.saveComplete(ajaxIndex); } ;
					this.ajaxObjects[ajaxIndex].onError = function() { JSTreeObj.reportError(ajaxIndex); } ;
	
					this.ajaxObjects[ajaxIndex].runAJAX();
				}
			}
			// The nodes could be saved by submitting a form or by ajax
			/* Examples:
			
			1) By submitting a form:
			
			* Create form
			
			<form name="myForm" id="myForm" method="Post" action="aFile.php">
				<input type="hidden" name="hiddenNodes">
			</form>
			
			* Update the hidden form field from this function
			
			document.forms['myForm'].hiddenNodes.value = saveString
			
			* Submit the form
			
			document.forms['myForm'].submit;
			
			2) By ajax: 
			
			Simply use the ajax library you find in the ajax section
			
			var ajaxObj = new sack();
			var url = 'aFile.php?saveString=' + saveString;
			ajaxObj.requestFile = url;	// Specifying which file to get
			ajaxObj.onCompletion = aFunctionName;	// Specify function that will be executed after file has been found
			ajaxObj.runAJAX();		// Execute AJAX function	

			*/
			return saveString;
		}
		,
		saveComplete : function(index)
		{
			if (this.onComplete) {
				this.onComplete();
			}
		}
		,
		reportError : function(index) {
			if (this.onError) {
				this.onError();
			}
		}
		,
		initTree : function()
		{
			JSTreeObj = this;
			JSTreeObj.createDropIndicator();
			document.documentElement.onselectstart = JSTreeObj.cancelSelectionEvent;
			document.documentElement.ondragstart = JSTreeObj.cancelEvent;
			var nodeId = 0;
			var dhtmlgoodies_tree = document.getElementById(this.idOfTree);
			var menuItems = dhtmlgoodies_tree.getElementsByTagName('LI');	// Get an array of all menu items
			for(var no=0;no<menuItems.length;no++){
				menuItems[no].style.listStyleType = 'none';
			
				// No children var set ?
				var noChildren = false;
				var tmpVar = menuItems[no].getAttribute('noChildren');
				if(!tmpVar)tmpVar = menuItems[no].noChildren;
				if(tmpVar=='true')noChildren=true;
				// No drag var set ?
				var noDrag = false;
				var tmpVar = menuItems[no].getAttribute('noDrag');
				if(!tmpVar)tmpVar = menuItems[no].noDrag;
				if(tmpVar=='true')noDrag=true;
						 
				nodeId++;
				var subItems = menuItems[no].getElementsByTagName('UL');
				var img = document.createElement('IMG');
				img.src = this.imageFolder + this.plusImage;
				img.onclick = JSTreeObj.showHideNode;
				
				if(subItems.length==0)img.style.visibility='hidden';else{
					subItems[0].style.display = 'none'; // force display
					subItems[0].id = 'tree_ul_' + treeUlCounter;
					treeUlCounter++;
				}
				
				var aTag = menuItems[no].getElementsByTagName('*')[0];
				if (aTag.tagName == 'IMG') {
					aTag = menuItems[no].getElementsByTagName('*')[1];
				}
				
				//aTag.onclick = JSTreeObj.showHideNode;
				if(!noDrag)aTag.onmousedown = JSTreeObj.initDrag;
				aTag.onmousemove = JSTreeObj.moveDragableNodes;
				menuItems[no].id = 'dhtmlgoodies_treeNode' + nodeId;

				var folderImg = false;
				if (this.includeImage) {
					folderImg = document.createElement('IMG');
				}
				else {
					var images = menuItems[no].getElementsByTagName('IMG');
					if (images.length > 0) {
						folderImg = images[0];
					}
				}
				
				if (folderImg) {
					if(!noDrag)folderImg.onmousedown = JSTreeObj.initDrag;
					folderImg.onmousemove = JSTreeObj.moveDragableNodes;
				
					if (this.includeImage) {
						if (menuItems[no].className) {
							folderImg.src = this.imageFolder + menuItems[no].className + ".gif";
						}
						else {
							if (!noChildren) {
								folderImg.src = this.imageFolder + this.folderImage;
							} else {
								folderImg.src = this.imageFolder + this.leafImage;
							}
						}
					}
				}
				else {
					folderImage = document.createElement('IMG');
					folderImg.src = this.imageFolder + this.blankImage;
					menuItems[no].insertBefore(folderImg, aTag);
				}

				if (this.includeImage) {
					menuItems[no].insertBefore(img, aTag);
					menuItems[no].insertBefore(folderImg, aTag);
				}
				else {
					if (folderImg) {
						menuItems[no].insertBefore(img, folderImg);			
					}
					else {
						menuItems[no].insertBefore(img, aTag);					
					}
				}
			}

			initExpandedNodes = this.Get_Cookie('dhtmlgoodies_expandedNodes_' + this.idOfTree);
			if(initExpandedNodes){
				var nodes = initExpandedNodes.split(',');
				for(var no=0;no<nodes.length;no++){
					if(nodes[no])this.showHideNode(false,nodes[no]);	
				}			
			}			
			
			document.documentElement.onmousemove = JSTreeObj.moveDragableNodes;	
			document.documentElement.onmouseup = JSTreeObj.dropDragableNodes;
		}		
	}