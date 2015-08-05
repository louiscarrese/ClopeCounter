package com.louiscarrese.clopecounter.business;

import android.content.Context;

import com.louiscarrese.clopecounter.model.Clope;
import com.louiscarrese.clopecounter.model.Jour;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by loule on 17/07/2015.
 */
public class JourBusiness {

    private Context context;

    public JourBusiness(Context ctx) {
        this.context = ctx.getApplicationContext();
    }

    public Date getCurrentDate() {
        return stripHours(new Date());
    }


    public Date stripHours(Date in) {
        //Calendar cal = new GregorianCalendar();
        Calendar cal = Calendar.getInstance();
        cal.setTime(in);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date out = cal.getTime();

        return out;
    }

    /**
     * Récupère le jour courant. Si il n'existe pas, il est initialisé.
     * @return Le Jour courant
     */
    public Jour getToday() {
        //Ouverture de la base de données
        Realm realm = Realm.getDefaultInstance();
        //Construction de la requete
        RealmQuery<Jour> query = realm.where(Jour.class);
        query.equalTo("date", this.getCurrentDate());

        //Execution
        Jour jour = query.findFirst();

        //Récupération des données
        if (jour == null) {
            jour = initJour(this.getCurrentDate());
        }

        return jour;
    }

    /**
     * Ajoute une Clope au Jour en cours.
     *
     * @return La journée en cours
     */
    public Jour addClope() {
        //Récupération du jour courant
        Jour jour = getToday();

        return addClope(jour);
    }

    /**
     * Ajoute une clope au Jour spécifié.
     *
     * @param jour Le Jour à modifier
     * @return Le jours modifié.
     */
    public Jour addClope(Jour jour) {
        //Ouverture de la transaction
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        //Création de l'objet Clope
        Clope clope = new Clope();
        clope.setDate(new Date());

        //Ecriture de la Clope
        realm.copyToRealm(clope);

        //Mise à jour des stats du jour qui prennent en compte le Jour courant
        jour.setNbClopes(jour.getNbClopes() + 1);
        jour.setAvg7Predict(computeAvg(clope.getDate(), -7, 0));

        //Commit
        realm.commitTransaction();

        return jour;
    }

    /**
     * Renvoie tous les jours (attention aux perfs...)
     * @return Un array de tous les jours en BDD
     */
    public Jour[] getAll() {

        Realm realm = Realm.getDefaultInstance();

        //TODO: Simplifier en n'explicitant pas la Query et en utilisant allObjects() ?
        RealmQuery<Jour> query = realm.where(Jour.class);

        RealmResults<Jour> results = query.findAllSorted("date");

        return results.toArray(new Jour[0]);
    }

    /**
     * Equivalent de Jour.toString() qu'on ne peut pas mettre dans jour à cause de Realm
     *
     * @param j le Jour à convertir en String
     * @return Une String représentant le Jour
     */
    public String jourToString(Jour j) {
        return j.getId() + " - " + j.getDate().toString() + " - " + j.getNbClopes() + " - " + j.getAvg7() + " - " + j.getAvg7Predict();
    }

