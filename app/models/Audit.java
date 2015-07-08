package models;

import com.avaje.ebean.Model;
import play.mvc.Http;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
public class Audit extends Model {

    @Id
    public UUID id;

    public Date createdOn;
    @ManyToOne
    public User createdBy;
    public Date updatedOn;
    @ManyToOne
    public User updatedBy;
    public Date deletedOn;
    @ManyToOne
    public User deletedBy;
    public java.lang.Boolean deleted;

    public void clean() {
        this.createdOn = null;
        this.createdBy = null;
        this.updatedOn = null;
        this.updatedOn = null;
        this.deletedOn = null;
        this.deletedBy = null;
        this.deleted = null;
    }

    @Override
    public void save() {
        if (createdOn == null) {
            createdOn = new Date();
            createdBy = User.findByEmail(Http.Context.current().session().get("email"));
        }
        super.save();
    }

    @Override
    public void insert() {
        createdOn = new Date();
        createdBy = User.findByEmail(Http.Context.current().session().get("email"));
        super.insert();
    }

    @Override
    public void update() {
        updatedOn = new Date();
        updatedBy = User.findByEmail(Http.Context.current().session().get("email"));
        super.update();
    }

    @Override
    public void delete() {
        deleted = true;
        deletedOn = new Date();
        deletedBy = User.findByEmail(Http.Context.current().session().get("email"));
        super.delete();
    }
}
