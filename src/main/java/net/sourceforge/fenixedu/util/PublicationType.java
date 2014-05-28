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
/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class PublicationType extends FenixValuedEnum {

    public static final int COMUNICATION_TYPE = 1;

    public static final int MAG_ARTICLE_TYPE = 2;

    public static final int AUTHOR_BOOK_TYPE = 3;

    public static final int EDITOR_BOOK_TYPE = 4;

    public static final int ARTICLES_CHAPTERS_TYPE = 5;

    public static PublicationType COMUNICATION = new PublicationType("Comunication", COMUNICATION_TYPE);

    public static PublicationType MAG_ARTICLE = new PublicationType("MagazineArticle", MAG_ARTICLE_TYPE);

    public static PublicationType AUTHOR_BOOK = new PublicationType("AuthorBook", AUTHOR_BOOK_TYPE);

    public static PublicationType EDITOR_BOOK = new PublicationType("EditBook", EDITOR_BOOK_TYPE);

    public static PublicationType ARTICLES_CHAPTERS = new PublicationType("ArticlesChapters", ARTICLES_CHAPTERS_TYPE);

    public PublicationType(String name, int value) {
        super(name, value);
    }

    public static PublicationType getEnum(String publicationType) {
        return (PublicationType) getEnum(PublicationType.class, publicationType);
    }

    public static PublicationType getEnum(int publicationType) {
        return (PublicationType) getEnum(PublicationType.class, publicationType);
    }

    public static Map getEnumMap() {
        return getEnumMap(PublicationType.class);
    }

    public static List getEnumList() {
        return getEnumList(PublicationType.class);
    }

    public static Iterator iterator() {
        return iterator(PublicationType.class);
    }

    @Override
    public String toString() {
        String result = "Publication Type Type :\n";
        result += "\n  - Publication Type : " + this.getName();

        return result;
    }
}