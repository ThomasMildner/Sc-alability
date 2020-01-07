# Scéalability
Scéalability's Alexa Skill Variations

This repository contains three skills: the master-skill, the interactive storytelling skill, and the non-interactive storytelling skill.
The skills can be installed on any Echo device as a developer project.

## The Master-Skill:
The master-skill contains the entire functionality of the Alexa modules that are part of Scéalability. This includes the initialization of the blackboard architecture as a DynamoDB table. In order to run the skill on any Echo device, a new DynamoDB table needs to be created and necessary credentials have to passed to the stream handler function of the source code. The same applies for the necessary Scéalextric knowledge-base modules. During this project, we relied on Amazon's S3 bucket service for this purpose which offers quick access for any kinds of data.\par

## The Interactive and the non-Interactive Skill:
Both the interactive and the non-interactive skill require the same precautions as the master-skill in order to be run a new device. Once installed, they perform the same tasks as the study conditions 5 and 6.
