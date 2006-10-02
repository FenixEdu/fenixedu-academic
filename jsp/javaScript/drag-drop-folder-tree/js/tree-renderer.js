/**
 * Helper javascript functions for the TreeRenderer.
 *
 * initializing a tree:
 *   treeRenderer_init(treeId, options);
 *
 *   options:
 *     includeImage: if standard images should be included (required)
 *     imageFolder: base folder path where images will be searched (required)
 *     fieldId: name of the field to update with the structure when saveTree() is called
 *     movedClass: class to set on items that are moved
 *     linkClasses: class of the span containing the links to be hidden
 *
 * saving structure and submiting form containing updated field:
 *   treeRenderer_saveTree(treeId)
 *
 * collapsing/expanding tree:
 *   treeRenderer_(expand|collapse)All(treeId);
 */

var treeRenderer_map = {};

function treeRenderer(id) {
    return treeRenderer_map[id];
}

function treeRenderer_saveTree(id) {
    treeRenderer(id).saveTree();
}

function treeRenderer_expandAll(id) {
    treeRenderer(id).expandAll();
}

function treeRenderer_collapseAll(id) {
    treeRenderer(id).collapseAll();
}

function treeRenderer_hideLinks(id, linkClasses) {
    var items = document.getElementById(id).getElementsByTagName('SPAN');
    for (var item=0; item<items.length; item++) {
        var span = items[item];

        if (span.className && span.className.indexOf(linkClasses) >= 0) {
            var childSpans = span.getElementsByTagName('SPAN');
            if (childSpans.length > 0) {
                span.getElementsByTagName('SPAN')[0].style.display = 'none';
            }
        }
    }
}

function treeRenderer_addOnLoad(func) {
    if (typeof document.onload != 'function') {
        window.onload = func;
    } else {
        var oldonload = document.onload;
        document.onload = function() {
            if (oldonload) {
                oldonload();
            }
            func();
        }
    }
}

function treeRenderer_init(id, options) {
	treeRenderer_addOnLoad(function () {
	    var tree = new JSDragDropTree();
	    tree.setTreeId(id);
	    
	    treeRenderer_map[id] = tree;
	    
	    tree.setImageFolder(options.imageFolder);
	    tree.setIncludeImage(options.includeImage);
	
	    options.requestUrl       && tree.setRequestUrl(options.requestUrl);
	    options.fieldId          && tree.setFieldId(options.fieldId);
	    options.movedClass       && tree.setMovedClass(options.movedClass);
	    options.requestParameter && tree.setRequestParameter(options.requestParameter);
	    options.onComplete       && tree.setOnComplete(options.onComplete);
	    options.onError          && tree.setError(options.onError);
	
	    tree.initTree();
	    
	    if (options.linkClasses) {
	        treeRenderer_hideLinks(id, options.linkClasses);
	    }
    });
}
