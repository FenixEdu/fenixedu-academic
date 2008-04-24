package net.sourceforge.fenixedu.presentationTier.renderers.providers.executionCourse;

import net.sourceforge.fenixedu.domain.FileContent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemPermissionBean;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

import org.apache.commons.fileupload.FileItem;

public class FileItemGroupProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
        Site site;
        
        if (source instanceof FileItem) {
            FileContent fileItem = (FileContent) source;
            site = fileItem.getSite();
        }
        else if (source instanceof FileItemCreationBean) {
            FileItemCreationBean bean = (FileItemCreationBean) source;
            site = bean.getItem().getSection().getSite();
        }
        else if (source instanceof FileItemPermissionBean) {
            FileItemPermissionBean bean = (FileItemPermissionBean) source;
            site = bean.getFileItem().getSite();
        }
        else {
            throw new RuntimeException("type of source not supported: " + source);
        }
        
        return site.getContextualPermissionGroups();
    }

    public Converter getConverter() {
        return null;
    }

}
