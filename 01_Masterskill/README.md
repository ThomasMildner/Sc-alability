# Masterskill
The complete Alexa skill as part of the Scéalability framework.


### Content:

1. Description of the Skill
2. Setting Up Alexa
3. Editing Code

---

### Description of the Skill

The master-skill contains all Alexa-based modules of Scéalability's theatricality and interactivity scenarios. It initializes the blackboard as an Amazon DynamoDB table and retrieves necessary Scéalextric knowledge-base modules from an Amazon S3 bucket. Both the DynamoDB table and the S3 bucket have to be instantiated beforehand. Moreover, the Scéalextric knowledge-base modules have to be uploaded to the bucket while URLs have to be passed to the program code of Scéalability. Also, the stream handler functions have to be passed to Amazon’s skill back-end and sample utterances have to entered that serve as invokes for the particular handler functions . Once all credentials s and preparations have been set up properly, the skill can be uploaded as a custom Amazon Lambda function.

The master-skill provides functions for both solo-acts and partner-acts of the theatricality scenario. While Alexa simply reads a pre-chosen story from the blackboard in a solo-act condition, it needs a commentary agent as a partner to tell a story in the form of a partner-act. When telling a story alone, Alexa can either fetch a story based on user input or a random one.

In case of a partner-act, the master-skill includes all necessary functionality to follow the described meta-model of Alexa. This allows Alexa to react to the commentary agent and keep the narration going. It also gives Alexa the ability to comment on certain behaviour of the commentary agent if necessary.

This skill further provides all functions necessary to generate individual Scéalextric NOC character instances. Such instances are needed, if a character is to be introduced in a story, or to find a character-based story Alexa will tell. The introductions can be either in the form of a ‘weaponized introduction’ which uses the affordances of the favourable weapon of a NOC to enter a scene, or in the form of a ‘cross-over introduction’ which uses positive and negative talking points of similar NOCs whose intersection is the desired character. Another function uses the appearing NOCs of a story to generate unique story titles. These are in the style of ‘The Good, the Bad and the Ugly’ (The {X}, the {Y} and the {Z}).

Depending on the desired usage, the code can be adapted to allow other performances as well. Since everything is programmed modular, the code should be easily expandable in the future.

### Setting Up Alexa

To use Scéalability on an Echo device requires an active Amazon account. Once created, developer.amazon.com can be accessed with the same login credentials. Here, the Alexa Skills Kit (ASK) can be used that allows for new custom Alexa skills to be created. Usually, Alexa skills are carried out by two of Amazon’s services: (1) Amazon Lambda, which is used to host the program code of the skill, and (2) the Alexa Developer Console, which controls how user input is recognised by an Echo device. Both services have to be connected using the provided credentials. 


While Amazon Lambda contains the underlying logic of a skill, Alexa Developer Console manages the executive logic between user and program code. This can further be seen as  the interaction model of the skill, which describes what the application can do, and how it will interact with its audience. It starts with the invocation name, the title of the skill, which is used to open and execute a skill. This name has to set inside the Alexa Developer Console and reaches out to the Lambda functions.

The different handler functions (beneath the main stream handler that opens a new skill session) enable the different functionalities of the master-skill. Although there are handlers for both the solo-acts as well as the partner-acts, each handler can be linked to specific user input in Alexa’s Developer Console. It is important to note, that Alexa uses machine learning to expand the possible user input. Although this may be of assistance in some cases, more possible functions to be invoked may result in possible overlaps when activating a function. This may lead to misunderstandings of the system that does not react appropriately to the commands. 


### Editing Code

The project consists of four main packages. The .alexa package contains all handlers including the NarratorStreamHandler which opens a new session with included function handlers. Similar to a main function, this handler maintains included functionalities and is the gate to Amazon’s web-services and thus needs all credentials. The .knowledgebase package contains the objects necessary to create new Scéalextric knowledge modules and NOC instances. Everything related to story generation and parsing of input text is contained within the .story package. Last, the .toolkit package contains necessary retrievers for the blackboard, the stories, and the Scéalextric tables. It further contains a class to generate a NOC counterpart list that is used by the introduction generator of Scéalability. 

The code may be altered in future to describe more agents for the multi-agent storytelling performances. Since it is within this Alexa skill that the blackboard is initialized and further partially maintained (each participating agent maintains their own entries of the blackboard), it needs to add new agents to be included on the blackboard as well as necessary data for them to perform a story. 

The skill relies on a variety of Maven dependencies that have to be included to the project’s structure. In order to build the code the following Maven command-line inout needs to be entered within the project’s source folder (where the pom.xml file should be).

'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'

The created .jar can then be uploaded to the Amazon Lamda function. Any alterations on this end only affect the functionality of the Alexa Development Console if changes are made in regards to the stream handlers. The testing environment may help to identify possible errors.
