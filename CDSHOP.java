import java.util.Scanner;
import java.sql.*;
public class CDSHOP {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/CDSHOP?useSSL=false";
    static final String USER = "root";
    static final String PASS = "monjoync";

    static Boolean isAvailable = true;
    static Boolean isAdmin = false ; 
    public static void main(String[] arg){
        Connection c = null;
        Statement st = null; 
        try {
            Class.forName(JDBC_DRIVER);
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            st = c.createStatement();
            Scanner sc = new Scanner(System.in);

            

            System.out.println("Welcome to Rent a CD store");

            main_menu(st, sc);
            sc.close();
            st.close();
            c.close();
            
        } catch (SQLException e) {
           
        }
        catch(Exception e){

        }
        finally{
            try {
                if (st != null)
                   st.close();
             } catch (SQLException se2) {
             }
             try {
                if (c != null)
                   c.close();
             } catch (SQLException se) {
                
             }
        }

    }
    static void main_menu(Statement st,Scanner sc){
        //Switch Case Menu 
        System.out.println("How do you want to sign in as ? ");
        System.out.println("1. Customer");
        System.out.println("2. Issuer");
        System.out.println("3. Admin");
        System.out.println("9. Exit");
        System.out.print("Please enter your CHOICE ");
        Integer choice = Integer.parseInt(sc.nextLine()); 
        
        flush();

        switch (choice) {
            case 9:
                System.out.println("Thank you using the database. Exiting now ...");
                System.exit(0);
                break;
            case 1:
                isAdmin = false; 
                customer_menu(st, sc);
                
                break;
            case 2:
                isAdmin = false; 
                issuer_auth(st,sc);


                break;
            case 3:
                isAdmin = true;
                admin_auth(st,sc);;

                break; 
            default:
                isAdmin = false ; 
                flush();
                System.out.println("What you entered was not valid. Enter a valid CHOICE");

                break;
        }

    }
    private static void issuer_auth(Statement st, Scanner sc) {
        if(!auth(st, sc)){
            System.out.println("Wrong Credentials. Do you want to try again ? (Y/N)");
            String prompt = sc.nextLine(); 
            if(prompt.equals("Y")) issuer_auth(st, sc);
            else return ; 


        }
        else{
            issuer_menu(st, sc);
        }
    }
    private static void admin_auth(Statement st, Scanner sc) {
        if(!auth(st, sc)){
            System.out.println("Wrong Credentials. Do you want to try again ? (Y/N)");
            String prompt = sc.nextLine(); 
            if(prompt.equals("Y")) admin_auth(st, sc);
            else return ; 


        }
        else{
            admin_menu(st, sc);
        }
    }
    //Authentication for admin and issuer / storekeeper 
    private static Boolean auth(Statement st,Scanner sc){
        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("\nEnter Password: ");
        String password = sc.nextLine(); 

        flush();
        Boolean success = false ;
        System.out.println(username+" "+password);
        if(isAdmin){
            String sql = "SELECT * from super_admin" ; 
            ResultSet r = Execute(st, sql);

            try {
                while(r.next()){
                    String id = r.getString("admin_name");
                    String pass = r.getString("admin_password");
                    if(id.equals(username) && pass.equals(password)){
                        System.out.println("Authentication Succesful");
                        success = true;
                        break; 
                    }

                }
                r.close();
                
            } catch (SQLException e) {
             
            }

        }
        else{
            String sql = "SELECT * from issuer" ; 
            ResultSet r = Execute(st, sql);

            try {
                while(r.next()){
                    String id = r.getString("issuer_name");
                    String pass = r.getString("issuer_password");
                    if(id.equals(username) && pass.equals(password)){
                        success = true;
                        break; 
                    }

                }
                r.close();
                
            } catch (SQLException e) {
             
            }

        }
        
        
        return success;
        



   }
    private static void flush() {
        System.out.print("\033[H\033[2J"); 
        System.out.flush(); 
    }
    //3 Types of User : Customer who will issue , Issuer : for inverntory , Admin : superuser / admin 
    static void customer_menu(Statement st,Scanner sc){
        System.out.println("Please select one of the following OPTIONS: ");
        System.out.println("1. Available CDs");
        System.out.println("9. Exit");

        System.out.println("Please enter your CHOICE ");
        Integer i = Integer.parseInt(sc.nextLine()); 
        switch (i) {
            case 1:
                isAvailable = false ;
                equipment_list(st, sc);
                
                break;
            case 9:
                return; 
            default:
                flush();
                System.out.println("Please enter a valid CHOICE ");
                break;
        }
        customer_menu(st, sc);
    }
    static void issuer_menu(Statement st,Scanner sc){
        System.out.println("Please select one of the following options ");
        System.out.println("1. List of ALL CDs");
        System.out.println("2. List of AVAILABLE CDs");
        System.out.println("3. ISSUE CD for a Customer");
        System.out.println("4. RETURN a CD");
        System.out.println("5. Add CD");
        System.out.println("6. Delete CD record");
        System.out.println("9. Exit");

        System.out.println("Please enter your CHOICE: ");

        Integer i = Integer.parseInt(sc.nextLine()); 

        switch (i) {
            case 1:
                isAvailable=false;
                equipment_list(st, sc);

                
                break;
            case 2:
                isAvailable = true ;
                equipment_list(st, sc);

                
                break;
            case 3:
                issue_equipment(st,sc);

                break;
            case 4:
                return_equipment(st,sc); 
                break;
            case 5:
                add_equipment(st,sc);


                break;
            case 6:
                delete_equipment(st,sc);

                break;

            case 9:
                return; 
            default:
                flush();
                System.out.println("Please enter a valid CHOICE ");
                break;
        }
        issuer_menu(st, sc);

    }

