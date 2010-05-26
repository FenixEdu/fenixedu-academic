package net.sourceforge.fenixedu.presentationTier.gwt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FenixGWTAntTasksCreator {
    
    public String gwtModulesPath;
    public String buildFilePath;
    public String gwtDestDir;
    public String gwtSdk;
    public String gwtArgs;
    public PrintWriter out;
    public File file;
    
    
    public FenixGWTAntTasksCreator(String... args) {
	
	gwtModulesPath = args[0] + "/src/net/sourceforge/fenixedu/presentationTier/gwt";
	buildFilePath = args[0] + "/build_gwtautogentasks.xml";
	
	gwtDestDir = args[1];
	gwtSdk = args[2];
	gwtArgs = args[3];
    }
    
    /**
     * args[0] - ${basedir}
     * args[1] - ${build.gwtdestdir}
     * args[2] - ${gwt.sdk}
     * args[3] - ${gwt.args}
     */
    public static void main(String [ ] args) {
	
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
	    writeTail();
	    
	} catch (IOException ioe) {
	    System.err.println ("Unable to write to file");
	    System.exit(-1);
	}
	
    }
    
    private void writeHeader() {
	
	out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
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
	out.println("<fileset dir=\"${gwt.sdk}\" includes=\"gwt-dev*.jar\"/>");
	out.println("<fileset dir=\"${build.home.webinf}/lib\" includes=\"**/*.jar\"/>");
	out.println("</path>");
	out.println("");
	
	//Compiled JS destination dir
	out.println("<property name=\"build.destdir\" location=\"web/gwt\"/>");
	out.println("");
	
	out.println("<target name=\"gwtc\" description=\"Compiles GWT client-side code to JavaScript\">");
	out.println("");
	out.println("<sequential>");
	out.println("");
	
	out.flush();
    }
    
    private void writeTasks() {
	
	List<String> modules = lookupForModules(gwtModulesPath);
	
	for(String module : modules) {
	    writeTaskTemplate();
	    out.println("<arg value=\"" + digest(module) + "\"/>");
	    out.println("</java>");
	    out.println("");    
	}
	
	out.flush();
	
    }
    
    private void writeTail() {
	
	out.println("</sequential>");
	out.println("");
	out.println("</target>");
	out.println("");
	out.println("</project>");
	out.println("");
	
	out.flush();
	
    }
    
    private void writeTaskTemplate() {
	
	out.println("<java failonerror=\"true\" fork=\"true\" classname=\"com.google.gwt.dev.Compiler\">");
	out.println("<classpath>");
	out.println("<pathelement location=\"src\"/>");
	out.println("<path refid=\"project.class.path\"/>");
	out.println("</classpath>");
	out.println("<jvmarg value=\"-Xmx256M\"/>");
	out.println("<arg line=\"${gwt.args}\"/>");
	out.println("<arg value=\"-war\"/>");
	out.println("<arg value=\"" + this.gwtDestDir + "\"/>");
	
    }
    
    private List<String> lookupForModules(String gwtModulesPath){
	
	List<String> result = new ArrayList<String>();
	String path = gwtModulesPath;
	
	File baseDir = new File(gwtModulesPath);
	
	DFS(result, path, baseDir);
	
	return result;
    }
    
    private void DFS(List<String> resultSet, String path, File baseDir) {
	
	String[] children = baseDir.list();
	
	if(children != null) {
	    for(int i=0; i<children.length; i++) {
		
		//RaphaelGWT module serves only as a lib. Does not define an entrypoint,
		// donc it can't be gwtcompiled and must be ignored.
		if(!children[i].equals("RaphaelGWT.gwt.xml")) {
		    
		    if(children[i].matches(".*\\.gwt\\.xml")) {
			path += "/" + children[i];
			resultSet.add(path);
		    }
		
		}
		
		String newPath = path + "/" + children[i];
		File newDir = new File(newPath);
		if(newDir.isDirectory()) {
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


}