    /**
     * Recalcule tous les Jours.
     */
    public void refreshStats() {
        Realm realm = Realm.getDefaultInstance();

        //Suppression de tous les jours
        realm.beginTransaction();
        realm.allObjects(Jour.class).clear();
        realm.commitTransaction();

        //Récupération de toutes les clopes
        RealmResults<Clope> clopes = realm.where(Clope.class).findAll();

        Map<String, Jour> jours = new HashMap<String, Jour>();

        for(Clope c : clopes) {
            //Calcul du Jour correspondant
            Date dateJour = getDateJourFromDateClope(c.getDate());
            String dateJourString = dateJour.toString();

            //Si c'est la première fois qu'on le voit on l'ajoute
            Jour j;
            if(!jours.containsKey(dateJourString)) {
                j = new Jour();
                j.setDate(dateJour);

                jours.put(dateJourString, j);
            } else {
                j = jours.get(dateJourString);
            }

            //On lui ajoute une clope
            j.setNbClopes(j.getNbClopes() + 1);
        }

        //On ajoute tous les jours au Realm
        realm.beginTransaction();
//        List<Jour> joursFromDb = realm.copyToRealm(jours.values());
        List<Jour> joursFromDb = new ArrayList<Jour>();
        for(Jour j : jours.values()) {
            joursFromDb.add(realm.copyToRealm(j));
        }
        realm.commitTransaction();

        //On recalcule les moyennes de tous les Jours après que toutes les clopes aient été mises
//        RealmResults<Jour> joursFromDb = realm.where(Jour.class).findAll();
        realm.beginTransaction();
        for(int i = 0; i < joursFromDb.size(); i++) {
            Jour j = joursFromDb.get(i);
            j.setAvg7(computeAvg(j.getDate(), -8, -1));
            j.setAvg7Predict(computeAvg(j.getDate(), -7, 0));
        }
        realm.commitTransaction();


    }

    /**
     * Calcule la date du Jour pour une date de clope donnée.
     * TODO: Pour l'instant, ne fait que supprimer les heures, mais pourrait à terme permettre
     * d'avoir des journées qui vont de 4h AM -> 4h AM.
     * @param dateClope La Date de la clope.
     * @return La date du Jour.
     */
    private Date getDateJourFromDateClope(Date dateClope) {
        return stripHours(dateClope);

    }

    /**
     * Initialise un Jour pour la date donnée.
     * -> Nombre de clopes : 0
     * -> Calcul de la moyenne à 7 jours en fonctions des autres jours présents en base.
     *
     * @param date La Date pour laquelle initialiser le Jour
     * @return Le Jour initialisé.
     */
    private Jour initJour(Date date) {

        //Création de l'objet
        Jour ret = new Jour();

        ret.setDate(date);

        //Ecriture en base
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Jour realmJour = realm.copyToRealm(ret);
        realm.commitTransaction();

        realm.beginTransaction();
        realmJour.setNbClopes(0);
        realmJour.setAvg7(computeAvg(date, -8, -1));
        realmJour.setAvg7Predict(computeAvg(date, 7, 0)); //TODO: Calcul certainement faux puisque le jour courant n'existe pas encore...
        realm.commitTransaction();
        return ret;
    }

    /**
     * Calcule le nombre de Clope moyen sur une période flottante à partir d'une date
     * TODO: Plutot que d'avoir nbJour, avoir un offsetStart et un offsetEnd
     * @param date La date de référence.
     * @param offsetStart Le début de la période à calculer par rapport à la date de référence.
     * @param offsetEnd La fin de la période à calculer par rapport à la date de référence.
     * @return
     */
    private float computeAvg(Date date, int offsetStart, int offsetEnd) {
        Date strippedDate = stripHours(date);

        //Calcul de la date de debut
        Calendar calDeb = new GregorianCalendar();
        calDeb.setTime(strippedDate);
        calDeb.add(Calendar.DAY_OF_YEAR, offsetStart);
        Date dateDeb = calDeb.getTime();

        //Calcul de la date de fin
        Calendar calFin = new GregorianCalendar();
        calFin.setTime(strippedDate);
        calFin.add(Calendar.DAY_OF_YEAR, offsetEnd);
        Date dateFin = calFin.getTime();

        //Ouverture de la base de données
        Realm realm = Realm.getDefaultInstance();

        //Construction de la requete
        RealmQuery<Jour> query = realm.where(Jour.class).between("date", dateDeb, dateFin);

        //Execution de la requete
        RealmResults<Jour> result = query.findAll();

        //Somme
        int sum = result.sum("nbClopes").intValue();
        int nbJour = offsetEnd - offsetStart;
        float avg = ((float)sum) / nbJour;


        return avg;

    }
}
