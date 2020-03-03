package framework;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {

    private static Logger _logger = LoggerFactory.getLogger(TestListener.class);

    FileWriter fileWriter;
    FileWriter fileWriterTest;


    @Override
    public void onTestStart(ITestResult result) {
        TestSetup.methodInfo = TestSetup.testInfo.createNode(result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TestSetup.methodInfo.pass("Test Case Name : " + result.getName() + " is passed");

    }

    @Override
    public void onTestFailure(ITestResult result) {
        TestSetup.methodInfo.log(Status.FAIL, "Test Case Name : " + result.getName() + " is failed");
        TestSetup.methodInfo.log(Status.FAIL, "Test failure : " + result.getThrowable());

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        TestSetup.methodInfo = TestSetup.testInfo.createNode(result.getName());
        TestSetup.methodInfo.log(Status.SKIP, "Test Case Name : " + result.getName() + " is skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        //_logger.info("onTestFailedButWithinSuccessPercentage");
    }

    @Override
    public void onStart(ITestContext context) {
        TestSetup.testInfo = TestSetup.reports.createTest(context.getName()).assignCategory(context.getName());
        TestSetup.testName = context.getName();
        _logger.info("Test Case : {} ", context.getName());
        _logger.info("\n");

    }

    @Override
    public void onFinish(ITestContext context) {

        if (context.getFailedConfigurations().size() > 0 || context.getFailedTests().size() > 0) {
            _logger.info("Status : Failed");
        } else {
            _logger.info("Status : Pass");
        }

        if (context.getFailedConfigurations().size() > 0 || context.getFailedTests().size() > 0) {

            try {
                fileWriter = new FileWriter("resources/report/failedSubject.txt", false);
                fileWriter.write("FAILED\n");
                fileWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                fileWriterTest = new FileWriter("resources/report/failedMethods.txt", true);
                fileWriterTest.write(context.getName() + " Failed.\n");
                fileWriterTest.close();

            } catch (Exception e) {
                e.printStackTrace();

            }


        } else {

            try {
                Stream<String> lines = Files.lines(Paths.get("resources/report/failedSubject.txt"));

                if (lines.noneMatch(t -> t.contains("FAILED"))) {
                    fileWriter = new FileWriter("resources/report/failedSubject.txt", false);
                    fileWriter.write("PASSED\n");
                    fileWriter.close();

                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

    }

}