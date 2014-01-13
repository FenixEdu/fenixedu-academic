package net.sourceforge.fenixedu.presentationTier.gwt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FenixGWTAntTasksCreator {

    private static final Logger logger = LoggerFactory.getLogger(FenixGWTAntTasksCreator.class);

    public String gwtModulesPath;
    public String buildFilePath;
    public String gwtDestDir;
    public String gwtSdk;
    public String gwtArgs;
    public PrintWriter out;
    public File file;
    public List<String> targetsList;

    public FenixGWTAntTasksCreator(String... args) {

        gwtModulesPath = args[0] + "/src/net/sourceforge/fenixedu/presentationTier/gwt";
        buildFilePath = args[0] + "/build_gwtautogentasks.xml";

        gwtDestDir = args[1];
        gwtSdk = args[2];
        gwtArgs = args[3];

        targetsList = new ArrayList<String>();
    }

    /**
     * args[0] - ${basedir}
     * args[1] - ${build.gwtdestdir}
     * args[2] - ${gwt.sdk}
     * args[3] - ${gwt.args}
     */
    public static void main(String[] args) {

        FenixGWTAntTasksCreator tasksCreator = new FenixGWTAntTasksCreator(args);
        tasksCreator.createAnt();

    }

    public void createAnt() {

        try {
            file = new File(buildFilePath);
            file.createNewFile();

            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            writeHeader();
            writeTasks();
            writeTargetCalls();
            writeTail();

        } catch (IOException ioe) {
            logger.error("Unable to write to file: " + ioe.getMessage(), ioe);
            System.exit(-1);
        }

    }

    private void writeHeader() {

        out.println("<?xml version=\"1.0\" encoding=\"" + Charset.defaultCharset().name() + "\"?>");
        out.println("<project basedir=\".\">");
        out.println("");

        //Source path
        out.println("<property name=\"src\" location=\"src/net/sourceforge/fenixedu/presentationTier/gwt/\"/>");
        out.println("");

        //GWT args
        out.println("<property name=\"gwt.args\" value=\"" + this.gwtArgs + "\" />");
        out.println("");

        //GWT-SDK path
        out.println("<property name=\"gwt.sdk\" location=\"" + this.gwtSdk + "\" />");
        out.println("");

        //GWT classpath
        out.println("<path id=\"project.class.path\">");
        out.println("<pathelement location=\"${build.home}/WEB-INF/classes\"/>");
        out.println("<pathelement location=\"${gwt.sdk}/gwt-user.jar\"/>");
        out.println("<fileset dir=\"${gwt.sdk}\" includes=\"**/*.jar\"/>");
        out.println("<fileset dir=\"${build.home.webinf}/lib\" includes=\"**/*.jar\"/>");
        out.println("</path>");
        out.println("");

        //Compiled JS destination dir
        out.println("<property name=\"build.destdir\" location=\"web/gwt\"/>");
        out.println("");

        out.flush();
    }

    private void writeTasks() {

        List<String> modules = lookupForModules(gwtModulesPath);

        for (String module : modules) {
            targetsList.add(extractModuleName(module));
            writeRequirementCheck(module);
            writeTask(module);
        }

        out.flush();

    }

    private void writeTail() {

        out.println("</project>");
        out.println("");

        out.flush();

    }

    private void writeTask(String module) {

        out.println("<target name=\"" + extractModuleName(module) + "\" unless=\"compile-" + extractModuleName(module)
                + ".notRequired\">");
        out.println("<java failonerror=\"true\" fork=\"true\" classname=\"com.google.gwt.dev.Compiler\">");
        out.println("<classpath>");
        out.println("<pathelement location=\"src\"/>");
        out.println("<path refid=\"project.class.path\"/>");
        out.println("</classpath>");
        out.println("<jvmarg value=\"-Xmx256M\"/>");
        out.println("<arg line=\"${gwt.args}\"/>");
        out.println("<arg value=\"-war\"/>");
        out.println("<arg value=\"" + this.gwtDestDir + "\"/>");
        out.println("<arg value=\"" + digest(module) + "\"/>");
        out.println("</java>");
        out.println("</target>");
        out.println("");

    }

    private List<String> lookupForModules(String gwtModulesPath) {

        List<String> result = new ArrayList<String>();
        String path = gwtModulesPath;

        File baseDir = new File(gwtModulesPath);

        DFS(result, path, baseDir);

        return result;
    }

    private void DFS(List<String> resultSet, String path, File baseDir) {

        String[] children = baseDir.list();

        if (children != null) {
            for (int i = 0; i < children.length; i++) {

                //RaphaelGWT module serves only as a lib. Does not define an entrypoint,
                // donc it can't be gwtcompiled and must be ignored.
                if (!children[i].equals("RaphaelGWT.gwt.xml")) {

                    if (children[i].matches(".*\\.gwt\\.xml")) {
                        path += "/" + children[i];
                        resultSet.add(path);
                    }

                }

                String newPath = path + "/" + children[i];
                File newDir = new File(newPath);
                if (newDir.isDirectory()) {
                    DFS(resultSet, newPath, newDir);
                }

            }
        }
    }

    private String digest(String rawString) {
        int padding = rawString.indexOf("net/sourceforge/fenixedu/presentationTier/gwt");
        String inProcess = rawString.substring(padding);
        inProcess = inProcess.replace(".gwt.xml", "");
        String processed = inProcess.replace('/', '.');
        return processed;

    }

    private void writeRequirementCheck(String module) {

        out.println("");
        out.println("<condition property=\"compile-" + extractModuleName(module) + ".notRequired\">");
        out.println("<and>");
        out.println("<uptodate targetfile=\"" + this.gwtDestDir + "/" + extractModuleName(module) + "/"
                + extractModuleName(module) + ".nocache.js\">");
        out.println("<srcfiles dir=\"" + extractModulePath(module) + "client\" />");
        out.println("</uptodate>");
        out.println("<uptodate targetfile=\"" + this.gwtDestDir + "/" + extractModuleName(module) + "/"
                + extractModuleName(module) + ".nocache.js\">");
        out.println("<srcfiles dir=\"" + this.gwtModulesPath + "/frameworks\" />");
        out.println("</uptodate>");
        out.println("</and>");
        out.println("</condition>");
        out.println("");

    }

    private String extractModuleName(String module) {

        String digested = digest(module);
        String[] splinters = digested.split("\\.");
        String moduleName = splinters[splinters.length - 2];
        return moduleName;

    }

    private void writeTargetCalls() {

        out.println("<target name=\"gwtc\" description=\"Compiles GWT client-side code to JavaScript\">");
        for (String target : targetsList) {
            out.println("<antcall target=\"" + target + "\" />");
        }
        out.println("</target>");
        out.println("");

    }

    private String extractModulePath(String module) {
        String digested = digest(module);
        String[] splinters = digested.split("\\.");
        String gwtXmlFileName = splinters[splinters.length - 1];

        String result = module.replace(gwtXmlFileName + ".gwt.xml", "");

        return result;
    }

}
