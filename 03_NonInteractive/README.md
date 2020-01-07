# Non-Interactive Alexa Skill

The non-interactive Alexa storytelling skill.


### Content:

1. Description of the Skill
2. Setting Up Alexa
3. Editing Code

---

### Description of the Skill

The non-interactive Alexa skill presents its users with narrative that, unlike the interactive skill, does not allow for changes throughout the narration. Although it asks users whether they would like to continue the narration, their responses do not affect the skill which keeps telling the story regardless of the user’s input. It can certainly be argued, that this skill presents its users with a limited form of interactivity anyhow. The reason why it is called ‘non-interactive’ is to distinguish it from the passive narratives of Scéalability’s theatricality scenario. 

The skill uses a DynamoDB table to store the story sections. This table is the blackboard which includes all story related information for Alexa and keeps track of the current progress of a narration. This enables the skill to stop whenever one wishes and can be continued from thereon. As the skill relies for a DynamoDB table to be active beforehand, it needs to be provided (including the necessary credentials) before the skill can be run. Of course, the skill can be altered to create and maintain its own tables, this would, however, cause monthly costs for its maintenance by Amazon. Because this skill was used in a single study only, it was decided to rely on a single table.

This skill has been part of the studies of Scéalability.  

### Setting Up Alexa

To use Scéalability on an Echo device requires an active Amazon account. Once created, developer.amazon.com can be accessed with the same login credentials. Here, the Alexa Skills Kit (ASK) can be used that allows for new custom Alexa skills to be created. Usually, Alexa skills are carried out by two of Amazon’s services: (1) Amazon Lambda, which is used to host the program code of the skill, and (2) the Alexa Developer Console, which controls how user input is recognised by an Echo device. Both services have to be connected using the provided credentials. 

While Amazon Lambda contains the underlying logic of a skill, Alexa Developer Console manages the executive logic between user and program code. This can further be seen as  the interaction model of the skill, which describes what the application can do, and how it will interact with its audience. It starts with the invocation name, the title of the skill, which is used to open and execute a skill. This name has to set inside the Alexa Developer Console and reaches out to the Lambda functions.

The different handler functions (beneath the main stream handler that opens a new skill session) enable the different functionalities of the master-skill. Although there are handlers for both the solo-acts as well as the partner-acts, each handler can be linked to specific user input in Alexa’s Developer Console. It is important to note, that Alexa uses machine learning to expand the possible user input. Although this may be of assistance in some cases, more possible functions to be invoked may result in possible overlaps when activating a function. This may lead to misunderstandings of the system that does not react appropriately to the commands. 


### Editing Code

The project consists of three main packages. The .alexa package, which includes the NarratorStreamHandler, is responsible for opening a new session. Similar to a main function, this handler maintains included functionalities and is the gate to Amazon’s web-services and thus needs all credentials. The .handlers package contains all other function handlers that can be added to the NarratorStreamHandler. Last, the .server package contains necessary retrievers for the blackboard and the character board that stores the characters as provided by a user.

The skill relies on a variety of Maven dependencies that have to be included to the project’s structure. In order to build the code the following Maven command-line inout needs to be entered within the project’s source folder (where the pom.xml file should be).

'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'

The created .jar can then be uploaded to the Amazon Lamda function. Any alterations on this end only affect the functionality of the Alexa Development Console if changes are made in regards to the stream handlers. The testing environment may help to identify possible errors.
