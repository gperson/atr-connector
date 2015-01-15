# atr-connector
This project is a java connector for [angular-test-reporter](https://github.com/gperson/angular-test-reporter).  When you run 'maven clean install' it will generate 'atr-connector-x.x.x-SNAPSHOT-jar-with-dependencies.jar', this jar is used to connect a java project to a angular-test-reporter webapp instance.

## Usage
To use angular-test-reporter with Java projects, simply add the atr-connector-x-x-x.jar (in the connectors folder) to the projects build path. Bellow are some basic examples of usage:

<pre>
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import atr.connect.enums.TestStatus;
import atr.connect.reporter.TestReporter;

public class DummyTest {

    /**
     * Initialize TestReporter with URL of the default data-server.
     * By default it posts tests to the tests_Default database table,
     * to change use tr.setProjectTable("tests_ExampleProject")
     */
    TestReporter tr = new TestReporter("http://localhost:4968/");
    
    @BeforeMethod
    public void setup() {
    	//Creates a 'test' and sets the Run Info
        tr.setTestInfo("Regression, Sprint 1");

    }

    /**
     * Basic and most common usage
     * @throws Throwable
     */
    @Test
    public void test_0() throws Throwable {
        try {
            int x = 2;
            int y = 3;
        	
        	//Sets information for the test
            tr.setTestInfo("test_0","X = "+x+", Y = "+y, "Checking if X + Y = Y + X");
            
            //The test
            Assert.assertEquals(x+y, y+x);
            
        } catch (Throwable e) {
            //If there is an error - catches the error, posts the results, and finally throws the error
            tr.failTest(e);
        } finally {
            //If there is no error -posts the results
            if (tr.status) {
                tr.passTest();
            }
        }
    }
    
    /**
     * Example where no information is set (Except run info in the before method...which we could omit also)
     * @throws Throwable
     */
    @Test
    public void test_2() throws Throwable { 
        try {
            //The test
            Assert.assertEquals("Are equal", "No");
        } catch (Throwable e) {
            tr.failTest(e);
        } finally {
            if (tr.status) {
                tr.passTest();
            }
        }
    }
    
    /**
     * Example of test results being posted the non-default table 
     * and non-default error message
     * @throws Throwable
     */
    @Test
    public void test_3() throws Throwable {
        try {
            //Sets the table to post info to
            tr.setProjectTable("tests_ExampleProject");
            
            //Sets the name of the test
            tr.setName("test_3");
            
            //The test
            Assert.assertEquals("Actual", "Expected");
            
        } catch (Throwable e) {
            //If error we add extra info to the test result
            tr.setExtra("Error with test");
            
            //Fails the test with the non-default error status and throws the error
            tr.failTestNonDefault(TestStatus.ERROR, e);
        } finally {
            if (tr.status) {
                tr.passTest();
            }
        }
    }
}
</pre>
