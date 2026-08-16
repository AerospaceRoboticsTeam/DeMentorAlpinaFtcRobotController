package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@TeleOp(name="Mecanum TeleOp (Pinpoint - Field-Centric)", group="Drive")
public class PinpointTeleOp extends LinearOpMode {

    // Use the shared hardware class
    RobotHardware robot = new RobotHardware();

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize all hardware using the shared class
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized - Pinpoint Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Update the Pinpoint computer
            robot.pinpoint.update();

            // Reset heading/position if the 'options' button is pressed
            if (gamepad1.options) {
                robot.pinpoint.resetPosAndIMU();
            }

            double stickY = -gamepad1.left_stick_y;
            double stickX = gamepad1.left_stick_x * 1.1; // Counteract friction
            double rx = gamepad1.right_stick_x;

            // Get heading from Pinpoint for field-centric logic
            Pose2D pos = robot.pinpoint.getPosition();
            double botHeading = pos.getHeading(AngleUnit.RADIANS);

            // Rotate movement vector by robot heading
            double rotX = stickX * Math.cos(-botHeading) - stickY * Math.sin(-botHeading);
            double rotY = stickX * Math.sin(-botHeading) + stickY * Math.cos(-botHeading);

            // Calculate motor powers
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double fl = (rotY + rotX + rx) / denominator;
            double bl = (rotY - rotX + rx) / denominator;
            double fr = (rotY - rotX - rx) / denominator;
            double br = (rotY + rotX - rx) / denominator;

            // Apply powers to motors
            robot.setDrivePowers(fl, bl, fr, br);

            telemetry.addData("X (in)", pos.getX(DistanceUnit.INCH));
            telemetry.addData("Y (in)", pos.getY(DistanceUnit.INCH));
            telemetry.addData("Heading (deg)", pos.getHeading(AngleUnit.DEGREES));
            telemetry.update();
        }
    }
}
