package com.FeedMyPet.data;

import com.FeedMyPet.data.model.LoggedInUser;
import com.FeedMyPet.helper.Constants;
import com.FeedMyPet.helper.WebRequestManager;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            WebRequestManager webRM = new WebRequestManager();
            String result = webRM.UserLogin(username,password);
            // TODO: handle loggedInUser authentication
            /*
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);

            */
            if (result.contains(Constants.USER_SUCCESS_LOGIN)){
                LoggedInUser user = new LoggedInUser("1",username);
                return new Result.Success<>(user);
            } else {
                throw new Exception();
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
