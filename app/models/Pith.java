package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pith extends Audit {

    static final Finder<Long, Pith> find = new Finder<>(Pith.class);

    @Column(length = 128, unique = true)
    public String code;
    @ManyToMany(mappedBy = "piths")
    public List<BonMot> bonMots = new ArrayList<>();


    public static List<Pith> findList(String[] codes) {

        List<Pith> piths = find.where()
                .in("code", codes)
                .findList();

        return piths;

    }

    public static Pith findOrCreate(String code) {

        Pith pith = find.where()
                .eq("code", code)
                .findUnique();

        if (pith == null) {
            pith = new Pith();
            pith.code = code;
            pith.save();
            Logger.info("New pith created [{}]", pith);
        }

        return pith;
    }

    @Override
    public String toString() {
        return String.format("#%s", code);
    }

}
