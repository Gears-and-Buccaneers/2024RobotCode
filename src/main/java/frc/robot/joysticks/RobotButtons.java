package frc.robot.joysticks;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface RobotButtons extends AutoCloseable {
  Trigger toggleBreakMode();

  Trigger zeroSensors();
}
