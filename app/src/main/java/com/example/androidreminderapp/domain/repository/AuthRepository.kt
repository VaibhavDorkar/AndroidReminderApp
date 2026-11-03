package com.example.androidreminderapp.domain.repository

import android.app.Activity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signInWithCredential(credential: AuthCredential): Result<Boolean>
    suspend fun signInWithCustomToken(token: String): Result<Boolean>
    suspend fun signInWithGoogle(activity: Activity): Result<Boolean>
    suspend fun requestPhoneOtp(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: (verificationId: String, token: PhoneAuthProvider.ForceResendingToken?) -> Unit,
        onAutoVerificationSuccess: () -> Unit,
        onVerificationFailed: (Exception) -> Unit,
        forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    )
    suspend fun verifyPhoneOtp(verificationId: String, smsCode: String): Result<Boolean>
    fun signOut()
}
