function switchDisplay(id) {
	element = document.getElementById(id);
	
	if(element.className == 'dnone') {
		element.className = 'dblock';
	} else {
		element.className = 'dnone';
	}
}

function showElement(id) {
	element = document.getElementById(id);
	
	element.className = 'dblock';
}

function hideElement(id) {
	element = document.getElementById(id);
	
	element.className = 'dnone';
}

function getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp('(^|\\s)'+searchClass+'(\\s|$)');
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}

function switchGlobal() {
	var blockElements = getElementsByClass('switchBlock');
	var inlineElements = getElementsByClass('switchInline');
	var noneElements = getElementsByClass('switchNone');
	
	for (i = 0; i < blockElements.length; i++) {
		blockElements[i].className = 'dblock';
	}
	
	for (i = 0; i < inlineElements.length; i++) {
		inlineElements[i].className = 'dinline';
	}
	
	for (i = 0; i < noneElements.length; i++) {
		noneElements[i].className = 'dnone';
	}
}
