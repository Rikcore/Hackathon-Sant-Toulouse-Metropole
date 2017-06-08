package hackatonsant.wcs.fr.hackatonsant;


import java.io.Serializable;

public class UserModel implements Serializable{
    public String name;
    public boolean canUse;

    public UserModel(){}

    public UserModel(String name, boolean canUse){

        this.name = name;
        this.canUse = canUse;
    }

}
