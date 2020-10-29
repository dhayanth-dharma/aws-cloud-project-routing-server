# Cloud Computing Project
## Project Members : 

>**Poulomi Nandy**

>**Aninda Maulik**

>**Aditya Das**

>**Dhayananth Dharmalingam**

(Video demo : [click-here](https://www.youtube.com/watch?v=gS8hE2Xa2jM))

## Project Overview 
![Alt text](https://raw.githubusercontent.com/ujm-cloud-computing-open/front-app/main/proj-architecture.png "Project architecture")


## About the Project

This project is developed in order to demostrate AWS Cloud Services by following **WEB-QUEUE-WORKER** architecture. 
The project is consist of three applications. 

# Application overview 
 This project consist of three main applications.  
    
> ## **FRONT-APP**
>
> found in `front-app` repository 
>   
>  This application is an Angular application that developed in order to serve the user interface to the client. All the functionalities were developed based on asynchronous refresh method. 

> ## **CLIENT-WORKER**
>
> found in `server-routing-client` repository 
>   
>  This application is a Java Spring Cloud application that act as a client in the [WEB-QUEUE-WORKER] architecture. This application connects to the [front-app] and passing request to the SQS. Also, recieves responses from SQS and send it to the [front-app]. The [front-app] will be communicated using [REST HTTP] and [Web Socket] protocols.   


> ## **SERVER-WORKER**
>
> found in `server-worker` repository 
>   
>   This application is a Java Java Spring Cloud application that act as a worker in the [WEB-QUEUE-WORKER] architecture. This application perfomrs the computations based on the request. This application only can be communicated through SQS Message and responding to the SQS Message. This application will be hosted in the EC2 Instance.    

. The detail overview and the implementation of this Project is described `report.pdf` file in our repository.




## How to get started with the Project
 
 1. To run the program Java 11 or greater version must be installed in the computer
 2. Download the Application file from Git repositories (front-app & server-routing-client repositories need to be downloaded) (Here is the link : [click-here](https://github.com/ujm-cloud-computing-open))

 3. edit configuration files in server-routing-client as follow.
    * aws.access.key: your AWS Access key. (Ex: `AWSDZXCSD23`)
    * aws.secret.key: your AWS SAecret key. (Ex: `AWSDZXCSD23`)
    * aws.session: your AWS Access session. (Ex: `some long string`)
    * aws.queue.number_list_sender: your SQS queue name for number list `request`. 
    * aws.queue.number_list_reciever: your SQS queue name for number list `response`. 
    * aws.s3.bucket.image_list.name: Your AWS S3 Bucket name for image storing.
    * aws.s3.bucket.image_list.original.folder: Your AWS S3 Bucket **folder** name for **orignal** image storing
    * aws.s3.bucket.image_list.edited.folder: Your AWS S3 Bucket **folder** name for **edited** image storing

 5. Import the project to Eclipse as Maven Project. (Here is the link : [click-here](https://www.eclipse.org/downloads/packages/installer))
 7. Run the java application
 8. Run the front-app (see below for instruction)
 9. In the User interface there are Specific buttons  which allows to see what the application is performing. 



#### NOTE!!!
> Please refer `report.pdf` for more information. 

## Stack
* Java Spring boot v2
* Java 11
* Angular 8

## Reference 
 ### Library Reference 
**Java AWS SDK** : https://aws.amazon.com/sdk-for-java/

**Configuration file** : https://www.codejava.net/coding/reading-and-writing-configuration-for-java-application-using-properties-class

**Spring boot** : https://spring.io/projects/spring-boot

**Jackson.jar** : https://github.com/FasterXML/jackson

 
## ANGULAR SPECIFIC SUPPORT

Your are required to install NODE JS and Angular CLI in local computer to proceed with below steps. 
(How to install NODE JS : [click-here](https://phoenixnap.com/kb/install-node-js-npm-on-windows))
(How to install Angular CLI: [click-here](https://cli.angular.io/))

## Initiate node package
Run `npm install` in the root folder.
## Start the application
Run `ng serve` to start the application in local environment.

# Front-App For Cloud Computing Project

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 8.3.24.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
