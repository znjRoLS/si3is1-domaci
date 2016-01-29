/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domaci;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Rols
 */
public class Domaci {

    private static String usernameLogged;
    private static boolean loggedInCompany = false;
    private static boolean loggedInUser = false;
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static EntityTransaction t;
    
    private static Scanner reader = new Scanner(System.in);
    
    private static void startTransaction()
    {
        emf = Persistence.createEntityManagerFactory("domaciPU");
        em = emf.createEntityManager();
        t = em.getTransaction();
        t.begin();
    }
    
    private static void closeTransaction()
    {
        t.commit();
        em.close();
        emf.close();
    }
    
    /**
     * @param args the command line arguments
     */
    
    public static int showMenu()
    {
        if (!loggedInCompany && !loggedInUser)
        {
            System.out.println(
                "(1) Registruj se kao korisnik\n" +
                "(2) Uloguj se kao korisnik\n" +
                "(3) Registruj se kao firma\n" +
                "(4) Uloguj se kao firma\n" +
                "(0) Kraj\n"
            );
        }
        else if (loggedInUser)
        {
            System.out.println(
                "(1) Izloguj se\n" +
                "(2) Azuriraj podatke\n" +
                "(3) Prikazi firme\n" +
                "(4) Prikazi komponente\n" +
                "(5) Kupi komponente\n" +
                "(6) Rezervisi komponente\n" +
                "(7) Vidi rezervisane komponente\n" +
                "(0) Kraj\n"
            );
        }
        else if (loggedInCompany)
        {
            System.out.println(
                "(1) Izloguj se\n" +
                "(2) Azuriraj podatke\n" +
                "(3) Azuriraj artikle\n" +
                "(0) Kraj\n"
            );
        }
        
        return reader.nextInt();
    }
    
    public static void main(String[] args) {
        
        int res = 1;
        while(res != 0)
        {
            res = showMenu();
            if (res == 0)
                return;
            
            if (!loggedInCompany && !loggedInUser)
            {
                switch(res)
                {
                    case 1: userRegister();break;
                    case 2: userLogin();break;
                    case 3: companyRegister();break;
                    case 4: companyLogin();break;
                }
            }
            else if (loggedInUser)
            {
                switch (res)
                {
                    case 1: userLogout();break;
                    case 2: userEdit();break;
                    case 3: showCompanies();break;
                    case 4: showArticles();break;
                    case 5: buyArticles();break;
                    case 6: reserveArticles();break;
                    case 7: showReservedArticles();break;
                }
            }
            else if (loggedInCompany)
            {
                switch (res)
                {
                    case 1: companyLogout();break;
                    case 2: companyEdit();break;
                    case 3: editArticles();break;
                }
            }
        }
        
    }

    private static void userLogin()
    {
        System.out.print("Unesite korisnicko ime: ");
        String username = reader.next();
        if (!usernameUserExists(username)) 
        {
            System.out.println("Korisnik ne postoji! Registracija: ");
            userRegister();
            return;
        }
        
        System.out.print("Unesite sifru: ");
        String password = reader.next();
        if (!userLogin(username, password))
        {
            System.out.println("Pogresna sifra!");
            return;
        }
    }
    
    private static boolean userLogin(String username, String password)
    {
        startTransaction();
        Query query = em.createNamedQuery("User.findByNamePassword", User.class)
                .setParameter("name", username).setParameter("password", password);
        List<User> results = query.getResultList();
        boolean res = !results.isEmpty();
        closeTransaction();
        if (res)
        {
            usernameLogged = username;
            loggedInUser = true;
        }
        return res;
    }
    
