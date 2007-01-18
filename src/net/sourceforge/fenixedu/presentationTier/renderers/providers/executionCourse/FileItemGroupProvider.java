package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import net.sourceforge.fenixedu.domain.FileItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemPermissionBean;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class FileItemGroupProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        Item item;
        
        if (source instanceof FileItem) {
            FileItem fileItem = (FileItem) source;
            item = fileItem.getItem();
        }
        else if (source instanceof FileItemCreationBean) {
            FileItemCreationBean bean = (FileItemCreationBean) source;
            item = bean.getItem();
        }
        else if (source instanceof FileItemPermissionBean) {
            FileItemPermissionBean bean = (FileItemPermissionBean) source;
            item = bean.getFileItem().getItem();
        }
        else {
            throw new RuntimeException("type of source not supported: " + source);
        }
        
        return item.getSection().getSite().getContextualPermissionGroups();
    }

    public Converter getConverter() {
        return null;
    }

}
