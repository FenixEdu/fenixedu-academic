package net.sourceforge.fenixedu._development;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;

import pt.utl.ist.fenix.tools.util.FileUtils;

public class Custodian {

    private static Set<Integer> pids = new HashSet<Integer>();

    public static void registerPID() {
        try {
            final Process process = Runtime.getRuntime().exec("ps");
            process.waitFor();
            final String processOutput = readProcessOutput(process);
            final String[] lines = processOutput.split("\n");
            for (int i = 0; i < lines.length; i++) {
                final String line = lines[i].trim();
                final String[] psParts = line.split(" ");
                final String pid = psParts[0].trim();
                
                for (int k = 1; k < psParts.length; k++) {
                    final String command = psParts[k].trim();
                    if (command.startsWith("java") || command.endsWith("/java") || command.endsWith("\\java")) {
                        pids.add(Integer.valueOf(pid));
                        System.out.println("Adding pid: " + pid + " to custodians register.");
                    }
                }
            }
            if (pids.isEmpty()) {
                System.out.println("No java pids where found!?!");
            }
        } catch (Exception e) {
            System.out.println("Unable to determine PID: " + e.getMessage());
        }
    }

    public static void dumpThreadTrace() throws IOException, InterruptedException {
        if (!pids.isEmpty()) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("kill -QUIT");
            for (final Integer pid : pids) {
                stringBuilder.append(" ");
                stringBuilder.append(pid);
            }
            System.out.println("Sending kill -QUIT signal to java processes.");
            final Process process = Runtime.getRuntime().exec(stringBuilder.toString());
            process.waitFor();
        }
    }

    private static String readProcessOutput(final Process process) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileUtils.copy(process.getInputStream(), byteArrayOutputStream);
        return byteArrayOutputStream.toString();
    }

}
