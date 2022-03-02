import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.AssumptionViolatedException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.InflaterInputStream;

/**
 * Class Arguments holds three types of variables which contain the port, directory, and responses
 * to be used to send a HTTP response to be generated in one exchange
 *
 * **/
class Arguments {
    private final int port;
    private final String directory;
    private final int responses;
    private final int threads;
    /** Constructor Arguments **/
    Arguments(int port, String directory, int responses, int threads) {
        this.port = port;
        this.directory = directory;
        this.responses = responses;
        this.threads = threads;

        if(port < 0)
            throw new AssertionError("Negative port");
        else if (port > 65535){
            throw new AssertionError("Port too large");
        }
        else if (directory == null){
            throw new AssertionError("Directory is null");
        }
        else if(directory.equals("")){
            throw new AssertionError("Directory is 0");
        }
        else if(threads < 0){
            throw new AssertionError("Threads can't be negative");
        }
        else if(threads == 0){
            throw new AssertionError("Threads can't be zero :(");
        }
    }

    int getPort() { return port; }
    String getDirectory() { return directory; }
    int getResponses() { return responses; }
    int getThreads(){ return threads; }
}

class MyHttpHandler implements HttpHandler {
    private final Arguments arguments;
    private final AtomicInteger countResp = new AtomicInteger(); // private static int countResp;

    MyHttpHandler(Arguments arguments) {
        this.arguments = arguments;
    }
    public synchronized void Destroy(){
        // make entire a critical //syncronize block if

        if(countResp.get()== arguments.getResponses()){
            System.exit(0);
        }
    }
    /**
     * {@linkhttps://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpExchange.html}
     * {@linkhttps://docs.oracle.com/javase/7/docs/api/java/io/FileInputStream.html
     * **/
    @Override
    //Method locked
    public synchronized void handle(HttpExchange httpExchange) throws IOException {
        countResp.incrementAndGet(); //countResp++;
        System.out.println(httpExchange.getRequestURI()); // Browser request for icon in page so that counts as one response
        // Because I have my project in a long path folder I had to hard code the path until I reached Assignment_01
        //String pathName = "C:/Users/willi/OneDrive/Desktop/SPRING_2022/CS39AC_Concurrent_programming/Assignment_01" + arguments.getDirectory() + httpExchange.getRequestURI();
        String pathName = arguments.getDirectory() + httpExchange.getRequestURI();
        File f = new File(pathName);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        }catch (FileNotFoundException FNFE){
         //   System.out.println(f.getCanonicalPath()); // Used to debug to see where it was looking for the file.
            fileNotFound(httpExchange);
            Destroy();
            return;

        }
        fileFound(httpExchange, f.length(), fis);

        System.out.println(countResp + "\n" + arguments.getResponses() );
        Destroy();

    }
    //Method locked
    private synchronized void fileNotFound(HttpExchange httpExchange) throws IOException {

        String fileFoundNot = "FILE DOES NOT EXIST IN DIRECTORY";
        InputStream is = httpExchange.getRequestBody();
        //is.read();
        httpExchange.sendResponseHeaders(404, fileFoundNot.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(fileFoundNot.getBytes(StandardCharsets.UTF_8));
        os.close();
        System.out.println("exit");
    }
    //locked
    private synchronized void fileFound(HttpExchange httpExchange, long length, FileInputStream in) throws IOException {
        InputStream is = httpExchange.getRequestBody();
        is.read();
        httpExchange.sendResponseHeaders(200, length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(in.readAllBytes());
        os.close();

    }
}

public class Main {
    private static final int NTHREADS = 2; //HardCoded Threads not used
   // private static final Executor exec = Executor.newFixedThreadPool(NTHREADS);
    /** Method parseArgs parses strings that are passed into through
     * @param args it's a command line argument stored as collection of Strings, separated by a space, which can be
     * typed into the program on a terminal. In IntelliJ, it must be written into configuration -- program arguments
     * parseArgs verifies that there is a port, directory and if not throws AssertionErrors.
     * It also throws IllegalArgumentException args contains not valid port and directory String values.
     * @return a new object created from the Arguments class with port, dir, and resp.
     * */
    static Arguments parseArgs(String[] args)  {
        String[] info = args.clone();
        //System.out.println(info);
        String port = null;
        String dir = null;
        String threadNumber = null;

        int resp = 0;
        // Finished passing all test with the help of Dr. Beaty
        for(int i = 0; i < info.length; i ++){
            if(info[i].equals("--port"))
                port = info[++i];
            else if(info[i].equals("--directory"))
                dir = info[++i];
            else if(info[i].equals("--responses"))
                resp = Integer.parseInt(info[++i]);
            else if(info[i].equals("--threadNumber"))
                threadNumber = info[++i];
            else{
                throw new IllegalArgumentException(info[i]);
            }
        }
        if(dir == null)
            throw new AssertionError();

        if(port ==null)
            throw new AssertionError();
        if(threadNumber == null)
            throw new AssertionError();

        Arguments newArgument = new Arguments(Integer.parseInt(port), dir, resp, Integer.parseInt(threadNumber));
        return newArgument;

    }


/**
 * {@link <a href="https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/package-summary.html">...</a>}
 * */
    public static void main(String[] args) throws IOException {

        // String[] stringArguments = {"--port","8080", "--directory", "/tmp", "--responses", "5"};
        Arguments arguments = parseArgs(args);
        System.out.println("Running...");
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(arguments.getPort()), 0);
        httpServer.createContext("/", new MyHttpHandler(arguments));

        httpServer.setExecutor(Executors.newFixedThreadPool(arguments.getThreads()));
        //System.out.println(arguments.getResponses());

        
        //System.out.println(arguments.getPort() + arguments.getDirectory());
        httpServer.start();


    }
}