    static void admin_menu(Statement st,Scanner sc){
        System.out.println("Please select one of the following options ");
        System.out.println("1. List of ALL customer");
        System.out.println("2. List of ALL ISSUERS");
        System.out.println("3. ADD a CUSTOMER");
        System.out.println("4. DE-ENROLL a CUSTOMER");
        System.out.println("5. ADD an ISSUER");
        System.out.println("6. DE-ENROLL an ISSUER");
        System.out.println("9. Exit");
        System.out.println("Please enter your choice");
        
        Integer i = Integer.parseInt(sc.nextLine()); 
        switch (i) {
            case 1:
                customer_list(st, sc);
                admin_menu(st, sc);
                break;
            case 2:
                issuer_list(st, sc);
                admin_menu(st, sc);
                break;
            case 3:
                add_customer(st,sc); 
                admin_menu(st, sc);
                break;
            case 4:
                delete_customer(st,sc);
                admin_menu(st, sc);
                break; 
            case 5:
                add_issuer(st,sc);
                admin_menu(st, sc);
                break;
            case 6: 
                delete_issuer(st,sc); 
                admin_menu(st, sc);
                break; 
            
                
            default:
                flush();
                
                break;
        }


    }  
    
    static void add_equipment(Statement st, Scanner sc) {
        try {
            System.out.println("Enter CD name: ");
            String name = sc.nextLine(); 
            System.out.println("Enter Artist/Brand");
            String brand = sc.nextLine();
            System.out.println("Enter Year of Release");

            Integer year = Integer.parseInt(sc.nextLine());

            String sql = String.format("INSERT INTO cd(cd_name,artist,release_year) VALUES('%s','%s','%d')",name,brand,year);
            int res = Update(st, sql);
            if(res!=0) System.out.println("CD added successfully");
            else System.out.println("Something went wrong. Didn't add CD");

            
        } catch (Exception e) {
        
        }
    
    }
   
    
    static void delete_equipment(Statement st, Scanner sc) {
        try {
            System.out.println("Enter ID to remove");
            Integer id = Integer.parseInt(sc.nextLine());  
            flush();
            //roll_num
            String sql =String.format("DELETE FROM cd WHERE id = '%d'",id); 
            int res = Update(st, sql);

            if(res!=0) System.out.println("CD has been deleted successfully");
            else System.out.println("Something went wrong. Didn't delete CD");
            
        } catch (Exception e) {
            
        }

    }
    static void issue_equipment(Statement st, Scanner sc) {
        isAvailable =true; 
        boolean not_available =  equipment_list(st, sc);
        if(!not_available){
            System.out.println("Enter CD ID: ");
            Integer cdid = Integer.parseInt(sc.nextLine()); 
            System.out.println("Enter Customer ID");
            Integer custid = Integer.parseInt(sc.nextLine()); 
            
            flush();
            
            String sql = String.format("UPDATE cd SET issuedby='%d' WHERE id = '%d' ", custid,cdid);
            int ret = Update(st, sql);
            String sql2 = String.format("UPDATE customer SET cd_id='%d' WHERE cust_id = '%d' ", cdid,custid);
            int ret2 = Update(st, sql2);
            if(ret==0 || ret2==0) System.out.println("Something went wrong. Didn't issue CD");
            else System.out.println("CD issued successfully");

        }


    }
    static void return_equipment(Statement st, Scanner sc) {
        try {
            System.out.println("Enter CD id to return");
            Integer cdid = Integer.parseInt(sc.nextLine());
            flush();

            String sql = String.format("UPDATE cd SET issuedby=NULL WHERE id='%d' ",cdid); 
            int ret = Update(st, sql);
            if(ret==0) System.out.println("Something went wrong. Return failed");
            else System.out.println("CD returned to store");
        } catch (Exception e) {
            
        }


    }
   