    private static void userRegister() {
        while(true)
        {
            System.out.print("Unesite zeljeno korisnicko ime (prazno za izlaz): ");
            String username = reader.next();
            if (username.equals("")) return;

            if (usernameUserExists(username)) 
            {
                System.out.println("Username exists!");
                continue;
            }

            System.out.print("Unesite zeljenu lozinku (prazno za izlaz): ");
            String password = reader.next();
            if (password.equals("")) return;

            System.out.print("Unesite zeljeni email (prazno za izlaz): ");
            String email = reader.next();
            if (email.equals("")) return;
            
            startTransaction();
            User thisUser = new User();
            thisUser.setName(username);
            thisUser.setEmail(email);
            thisUser.setPassword(password);
            em.persist(thisUser);
            closeTransaction();
            
            return;
        }
    }
    
    private static void companyLogin()
    {
        System.out.print("Unesite ime kompanije: ");
        String username = reader.next();
        if (!usernameCompanyExists(username)) 
        {
            System.out.println("Kompanija ne postoji!Registracija:");
            companyRegister();
            return;
        }
        
        System.out.print("Unesite sifru: ");
        String password = reader.next();
        if (!companyLogin(username, password))
        {
            System.out.println("Pogresna sifra!");
            return;
        }
    }
    
    private static boolean companyLogin(String username, String password)
    {
        startTransaction();
        Query query = em.createNamedQuery("Company.findByNamePassword", Company.class)
                .setParameter("name", username).setParameter("password", password);
        List<Company> results = query.getResultList();
        boolean res = !results.isEmpty();
        closeTransaction();
        if (res)
        {
            usernameLogged = username;
            loggedInCompany = true;
        }
        return res;
    }
    
    private static void companyRegister() {
        while(true)
        {
            System.out.print("Unesite zeljeno ime kompanije (q za izlaz): ");
            String username = reader.next();
            if (username.equals("q")) return;

            if (usernameCompanyExists(username)) 
            {
                System.out.println("Username exists!");
                continue;
            }

            System.out.print("Unesite zeljenu lozinku (q za izlaz): ");
            String password = reader.next();
            if (password.equals("q")) return;

            System.out.print("Unesite zeljeni email (q za izlaz): ");
            String email = reader.next();
            if (email.equals("q")) return;
           
            
            startTransaction();
            Company thisCompany = new Company();
            thisCompany.setName(username);
            thisCompany.setEmail(email);
            thisCompany.setPassword(password);
            em.persist(thisCompany);
            closeTransaction();
            
            return;
        }
    }

    private static boolean usernameUserExists(String username) {
        startTransaction();
        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", username);
        List<User> results = query.getResultList();
        boolean res = !results.isEmpty();
        closeTransaction();
        return res;
    }
    
    private static boolean usernameCompanyExists(String username) {
        startTransaction();
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", username);
        List<Company> results = query.getResultList();
        boolean res = !results.isEmpty();
        closeTransaction();
        return res;
    }
    
    private static void userLogout() {
        usernameLogged = "";
        loggedInUser = false;
    }

    private static void userEdit() {
        System.out.println(
            "(1) Promena korisnickog imena\n" +
            "(2) Promena sifre\n" +
            "(3) Promena emaila\n" +
            "(4) Promena adrese\n" +
            "(5) Promena telefona\n" +
            "(6) Promena grada\n" +
            "(7) Promena zemlje\n" +
            "(8) Promena punog imena\n" +
            "(9) Promena pola\n" +
            "(10) Prikazi korisnika\n" +
            "(0) Izlaz\n"
        );
        
        switch( reader.nextInt() )
        {
            case 1: changeUserUsername(); break;
            case 2: changeUserPassword(); break;
            case 3: changeUserEmail(); break;
            case 4: changeUserAddress(); break;
            case 5: changeUserTelephone(); break;
            case 6: changeUserCity(); break;
            case 7: changeUserCountry(); break;
            case 8: changeUserFullName(); break;
            case 9: changeUserSex(); break;
            case 10: showUserInfo(); break;
        }
    }

