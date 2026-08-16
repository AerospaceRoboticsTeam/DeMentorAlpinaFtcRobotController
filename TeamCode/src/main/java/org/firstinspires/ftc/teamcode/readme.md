## Code Notes

### What Is BotPose and How is it Used.

Botpose (short for "Robot Pose") is the Limelight's calculation of your robot's exact 3D position and orientation relative to the field's starting origin (usually the center of the field).
Here is a breakdown of what it is and how to use it:

1. How it Works
The Limelight uses AprilTags to calculate Botpose. Because the field layout is standardized, the Limelight knows exactly where every AprilTag is located (e.g., "Tag #11 is at $X=72, Y=0$").
When the camera sees a tag, it calculates its own position relative to that tag. By combining that with the "Camera Offset" you set in the Limelight web dashboard (where the camera is mounted on your robot), it translates that into where the center of your robot is on the field.

2. The Data Structure (Pose3D)
In the code you have, `result.getBotpose()` returns a Pose3D object:
- Position (X, Y, Z): These are coordinates in meters.
  - $0,0$ is the center of the tiles.
  - $Z$ is the height of your robot's center off the ground.
- Orientation (Yaw, Pitch, Roll):
  - Yaw: This is your robot's heading (which way it's facing). This is the most useful value for FTC.
  - Pitch/Roll: Usually near zero unless your robot is tipping or climbing.

3. Practical Use Cases
  A. Field-Centric Navigation
Instead of just knowing a target is "to the left," you know exactly where you are. You can use this to "re-localize" your Odometry (like your Pinpoint driver). If your wheels slip and your encoders get off track, one glance at an AprilTag provides a "ground truth" coordinate to reset your position.
  B. "Go to Point" Autonomous
If you know your botpose is $(1.0, 0.5)$ and the scoring basket is at $(0, 1.5)$, you can calculate the exact vector to drive there:
```double distanceX = targetX - botpose.getPosition().x;
double distanceY = targetY - botpose.getPosition().y;
// Use these to drive the robot to the specific spot
```
  C. Alliance-Specific Poses
Limelight actually provides different versions of Botpose depending on which side of the field you are on. In your code, you can use:
- `result.getBotpose()`: Field-center origin.
- `result.getBotpose_red()`: Origin at the Red Alliance wall.
- `result.getBotpose_blue()`: Origin at the Blue Alliance wall.

4. Tips for Accuracy
- Calibration: You must enter the correct "Camera Transform" in the Limelight web UI (how many inches forward/up/left the camera is from the robot's center). If this is wrong, your Botpose will "swing" in a circle when the robot turns.
- Multiple Tags: Botpose is much more stable when the camera sees two or more tags at once.
- Staleness: Always check result.isValid() before using the data, as shown in your enhanced test code, to ensure the camera currently sees a valid tag.

#OLD

## TeamCode Module

Welcome!

This module, TeamCode, is the place where you will write/paste the code for your team's
robot controller App. This module is currently empty (a clean slate) but the
process for adding OpModes is straightforward.

## Creating your own OpModes

The easiest way to create your own OpMode is to copy a Sample OpMode and make it your own.

Sample opmodes exist in the FtcRobotController module.
To locate these samples, find the FtcRobotController module in the "Project/Android" tab.

Expand the following tree elements:
 FtcRobotController/java/org.firstinspires.ftc.robotcontroller/external/samples

### Naming of Samples

To gain a better understanding of how the samples are organized, and how to interpret the
naming system, it will help to understand the conventions that were used during their creation.

These conventions are described (in detail) in the sample_conventions.md file in this folder.

To summarize: A range of different samples classes will reside in the java/external/samples.
The class names will follow a naming convention which indicates the purpose of each class.
The prefix of the name will be one of the following:

Basic:  	This is a minimally functional OpMode used to illustrate the skeleton/structure
            of a particular style of OpMode.  These are bare bones examples.

Sensor:    	This is a Sample OpMode that shows how to use a specific sensor.
            It is not intended to drive a functioning robot, it is simply showing the minimal code
            required to read and display the sensor values.

Robot:	    This is a Sample OpMode that assumes a simple two-motor (differential) drive base.
            It may be used to provide a common baseline driving OpMode, or
            to demonstrate how a particular sensor or concept can be used to navigate.

Concept:	This is a sample OpMode that illustrates performing a specific function or concept.
            These may be complex, but their operation should be explained clearly in the comments,
            or the comments should reference an external doc, guide or tutorial.
            Each OpMode should try to only demonstrate a single concept so they are easy to
            locate based on their name.  These OpModes may not produce a drivable robot.

After the prefix, other conventions will apply:

* Sensor class names are constructed as:    Sensor - Company - Type
* Robot class names are constructed as:     Robot - Mode - Action - OpModetype
* Concept class names are constructed as:   Concept - Topic - OpModetype

Once you are familiar with the range of samples available, you can choose one to be the
basis for your own robot.  In all cases, the desired sample(s) needs to be copied into
your TeamCode module to be used.

This is done inside Android Studio directly, using the following steps:

 1) Locate the desired sample class in the Project/Android tree.

 2) Right click on the sample class and select "Copy"

 3) Expand the  TeamCode/java folder

 4) Right click on the org.firstinspires.ftc.teamcode folder and select "Paste"

 5) You will be prompted for a class name for the copy.
    Choose something meaningful based on the purpose of this class.
    Start with a capital letter, and remember that there may be more similar classes later.

