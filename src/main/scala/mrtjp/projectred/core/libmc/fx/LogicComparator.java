package mrtjp.projectred.core.libmc.fx;

public class LogicComparator {

    public static int compare(ParticleLogic o1, ParticleLogic o2) {
        return (o1.equals(o2)) ? 0 : (o1.getPriority() > o2.getPriority()) ? 1 : -1;
    }
}