    private static void changeUserUsername() {
        while(true)
        {
            System.out.print("Unesite novo korisnicko ime (q za izlaz): ");
            String username = reader.next();
            if (username.equals("q")) return;

            if (usernameUserExists(username)) 
            {
                System.out.println("Username exists!");
                continue;
            }

            startTransaction();
            
            Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
            List<User> results = query.getResultList();
            User thisUser = results.get(0);
            
            thisUser.setName(username);
            em.persist(thisUser);
            
            usernameLogged = username;
            
            closeTransaction();

            break;
        }      
    }
    
    private static void changeUserPassword()
    {
        System.out.print("Unesite novu sifru (q za izlaz): ");
        String password = reader.next();
        if (password.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
        List<User> results = query.getResultList();
        User thisUser = results.get(0);

        thisUser.setPassword(password);
        em.persist(thisUser);

        closeTransaction();
    }
    private static void changeUserEmail()
    {
        System.out.print("Unesite novi email (q za izlaz): ");
        String email = reader.next();
        if (email.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
        List<User> results = query.getResultList();
        User thisUser = results.get(0);

        thisUser.setEmail(email);
        em.persist(thisUser);

        closeTransaction();
    }
    
    
    private static void showCompanies() {
        startTransaction();
        
        Query query = em.createNamedQuery("Company.findAll", Company.class);
        List<Company> results = query.getResultList();
        
        closeTransaction();
        
        for (Company result : results) {
            System.out.println("Name: " + result.getName() + " - Email: " + result.getEmail());
        }
    }

    private static void showArticles() {
        System.out.println("Unesite ime kompanije (q za izlaz):");
        String companyName = reader.next();
        if (companyName.equals("q")) return;
        if (!usernameCompanyExists(companyName))
        {
            System.out.println("Ne postoji kompanija!");
            return;
        }
        
        startTransaction();
        
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", companyName);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);
        
        for( Articles article : thisCompany.getArticlesCollection())
        {
            System.out.println("Name: " + article.getName() + " - Price: " + article.getPrice() + " - Quantity: " + article.getNumber());
        }
        
        closeTransaction();
    
    }

    private static void buyArticles() {
        System.out.println("Unesite ime kompanije (q za izlaz):");
        String companyName = reader.next();
        if (companyName.equals("q")) return;
        if (!usernameCompanyExists(companyName))
        {
            System.out.println("Ne postoji kompanija!");
            return;
        }
        
        System.out.println("Unesite ime artikla (q za izlaz):");
        String articleName = reader.next();
        if (articleName.equals("q")) return;
        if (!articleExists(companyName, articleName))
        {
            System.out.println("Ne postoji artikal!");
            return;
        }
        
        System.out.println("Unesite kolicinu (0 za izlaz):");
        int number = reader.nextInt();
        if (number == 0) return;
        if (!articleNumberExists(companyName, articleName, number))
        {
            System.out.println("Ne postoji dovoljna kolicina artikla!");
            return;
        }
        
        startTransaction();
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", companyName);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);
        
        int idArt = -1;
        
        for (Articles article : thisCompany.getArticlesCollection() )
        {
            if (article.getName().equals(articleName))
            {
                idArt = article.getId();
            }
        }
        
        List<User> users = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged).getResultList();
        int idUser = users.get(0).getId();
        
        query = em.createNamedQuery("Reserved.findByIdartIduser", Reserved.class).setParameter("idart", idArt).setParameter("iduser",idUser );
        List<Reserved> resultsReserved = query.getResultList();
        Reserved resultRes = null;
        if (!resultsReserved.isEmpty())
            resultRes = resultsReserved.get(0);
        
        if (resultsReserved.isEmpty())
        {
            for (Articles article : thisCompany.getArticlesCollection() )
            {
                if (article.getName().equals(articleName))
                {
                    article.setNumber(article.getNumber() - number);
                    em.persist(article);
                }
            }
        }
        else if (resultRes.getNumber() <= number)
        {
            number -= resultRes.getNumber();
            em.remove(resultRes);
            for (Articles article : thisCompany.getArticlesCollection() )
            {
                if (article.getName().equals(articleName))
                {
                    article.setNumber(article.getNumber() - number);
                    em.persist(article);
                }
            }
        }
        else
        {
            resultRes.setNumber(resultRes.getNumber() - number);
            em.persist(resultRes);
        }
        
        closeTransaction();
    }

    private static boolean articleExists(String companyName, String articleName) {
        startTransaction();
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", companyName);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);
        
        closeTransaction();
        
        boolean res = false;
        for (Articles article : thisCompany.getArticlesCollection() )
        {
            if (article.getName().equals(articleName))
                res = true;
        }
        
        return res;
    }
    
    private static boolean articleNumberExists(String companyName, String articleName, int number)
    {
        startTransaction();
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", companyName);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);
        
        int idArt = -1;
        
        for (Articles article : thisCompany.getArticlesCollection() )
        {
            if (article.getName().equals(articleName))
            {
                number -= article.getNumber();
                idArt = article.getId();
            }
        }
        
        List<User> users = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged).getResultList();
        int idUser = users.get(0).getId();
        
        query = em.createNamedQuery("Reserved.findByIdartIduser", Reserved.class).setParameter("idart", idArt).setParameter("iduser",idUser );
        List<Reserved> resultReserved = query.getResultList();
        if (resultReserved.isEmpty())
            return number <= 0;
        
        Reserved resultRes = resultReserved.get(0);
        
        number -= resultRes.getNumber();
        
        closeTransaction();
        
        return number <= 0;
    }
    
