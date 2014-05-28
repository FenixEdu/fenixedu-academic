/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.FileContent;

/**
 * 
 * @author naat
 * 
 */
public class InfoFileContent extends InfoObject {

    public static final Comparator<InfoFileContent> COMPARATOR_BY_DISPLAY_NAME = new Comparator<InfoFileContent>() {

        @Override
        public int compare(InfoFileContent o1, InfoFileContent o2) {
            return o1.getDisplayName().compareTo(o2.getDisplayName());
        }

    };

    private String name;

    private String displayName;

    private String mimeType;

    private String checksum;

    private String checksumAlgorithm;

    private Integer size;

    public InfoFileContent() {

    }

    public void copyFromDomain(FileContent fileItem) {
        super.copyFromDomain(fileItem);
        if (fileItem != null) {
            setName(fileItem.getFilename());
            setDisplayName(fileItem.getDisplayName());
            setMimeType(fileItem.getContentType());
            setChecksum(fileItem.getChecksum());
            setChecksumAlgorithm(fileItem.getChecksumAlgorithm());
            setSize(fileItem.getSize().intValue());
        }
    }

    /**
     * @param item
     * @return
     */
    public static InfoFileContent newInfoFromDomain(FileContent item) {
        InfoFileContent infoItem = null;
        if (item != null) {
            infoItem = new InfoFileContent();
            infoItem.copyFromDomain(item);
        }
        return infoItem;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    public void setChecksumAlgorithm(String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFilename() {
        return name;
    }

    public void setName(String filename) {
        this.name = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getHtmlFriendlyFilename() {
        return getFilename().replaceAll("&", "&amp;").replaceAll(" ", "%20");
    }

}