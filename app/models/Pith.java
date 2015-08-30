package models;

import play.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Pith model
 *
 * Used to store #tags within bonmots.
 *
 * NOTE: piths are stored without the # character prefix.
 */
public class Pith extends ModelBase {

    /**
     * Text code representing the pith
     */
    @Column(length = 128, unique = true)
    public String code;

    /**
     * Get the pith entries matching the supplied codes
     *
     * @param codes
     * @return
     */
    public static List<Pith> findList(String[] codes) {

//        List<Pith> piths = find.where()
//                .in("code", (Object[])codes)
//                .findList();
//
//        return piths;

        return null;

    }

    /**
     * Get or create and get a pith matching the supplied code
     * @param code
     * @return
     */
    public static Pith findOrCreate(String code) {

//        // Attempt to find the pith
//        Pith pith = find.where()
//                .eq("code", code)
//                .findUnique();
//
//        /* NOTE: Will need to be threadsafe here.. */
//
//        // If the pith does not exist, create it
//        if (pith == null) {
//            pith = new Pith();
//            pith.code = code;
//            pith.save();
//            Logger.info("New pith created [{}]", pith);
//        }
//
//        return pith;

        return null;
    }

    /**
     * Format the pith to include a # prefix
     * @return
     */
    @Override
    public String toString() {
        return String.format("#%s", code);
    }

}