Once your copy has been created, you should prepare it for use on your robot.
This is done by adjusting the OpMode's name, and enabling it to be displayed on the
Driver Station's OpMode list.

Each OpMode sample class begins with several lines of code like the ones shown below:

```
 @TeleOp(name="Template: Linear OpMode", group="Linear Opmode")
 @Disabled
```

The name that will appear on the driver station's "opmode list" is defined by the code:
 ``name="Template: Linear OpMode"``
You can change what appears between the quotes to better describe your opmode.
The "group=" portion of the code can be used to help organize your list of OpModes.

As shown, the current OpMode will NOT appear on the driver station's OpMode list because of the
  ``@Disabled`` annotation which has been included.
This line can simply be deleted , or commented out, to make the OpMode visible.



## ADVANCED Multi-Team App management:  Cloning the TeamCode Module

In some situations, you have multiple teams in your club and you want them to all share
a common code organization, with each being able to *see* the others code but each having
their own team module with their own code that they maintain themselves.

In this situation, you might wish to clone the TeamCode module, once for each of these teams.
Each of the clones would then appear along side each other in the Android Studio module list,
together with the FtcRobotController module (and the original TeamCode module).

Selective Team phones can then be programmed by selecting the desired Module from the pulldown list
prior to clicking to the green Run arrow.

Warning:  This is not for the inexperienced Software developer.
You will need to be comfortable with File manipulations and managing Android Studio Modules.
These changes are performed OUTSIDE of Android Studios, so close Android Studios before you do this.
 
Also.. Make a full project backup before you start this :)

To clone TeamCode, do the following:

Note: Some names start with "Team" and others start with "team".  This is intentional.

1)  Using your operating system file management tools, copy the whole "TeamCode"
    folder to a sibling folder with a corresponding new name, eg: "Team0417".

2)  In the new Team0417 folder, delete the TeamCode.iml file.

3)  the new Team0417 folder, rename the "src/main/java/org/firstinspires/ftc/teamcode" folder
    to a matching name with a lowercase 'team' eg:  "team0417".

4)  In the new Team0417/src/main folder, edit the "AndroidManifest.xml" file, change the line that contains
         package="org.firstinspires.ftc.teamcode"
    to be
         package="org.firstinspires.ftc.team0417"

5)  Add:    include ':Team0417' to the "/settings.gradle" file.
    
6)  Open up Android Studios and clean out any old files by using the menu to "Build/Clean Project""