    private static void reserveArticles() {
        System.out.println("Unesite ime kompanije (q za izlaz):");
        String companyName = reader.next();
        if (companyName.equals("q")) return;
        if (!usernameCompanyExists(companyName))
        {
            System.out.println("Ne postoji kompanija!");
            return;
        }
        
        System.out.println("Unesite ime artikla (q za izlaz):");
        String articleName = reader.next();
        if (articleName.equals("q")) return;
        if (!articleExists(companyName, articleName))
        {
            System.out.println("Ne postoji artikal!");
            return;
        }
        
        System.out.println("Unesite kolicinu (0 za izlaz):");
        int number = reader.nextInt();
        if (number == 0) return;
        if (!articleNumberExists(companyName, articleName, number))
        {
            System.out.println("Ne postoji dovoljna kolicina artikla!");
            return;
        }
        
        startTransaction();
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", companyName);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);
        
        int idArt = -1;
        
        for (Articles article : thisCompany.getArticlesCollection() )
        {
            if (article.getName().equals(articleName))
            {
                idArt = article.getId();
            }
        }
        
        List<User> users = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged).getResultList();
        int idUser = users.get(0).getId();
        
        query = em.createNamedQuery("Reserved.findByIdartIduser", Reserved.class).setParameter("idart", idArt).setParameter("iduser",idUser );
        List<Reserved> resultReserved = query.getResultList();
        
        if (!resultReserved.isEmpty())
        {
            Reserved resultRes = resultReserved.get(0);
            
            int numBefore= resultRes.getNumber();
            
            resultRes.setNumber(number);
            
            em.persist(resultRes);
            
            for (Articles article : thisCompany.getArticlesCollection() )
            {
                if (article.getName().equals(articleName))
                {
                    article.setNumber(article.getNumber() - number + numBefore);
                    em.persist(article);
                }
            }
        }
        
        else
        {
            ReservedPK pk = new ReservedPK();
            pk.setIdart(idArt);
            pk.setIduser(idUser);
            Reserved reserved = new Reserved();
            reserved.setReservedPK(pk);
            reserved.setNumber(number);
            Date today = new Date();
            Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
            reserved.setVaziDo(today);
            em.persist(reserved);
            
            for (Articles article : thisCompany.getArticlesCollection() )
            {
                if (article.getName().equals(articleName))
                {
                    article.setNumber(article.getNumber() - number);
                    em.persist(article);
                }
            }
        }
        
