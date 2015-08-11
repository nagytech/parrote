package models;

import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.UUID;

/**
 * Created by jnagy on 11/08/15.
 */
@Entity
public class Session extends Audit {

    @ManyToMany
    public User user;
    public DateTime lastAccess;

}
