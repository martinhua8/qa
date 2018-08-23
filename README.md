# CamviTest


One Paragraph of project description goes here

## Getting Started


### Prerequisites

Eclipse with TestNG plug-in

### Installing

More detailed steps can be found in following URL.
http://toolsqa.com/selenium-webdriver/install-testng/

Install TestNG plug-in
1) Launch the Eclipse IDE and from Help menu, click “Install New Software”.
2) You will see a dialog window, click “Add” button.
3) Type name as you wish, lets take “TestNG” and type “http://beust.com/eclipse/” as location. Click OK.
4) You come back to the previous window but this time you must see TestNG option in the available software list. Just Click TestNG and press “Next” button.
5) Click "Next","Next" until finish.
6) Restart eclipse.



## Running the tests

This autotest platform is developed based on TestNG.\
Writing a test is typically a three-step process:

1. Write the business logic of your test and insert TestNG annotations in your code.
2. Add the information about your test (e.g. the class name, the groups you wish to run, etc...) in a testng.xml file or in build.xml.
3. Run TestNG.


####How to run from eclipse
1. Open eclipse.
2. Click File > Import.
3. Click Maven, Select Existing Maven Projects, then import project.
4. Right click CamviTestNGPlatform_testng.xml and click Run as > TestNG Suit.

#### How to run from commend line
1. open terminal.
2. cd </path/to/project/directory/>
3. run by entering: mvn clean test

####How to write TestNG.xml
Open CamviTestNGPlatform_testng.xml.\
Specify the class names along with packages between the classes tags.\
The below is the example testng.xml which will execute the class names that are specified.

```
Give an example
----------------------------------------------------------------------
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >

<suite name="TestSuit1">
    <test name="test1">
        <classes>
            <class name="com.camvi.autotest.CamviTestNGPlatform"/>
            <class name="com.camvi.autotest.personApi.PersonApiCreate"/>
            <class name="com.camvi.autotest.FaceRecApi.FaceRecApiRecognize"/>  
        </classes>
    </test>
</suite>
```
PersonApiCreate, FaceRecApiRecognize specified in the xml will get executes which have TestNG annotations.\
CamviTestNGPlatform class is used to connect to the server, so please keep it and add classes you want to test after that.\


####How to test all the classes in one package
write a xml file as following:
```
Give an example
----------------------------------------------------------------------
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >

<suite name="TestSuit1">

    <test name="test1">
        <classes>
            <class name="com.camvi.autotest.CamviTestNGPlatform"/>
        </classes>
    </test>

    <test name="test2">
        <packages>
            <package name="com.camvi.autotest.personApi.*"/>
        </packages>
    </test>

</suite>
```


####How to write test class

Documentaion of TestNG is [here](http://testng.org/doc/documentation-main.html#introduction)  
You can find a quick example on the [Welcome page](http://testng.org/doc/index.html)

Some annotations used in test\
@BeforeClass: The annotated method will be run before the first test method in the current class is invoked.  

@AfterClass: The annotated method will be run after all the test methods in the current class have been run.  

@DataProvider	Marks a method as supplying data for a test method. The annotated method must return an Object[][] \
where each Object[] can be assigned the parameter list of the test method. The @Test method that wants to receive \
data from this DataProvider needs to use a dataProvider name equals to the name of this annotation.

I write some common methods in SystemHelper, and the common structures of test are as following
1. Use SystemHelper.setup() in @BeforeClass to import pictures to database
2. Use @DataProvider to prepare some pictures for testing by using SystemHelper.getTestData();
3. Use @Test(dataProvider = "TestData") to test API, and Assert method to confirm the result.
4. Use SystemHelper.cleanUp in @AfterClass to delete added pictures in database.
