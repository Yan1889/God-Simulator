package adamyan.neuralengine.activations;

/**
 * Functional interface for activation functions
 */
@FunctionalInterface
public interface ActivationFunction_I {
    double apply(double input);
}
