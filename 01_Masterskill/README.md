# Masterskill
The complete Alexa skill as part of the Scéalability framework.


###Content:

1. Setting Up the Skill
2. Setting Up Alexa
3. Editing Code
4. Testing the Application

---

###Introduction to the Alexa Environment

In order to get Alexa started, an account to the Amazon Web Services (AWS) is mandatory. Once set up, log into AWS (console.aws.amazon.com) and enter the Lambda environment (you can search for it using the search function). Here, create a new custom skill and give it an appropriate name.
You will see, that currently the application uses the Alexa Skill Kit, which is needed in order to upload any skill to Alexa and use it with an Echo device, Amazon CloudWatch Logs, which is always part of an app, and DynamoDB, a No-SQL Database, where the stories will be stored and referenced and which is further being used in order to generate the stories (however this is not implemented yet).

You can click through all these configurations and see their settings. The Alexa Skill Kit includes the Skill ID from the application, hosted on developer.amazon.com (more on that later). The Skill ID needs to be added here, otherwise the communication between the servers will not work.. However, most interesting is the Narrator tab, which allows to upload new code (currently in Java 8). The most important part is, the ‘Function Code’ area. Code can be entered in three ways:
As a build .jar file
As a packaged .zip file
As either, but uploaded into a Amazon S3 bucket. Last has the advantage, that a toolkit for the Eclipse IDE was released, in order to build and upload code directly. This is not included yet, since I programmed in IntelliJ IDEA.
Any application for AWS needs to have a handler, a special function that basically builds a main method and responds to AWS. The path (where this function would lie inside the packed .jar or .zip) needs to be entered here.

Once everything is set up the project can be saved (upper right corner). If you need to change anything at the Alexa Skills side (Utterances, Invocation..) you will need to copy the ARN (arn:aws:lambda:us-east-1:253702680774:function:Narrator), which is basically the key for Alexa Skills Kit (ASK).

###Setting Up Alexa

Log into to developer.anazon.com with the same login credentials, and continue to Alexa Skills Kit. Again, a list with all current Alexa Skills will show, including the Narrator Skill which you need to open in order to set things up.

*From top to bottom:*
Firstly, the copied ARN  needs to be linked here. Within the Endpoint section, see the Default Region. The here entered ARN needs to be the very same as the one of the Amazon Lambda Function, that stores the code. Further, the Skill ID (amzn1.ask.skill.755a3cb6-b57c-4c13-ae65-616bfe38a9e4) needs to be entered within the Handler class inside the code for approving the usage. Once, everything is setup correctly, the rest can be added.

The interaction model sets the framework, what the app can do, and how it will interact with its audience. It starts with the Invocation, its name. Since the application is called Narrator, in order to start it the audience needs to invoke it by saying: ‘Alexa, open Narrator’. Once the application active, other phrases can be used, such as the NarratorIntent.

The NarratorIntent will create a story. Everything entered here, will invoke this part of the application. Important to know, is that every Intent setup here needs to be handled inside the Java Project. Intents need to be handled and responded to in order for anything to happen.

No complete interaction model has been designed as of know, for not all features and possibilities were set. This is one important point for the future and needs to be designed soon. The complete interaction model will determine the usage of the Skill and thus the functions to be programmed.  

Whenever changes were made, the project needs to be saved and build. It then will be automatically setup to the Echo Device of the workgroup, which is already connected up to this account.

(The echo can be accessed by logging in to alexa.amazon.com using the same login credentials. Further, all interactions will be stored there which can be handy for testing.)

###Editing Code

The code for the Narrator project lies in my (Thomas) GitHub. On request, it can be joined and cloned, but for now, it is private.
Once the code is cloned, verify that all maven dependencies are setup correctly. The pom.xml lists all dependencies needed both from Amazon and others. Most errors will occur, when something is simply missing.

Within the GitHub project, a JavaDoc is included. I tried to describe every method. Since Amazon’s ScoreKeeper was used as a very good reference, many lines were taken and edited to match the Narrator’s functionality. However, much of the original logic still persists.

The project consists of three main packages. The .narrator as the top level one, the .storage and the .storygenerator. Firstly, the narrator  package takes care of the main functionality of the application. It includes the handler function (NarratorSpeechletRequestStreamHandler.class) and interaction phrases. Secondly, the storage package sets a link to DynamoDB, the NoSQL database. This is not completely setup yet and needs completion. Lastly, the storygenerator does exactly what its name suggests, namely generating the story. For now, a hardcoded story is provided for testing reason. However, this is only temporary. The class shall connect  to Stefan’s server and receive a story. Most functions are already laid out, but need to be called or edited slightly. But the core idea might be changed, meaning the stories would be created differently.

The most important thing here was, to generate stories using nouns (e.g. ‘Narrator, tell me a story about intrigue, misery, and comedy’). It is not clear, whether Stefan’s code could support this functionality.

Additional functionality can be either added to the current packages or included within a matching new one. Please keep the neatness alive!

When finished with any changes, the code needs to be uploaded to the Lambda function. When using Eclipse AWS Toolkit, please follow the instructions of their toolbox (-> Eclipse AWS ToolKit). Otherwise, build a .zip or .jar file. I would recommend to do it using Maven within any terminal shell. Therefore install Maven with your package manager, open a terminal and navigate the your local folder, where the pom.xml of the Narrator project can be found. Here, type the following:

'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'

In this case, a .jar file would be build inside a new folder named Target. Simply upload this file to inside the Lamda function and voilà, the now runs online.

###Testing the Application

I am not going to lie, testing an Amazon Skill is a hassle. Within the code, I mostly ran main methods, to verify that my objects have the right layout or structure, that strings are being concatenated correctly and such things. Outside it gets a little more tricky but not impossible.

Firstly, no Amazon Echo is mandatory for using a skill. Within the ASK environment a testing feature is presented, which allows to enter any utterances and see their results. Nevertheless, I found that the results are not necessarily coherent with ‘real’ ones given by an echo. I struggled long before finding this out, debugging code were it turned out nothing needed to be done.

If in doubt (which might happen just too often) you can copy the Json of the test result and switch to the environment of the Lambda Function. Here, next to the save button (also upper right corner) is the test button. Create a new test scenario and paste the Json result and run the test. The result gives some more hints on where an error might have occured.

As a tipp, always manually verify that all endpoints (inside the code, and also ASK + Lambda) are correct. Secondly, check the path to the handler.class. It needs to be correct. Other then that, take a look at all responses and test them inside your IDE first.

If nothing else work, please do not contact me. Good Luck!


: )
