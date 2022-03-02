import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArgumentsTest {
    @Test(expected = AssertionError.class)
    public void testNegativePortNumber() {
        new Arguments(-1, "foo", 1, 10);
    }

    @Test(expected = AssertionError.class)
    public void testTooLargePortNumber() {
        new Arguments(65537, "foo", 1, 10);
    }

    @Test(expected = AssertionError.class)
    public void testNullDirectory() {
        new Arguments(8080, null, 1, 10);
    }



    @Test(expected = AssertionError.class)
    public void testZeroThreads() {
        new Arguments(8080, "/tmp", 1, 0);
    }
    @Test(expected = AssertionError.class)
    public void testNegativeThreads(){
        new Arguments(8080, "/tmp", 1, -1);
    }

    @Test
    public void testGood() {
        Arguments arguments = new Arguments(8080, "/", 1, 10);
        assertEquals(arguments.getPort(), 8080);
        assertEquals(arguments.getDirectory(), "/");
        assertEquals(arguments.getResponses(), 1);
    }
}