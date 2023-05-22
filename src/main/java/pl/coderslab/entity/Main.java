package pl.coderslab.entity;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        User user = new User();
        UserDao userDao = new UserDao();

//        user.setUserName("Karolina ");
//        user.setEmail("kpietrzak55@gmail.com");
//        user.setPassword("pass");
//        userDao.createUser(user);

        User user1 = userDao.read(8);
        System.out.println(user1);


        UserDao UserDao = new UserDao();
        User existingUser = userDao.read(1);
        existingUser.setUserName("newName");
        existingUser.setEmail("karolina@onet.pl");
        try {
            userDao.update(existingUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            userDao.delete(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        User deletedUser = userDao.read(1);
        if (deletedUser == null) {
            System.out.println("User was deleted successfully ");
        }

        User[] users = new User[0];
        try {
            users = userDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (users == null) {
            System.out.println("Method did not return an array");
            return;
        }
        System.out.println("Method returned an array");

    }
}


