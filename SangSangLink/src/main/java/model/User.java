package model;

public class User {
    private String email;
    private String userName;
    private String password;
    private String port;

    public User(String email, String userName, String password, String port) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.port = port;
    }

    public static class UserBuilder{
        private String email;
        private String userName;
        private String password;
        private String port;

        public UserBuilder(){

        }
        public UserBuilder email(String email){ this.email = email; return this; }
        public UserBuilder userName(String userName){ this.userName = userName; return this; }
        public UserBuilder password(String password){ this.password = password; return this; }
        public UserBuilder port(String port){ this.port = port; return this; }

        public User build(){
            return new User(this.email, this.userName, this.password, this.port);
        }
    }


    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
