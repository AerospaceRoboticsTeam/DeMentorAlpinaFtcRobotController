package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Robot-Centric Mecanum TeleOp code for FTC.
 * Features:
 * - Traditional robot-relative driving (joystick forward = robot forward)
 * - Power normalization to maintain movement ratios
 */
@TeleOp(name="Mecanum TeleOp (Robot-Centric)", group="Drive")
public class RobotCentricTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors using the configuration names from your project
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("yellow_left_front_mtr");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("blue_left_rear_mtr");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("orange_right_front_mtr");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("red_right_rear_mtr");

        // Standard Mecanum setup: Reverse the left side motors
        // This ensures that a positive power value moves all wheels "forward"
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized - Robot Centric");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // Raw joystick values
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            // Calculate motor powers using standard Mecanum kinematics
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            // Set motor powers
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            telemetry.addData("Status", "Running (Robot-Centric)");
            telemetry.addData("Stick Y (Drive)", y);
            telemetry.addData("Stick X (Strafe)", x);
            telemetry.update();
        }
    }
}
