import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.simulation.DoubleSolenoidSim;
import edu.wpi.first.wpilibj.simulation.PWMSim;
import frc.robot.Subsytems.Intake.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntakeUTest {
    static final double DELTA = 1e-2; // acceptable deviation range
    IntakeSub intake;
    IntakeIO intakeIO;

    @BeforeEach // this method will run before each test
    void setup() {
        assert HAL.initialize(500, 0); // initialize the HAL, crash if failed
        intakeIO = new IntakeIOSim();
        intake = new IntakeSub(intakeIO);
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @AfterEach // this method will run after each test
    void shutdown() throws Exception {
        intake.close(); // destroy our intake object
    }

    @Test // marks this method as a test
    void retractThenIntakePice() {
        intake.retract(); // close the intake
        intake.intakePice(); // try to activate the motor

        assertEquals(6.0, intake.getInputs().MotorVoltsOutput, DELTA);
        assertEquals(true, intake.getInputs().isDeployed);
    }

    @Test
    void IntakePice() {
        intake.intakePice(); // try to activate the motor

        assertEquals(6.0, intake.getInputs().MotorVoltsOutput, DELTA);
        assertEquals(true, intake.getInputs().isDeployed);
    }

    @Test
    void ExstendThenIntakePice() {
        intake.extend(); // close the intake
        intake.intakePice(); // try to activate the motor

        assertEquals(6.0, intake.getInputs().MotorVoltsOutput, DELTA);
        assertEquals(true, intake.getInputs().isDeployed);
    }

    @Test
    void Extend() {
        intake.extend();
        assertEquals(true, intake.getInputs().isDeployed);
    }

    @Test
    void Retract() {
        intake.retract();
        assertEquals(false, intake.getInputs().isDeployed);
    }
}
