# MET-CS622-ChatBot-Project (Chat Bot)
An Android chat bot for MET CS622 final Project which queries smartwatch database to give user insights on their activity.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

* Please fork the repository and work from it so that I can incoorporate the changes you make or if you add any new features.
* You need to have the googleservices.json file to link to firebase, you can set it up using [Set up Firebase](https://firebase.google.com/docs/android/setup) or email me on (adit.modhvadia@gmail.com) for further instructions.
* You need to clone the backend for this project as well which you can find here [MET-CS622-ChatBot-Backend](https://github.com/aditmodhvadia/MET-CS622-ChatBot-Project-Backend). Instructions on how to clone it are given below.

### Installing

* Fork and clone both the front end Android repo and the back end Java server.
  * [MET-CS622-ChatBot-Android-App](https://github.com/aditmodhvadia/MET-CS622-ChatBot-Project.git)
  * [MET-CS622-ChatBot-Backend](https://github.com/aditmodhvadia/MET-CS622-ChatBot-Project-Backend)
* Simply run the following command from your terminal to get the source code on your system in the desired directory.

```
git clone https://github.com/<your_username>/MET-CS622-ChatBot-Project.git
```
```
git clone https://github.com/<your_username>/MET-CS622-ChatBot-Project-Backend.git
```

* Or you can fork this repository and then create a pull request for implementing the changes

* If you want to install the release apk on your android device then go to the release tab of the repo, and download the apk from the latest release.

## Deployment

* All releases would be ready to be isntalled on supported android devices
* This app is for education/research purpose only and hence won't be released on the playstore as of yet, prior announcement will be made.

## Features
* Run queries to database via chat messages.
* Messages are stored both locally and on cloud.
* User authentication allows any user to log in and retrieve previously typed messages and responses.
* Built with MVVM hence easy to maintain, test and easy to pickup.

## Built With

* [Firebase](https://firebase.google.com/) - A comprehensive mobile development platform, go serverless with firebase
* [Maven](https://maven.apache.org/) - Dependency Management
* [Git](https://git-scm.com/downloads) - Used for version control
* [FastAndroidNetworking](https://github.com/amitshekhariitbhu/Fast-Android-Networking) - Used FAN API to call REST APIs
* [Timber](https://github.com/JakeWharton/timber) - Used for logging

## Contributing

Will update the requirements and code of conduct soon.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/aditmodhvadia/MET-CS622-ChatBot-Project/tags). 

## Authors

* **Adit Modhvadia** - *Initial work* - [aditmodhvadia](https://github.com/aditmodhvadia/)

See also the list of [contributors](https://github.com/aditmodhvadia/MET-CS622-ChatBot-Project/contributors) who participated in this project.

## License

This project is licensed under the GPL License - see the [LICENSE](https://github.com/aditmodhvadia/Canteen_App/blob/master/LICENSE) file for details
