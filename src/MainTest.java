import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {
    @Test(expected = AssertionError.class)
    public void testNoDirectory() {
        String[] a = {"--port", "8080"};
        Main.parseArgs(a);
    }

    @Test(expected = AssertionError.class)
    public void testNoPort() {
        String[] a = {"--directory", "/tmp"};
        Main.parseArgs(a);
    }
    @Test(expected = AssertionError.class)
    public void testNoThread(){
        String[] a = {"--port", "8080", "--directory", "/tmp" };
        Main.parseArgs(a);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownArgument() {
        String[] a = {"--foo", "/bar"};
        Main.parseArgs(a);
    }

    @Test
    public void testGoodArguments() {
        String[] a = {"--port", "8080", "--directory", "/tmp", "--threadNumber", "10" };
        Arguments arguments = Main.parseArgs(a);
        assertEquals(arguments.getPort(), 8080);
        assertEquals(arguments.getDirectory(), "/tmp");

        String[] b = {"--threadNumber", "10", "--directory", "/tmp", "--port", "8080"};
        arguments = Main.parseArgs(b);
        assertEquals(arguments.getPort(), 8080);
        assertEquals(arguments.getDirectory(), "/tmp");
    }
}