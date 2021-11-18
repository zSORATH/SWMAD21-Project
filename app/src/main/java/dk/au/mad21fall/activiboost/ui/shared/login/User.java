package dk.au.mad21fall.activiboost.ui.shared.login;

public class User {

    public static final int PATIENT = 101, CAREGIVER = 102;
    private int userType;

    public void setUserType(int _userType) { userType = _userType; }
    public int getUserType() {return userType; }
}
