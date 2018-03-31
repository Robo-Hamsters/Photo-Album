package Repo;

import Model.User;
import org.hibernate.query.Query;

import java.util.List;

public class UserRepo {

        public User findUserByUsernameAndPassword(User user,DBConnector con)
        {

            Query query= con.getSession().createQuery("SELECT email,password from User where :frmemail=email and :frmpwd=password");
            query.setParameter("frmemail",user.getEmail());
            query.setParameter("frmpwd",user.getPassword());
            List<Object[]> list=query.list();

            User returnUser=null;

            if(list.size() != 0)
            {
                Object[] columns = list.get(0);
                returnUser = new User();
                returnUser.setEmail((String)columns[0]);
                returnUser.setPassword((String)columns[1]);
            }
            con.databaseDisconnect();
            return returnUser;
        }
    public void dbInsertUser(User user,DBConnector con)
    {
        con.setSession(con.getFactory().getCurrentSession()) ;
        con.getSession().beginTransaction();
        con.getSession().save(user);
        con.getSession().getTransaction().commit();
    }


}