        closeTransaction();
    }

    private static void showReservedArticles() {
        startTransaction();        
        List<User> users = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged).getResultList();
        int idUser = users.get(0).getId();
        
        Query query = em.createNamedQuery("Reserved.findByIduser", Reserved.class).setParameter("iduser",idUser );
        List<Reserved> resultReserved = query.getResultList();
        
        for ( Reserved res : resultReserved)
        {
            System.out.println(
                    "Company: " + res.getArticles().getIdcompany().getName() + 
                    " - Article: " + res.getArticles().getName() + 
                    " - Quantity: " + res.getNumber() + 
                    " - Date: " + res.getVaziDo()
            );
        }
        
        closeTransaction();
        
    }

    private static void companyLogout() {
        usernameLogged = "";
        loggedInCompany = false;
    }

    private static void companyEdit() {
        System.out.println(
            "(1) Promena imena kompanije\n" +
            "(2) Promena sifre\n" +
            "(3) Promena emaila\n" +
            "(4) Promena adrese\n" +
            "(5) Promena telefona\n" +
            "(6) Promena grada\n" +
            "(7) Promena drzave\n" +
            "(8) Promena punog naziva\n" +
            "(9) Promena PIB\n" +
            "(10) Promena JB\n" +
            "(11) Promena BR LK\n" +
            "(12) Prikazi kompaniju\n" +
            "(0) Izlaz\n"
        );
        
        switch( reader.nextInt() )
        {
            case 1: changeCompanyUsername(); break;
            case 2: changeCompanyPassword(); break;
            case 3: changeCompanyEmail(); break;
            case 4: changeCompanyAddress(); break;
            case 5: changeCompanyTelephone(); break;
            case 6: changeCompanyCity(); break;
            case 7: changeCompanyCountry(); break;
            case 8: changeCompanyFullcompanyname(); break;
            case 9: changeCompanyPIB(); break;
            case 10: changeCompanyJB(); break;
            case 11: changeCompanyBRLK(); break;
            case 12: showCompanyInfo(); break;
        }
    }

    private static void changeCompanyUsername() {
        while(true)
        {
            System.out.print("Unesite novo ime (q za izlaz): ");
            String username = reader.next();
            if (username.equals("q")) return;

            if (usernameCompanyExists(username)) 
            {
                System.out.println("Username exists!");
                continue;
            }

            startTransaction();
            
            Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
            List<Company> results = query.getResultList();
            Company thisCompany = results.get(0);
            
            thisCompany.setName(username);
            em.persist(thisCompany);
            
            usernameLogged = username;
            
            closeTransaction();

            break;
        }      
    }
    
    private static void changeCompanyPassword()
    {
        System.out.print("Unesite novu sifru (q za izlaz): ");
        String password = reader.next();
        if (password.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setPassword(password);
        em.persist(thisCompany);

        closeTransaction();
    }
    private static void changeCompanyEmail()
    {
        System.out.print("Unesite novi email (q za izlaz): ");
        String email = reader.next();
        if (email.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setEmail(email);
        em.persist(thisCompany);

        closeTransaction();
    }

    private static void editArticles() {
        System.out.println(
            "(1) Dodavanje artikla\n" +
            "(2) Prikazivanje artikala\n" +
            "(3) Brisanje artikla\n" +
            "(4) Promeni cenu artikla\n" +
            "(0) Izlaz\n"
        );
        
        switch( reader.nextInt() )
        {
            case 1: addArticle(); break;
            case 2: showArticlesForCompany(); break;
            case 3: deleteArticle(); break;
            case 4: changePriceArticle(); break;
        }
    }

    private static void addArticle() {
        System.out.println("Unesite ime artikla (q za izlaz):");
        String articleName = reader.next();
        if (articleName.equals("q")) return;
        
        System.out.println("Unesite kolicinu (0 za izlaz):");
        int number = reader.nextInt();
        if (number == 0) return;
        
        startTransaction();
        
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);
        
        boolean alreadyExists = false;
        for( Articles article : thisCompany.getArticlesCollection())
        {
            if (article.getName().equals(articleName))
            {
                alreadyExists = true;
                article.setNumber(article.getNumber() + number);
                //em.persist(article);
            }
        }
        
        if (!alreadyExists)
        {
            System.out.println("Unesite cenu ( 0 za izlaz):");
            float price = reader.nextFloat();
            if (price == 0) return;
            
            Articles art = new Articles();
            art.setPrice(price);
            art.setNumber(number);
            art.setName(articleName);
            art.setIdcompany(thisCompany);
            em.persist(art);
        }
        
        closeTransaction();
    }
    
    private static void showArticlesForCompany() {
        startTransaction();
        
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);
        
        for( Articles article : thisCompany.getArticlesCollection())
        {
            System.out.println("Name: " + article.getName() + " - Price: " + article.getPrice() + " - Quantity: " + article.getNumber());
        }
        
        closeTransaction();
    }

    private static void deleteArticle() {
        System.out.println("Unesite ime artikla (q za izlaz):");
        String articleName = reader.next();
        if (articleName.equals("q")) return;
        
        startTransaction();
        
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);
        
        boolean exists = false;
        for( Articles article : thisCompany.getArticlesCollection())
        {
            if (article.getName().equals(articleName))
            {
                exists = true;
                em.remove(article);
            }
        }
        
        if (!exists)
        {
            System.out.println("Not exists!");
        }
        
        closeTransaction();
    }

    private static void changePriceArticle() {
        System.out.println("Unesite ime artikla (q za izlaz):");
        String articleName = reader.next();
        if (articleName.equals("q")) return;
        
        System.out.println("Unesite novu cenu (0 za izlaz):");
        float price = reader.nextFloat();
        if (price == 0) return;
        
        startTransaction();
        
        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);
        
        boolean exists = false;
        for( Articles article : thisCompany.getArticlesCollection())
        {
            if (article.getName().equals(articleName))
            {
                exists = true;
                article.setPrice(price);
                em.persist(article);
            }
        }
        
        if (!exists)
        {
            System.out.println("Not exists!");
        }
        
        closeTransaction();
    }

    private static void changeUserAddress() {
        System.out.print("Unesite novu adresu (q za izlaz): ");
        String address = reader.next();
        if (address.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
        List<User> results = query.getResultList();
        User thisUser = results.get(0);

        thisUser.setAddress(address);
        em.persist(thisUser);

        closeTransaction();
    }

    private static void changeUserTelephone() {
        System.out.print("Unesite novi telefon (q za izlaz): ");
        String telephone = reader.next();
        if (telephone.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
        List<User> results = query.getResultList();
        User thisUser = results.get(0);

        thisUser.setTelephone(telephone);
        em.persist(thisUser);

        closeTransaction();
    }

    private static void changeUserCity() {
        System.out.print("Unesite novi grad (q za izlaz): ");
        String city = reader.next();
        if (city.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
        List<User> results = query.getResultList();
        User thisUser = results.get(0);

        thisUser.setCity(city);
        em.persist(thisUser);

        closeTransaction();
    }

    private static void changeUserCountry() {
        System.out.print("Unesite novu zemlju (q za izlaz): ");
        String country = reader.next();
        if (country.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
        List<User> results = query.getResultList();
        User thisUser = results.get(0);

        thisUser.setCounty(country);
        em.persist(thisUser);

        closeTransaction();
    }

    private static void changeUserFullName() {
        System.out.print("Unesite novo puno ime (q za izlaz): ");
        String fullname = reader.next();
        if (fullname.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
        List<User> results = query.getResultList();
        User thisUser = results.get(0);

        thisUser.setFullname(fullname);
        em.persist(thisUser);

        closeTransaction();
    }

    private static void changeUserSex() {
        System.out.print("Unesite novi pol (q za izlaz): ");
        String sex = reader.next();
        if (sex.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
        List<User> results = query.getResultList();
        User thisUser = results.get(0);

        thisUser.setSex(sex);
        em.persist(thisUser);

        closeTransaction();
    }

    private static void showUserInfo() {
        startTransaction();

        Query query = em.createNamedQuery("User.findByName", User.class).setParameter("name", usernameLogged);
        List<User> results = query.getResultList();
        User thisUser = results.get(0);

        System.out.println("id: " + thisUser.getId());
        System.out.println("name: " + thisUser.getName());
        System.out.println("email: " + thisUser.getEmail());
        System.out.println("address: " + thisUser.getAddress());
        System.out.println("telephone: " + thisUser.getTelephone());
        System.out.println("sex: " + thisUser.getSex());
        System.out.println("full name: " + thisUser.getFullname());
        System.out.println("city: " + thisUser.getCity());
        System.out.println("country: " + thisUser.getCounty());

        closeTransaction();
    }

    private static void changeCompanyAddress() {
        System.out.print("Unesite novu adresu (q za izlaz): ");
        String address = reader.next();
        if (address.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setAddress(address);
        em.persist(thisCompany);

        closeTransaction();
    }

    private static void changeCompanyCity() {
        System.out.print("Unesite novi grad (q za izlaz): ");
        String address = reader.next();
        if (address.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setCity(address);
        em.persist(thisCompany);

        closeTransaction();
    }

    private static void changeCompanyTelephone() {
        System.out.print("Unesite novi telefon (q za izlaz): ");
        String address = reader.next();
        if (address.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setTelephone(address);
        em.persist(thisCompany);

        closeTransaction();
    }

    private static void changeCompanyPIB() {
        System.out.print("Unesite novi PIB (q za izlaz): ");
        String address = reader.next();
        if (address.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setPib(address);
        em.persist(thisCompany);

        closeTransaction();
    }

    private static void changeCompanyCountry() {
        System.out.print("Unesite novu zemlju (q za izlaz): ");
        String address = reader.next();
        if (address.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setCountry(address);
        em.persist(thisCompany);

        closeTransaction();
    }

    private static void changeCompanyFullcompanyname() {
        System.out.print("Unesite novo puno ime kompanije (q za izlaz): ");
        String address = reader.next();
        if (address.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setFullcompanyname(address);
        em.persist(thisCompany);

        closeTransaction();
    }

    private static void changeCompanyJB() {
        System.out.print("Unesite novi jedinstveni broj (q za izlaz): ");
        String address = reader.next();
        if (address.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setJbk(address);
        em.persist(thisCompany);

        closeTransaction();
    }

    private static void changeCompanyBRLK() {
        System.out.print("Unesite novi broj LK (q za izlaz): ");
        String address = reader.next();
        if (address.equals("q")) return;

        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", Company.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisCompany = results.get(0);

        thisCompany.setBrlk(address);
        em.persist(thisCompany);

        closeTransaction();
    }

    private static void showCompanyInfo() {
        startTransaction();

        Query query = em.createNamedQuery("Company.findByName", User.class).setParameter("name", usernameLogged);
        List<Company> results = query.getResultList();
        Company thisUser = results.get(0);

        System.out.println("id: " + thisUser.getId());
        System.out.println("name: " + thisUser.getName());
        System.out.println("email: " + thisUser.getEmail());
        System.out.println("address: " + thisUser.getAddress());
        System.out.println("telephone: " + thisUser.getTelephone());
        System.out.println("full name: " + thisUser.getFullcompanyname());
        System.out.println("city: " + thisUser.getCity());
        System.out.println("country: " + thisUser.getCountry());
        System.out.println("PIB: " + thisUser.getPib());
        System.out.println("JB: " + thisUser.getJbk());
        System.out.println("br lk: " + thisUser.getBrlk());

        closeTransaction();
    }

    
}
