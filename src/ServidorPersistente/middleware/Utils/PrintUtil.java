/*
 * Created on 1/Mar/2003 by jpvl
 *
 */
package ServidorPersistente.middleware.Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import ServidorPersistente.middleware.MigrationLesson;
import ServidorPersistente.middleware.constants.Constants;

/**
 * @author jpvl
 */
public abstract class PrintUtil {

    /**
     * @param migrationLessonsList
     */
    static public void printMigrationLessonList(List migrationLessonsList) {

        Iterator keysSetIterator = Constants.filePathPredicateForMigrationLessonHashMap.keySet()
                .iterator();

        while (keysSetIterator.hasNext()) {
            String filePath = (String) keysSetIterator.next();
            List predicatedList = (List) CollectionUtils.select(migrationLessonsList,
                    (Predicate) Constants.filePathPredicateForMigrationLessonHashMap.get(filePath));
            PrintUtil.printMigrationLessonList(predicatedList, filePath);
        }

    }

    static public void printMigrationLessonList(List migrationLessonList, String filePath) {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(filePath);
            PrintWriter pw = new PrintWriter(output, true);
            Iterator iterator = migrationLessonList.iterator();
            pw.println("Número de aulas " + migrationLessonList.size());
            while (iterator.hasNext()) {
                MigrationLesson migrationLesson = (MigrationLesson) iterator.next();
                pw.println(migrationLesson.toString());
                pw.println("");
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void printList(Collection list, String filePath) {
        try {
            FileOutputStream output = new FileOutputStream(filePath);
            PrintWriter pw = new PrintWriter(output, true);
            pw.println("Size=" + list.size());
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Object element = iter.next();
                pw.println(element.toString());
            }
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void printList(Collection list, String filePath, Predicate predicate) {
        Collection listAfterPredicate = CollectionUtils.select(list, predicate);
        printList(listAfterPredicate, filePath);
    }

}