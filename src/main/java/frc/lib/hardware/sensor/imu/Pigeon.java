package frc.lib.hardware.sensor.imu;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;

public class Pigeon implements IMU {
  private Pigeon2 pigeon2;

  private final StatusSignal<Double> pitch;
  private final StatusSignal<Double> pitchVelocity;

  private final StatusSignal<Double> yaw;
  private final StatusSignal<Double> yawVelocity;

  private final StatusSignal<Double> roll;
  private final StatusSignal<Double> rollVelocity;

  // Hardware Getting and setting methods-------------------------------

  public Pigeon(int canID) {
    System.out.println("[Init] Creating Pigeon2");

    // config pidgion
    pigeon2 = new Pigeon2(canID);

    pigeon2.getConfigurator().apply(new Pigeon2Configuration());
    pigeon2.getConfigurator().setYaw(0.0);

    // config values
    pitch = pigeon2.getPitch();
    pitch.setUpdateFrequency(25.0);
    pitchVelocity = pigeon2.getAngularVelocityXDevice(); // TODO check if x is correct value
    pitchVelocity.setUpdateFrequency(25.0);

    yaw = pigeon2.getYaw();
    yaw.setUpdateFrequency(100.0);
    yawVelocity = pigeon2.getAngularVelocityYDevice(); // TODO check if y is correct value
    yawVelocity.setUpdateFrequency(100.0);

    roll = pigeon2.getRoll();
    roll.setUpdateFrequency(25.0);
    rollVelocity = pigeon2.getAngularVelocityZDevice(); // TODO check if z is correct value
    rollVelocity.setUpdateFrequency(25.0);

    pigeon2.optimizeBusUtilization();
  }

  @Override
  public Rotation2d getPitch() {
    return Rotation2d.fromDegrees(pitch.getValueAsDouble());
  }

  @Override
  public double getPitchVelocity() {
    return pitchVelocity.getValueAsDouble();
  }

  @Override
  public Rotation2d getYaw() {
    return Rotation2d.fromDegrees(yaw.getValueAsDouble());
  }

  @Override
  public double getYawVelocity() {
    return yawVelocity.getValueAsDouble();
  }

  @Override
  public Rotation2d getRoll() {
    return Rotation2d.fromDegrees(roll.getValueAsDouble());
  }

  @Override
  public double getRollVelocity() {
    return rollVelocity.getValueAsDouble();
  }

  @Override
  public void offsetIMU(Rotation2d offset) {
    pigeon2.getConfigurator().setYaw(offset.getDegrees());
  }

  // ----------------------------------------------------------

  @Override
  public boolean connected() {
    return BaseStatusSignal.refreshAll(
            pitch, pitchVelocity,
            yaw, yawVelocity,
            roll, rollVelocity)
        .equals(StatusCode.OK);
  }

  // Unit Testing
  @Override
  public void close() throws Exception {
    pigeon2.close();
  }
}
