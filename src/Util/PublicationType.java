/*
 * Created on Nov 13, 2003
 *  
 */
package Util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class PublicationType extends ValuedEnum
{

    public static final int COMUNICATION_TYPE = 1;
    public static final int MAG_ARTICLE_TYPE = 2;
    public static final int AUTHOR_BOOK_TYPE = 3;
    public static final int EDITOR_BOOK_TYPE = 4;
    public static final int ARTICLES_CHAPTERS_TYPE = 5;

    public static PublicationType COMUNICATION = new PublicationType("Comunication", COMUNICATION_TYPE);
    public static PublicationType MAG_ARTICLE = new PublicationType("MagazineArticle", MAG_ARTICLE_TYPE);
    public static PublicationType AUTHOR_BOOK = new PublicationType("AuthorBook", AUTHOR_BOOK_TYPE);
    public static PublicationType EDITOR_BOOK = new PublicationType("EditBook", EDITOR_BOOK_TYPE);
    public static PublicationType ARTICLES_CHAPTERS =
        new PublicationType("ArticlesChapters", ARTICLES_CHAPTERS_TYPE);

    public PublicationType(String name, int value)
    {
        super(name, value);
    }

    public static PublicationType getEnum(String publicationType)
    {
        return (PublicationType) getEnum(PublicationType.class, publicationType);
    }

    public static PublicationType getEnum(int publicationType)
    {
        return (PublicationType) getEnum(PublicationType.class, publicationType);
    }

    public static Map getEnumMap()
    {
        return getEnumMap(OrientationType.class);
    }

    public static List getEnumList()
    {
        return getEnumList(OrientationType.class);
    }

    public static Iterator iterator()
    {
        return iterator(OrientationType.class);
    }

    public String toString()
    {
        String result = "Publication Type Type :\n";
        result += "\n  - Publication Type : " + this.getName();

        return result;
    }
}
