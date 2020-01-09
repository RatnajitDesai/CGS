NOTE : Final code is available under branch "fianl_build". Due to some issue I was unable to push code to "master" branch.

# CGS 
Summary: 
CGS is a mobile application designed to provide smooth grievance redressal mechanism.
It is designed in a way that will increase citizen coordination/engagement in the grievance redressal procedure as well as make it transparent.

Follwoing are the technologies we used for making CGS:

1) We have used architecture components alogn with Material design to enhance the user interface.
2) For Authentication and database, we made use of Google's Firebase Authentication services and Firebase Firestore for storing the data as well as to give real-time updates.
3) Firebase FCM is used to provide notifications regarding any change on the grievances raised by the citizen giving real-time updates on the grievances.
4) Firebase cloud functions that runs on server-side are used to listen to changes on each grievances in the database and in turn triggers notifications if any change happens on the database.
5) MVVM pattern used to design the application makes it easily scalable.
