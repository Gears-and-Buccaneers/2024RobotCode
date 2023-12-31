package frc.robot.Subsytems.Arm;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class ArmSub extends SubsystemBase implements AutoCloseable {
  public final ArmRequirments arm;

  private final String simpleName = this.getClass().getSimpleName();

  public ArmSub(ArmRequirments arm) {
    this.arm = arm;

    System.out.println("[Init] Creating " + simpleName + " with:");
    System.out.println("\t" + arm.getClass().getSimpleName());

    this.arm.setBrakeMode(true);
  }

  @Override
  public void periodic() {
    Logger.processInputs(simpleName, arm);

    arm.loadPreferences();
    arm.periodic();
  }

  @Override
  public void simulationPeriodic() {
  }

  // Commands ---------------------------------------------------------
  public Command IntakePosition() {
    return run(() -> {
      arm.wristAngleSetpoint(Rotation2d.fromDegrees(270));
      arm.elevatorAngleSetpoint(Rotation2d.fromDegrees(15));
      arm.elevatorLengthSetpoint(Units.inchesToMeters(35));
    })
        .handleInterrupt(
            () -> {
              arm.disable();
            });
  }

  public Command OutakePositon() {
    return run(() -> {
      arm.wristAngleSetpoint(Rotation2d.fromDegrees(125));
      arm.elevatorAngleSetpoint(Rotation2d.fromDegrees(140));
      arm.elevatorLengthSetpoint(Units.inchesToMeters(60));
    })
        .handleInterrupt(
            () -> {
              arm.disable();
            });
  }

  public Command IntakePositionAuton() {
    return run(() -> {
      arm.wristAngleSetpoint(Rotation2d.fromDegrees(270));
      arm.elevatorAngleSetpoint(Rotation2d.fromDegrees(15));
      arm.elevatorLengthSetpoint(Units.inchesToMeters(35));
    })
        .handleInterrupt(
            () -> {
              arm.disable();
            })
        .until(() -> {
          return arm.atSetpoint();
        });
  }

  public Command OutakePositonAuton() {
    return run(() -> {
      arm.wristAngleSetpoint(Rotation2d.fromDegrees(125));
      arm.elevatorAngleSetpoint(Rotation2d.fromDegrees(140));
      arm.elevatorLengthSetpoint(Units.inchesToMeters(60));
    })
        .handleInterrupt(
            () -> {
              arm.disable();
            })
        .until(() -> {
          return arm.atSetpoint();
        });
  }

  // ---------------------------------------------------------
  @Override
  public void close() throws Exception {
    arm.close();
  }
}
