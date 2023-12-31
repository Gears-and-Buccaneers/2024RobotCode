package frc.robot.Subsystems.Arm;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import frc.lib.hardware.Motors.*;
import frc.lib.hardware.Motors.PID.EncoderConfigs;
import frc.lib.hardware.Motors.PID.PIDConfigs;
import frc.lib.hardware.Motors.PID.SimConfig;
import frc.lib.hardware.sensor.encoders.REVBoreEncoder;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.LogTable;

public class ArmHardware implements ArmRequirements {
  private final Motor mElvatorPivot;
  private final Motor mElvatorExstend;
  private final Motor mWristPivot;

  @AutoLogOutput private Mechanism2d mechSetpoint;
  private MechanismLigament2d elevatorSetpoint;
  private MechanismLigament2d wristSetpoint;

  @AutoLogOutput private Mechanism2d mechAcual;
  private MechanismLigament2d elevatorAcual;
  private MechanismLigament2d wristAcual;

  public ArmHardware() {
    mElvatorPivot = new Motor(Motor.ControllerType.TallonSRX, 1, Motor.Type.Falcon500);
    mElvatorExstend = new Motor(Motor.ControllerType.TallonSRX, 1, Motor.Type.Falcon500);
    mWristPivot = new Motor(Motor.ControllerType.TallonSRX, 1, Motor.Type.Falcon500);

    mElvatorPivot
        .addEncoder(new REVBoreEncoder())
        .inverted(true)
        .pidConfigs(new PIDConfigs())
        .EncoderConfigs(new EncoderConfigs())
        .SimConfig(new SimConfig(false, 111.8, .5))
        .setName("mElvatorPivot");

    mElvatorExstend
        .addEncoder(new REVBoreEncoder())
        .inverted(true)
        .pidConfigs(new PIDConfigs())
        .EncoderConfigs(new EncoderConfigs())
        .SimConfig(new SimConfig(false, 4.2, 1))
        .setName("mElvatorExstend");

    mWristPivot
        .addEncoder(new REVBoreEncoder())
        .inverted(true)
        .pidConfigs(new PIDConfigs())
        .EncoderConfigs(new EncoderConfigs())
        .SimConfig(new SimConfig(false, 39.5, .5))
        .setName("mWristPivot");

    configMech();
  }

  @Override
  public void elevatorAngleSetpoint(Rotation2d angle) {
    elevatorSetpoint.setAngle(angle);
  }

  @Override
  public void wristAngleSetpoint(Rotation2d angle) {
    wristSetpoint.setAngle(angle);
  }

  @Override
  public void elevatorLengthSetpoint(double ft) {
    elevatorSetpoint.setLength(ft);
  }

  @Override
  public void periodic() {

    mElvatorPivot.setPositoin(Rotation2d.fromDegrees(elevatorSetpoint.getAngle()).getRadians());
    mElvatorExstend.setPositoin(Rotation2d.fromDegrees(elevatorSetpoint.getLength()).getRadians());
    mWristPivot.setPositoin(Rotation2d.fromDegrees(wristSetpoint.getAngle()).getRadians());

    elevatorAcual.setAngle(Rotation2d.fromRadians(mElvatorPivot.getPosition()).getDegrees());
    elevatorAcual.setLength(Rotation2d.fromRadians(mElvatorExstend.getPosition()).getDegrees());
    wristAcual.setAngle(Rotation2d.fromRadians(mWristPivot.getPosition()).getDegrees());
  }

  public void setBrakeMode(boolean enable) {
    mElvatorPivot.brakeMode(enable);
    mElvatorExstend.brakeMode(enable);
    mWristPivot.brakeMode(enable);
  }

  @Override
  public void disable() {
    mElvatorPivot.disable();
    mElvatorExstend.disable();
    mWristPivot.disable();
  }

  public boolean atSetpoint() {
    boolean atElvatorPivot = (elevatorAcual.getAngle() - elevatorSetpoint.getAngle()) < 1;
    boolean atElvatorExstend = (elevatorAcual.getLength() - elevatorSetpoint.getLength()) < 1;
    boolean atWristPivot = (wristAcual.getAngle() - wristSetpoint.getAngle()) < 1;
    return (atElvatorPivot && atElvatorExstend && atWristPivot);
  }

  // -----------------------------
  @Override
  public void loadPreferences() {}

  @Override
  public void toLog(LogTable table) {
    mElvatorPivot.toLog(table);
    mElvatorExstend.toLog(table);
    mWristPivot.toLog(table);
    table.put("234", 123);
  }

  @Override
  public void close() throws Exception {
    mElvatorPivot.close();
    mElvatorExstend.close();
    mWristPivot.close();
  }

  private void configMech() {
    // units are in inches

    mechSetpoint = new Mechanism2d(Units.inchesToMeters(122), Units.inchesToMeters(126));
    // the mechanism root node
    MechanismRoot2d rootS =
        mechSetpoint.getRoot("arm", Units.inchesToMeters(50), Units.inchesToMeters(12));
    elevatorSetpoint =
        rootS.append(
            new MechanismLigament2d(
                "elevator", Units.inchesToMeters(40), 90, 7, new Color8Bit(Color.kPurple)));
    wristSetpoint =
        elevatorSetpoint.append(
            new MechanismLigament2d(
                "wrist", Units.inchesToMeters(15), 0, 5, new Color8Bit(Color.kPurple)));

    // units are in inches
    mechAcual = new Mechanism2d(Units.inchesToMeters(122), Units.inchesToMeters(126));
    // the mechanism root node
    MechanismRoot2d rootA =
        mechAcual.getRoot("arm", Units.inchesToMeters(50), Units.inchesToMeters(12));
    elevatorAcual =
        rootA.append(
            new MechanismLigament2d(
                "elevator", Units.inchesToMeters(40), 90, 8, new Color8Bit(Color.kCyan)));
    wristAcual =
        elevatorAcual.append(
            new MechanismLigament2d(
                "wrist", Units.inchesToMeters(15), 0, 6, new Color8Bit(Color.kCyan)));
  }
}
