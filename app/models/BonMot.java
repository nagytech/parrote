package models;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.Logger;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BonMot extends Audit {

    static final Finder<Long, BonMot> find = new Finder<>(BonMot.class);

    @ManyToOne
    public User user;
    @Constraints.Required
    @Constraints.MaxLength(128)
    public String text;
    @ManyToMany
    @JsonIgnore
    public List<Pith> piths = new ArrayList<>();

    public static List<BonMot> getLatest(int page, int pageSize, String[] pithArray) {

        ExpressionList<BonMot> motsEl = find.where();
        if (pithArray != null && pithArray.length > 0) {
            motsEl = motsEl.in("piths.code", pithArray);
        }

        List<BonMot> mots = motsEl.isNull("deleted")
                .orderBy("createdOn DESC")
                .findPagedList(page, pageSize)
                .getList();

        return mots;

    }

    public static List<BonMot> getLatestForUser(User user, int page, int pageSize) {

        List<BonMot> mots = find.where()
                .eq("user.id", user.id)
                .isNull("deleted")
                .orderBy("createdOn DESC")
                .findPagedList(page, pageSize)
                .getList();

        Logger.debug("Found [{}] bonmots for user [{}]", mots.size(), user);

        return mots;
    }

}
