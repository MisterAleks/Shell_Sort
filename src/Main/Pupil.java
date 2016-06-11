package Main;

public class Pupil {
    private int id;
    private String name;
    private double misses;

    public Pupil(int id, String name, double misses) {
        this.id = id;
        this.name = name;
        this.misses = misses;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMisses() {
        return misses;
    }
}
