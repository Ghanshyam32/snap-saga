# Snap Saga
Welcome to Snap Saga, a dynamic social media app that lets you share your moments, post photos and reels, connect with friends, and more! Built with Kotlin and powered by Firebase, Snap Saga provides a seamless experience for users to engage, follow, like, and interact with posts, all while maintaining high security and real-time data syncing.


## Features

- Post Photos & Reels: Share your creative moments by uploading photos and video reels to your profile.
- Authentication: Secure login via Email/Password or Google authentication using Firebase.

- Firestore Database: All user data, captions, post details, and interactions are stored safely in Firestore.
- Firebase Storage: Photos, videos, and other media are uploaded and stored in Firebase Storage.
- Like & Comment: React to others' posts with likes and comments.
- Follow Users: Follow your favorite users and stay up-to-date with their posts.
- Search Accounts: Find and follow other users by their usernames.
- Account Management: Update your username, password, and profile details.




## Table of Contents
1. Getting Started
2. Prerequisites
3. Installation
4. Usage
5. Features Walkthrough
6. Firebase Configuration

## Getting Started
Follow these steps to get a local copy of Snap Saga running on your device.

**Prerequisites**\
Before running the project, ensure you have the following installed:
- Android Studio (with Kotlin support)
- Firebase Console Account: To configure Firebase Authentication, - Firestore, and Storage.
- Emulator or Android Device: For testing the app.

## Installation

1. Clone the repository:

```bash
  git clone https://github.com/Ghanshyam32/snap-saga.git

```
2. Navigate to the project directory:
```bash
cd snap-saga
```
3. Open the project in Android Studio:

- Launch Android Studio.
- Open the cloned project (snap-saga).
4. Install dependencies:

- Android Studio will prompt you to install dependencies. If not, navigate to File > Sync Project with Gradle Files to install them.
5. Configure Firebase:

- Follow the steps in the Firebase Configuration section below to link Firebase services to your app.
6. Run the app:

- Connect an Android device or use an emulator.
- Click on the Run button (Shift + F10 in Android Studio).

## Usage

Once Snap Saga is set up, you can:

- Login/Sign-Up: Choose between Email and Google Sign-In for quick access.
- Create Posts: Upload photos and videos (reels) with captions.
- Like: Engage with your friends' posts by liking.
- Follow Users: Follow people and get updates on their latest posts.
- Manage Account: Update your profile picture, change your username, or update your password.

## Features Walkthrough
**Post Photos and Reels**
- Tap on the Create Post button to upload a photo or video.
- Add a creative caption and share your media with the world.
**Like and Comment on Posts**
- Browse your feed, like posts, and leave comments to show your support.
**Follow Users**
- Search for users by username and follow them to stay updated with their content.
**Account Management**
- Go to your profile settings to update your username, change your password, and upload a new profile photo.
**Search for Accounts**
- Use the Search bar to find new accounts and follow them.
## Firebase Configuration
1. **Set up Firebase Authentication**
    1. Go to the [Firebase Console](https://firebase.google.com/).
    2. Create a new Firebase project or select an existing one.
    3. Enable Email/Password and Google Sign-In authentication providers in the Firebase console:\
        Go to Authentication > Sign-in method and enable both Email/Password and Google.
    4. Download the google-services.json file from Firebase and place it in your app’s app/ directory.
    5. Make sure Firebase dependencies are included in your build.gradle files:
gradle
```
implementation 'com.google.firebase:firebase-auth:21.0.1'
implementation 'com.google.firebase:firebase-firestore:24.0.0'
implementation 'com.google.firebase:firebase-storage:20.0.0'

```

2. **Set up Firestore Database**
    1. In Firebase Console, go to Firestore Database and click Create Database.
    2. Set up the database rules (you can use test mode for development):
```
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```
3. Set up Firebase Storage
    1. Go to Firebase Storage and create a storage bucket.
    2. Configure the storage rules to allow authenticated users to upload content
```
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```
4. Final Firebase Integration
    1. Verify that the Firebase SDK dependencies are properly integrated.
    2. Test your app to ensure Authentication, Firestore, and Storage are working as expected.

## Showcase
Check out some screenshots of Snap Saga in action:
### **Screenshots**

Here are some screenshots from the app to give you a glimpse of the user interface and features:

#### 1. **Home Feed**
![Home Feed](assets/screenshots/home-feed.png)
*Browse your feed and like or comment on posts.*

#### 2. **Create Post Screen**
![Create Post](assets/screenshots/create-post.png)
*Upload your photos and videos and add captions.*

#### 3. **Profile Screen**
![Profile Screen](assets/screenshots/profile-screen.png)
*Update your profile, change your username, and see your latest posts.*

#### 4. **Follow Users**
![Follow Users](assets/screenshots/follow-users.png)
*Search for users and follow them to stay updated with their content.*


## Contact
For questions, suggestions, or feedback, feel free to contact us:

- Email: [Ghanshyam Mishra](mailto:ghanshyammishra3205615@gmail.com)
- LinkedIn: [Ghanshyam Mishra](https://www.linkedin.com/in/ghanshyam-mishra-83949a124/)
