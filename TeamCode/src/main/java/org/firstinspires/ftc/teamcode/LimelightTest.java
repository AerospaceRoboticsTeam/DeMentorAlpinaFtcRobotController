package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@TeleOp(name="Limelight 3A Enhanced Test", group="Drive")
public class LimelightTest extends LinearOpMode {

    private Limelight3A limelight;
    private int currentPipeline = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        // Set telemetry interval for faster updates
        telemetry.setMsTransmissionInterval(1000);  // Changed to 1000, default was 11

        // Initialize Limelight
        limelight.pipelineSwitch(currentPipeline);

        /*
         * Starts polling for data.
         * Note: You can also use limelight.setPollRate(Hz) to adjust polling frequency.
         */
        limelight.start();

        telemetry.addData("Status", "Initialized - Ready");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // --- Gamepad Controls ---
            // Switch pipelines (0 through 9)
            if (gamepad1.dpad_up) {
                currentPipeline = Math.min(9, currentPipeline + 1);
                limelight.pipelineSwitch(currentPipeline);
                sleep(200); // Debounce
            } else if (gamepad1.dpad_down) {
                currentPipeline = Math.max(0, currentPipeline - 1);
                limelight.pipelineSwitch(currentPipeline);
                sleep(200); // Debounce
            }

            // --- Data Retrieval ---
            LLResult result = limelight.getLatestResult();

            telemetry.addData("Pipeline", currentPipeline);

            if (result != null && result.isValid()) {
                // Basic Target Data
                double tx = result.getTx();   // Horizontal Offset from Crosshair
                double ty = result.getTy();   // Vertical Offset from Crosshair
                double ta = result.getTa();   // Target Area (0% to 100%)

                telemetry.addLine("--- Target Data ---");
                telemetry.addData("Target Found", "YES");
                telemetry.addData("TX", "%.2f°", tx);
                telemetry.addData("TY", "%.2f°", ty);
                telemetry.addData("Area", "%.2f%%", ta);

                // Botpose (3D Position on Field)
                Pose3D botpose = result.getBotpose();
                if (botpose != null) {
                    telemetry.addLine("--- Botpose (Field) ---");
                    telemetry.addData("X", "%.2f m", botpose.getPosition().x);
                    telemetry.addData("Y", "%.2f m", botpose.getPosition().y);
                    telemetry.addData("Z", "%.2f m", botpose.getPosition().z);
                    telemetry.addData("Yaw", "%.2f°", botpose.getOrientation().getYaw(AngleUnit.DEGREES));
                }

                // Performance Metrics
                telemetry.addLine("--- Performance ---");
                telemetry.addData("Latency", "%.1f ms", result.getCaptureLatency() + result.getTargetingLatency());
                telemetry.addData("Staleness", "%.0f ms", (double) result.getStaleness());

            } else {
                telemetry.addData("Target Found", "NO");
                if (result == null) {
                    telemetry.addData("Error", "No Result from Limelight");
                }
            }

            telemetry.update();
        }

        // Ensure limelight stops when OpMode is finished
        limelight.stop();
    }
}