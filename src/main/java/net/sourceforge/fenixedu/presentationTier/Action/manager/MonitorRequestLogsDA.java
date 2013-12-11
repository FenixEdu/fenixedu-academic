/*
 * Created on 2004/07/18
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.ConfirmManagerIdentity;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;
import net.sourceforge.fenixedu.util.renderer.BarChart;
import net.sourceforge.fenixedu.util.renderer.TimeLineChart;
import net.sourceforge.fenixedu.util.renderer.container.RequestEntry;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Luis Cruz
 */
@Mapping(module = "manager", path = "/monitorRequestLogs", attribute = "monitorRequestLogsPathForm",
        formBean = "monitorRequestLogsPathForm", scope = "request", validate = false, parameter = "method")
@Forwards(value = { @Forward(name = "ShowSelectionForm", path = "/manager/monitorRequests_bd.jsp"),
        @Forward(name = "ShowRequestAnalysisLog", path = "/manager/analyseRequestsLog_bd.jsp") })
public class MonitorRequestLogsDA extends FenixDispatchAction {

    private static String logImageDir = null;

    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static String logFileName = null;

    public ActionForward listFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ConfirmManagerIdentity.run();

        DynaActionForm actionForm = (DynaActionForm) form;
        String logDirName = (String) actionForm.get("logDirName");
        if (logDirName == null || logDirName.length() == 0 || logFileName == null || logFileName.length() == 0) {
            logDirName = FenixConfigurationManager.getConfiguration().getLogProfileDir();
            logFileName = FenixConfigurationManager.getConfiguration().getLogProfileFilename();
        }

        File logDir = new File(logDirName);
        SortedSet fileNames = new TreeSet();
        File[] logProfileFiles = logDir.listFiles();
        for (File fileOfDir : logProfileFiles) {
            if (fileOfDir.isFile() && fileOfDir.getName().startsWith(logFileName)) {
                fileNames.add(fileOfDir.getName());
            }
        }

        actionForm.set("logDirName", logDirName);
        request.setAttribute("monitorRequestLogsPathForm", actionForm);
        request.setAttribute("fileNames", fileNames);

        return mapping.findForward("ShowSelectionForm");
    }

    public ActionForward analyseFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ConfirmManagerIdentity.run();

        DynaActionForm actionForm = (DynaActionForm) form;
        String logDirName = (String) actionForm.get("logDirName");
        String logFileName = request.getParameter("logFileName");
        if (logDirName == null || logDirName.length() == 0 && logFileName == null || logFileName.length() == 0) {
            return listFiles(mapping, form, request, response);
        }

        File logFile = new File(logDirName + "/" + logFileName);
        Collection profiles = processFile(logFile, request);
        request.setAttribute("profiles", profiles);
        request.setAttribute("logFileName", logFileName);

        return mapping.findForward("ShowRequestAnalysisLog");
    }

    private Collection processFile(File file, HttpServletRequest request) {
        Map profileMap = new HashMap();
        SortedSet sortedSet = null;
        String fileContents = readFileContents(file);
        if (fileContents != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(fileContents, "\n");
            while (stringTokenizer.hasMoreElements()) {
                String line = (String) stringTokenizer.nextElement();
                if (!StringUtils.contains(line, "[Filter: profiling]")) {
                    // the line contains usefull info
                    processLine(profileMap, line);
                } else {
                    // nothing usefull... throw this away
                }
            }

            sortedSet = dumpInfo(file.getName(), profileMap, file.getName());
        }
        return sortedSet;
    }

    private SortedSet dumpInfo(String name, Map profileMap, String filename) {
        if (logImageDir == null) {
            setLogImageDir();
        }

        SortedSet sortedProfileSet = sortProfileMap(profileMap);
        new BarChart(sortedProfileSet, "Average Execution Time", logImageDir + "/" + filename + ".bar.png", 20);
        new TimeLineChart(sortedProfileSet, logImageDir + "/" + filename + ".timeline.png");
        return sortedProfileSet;
    }

    private void setLogImageDir() {
        logImageDir = FenixConfigurationManager.getConfiguration().getLogImageDirectory();
    }

    private SortedSet sortProfileMap(Map profileMap) {
        SortedSet sortedSet = new TreeSet(new Comparator() {
            @Override
            public int compare(Object arg0, Object arg1) {
                RequestEntry requestEntry0 = (RequestEntry) arg0;
                RequestEntry requestEntry1 = (RequestEntry) arg1;
                return requestEntry1.getAverageExecutionTime() - requestEntry0.getAverageExecutionTime();
            }
        });
        sortedSet.addAll(profileMap.values());
        return sortedSet;
    }

    private void processLine(Map profileMap, String line) {
        StringTokenizer stringTokenizer = new StringTokenizer(line, " \t[]-");
        Date logTime = parseTime(stringTokenizer.nextToken());
        String executionTimeString = stringTokenizer.nextToken();
        Integer executionTime = new Integer(executionTimeString.substring(0, executionTimeString.length() - 2));
        String requestPath = stringTokenizer.nextToken();

        RequestEntry requestEntry = (RequestEntry) profileMap.get(requestPath);
        if (requestEntry == null) {
            requestEntry = getNewHashKey(requestPath);
            profileMap.put(requestPath, requestEntry);
        }
        requestEntry.addEntry(executionTime, logTime);
    }

    private Date parseTime(String timeString) {
        Date result = null;
        try {
            result = dateFormat.parse(timeString);
        } catch (ParseException e) {
        }
        return result;
    }

    private String readFileContents(File file) {
        try {
            FileReader fileReader = new FileReader(file);
            // We'll initialize the buffer with the optimal value for the
            // example
            // log files we experimented with.
            char[] buffer = new char[4096];
            StringBuilder fileContents = new StringBuilder();
            int n = 0;
            while ((n = fileReader.read(buffer)) != -1) {
                fileContents.append(buffer, 0, n);
            }
            return fileContents.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RequestEntry getNewHashKey(String requestPath) {
        return new RequestEntry(requestPath);
    }

}