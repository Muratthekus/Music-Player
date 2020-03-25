
# Mussic -On Progress

Mussic is a music player app built on MVVM android architecture.
Mussic can play the songs that are on your device or songs on youtube

- [x] Database were built
- [x] Android service was added
- [x] Music player was added
- [x] BroadCast Receiver added
- [x] Notification added
- [x] Trigger action on notification
- [x] Share music on another app
- [ ] Play songs on youtube was successfully done
- [ ] Playlist create/delete/add music to playlist

## Getting Started
Since the app was developed in Kotlin, some changes have been made to use the android library
- On gradle.properties fields you must add
```
kapt.incremental.apt=true
```
- On app gradle file when import android room

> ~~annotationProcessor~~ "android.arch.persistence.room:compiler:$room_version"

> kapt "android.arch.persistence.room:compiler:$room_version"

To get more info
> [Annotation Processor](https://medium.com/@robhor/annotation-processing-for-android-b7eda1a41051)
> [Kapt](https://kotlinlang.org/docs/reference/kapt.html)

#### Mussic flow chart
```mermaid
graph LR

Activity[ Main Activity ] -- Activity Start </br> Inflate fragment home -->FragmentHome[Fragment Home]
FragmentHome -- Fetch musics --> Database


FragmentHome -- set adapter music list --> RecylerviewAdapter(Recyler View Adapter)

RecylerviewAdapter -- Send clicked tem --> FragmentHome

FragmentHome -- User trigger an action **Play/Pause/Resume** </br> Connect with service --> Service1(Service)

Service2(Service) -- Build notification </br> & </br> Send player state --> Notification1(Notification)

Notification1 -- Connect with Broadcast Receiver --> BroadCastReceiver1(Broadcast Receiver)

BroadCastReceiver1 -- User trigger an action on notification </br> pause/shuffle/replay --> Service2

Notification1 -- User clicked the content. </br> Open app and start Main Acitivty --> Activity2[Main Activity]
```

#### Architecture
Built on MVVM
> Model + View + ViewModel

> ```mermaid
> graph LR
> Activity/Fragments --> ViewModel[ViewModel - with Livedata-]
> ViewModel --> Repository
> Repository --> Database
> ```

- [Android Room Library](https://developer.android.com/jetpack/androidx/releases/room)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Livedata](https://developer.android.com/topic/libraries/architecture/livedata)
- [Observable](https://developer.android.com/reference/java/util/Observable)
- [BroadCast Receiver](https://android.jlelse.eu/broadcast-receivers-for-beginners-a9d7aa03fb76)
- [Service](https://developer.android.com/reference/android/app/Service.html)


###### Licence
[MIT](https://choosealicense.com/licenses/mit/)

