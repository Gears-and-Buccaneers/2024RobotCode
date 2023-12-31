package frc.robot.Subsystems.Intake;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Preferences;
import frc.lib.hardware.Motors.*;
import frc.lib.hardware.sensor.proximitySwitch.*;
import org.littletonrobotics.junction.LogTable;

public class IntakeHardware implements IntakeRequirements {
  public final Motor mIntake;
  public final ProximitySwitch switch1;

  private double setPercentOut = .1;

  private String SimpleName;

  public IntakeHardware() {
    mIntake = new Motor(Motor.ControllerType.TallonSRX, 10, Motor.Type.VP775);
    mIntake.setName("mIntake");
    switch1 = new Huchoo(3);

    initPrefrences();
  }

  public void setIntakeSpeed() {
    mIntake.setVolts(setPercentOut);
  }

  public void setOuttakeSpeed() {
    mIntake.setVolts(-setPercentOut);
  }

  public boolean canHoldPiece() {
    return switch1.isOpen();
  }

  public boolean hasPiece() {
    return switch1.isClosed();
  }

  // required things -------------------------------------------------
  public void setBrakeMode(boolean enable) {
    mIntake.brakeMode(enable);
  }

  public void periodic() {
    loadPreferences();
  }

  public void disable() {
    mIntake.disable();
  }

  // Loging ------------------------------------

  public void setSimpleName(String SimpleName) {
    this.SimpleName = SimpleName;
  }

  // Auto CLosing -------------------------------
  @Override
  public void close() throws Exception {
    switch1.close();
  }

  // prefrances ------------------
  private void initPrefrences() {
    Preferences.initDouble(SimpleName + "/maxVolts", setPercentOut);
  }

  public void loadPreferences() {
    setPercentOut = Preferences.getDouble(SimpleName + "/maxVolts", setPercentOut);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'initSendable'");
  }

  @Override
  public void toLog(LogTable table) {
    table.put("setPercentOut", setPercentOut);
    mIntake.toLog(table);
    switch1.toLog(table, "ProximitySwitch" + switch1.getDIOChannel());
  }
}
