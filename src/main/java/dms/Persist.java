/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dms;

import dms.model.Document;
import dms.model.User;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
/**
 *
 * @author Samuel
 */
public class Persist {

    private SessionFactory sf;
    private Session session;
    private Transaction tx;
    private Query q;
    
    public Persist() {
//        try {
            setUp();
//        } catch (Exception ex) {
//            System.out.println("ex = " + ex.getMessage());
//            Logger.getLogger(Persist.class.getName()).log(Level.SEVERE, ex.getMessage(), ex.getMessage());
//        }
    }

    private void setUp() {
        Configuration c = new Configuration();
        c.configure();
        sf = c.buildSessionFactory();
    }

    public void begin() {
        if(sf.isClosed()) sf.openSession();
        session = sf.getCurrentSession();
        tx = session.beginTransaction();
        tx.begin();
    }

    public void saveEntity(Object object) {
        session.save(object);
    }

//    public void mergeEntity(Object object) {
//        em.merge(object);
//    }

    public void commit() {
        tx.commit();
    }

//    public void rollback() {
//        em.getTransaction().rollback();
//    }
//
//    public boolean isActive() {
//        return em.getTransaction().isActive();
//    }
//    
    public void close() {
        if(tx.isActive() && session.isOpen()) session.close();
    }
//    
//    public void flush() {
//        em.flush();
//    }

    public User getUserWithCredentials(String userName, String userPassword) {
        q = session.createQuery("from User u where u.name = :name and u.password = :password");
        q.setParameter("name", userName);
        q.setParameter("password", userPassword);
        try {
            User u = (User) q.uniqueResult();
            return u;
        } catch (NoResultException nre) {
            Logger.getLogger(Persist.class.getName()).log(Level.SEVERE, nre.getMessage(), nre);
            return null;
        }
    }
    
    public User getUserByUserName(String userName) {
        q = session.createQuery("from User u where u.name = :name");
        q.setParameter("name", userName);
        try {
            User u = (User) q.uniqueResult();
            return u;
        } catch (NoResultException nre) {
            Logger.getLogger(Persist.class.getName()).log(Level.SEVERE, nre.getMessage(), nre);
            return null;
        }
    }
    
    public List<Document> getDocuments() {
        q = session.createQuery("select d from Document d where d.id>0");
        try {
            List<Document> d = q.list();
            return d;
        } catch (NoResultException nre) {
            Logger.getLogger(Persist.class.getName()).log(Level.SEVERE, nre.getMessage(), nre);
            return null;
        }
    }

    public <T> T getInstance(Class<T> type) {
        if (type.getName().equalsIgnoreCase("dms.model.User")) {
            return (T) new User();
//        } else if (type.getName().equalsIgnoreCase("timetool.server.impl.UserImpl")) {
//            return (T) new Changes();
//        } else if (type.getName().equalsIgnoreCase("timetool.server.impl.ConnectionImpl")) {
//            return (T) new Document();
        } else {
            return null;
        }
    }

    //Select
//    public <T> T getEntity(long id, Class<T> type) {
//        String temp = type.getSimpleName();
//        query = em.createNamedQuery("get" + temp.substring(0, temp.length()-4) + "ById"); // maybe a better way to get the Name
//        query.setParameter("id", id);
//        try{
//            T o = (T) query.getSingleResult();
//            return o;
//        }
//        catch(NoResultException nre){
//            Logger.getLogger(PersistImpl.class.getName()).log(Level.INFO, nre.getMessage(), nre.getMessage());
//            return null;
//        }
//        return em.find(type, id);
//    }
//    //Update
//    @Override
//    public <T> void updateEntity(long id, Class<T> type) throws RemoteException {
//    	T object = getEntity(id, type);
//        em.merge(object); // return useful ?
//    }
//    
//    //Delete
//    public <T> void deleteEntity(long id, Class<T> type) throws RemoteException {
//    	T object = getEntity(id, type);
//        em.remove(object);
//    }
//    
}