    private static void add_customer(Statement st, Scanner sc) {
        try {
            System.out.println("Enter the name of customer");
            String name = sc.nextLine(); 
            flush();
            //customer database
            String sql = String.format("INSERT INTO customer(cust_name) VALUES('%s')",name);
            int ret = Update(st,sql);
            if(ret==0) System.out.println("Something went wrong. Didn't add customer");
            else System.out.println("Customer added successfully");
            
        } catch (Exception e) {
            
        }
    }
    private static void delete_customer(Statement st, Scanner sc) {
        try {
            System.out.println("Enter ID to remove");
            Integer id = Integer.parseInt(sc.nextLine());  
            flush();
            //roll_num
            String sql =String.format("DELETE FROM customer WHERE cust_id = '%d'",id); 
            int res = Update(st, sql);

            if(res!=0) System.out.println("Customer has been deleted successfully");
            else System.out.println("Something went wrong. Didn't delete customer");
            
        } catch (Exception e) {
            
        }
    }
    private static void add_issuer(Statement st, Scanner sc) {
        try {
            
            System.out.println("Enter the name of issuer");
            String name = sc.nextLine();
            System.out.println("Enter password for the issuer");
            String password = sc.nextLine();  
            flush();
            //issuer database
            String sql = String.format("INSERT INTO issuer(issuer_name,issuer_password) VALUES('%s','%s')", name,password);
            int ret = Update(st,sql);
            if(ret==0) System.out.println("Something went wrong. Didn't add issuer");
            else System.out.println("Issuer added successfully");
            
        } catch (Exception e) {
            
        }

    }
    private static void delete_issuer(Statement st, Scanner sc) {
        try {
            System.out.println("Enter the issuer id to remove");
            Integer id = Integer.parseInt(sc.nextLine());  


            flush();
            //roll_num
            String sql =String.format("DELETE FROM issuer WHERE issuer_id= '%d'",id); 
            int res = Update(st, sql);

            if(res!=0) System.out.println("issuer has been deleted successfully");
            else System.out.println("Something went wrong. Didn't delete issuer");
            
        } catch (Exception e) {
            
        }
    }
    
    private static int Update(Statement st, String sql) {
        try {
            return st.executeUpdate(sql); 
            
        } catch (SQLException e) {
        
        }
        return 0;
    }
    //Tables 
    static void issuer_list(Statement st, Scanner sc){
        String sql = "SELECT * from issuer";
        ResultSet r = Execute(st,sql);
        try {
            System.out.println("List of issuers : ");
            while (r.next()) {
                Integer id = r.getInt("issuer_id");
                String name = r.getString("issuer_name");
                System.out.println("Name : "+name+" Issuer ID : " + Integer.toString(id));

                
            }
            r.close();
            
        } catch (Exception e) {
            
        }

    }

    static void customer_list(Statement st, Scanner sc){
        String sql = "SELECT * from customer";
        ResultSet r = Execute(st,sql);
        try {
            System.out.println("List of customers : ");
            while (r.next()) {
                Integer id = r.getInt("cust_id");
                String name = r.getString("cust_name");
                System.out.println("Name : "+name+" Customer ID : " + Integer.toString(id));

                
            }
            r.close();
            
        } catch (Exception e) {
            
        }


    }
   
    static boolean equipment_list(Statement st,Scanner sc){
        String sql = "SELECT * from cd ORDER BY id ";
        ResultSet r = Execute(st,sql);
        boolean flag = true;
        try {
            
            while (r.next()) {
                Integer id = r.getInt("id");
                String name = r.getString("cd_name");
                String artist = r.getString("artist");
                Integer yr = r.getInt("release_year");
                String issuer = r.getString("issuedby");

                if(isAvailable){
                    if(issuer==null){
                        System.out.println("ID: "+Integer.toString(id));
                        System.out.println("CD name: "+name);
                        System.out.println("Artist/Author: "+artist);
                        System.out.println("Year of release: "+Integer.toString(yr));
                        flag = false ;

                    }
                }
                else{
                    System.out.println("ID: "+Integer.toString(id));
                    System.out.println("CD name: "+name);
                    System.out.println("Artist/Author: "+artist);
                    System.out.println("Year of release: "+Integer.toString(yr));
                    if(issuer==null) System.out.println("Not yet issued");
                    else System.out.println("Issued by Customer ID: "+issuer);
                    //System.out.println("Issuer : "+issuer);
                    flag = false ;
                }
            }
            r.close();
            
        } catch (Exception e) {
            
        }
        if(flag) System.out.println("No CDs are here yet: ");
        return flag;

    }
    private static ResultSet Execute(Statement st,String sql) {
        try {
            return st.executeQuery(sql);
            
        } catch (SQLException e) {
            
        };
        return null;
    }

    
}
