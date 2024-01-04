package frc.lib.hardware.Motors;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import frc.lib.hardware.HardwareRequirements;
import frc.lib.hardware.Motors.MotorControlers.MotorController;
import frc.lib.hardware.Motors.MotorControlers.Talon_SRX;
import frc.lib.hardware.Motors.PID.EncoderConfigs;
import frc.lib.hardware.Motors.PID.PIDConfigs;
import frc.lib.hardware.Motors.PID.SimConfig;
import frc.lib.hardware.sensor.encoders.Encoder;
import frc.robot.Robot;
import org.littletonrobotics.junction.LogTable;

public class Motor2 implements HardwareRequirements {
  // Enums --------------
  public enum Type {
    CIM(DCMotor.getFalcon500(1)),
    Falcon500(DCMotor.getFalcon500Foc(1)),
    VP775(DCMotor.getFalcon500(1));

    private DCMotor dcMotor;

    private Type(DCMotor dcMotor) {
      this.dcMotor = dcMotor;
    }

    protected DCMotor config() {
      return this.dcMotor;
    }
  }



  // -----------------------------------------
  private final MotorController mController;
  private final Type mType;
  private Encoder mEncoder;

  private boolean hasEncoder = false;
  private boolean isSimulated = false;

  private String name;

  public Motor2(ControllerType motorControllerType, int canID, Type motor) {
    this.mController = motorControllerType.config(canID);
    this.mType = motor;
    this.name = "Motor" + canID;
    this.isSimulated = RobotBase.isSimulation();
  }

  // ----------------- COnfigs ---------------------
  public Motor addEncoder(Encoder encoder) {
    this.mEncoder = encoder;
    hasEncoder = true;
    return this;
  }

  public Motor inverted(boolean enable) {
    mController.setInverted(enable);

    return this;
  }

  public Motor brakeMode(boolean enable) {
    mController.brakeMode(enable);

    return this;
  }

  private PIDController mFeedback;

  public Motor pidConfigs(PIDConfigs PIDConfigs) {
    mFeedback = new PIDController(1, 0, 0);

    return this;
  }

  public Motor EncoderConfigs(EncoderConfigs encoderConfigs) {

    return this;
  }

  public Motor setName(String name) {
    this.name = name;
    return this;
  }

  // controlling Motor contoler ---------------------------------
  public void setVolts(double Volts) {
    mController.runVolts(Volts);
  }

  public void setPositoin(double Positoin) {
    if (!hasEncoder) {
      throw new Error("you dumb. This motor has no encoder");
    }

    mSimFeedback.setSetpoint(Positoin);
    // mFeedback.setSetpoint(Positoin);

    // mAppliedVolts = mFeedback.calculate(getPositoin());
    // mAppliedVolts = MathUtil.clamp(mAppliedVolts, -12.0, 12.0);

    setVolts(mAppliedVolts);
  }

  public void setVelocity(double velocity) {
    if (!hasEncoder) {
      throw new Error("you dumb. This motor has no encoder");
    }
    mSimFeedback.setSetpoint(velocity);
    // mFeedback.setSetpoint(velocity);

    // mAppliedVolts = mFeedback.calculate(getVelocity());
    // mAppliedVolts = MathUtil.clamp(mAppliedVolts, -12.0, 12.0);

    setVolts(mAppliedVolts);
  }

  public void disable() {
    mController.disable();
  }

  // get ---------------------------------
  public int getCanID() {
    return mController.getCanID();
  }

  @Override
  public void close() throws Exception {
    mController.close();
    mEncoder.close();
  }

  public double getPositoin() {
    if (!hasEncoder) {
      throw new Error("you dumb. This motor has no encoder");
    }
    if (RobotBase.isReal()) {
      return mEncoder.getPositoin();
    } else {
      if (simVelocity)
        return mSimPositionMeters;
      else
        return mSimPositionRad;
    }
  }

  public double getVelocity() {
    if (!hasEncoder) {
      throw new Error("you dumb. This motor has no encoder");
    }
    if (RobotBase.isReal()) {
      return mEncoder.getVelocity();
    } else {
      return mSimVelocityDeg;
    }
  }

  // Required ---------------------------

  @Override
  public void toLog(LogTable table) {
    mController.toLog(table, name);
    table.put(name + "/Motor Type", mType.toString());
    if (hasEncoder) {
      table.put(name + "/Position (degrees)", getPositoin());
      table.put(name + "/Velocity (degreesPerSecond)", getVelocity());
      table.put(name + "/Velocity (MetersPerSecond)", getVelocity() * Units.inchesToMeters(2));
      // table.put(name + "/AppliedVolts (Volts)", mAppliedVolts);
      // table.put(name + "/CurrentDraw (Amps)", Math.abs(mSim.getCurrentDrawAmps()));
    }
  }

  @Override
  public boolean connected() {
    if (!mController.connected())
      return false;
    if (hasEncoder)
      return mEncoder.connected();
    return true;
  }
}
