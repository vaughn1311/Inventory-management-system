package models;

/**
 * subclass for parts that are inhouse
 * @Author Christopher Vaughn
 */
public class InHouse extends Part{

    /**
     * holds machine id for a part
     */
    private int machineId = 1;

    /**
     *Constructor
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     *
     * @param machineId sets machine id
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * @return gets machine id
     */
    public int getMachineId() {
        return machineId;
    }
}
