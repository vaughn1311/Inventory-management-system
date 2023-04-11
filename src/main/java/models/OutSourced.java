package models;

/**
 * subclass for parts that are outsourced
 * @Author Christopher Vaughn
 */
public class OutSourced extends Part{

    /**
     * holds the company name for an outsourced part
     */
    private String companyName = "";

    /**
     *Constructor
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     *
     * @param companyName sets company name
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     *
     * @return gets company name
     */
    public String getCompanyName() {
        return companyName;
    }
}

