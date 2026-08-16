package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class RobotHardware {
    public DcMotor frontLeft, backLeft, frontRight, backRight;
    public GoBildaPinpointDriver pinpoint;

    public void init(HardwareMap hardwareMap) {
        // Motor initialization
        frontLeft = hardwareMap.dcMotor.get("yellow_left_front_mtr");
        backLeft = hardwareMap.dcMotor.get("blue_left_rear_mtr");
        frontRight = hardwareMap.dcMotor.get("orange_right_front_mtr");
        backRight = hardwareMap.dcMotor.get("red_right_rear_mtr");

        // Set motors to use brake mode for better precision
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set directions
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Pinpoint initialization
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        
        // --- CHANGE THESE OFFSETS TO YOUR PHYSICAL MEASUREMENTS ---
        pinpoint.setOffsets(92.5, -162.5, DistanceUnit.MM); 
        
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, 
                                      GoBildaPinpointDriver.EncoderDirection.REVERSED);
        
        pinpoint.resetPosAndIMU();
    }

    public void setDrivePowers(double fl, double bl, double fr, double br) {
        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);
    }

    public void stopRobot() {
        setDrivePowers(0, 0, 0, 0);
    }
}
