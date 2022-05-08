package com.example.moneycare.ui.viewmodel;

import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.moneycare.data.repository.LoginRepository;
import com.example.moneycare.data.custom.Result;
import com.example.moneycare.data.model.User;
import com.example.moneycare.R;

public class LoginViewModel extends ViewModel {

    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<User> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            User data = ((Result.Success<User>) result).getData();

        } else {

        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}