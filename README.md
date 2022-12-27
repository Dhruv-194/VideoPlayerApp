# VideoPlayerApp

### About -
A simple Video Player Android App which allows the user to view the videos present in their mobile phone.

### Working of the app - 
The app has three sections (a bottom nav-bar) - All Videos, Folders & Favourites Videos. 
The 'All Videos' tab shows all the videos present in the mobile phone. 
The 'Folders' fragment shows the videos sorted according to the folders. 

The user can also click on the 'like' button to like a video which can then be visisble in the 'Favourites' tab. 

### Built using - 
- Kotlin Programming Language 
- Exoplayer Library 
- Android Studio 

### Concepts used - 
- **MVVM Architecture :** Followed clean architecture and MVVM design pattern.
- **Coroutines :** To asynchronously load the videos in a list format in the fragment. 
- **Exoplayer :** Used the exoplayer library to play the videos present in the mobile. 
- **MediaStore :** To idenitfy and get the information of the media items present in the mobile phone of the user. 
- **ROOM Db :** To store the information of liked videos and retrieve the information to show in the 'Favourites Fragment'. 
- **Service :** Created a background VideoPlayer Service that runs in the app until the app is stopped by clearing from the memory. 
- **Dependency Injection Hilt :** Used along with ROOM Db to show favoruites video. 


