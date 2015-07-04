package models;

import javax.persistence.Id;

import com.avaje.ebean.Model;

import java.util.Date;

public class Audit extends Model {

    @Id
    public long Id;

    public Date CreatedOn;
    public int CreatedBy;
    public Date UpdatedOn;
    public int UpdatedBy;
    public Date DeletedOn;
    public int DeletedBy;

}
