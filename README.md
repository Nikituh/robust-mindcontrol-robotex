
# Robotex Mind Control sample application

Robust **Android Studio** solution of Robotex's Mindcontrol competition to assist struggling participants.

On purpose checked out dirty commit with bare functionality so it wouldn't give too much away (honestly, I really do not write such crappy code: leave unused methods just standing there, an activity with 800 lines, commented out functionality etc.)

Neither will I be explaining Android development basics. Google it! Besides, it's not like you need it. This is a hackathon.

## Prerequisites

* Android Studio
* Android SDK - downloaded from within Android Studio
* Debug drivers (Windows only, Linux and Mac automatically find them when a phone is plugged in)

I will not be explaining how to install Android Studio. There are enough tutorials out there. Google it.

## What it does

* Connects to a Muse Brainscanner from the UI
* Sends forward/stop commands to a specific endpoint based on a timer that reads accelerometer data
* Make sure you have bluetooth and location enabled beforehand as it does not have much errorhandling

## What it needs to do

* Send data other than just accelerometer data
* Specific commands for specific data (not two-in-one as in the example)
* Not be timer-based

## Hints

* `private final Runnable tickUi = new Runnable() { }` in `MainActivity.java` is the function that reads data and sets command starts on line 586
* Command parameters and endpoint are at the start of `Networking.java` (you will need to change the endpoint)
* `DoStuff` is the async method that is called from the Activity
* `SendCommand` is the function that sends the request - there is no response handling
