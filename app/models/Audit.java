package models;

import com.avaje.ebean.Model;
import play.mvc.Http;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.UUID;

/**
 * Audit class
 *
 * Used as a base class for all other models to record
 * primary key / create / update / delete metadata.
 */
@MappedSuperclass
public class Audit extends Model {

    /**
     * Primary Key
     */
    @Id
    public UUID id;

    /**
     * Created Date
     */
    public Date createdOn;
    /**
     * User who created the record
     */
    @ManyToOne
    public User createdBy;
    /**
     * Updated Date
     */
    public Date updatedOn;
    /**
     * User who updated the record
     */
    @ManyToOne
    public User updatedBy;
    /**
     * Deleted Date
     */
    public Date deletedOn;
    /**
     * User who deleted the record
     */
    @ManyToOne
    public User deletedBy;
    /**
     * Flag to denote if the record is soft deleted.
     */
    public java.lang.Boolean deleted;

    /**
     * Cleans the metadata fields
     */
    public void clean() {
        this.createdOn = null;
        this.createdBy = null;
        this.updatedOn = null;
        this.updatedOn = null;
        this.deletedOn = null;
        this.deletedBy = null;
        this.deleted = null;
    }

    /**
     * Override to update the record's metadata
     */
    @Override
    public void save() {
        if (createdOn == null) {
            createdOn = new Date();
            createdBy = User.findByEmail(Http.Context.current().session().get("email"));
        }
        super.save();
    }

    /**
     * Override to update record's metadata
     */
    @Override
    public void insert() {
        createdOn = new Date();
        createdBy = User.findByEmail(Http.Context.current().session().get("email"));
        super.insert();
    }

    /**
     * Override to update the record's metadata
     */
    @Override
    public void update() {
        updatedOn = new Date();
        updatedBy = User.findByEmail(Http.Context.current().session().get("email"));
        super.update();
    }

    /**
     * Override to soft delete the record
     */
    @Override
    public void delete() {
        deleted = true;
        deletedOn = new Date();
        deletedBy = User.findByEmail(Http.Context.current().session().get("email"));
        super.delete();
    }
}
