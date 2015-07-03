package models;

import javax.persistence.Id;
import java.util.Date;

public class Audit {

    @Id
    public int Id;

    public Date CreatedOn;
    public int CreatedBy;
    public Date UpdatedOn;
    public int UpdatedBy;
    public Date DeletedOn;
    public int DeletedBy;

}
