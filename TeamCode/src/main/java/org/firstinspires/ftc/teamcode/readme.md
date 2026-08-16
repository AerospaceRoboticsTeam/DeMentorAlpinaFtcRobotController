## Code Notes

### What Is BotPose and How is it Used.

Botpose (short for "Robot Pose") is the Limelight's calculation of your robot's exact 3D position and orientation relative to the field's starting origin (usually the center of the field).
Here is a breakdown of what it is and how to use it:

1. __How it Works__ - The Limelight uses AprilTags to calculate Botpose. Because the field layout is standardized, the Limelight knows exactly where every AprilTag is located (e.g., "Tag #11 is at $X=72, Y=0$"). When the camera sees a tag, it calculates its own position relative to that tag. By combining that with the "Camera Offset" you set in the Limelight web dashboard (where the camera is mounted on your robot), it translates that into where the center of your robot is on the field.

2. __The Data Structure (Pose3D)__ - In the code you have, `result.getBotpose()` returns a Pose3D object:
   - Position (X, Y, Z): These are coordinates in meters.
     - $0,0$ is the center of the tiles.
     - $Z$ is the height of your robot's center off the ground.
   - Orientation (Yaw, Pitch, Roll):
     - Yaw: This is your robot's heading (which way it's facing). This is the most useful value for FTC.
     - Pitch/Roll: Usually near zero unless your robot is tipping or climbing.

3. __Practical Use Cases__
   1. Field-Centric Navigation - Instead of just knowing a target is "to the left," you know exactly where you are. You can use this to "re-localize" your Odometry (like your Pinpoint driver). If your wheels slip and your encoders get off track, one glance at an AprilTag provides a "ground truth" coordinate to reset your position.
   2. "Go to Point" Autonomous - If you know your botpose is $(1.0, 0.5)$ and the scoring basket is at $(0, 1.5)$, you can calculate the exact vector to drive there:
      ```double distanceX = targetX - botpose.getPosition().x;
      double distanceY = targetY - botpose.getPosition().y;
      // Use these to drive the robot to the specific spot
      ```
   3. Alliance-Specific Poses - Limelight actually provides different versions of Botpose depending on which side of the field you are on. In your code, you can use:
      - `result.getBotpose()`: Field-center origin.
      - `result.getBotpose_red()`: Origin at the Red Alliance wall.
      - `result.getBotpose_blue()`: Origin at the Blue Alliance wall.

4. __Tips for Accuracy__
   - Calibration: You must enter the correct "Camera Transform" in the Limelight web UI (how many inches forward/up/left the camera is from the robot's center). If this is wrong, your Botpose will "swing" in a circle when the robot turns.
   - Multiple Tags: Botpose is much more stable when the camera sees two or more tags at once.
   - Staleness: Always check result.isValid() before using the data, as shown in your enhanced test code, to ensure the camera currently sees a valid tag.
