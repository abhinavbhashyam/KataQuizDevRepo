# Kata - Quiz
For the take-home Kata assessment, I chose the Quiz option (i.e. option #1). In addition to implementing the required features, I chose to extend my implementation by adding additional features such as timers, progress bars, and a card-swiping mechanism that adds an extra layer of "fun"
to the user's quiz-taking process. This application was built using Android Studio and Java, and uses Firebase's Firestore and Authentication services to facliliate the storage of user data and their ability to authenticate with the application. Questions are fetched from the Open Trivia Database. A link to the database can be found here: https://opentdb.com/. Additionally, all of the source code for this application has been documented with Javadoc documentation, and can be found in this repository.

## Testing the App
There are multiple ways to test this application: 
  1. (Preferred) If you have access to an Android device, you may use the following link to grab the latest version of the application: https://appdistribution.firebase.dev/i/760093349349e695. Please follow the directions that are given upon clicking the link.
  2. If you do not have access to an Android device, you can try the app using an Android Device Emulator. One way to do this is by cloning this repository, downloading the latest version of Android Studio, and opening up the project within Android Studio. Then, you can open up an Emulator and run the app.
  3. If the above two options don't work, I have created a small demonstration video of some of the main features of the application in action. You can find the video right below (note: the video does not demonstrate the procedure for signing up; I filmed the video after signing up a user account; also, please do not enter any of your personal information into the application, so we can keep users anonymous).

https://github.com/abhinavbhashyam/KataQuizDevRepo/assets/84284109/5a22cdb6-9c93-47bf-be0a-65543f7023de

## Final Notes
For the card stack structure in my application, I used the following library: https://github.com/yuyakaido/CardStackView.
Also, some of the starter Firebase code (as well as some basic code for UI design) is from one of my previous projects, QuizzingApp (a link to it can be found here: https://github.com/BananaBruise/QuizzingApp).

## Potential Improvements
One thing I can fix in future iterations of the application is the issue of some of the symbols in the titles of questions not rendering properly: for example, the symbols &, "", and ö (as in Schrödinger) seem to be rendered incorrectly in the JSON that is returned from the database. One way I could address this issue is by scanning for these improperly rendered symbols in the JSON data, and correcting them before displaying the questions to the user.
