/*
 * Created on Mar 24, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoLink;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.slide.common.SlideException;

/**
 * @author jmota
 *  
 */
public class CMSUtils {

    /**
     * @param fileSuport
     * @param item
     * @param infoItem
     * @throws SlideException
     */
    public static List getItemLinks(IFileSuport fileSuport, String slideName) throws SlideException {
        List files = fileSuport.getDirectoryFiles(slideName);
        if (files != null && !files.isEmpty()) {
            List links = new ArrayList();
            Iterator iterFiles = files.iterator();
            while (iterFiles.hasNext()) {
                FileSuportObject file = (FileSuportObject) iterFiles.next();
                InfoLink infoLink = new InfoLink();
                infoLink.setLink(file.getFileName());
                infoLink.setLinkName(file.getLinkName());
                Collections.sort(links, new BeanComparator("linkName"));
                links.add(infoLink);
            }
            return links;
        }
        return null;
    }
}