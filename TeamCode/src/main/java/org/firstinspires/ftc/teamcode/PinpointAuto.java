package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Autonomous(name="Pinpoint Autonomous (Verified)", group="Robot")
public class PinpointAuto extends LinearOpMode {

    // Use the shared hardware class
    RobotHardware robot = new RobotHardware();

    @Override
    public void runOpMode() {
        // Initialize hardware using the shared class
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized - Pinpoint Ready");
        telemetry.update();

        waitForStart();

        // --- EXECUTION STEPS ---
        
        // driveToPosition(Forward_Inches, StrafeLeft_Inches, Heading_Degrees, Speed)
        
        // Step 1: Drive Forward 6 inches (X=6, Y=0)
        driveToPosition(18, 0, 0, 0.4);
        sleep(1000);
        driveToPosition(18, 0, -90, 0.6);
        sleep(1000);
//        driveToPosition(18, 0, -90, 0.4);
//        sleep(1000);

        // Step 2: Drive back to start (X=0, Y=0)
        driveToPosition(0, 0, 0, 0.6);

        telemetry.addData("Status", "Autonomous Complete");
        telemetry.update();
    }

    /**
     * Drives the robot to a target coordinate using the Pinpoint Odometry Computer.
     * targetX: Inches Forward (positive) / Backward (negative)
     * targetY: Inches Left (positive) / Right (negative)
     * targetHeading: Target angle in Degrees (-180 to 180)
     */
    public void driveToPosition(double targetX, double targetY, double targetHeading, double speed) {
        while (opModeIsActive()) {
            // Must call update() every loop to get the latest position
            robot.pinpoint.update();
            Pose2D currentPos = robot.pinpoint.getPosition();
            
            // Calculate field-centric errors
            double xError = targetX - currentPos.getX(DistanceUnit.INCH);
            double yError = targetY - currentPos.getY(DistanceUnit.INCH);
            double hError = targetHeading - currentPos.getHeading(AngleUnit.DEGREES);

            // Normalize heading error so the robot takes the shortest path
            while (hError > 180) hError -= 360;
            while (hError < -180) hError += 360;

            // Exit loop if we are within 0.5 inches and 2 degrees of target
            if (Math.abs(xError) < 0.5 && Math.abs(yError) < 0.5 && Math.abs(hError) < 2) {
                robot.stopRobot();
                break;
            }

            // --- FIELD-CENTRIC TO ROBOT-CENTRIC CONVERSION ---
            double botHeading = currentPos.getHeading(AngleUnit.RADIANS);
            
            // Rotate field-centric error (x, y) into robot-centric (drive, strafe)
            double drive = xError * Math.cos(botHeading) + yError * Math.sin(botHeading);
            double strafe = -xError * Math.sin(botHeading) + yError * Math.cos(botHeading);

            // Turn speed calculation (P-gain). Capped at 0.5 for stability.
            double turn = Math.max(-0.5, Math.min(0.5, hError * 0.04)); 

            // --- MECANUM MOTOR POWER CALCULATION ---
            // Formula for standard Mecanum layout (Left motors reversed):
            // Drive: +Fwd, Strafe: +Left, Turn: +CCW
            double flPower = drive - strafe - turn;
            double blPower = drive + strafe - turn;
            double frPower = drive + strafe + turn;
            double brPower = drive - strafe + turn;

            // Normalize powers to stay within -1.0 to 1.0 and respect 'speed' limit
            double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(turn), 1);
            
            robot.setDrivePowers(
                (flPower / denominator) * speed,
                (blPower / denominator) * speed,
                (frPower / denominator) * speed,
                (brPower / denominator) * speed
            );

            telemetry.addData("Target", "X:%.1f Y:%.1f H:%.0f", targetX, targetY, targetHeading);
            telemetry.addData("Current", "X:%.1f Y:%.1f H:%.0f", 
                currentPos.getX(DistanceUnit.INCH), 
                currentPos.getY(DistanceUnit.INCH),
                currentPos.getHeading(AngleUnit.DEGREES));
            telemetry.update();
        }
    }
}
