package ge.tsu.spring2021.twitterclone.tclone.auth;

public class LoginView {
    private String userName;
    private String password;

    public LoginView (){

    }
    public LoginView(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
