package adamyan.neuralengine;

import java.util.*;
import java.io.*;
import java.util.function.Function;

/**
 * Represents a neural network
 */
public class Network {
    /**
     * Array of layer configurations
     */
    private final Layer[] layers;

    /**
     * Total number of layers in the network
     */
    private final int size;

    /**
     * 3D array of network weights: - First dimension: layer index - Second
     * dimension: input neuron index (including bias) - Third dimension: output
     * neuron index
     */
    private final double[][][] weights;

    /**
     * Makes a deep copy of another Neural network "other"
     */
    public Network(Network other) {
        this.layers = other.layers; // layers don't need to be deep copied
        this.size = other.size;
        this.weights = other.getWeightCopy();
    }

    /**
     * Constructs a network with random weights
     * @param layers the specified architecture
     */
    public Network(Layer[] layers) {
        this.layers = layers;
        this.size = layers.length;
        this.weights = getWeightsFromArchitecture(layers);
        randomizeWeights();
    }

    /**
     * Creates and initializes a new neural network from a file
     *
     * @param layers Array of layer configurations
     * @param srcFileName Weights source file name
     * @throws RuntimeException when the file isn't found
     */
    public Network(Layer[] layers, String srcFileName) {
        this.layers = layers;
        this.size = layers.length;
        this.weights = getWeightsFromArchitecture(layers);

        File file = new File(srcFileName);
        if (!file.exists()) throw new RuntimeException("Didn't find the file");

        try (DataInputStream dis = new DataInputStream(new FileInputStream(srcFileName))) {
            // Read network size and verify
            int savedSize = dis.readInt();
            if (savedSize != size) {
                throw new RuntimeException("Saved network size doesn't match current network.");
            }

            // Read each layer
            for (int layer = 0; layer < size - 1; layer++) {
                int inputSize = dis.readInt();
                int outputSize = dis.readInt();

                // Verify dimensions
                if (inputSize != layers[layer].length() + 1 ||
                        outputSize != layers[layer + 1].length()) {
                    throw new RuntimeException("Saved layer dimensions don't match current network.");
                }

                // Read weights
                for (int i = 0; i < layers[layer].length() + 1; i++) {
                    for (int j = 0; j < layers[layer + 1].length(); j++) {
                        weights[layer][i][j] = dis.readDouble();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading weights: " + e.getMessage());
        }
    }

    private double[][][] getWeightsFromArchitecture(Layer[] layers) {
        double[][][] resultingWeights = new double[layers.length - 1][][];

        for (int layer = 0; layer < layers.length - 1; layer++) {
            // Add +1 for bias weights
            resultingWeights[layer] = new double
                    [layers[layer].length() + 1]
                    [layers[layer + 1].length()];
        }
        return resultingWeights;
    }

    /**
     * Initializes network weights with random values using Xavier/Glorot
     * initialization
     * <p>
     * Implements Xavier/Glorot initialization which helps with: 1. Preventing
     * vanishing/exploding gradients 2. Maintaining appropriate scale of
     * gradients through the network Scale factor is calculated as sqrt(2 /
     * (fan_in + fan_out))
     */
    public void randomizeWeights() {
        Random random = new Random();

        for (int layer = 0; layer < size - 1; layer++) {
            // Xavier/Glorot initialization
            double scale = Math.sqrt(2.0f / (layers[layer].length() + layers[layer + 1].length()));

            for (int i = 0; i < layers[layer].length() + 1; i++) {
                for (int j = 0; j < layers[layer + 1].length(); j++) {
                    double r = (random.nextDouble() * 2.0f - 1.0f);
                    weights[layer][i][j] = r * scale;
                }
            }
        }
    }

    /**
     * Mutates the neural network randomly
     *
     * @param probabilityOfMutation is the probability that each weight is changed
     * @param maxMutationStrength   is the amount a weight is max allowed to change
     */

    public void mutateWeights(double probabilityOfMutation, double maxMutationStrength) {
        for (double[][] weightArray2D : weights) {
            for (double[] weightArray : weightArray2D) {
                for (int i = 0; i < weightArray.length; i++) {
                    // change each individual weight with a chance of "probabilityOfMutation"
                    if (Math.random() < probabilityOfMutation) {
                        weightArray[i] += (2 * Math.random() - 1) * maxMutationStrength;
                    }
                }
            }
        }
    }

    /**
     * Saves network weights to a file
     *
     * @return true if save was successful, false otherwise
     */
    public void writeToFile(String tarFileName) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(tarFileName))) {
            // Write network size
            dos.writeInt(size);

            // For each layer, write dimensions
            for (int layer = 0; layer < size - 1; layer++) {
                dos.writeInt(layers[layer].length() + 1); // +1 for bias
                dos.writeInt(layers[layer + 1].length());

                // Write weights for this layer
                for (int i = 0; i < layers[layer].length() + 1; i++) {
                    for (int j = 0; j < layers[layer + 1].length(); j++) {
                        dos.writeDouble(weights[layer][i][j]);
                    }
                }
            }

            System.out.println("Weights saved to " + tarFileName);
        } catch (IOException e) {
            throw new RuntimeException("Error saving weights: " + e.getMessage());
        }
    }

    /**
     * Performs forward propagation through the network
     * <p>
     * This function: 1. Propagates input through each layer 2. Applies weights
     * and biases 3. Handles special case for softmax in output layer 4. Applies
     * activation functions 5. Returns final layer output
     *
     * @param input Array of input values
     * @return Array containing output layer activations
     */
    public double[] feedForward(double[] input) {
        Objects.requireNonNull(input);
        double[] currentLayerActivations = input;

        for (int layerIdx = 0; layerIdx < size - 1; layerIdx++) {
            double[] nextLayerActivations = new double[layers[layerIdx + 1].length()];

            // Forward propagation
            for (int inputNeuron = 0; inputNeuron < layers[layerIdx].length(); inputNeuron++) {
                for (int outputNeuron = 0; outputNeuron < layers[layerIdx + 1].length(); outputNeuron++) {
                    nextLayerActivations[outputNeuron] += currentLayerActivations[inputNeuron] * weights[layerIdx][inputNeuron][outputNeuron];
                }
            }

            // Add bias terms
            for (int outputNeuron = 0; outputNeuron < layers[layerIdx + 1].length(); outputNeuron++) {
                nextLayerActivations[outputNeuron] += weights[layerIdx][layers[layerIdx].length()][outputNeuron];
            }

            // Activation function
            Function<Double, Double> activation = layers[layerIdx + 1].activationFunction();

            for (int outputNeuron = 0; outputNeuron < layers[layerIdx + 1].length(); outputNeuron++) {
                nextLayerActivations[outputNeuron] = activation.apply(nextLayerActivations[outputNeuron]);
            }

            // Swap buffers
            currentLayerActivations = nextLayerActivations;
        }

        return currentLayerActivations;
    }

    /**
     * @return a deep copy of the networks weights
     */
    public double[][][] getWeightCopy() {
        double[][][] weightClone = new double[size - 1][][];

        for (int layerIdx = 0; layerIdx < size - 1; layerIdx++) {
            int inputCount = layers[layerIdx].length() + 1;
            weightClone[layerIdx] = new double[inputCount][];

            for (int inputIdx = 0; inputIdx < inputCount; inputIdx++) {
                weightClone[layerIdx][inputIdx] = weights[layerIdx][inputIdx].clone();
            }
        }

        return weightClone;
    }

    @Override
    public String toString() {
        return Arrays.toString(layers);
    }

    public Layer[] getLayers() {
        return layers;
    }
}