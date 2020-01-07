# Scéalability
Scéalability's Alexa Skill Variations

This repository contains three skills: the master-skill, the interactive storytelling skill, and the non-interactive storytelling skill.

As it was decided to not publish any skills of this project using Amazon’s web services, in order to avoid monthly costs, this repository only stores each skills’ source code. As build .jar files, the skills can be uploaded to Amazon Lambda. Using the Alexa Developer Console, a new skill can then be created and linked to that code in order to run it on any Echo device. Since this repository only contains the source code, necessary DynamoDB tables and a storage for the Scéalextric knowledge-base modules (Amazon S3 was used in regards to this research) need to be integrated as well. 

Here is a link to Prof. Tony Veales Scéalextric repository that includes necessary files: https://github.com/prosecconetwork/Scealextric 

## The Master-Skill:
The master-skill contains the entire functionality of the Alexa modules that are part of Scéalability. This includes the initialization of the blackboard architecture as a DynamoDB table. In order to run the skill on any Echo device, a new DynamoDB table needs to be created and necessary credentials have to passed to the stream handler function of the source code. The same applies for the necessary Scéalextric knowledge-base modules. During this project, we relied on Amazon's S3 bucket service for this purpose which offers quick access for any kinds of data.\par

## The Interactive and the Non-Interactive Skill:
Both the interactive and the non-interactive skill require the same precautions as the master-skill in order to be run a new device. Once installed, they perform the same tasks as the study conditions 5 and 6.
