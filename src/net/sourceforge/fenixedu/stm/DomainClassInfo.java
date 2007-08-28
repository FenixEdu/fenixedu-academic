package net.sourceforge.fenixedu.stm;

import dml.DomainModel;
import dml.DomainClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.util.Map;
import java.util.IdentityHashMap;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.MetadataManager;

public class DomainClassInfo {

    private volatile static Map<Class,DomainClassInfo> classInfoMap;
    private volatile static DomainClassInfo[] classInfoById;

    static void initializeClassInfos() {
        PersistenceBroker broker = null;
        
        try {
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();

            // repeat until success
            while (true) {
                broker.beginTransaction();
            
                Connection conn = broker.serviceConnectionManager().getConnection();
                Statement stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery("SELECT DOMAIN_CLASS_NAME,DOMAIN_CLASS_ID FROM DOMAIN_CLASS_INFO");

                Map<Class,DomainClassInfo> map = new IdentityHashMap<Class,DomainClassInfo>();
                ArrayList<DomainClassInfo> array = new ArrayList<DomainClassInfo>();

                int maxId = 0;

                // read all infos
                while (rs.next()) {
                    String classname = rs.getString(1);
                    int cid = rs.getInt(2);

                    DomainClassInfo classInfo = new DomainClassInfo(findClass(classname), cid);

                    maxId = Math.max(maxId, cid);
                    addNewInfo(map, array, classInfo);
                }

                // create any missing records
                try {
                    DomainModel model = MetadataManager.getDomainModel();

                    for (DomainClass domClass : model.getDomainClasses()) {
                        Class javaClass = Class.forName(domClass.getFullName());
                        if (! map.containsKey(javaClass)) {
                            DomainClassInfo classInfo = new DomainClassInfo(javaClass, ++maxId);
                            addNewInfo(map, array, classInfo);

                            if (LogLevel.INFO) {
                                System.out.println("INFO: Registering new domain class '" + javaClass.getName() + "' with id " + classInfo.classId);
                            }
                            stmt.executeUpdate("INSERT INTO DOMAIN_CLASS_INFO VALUES ('" 
                                               + javaClass.getName()
                                               + "', "
                                               + classInfo.classId
                                               + ")");
                        }
                    }

                    // try to commit
                    broker.commitTransaction();

                    // the commit was ok, so finish the initialization by assigning to the static variables
                    classInfoMap = Collections.unmodifiableMap(map);
                    classInfoById = new DomainClassInfo[maxId+1];
                    array.toArray(classInfoById);
                    return;
                } catch (SQLException e) {
                    System.out.println("INFO: The registration of new DomainClassInfos failed.  Retrying...");
                    // the inserts into the database or the commit may fail if a concurrent execution tries to create new records also
                    // if that happens, abort the current transaction and retry with a new one
                    broker.abortTransaction();
                }
            }
        } catch (Exception e) {
            // if an exception occurs, throw an error
            throw new Error(e);
        } finally {
            if (broker != null) {
                if (broker.isInTransaction()) {
                    broker.abortTransaction();
                }
                broker.close();
            }
        }
    }

    private static Class findClass(String classname) {
        try {
            return Class.forName(classname);
        } catch (ClassNotFoundException cnfe) {
            // domain classes may disappear, but their id should not be reused
            // so, if the corresponding Java class does not exist, return null
            return null;
        }
    }

    private static void addNewInfo(Map<Class,DomainClassInfo> map, ArrayList<DomainClassInfo> array, DomainClassInfo info) {
        if (info.domainClass != null) {
            map.put(info.domainClass, info);
        }
        
        int index = info.classId;
        int size = array.size();
        if (size <= index) {
            array.ensureCapacity(index + 1);
            while (size < index) {
                array.add(null);
                size++;
            }
            array.add(info);
        } else {
            array.set(info.classId, info);
        }
    }

    public static int mapClassToId(Class objClass) {
        return classInfoMap.get(objClass).classId;
    }

    public static String mapIdToClassname(int cid) {
        if ((cid < 1) || (cid >= classInfoById.length)) {
            return null;
        } else {
            return classInfoById[cid].domainClass.getName();
        }
    }

    
    // the non-static part starts here

    public final Class domainClass;
    public final int classId;

    public DomainClassInfo(Class domainClass, int classId) {
        this.domainClass = domainClass;
        this.classId = classId;
    }
}
