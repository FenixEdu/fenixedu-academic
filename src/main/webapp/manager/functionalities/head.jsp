<script type="text/javascript" src="ciapl/javaScript/drag-drop-folder-tree/js/ajax.js"></script>
<script type="text/javascript" src="ciapl/javaScript/drag-drop-folder-tree/js/drag-drop-folder-tree.js"></script>

<script type="text/javascript">
	function ddft_initializeTree(treeId, imageFolder, fieldId, movedClass, includeImage) {
		var topLevelTree = new JSDragDropTree();
	
		topLevelTree.setTreeId(treeId);
		topLevelTree.setImageFolder(imageFolder);
		topLevelTree.setFieldId(fieldId);
		topLevelTree.setMovedClass(movedClass);
		topLevelTree.setIncludeImage(includeImage);
		
		topLevelTree.initTree();
	}
</script>
