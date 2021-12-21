# DietCompanion
# This Project is a part of Future Ready Talent Internship Program 


#### Note : Since the application is Non-Production-Ready it has been successfully tested to support Android Smartphones having screen sizes greater than 6 inch and having an sdk version 29 ,i.e, Android 10 . For testing you can make use of Smartphones satisfying both these condition's(of having screen size >=6 inch and sdk version of 29) or can make use of an emulator generating the required conditions.Few Features(Viewing Details about Diet,Nutrition History and Self Diet Detail Upload) are not compatible with SDK >=30 because of the Azure-Cosmos library(com.azure.android:azuredata) for Android not containing an updated version of OkHttp which does not let it support SDK>=30, Rest Features(Azure B2C Authentication, Blob Storage related features and Application Insight(Azure Monitor) related features) work as intended in devices having SDK>=30.



This Project is an Android Application(Non-Production Ready).The application is implemented keeping in mind the Clean Code Principles, therefore it is built using Clean Architecutre and MVI. The main aim of this application is to help people improve their Diet. It consist of Diet Recommendation System built using Tensorflow-Lite .The Application provides an interface to Note Down Items consumed to get their Nutritional Values via Azure Cosmos DB. User will be able to keep a note of the intakes they have consumed via this application. It provides the facility to the User to capture their Images and Review them overtime to keep a note of how they have progressed over time. The same functionality is implemented using Azure Blob Storage. The application keeps security in mind by Authorizing User's using Azure's B2C. The application also provides information about various Diet's it consist of.It also makes use of AppCenter and Application Insight platform to generate database with the help of the user for making Recommendation System perform better 

## Tests - Complete Project Working Successfully
The application is successfully tested on **Xiaomi Mi A2 Device**
* Device Name : Xiaomi Mi A2 
* Screen Size : 5.99 inch 
* SDK : 29 (Android 10)
* Video : https://youtu.be/4ACOCBn2JtA

## Articles
**Articles** About these following topics will be published soon:

* Making A Custom Recommendation System using Tensorflow Lite and connecting it to Android Project
* Connecting Android Application to Azure Platform for Authentication via Azure Active Directory B2C.
* Connecting Android Application to Azure Storage 
* Connecting Android Application to Cosmos DB
