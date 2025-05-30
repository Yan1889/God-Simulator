package adamyan;

import java.util.*;
import java.util.function.Function;

/**
 * represents a colony, made up of ants
 */
public class AntPopulation {
    private World world;

    // ---- ant stuff ----
    private int antCount;
    private List<Ant> ants;
    private int rayCount;
    private double rayLength;
    private double maxVelocity;
    private double lifeSpan;

    // ---- genetic stuff ----
    private double elitePercentage;
    private Function<Ant, Double> fitnessFunction;


    public void calculateFrame() {}
    public void flood() {}
}
