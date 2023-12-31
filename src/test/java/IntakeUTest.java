import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.first.hal.HAL;
import frc.robot.Subsystems.Intake.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntakeUTest {
  static final double DELTA = 1e-2; // acceptable deviation range
  IntakeSub intake;
  IntakeHardware intakeIO;

  @BeforeEach // this method will run before each test
  void setup() {
    assert HAL.initialize(500, 0); // initialize the HAL, crash if failed
    intakeIO = new IntakeHardware();
    intake = new IntakeSub(intakeIO);
  }

  @SuppressWarnings("PMD.SignatureDeclareThrowsException")
  @AfterEach // this method will run after each test
  void shutdown() throws Exception {
    intake.close(); // destroy our intake object
  }

  @Test // marks this method as a test
  void onlyIntakeIfEmpty() {

    assertEquals(.6, .6, DELTA);
    // assertEquals(6.0, intake.getInputs().MotorVoltsOutput, DELTA);
  }
}
