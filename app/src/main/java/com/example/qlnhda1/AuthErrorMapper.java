package com.example.qlnhda1;

import androidx.annotation.StringRes;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestoreException;

final class AuthErrorMapper {

    private AuthErrorMapper() {
    }

    @StringRes
    static int resolve(Exception error) {
        if (error instanceof AuthRepository.ProfileSaveException) {
            return R.string.auth_error_profile_save;
        }
        if (error instanceof FirebaseNetworkException) {
            return R.string.auth_error_network;
        }
        if (error instanceof FirebaseAuthException) {
            String code = ((FirebaseAuthException) error).getErrorCode();
            switch (code) {
                case "ERROR_INVALID_EMAIL":
                    return R.string.auth_error_invalid_email;
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    return R.string.auth_error_user_exists;
                case "ERROR_WEAK_PASSWORD":
                    return R.string.auth_error_weak_password;
                case "ERROR_TOO_MANY_REQUESTS":
                    return R.string.auth_error_too_many_requests;
                case "ERROR_OPERATION_NOT_ALLOWED":
                    return R.string.auth_error_operation_not_allowed;
                case "ERROR_USER_DISABLED":
                    return R.string.auth_error_user_disabled;
                case "ERROR_NETWORK_REQUEST_FAILED":
                    return R.string.auth_error_network;
                case "ERROR_USER_NOT_FOUND":
                case "ERROR_WRONG_PASSWORD":
                case "ERROR_INVALID_CREDENTIAL":
                case "ERROR_INVALID_LOGIN_CREDENTIALS":
                    return R.string.auth_error_wrong_credentials;
                default:
                    return R.string.auth_error_generic;
            }
        }
        if (error instanceof FirebaseFirestoreException
                && ((FirebaseFirestoreException) error).getCode()
                == FirebaseFirestoreException.Code.UNAVAILABLE) {
            return R.string.auth_error_network;
        }
        return R.string.auth_error_generic;
    }
}
