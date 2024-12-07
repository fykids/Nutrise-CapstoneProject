package com.capstone.nutrise.firebase.repository

import com.capstone.nutrise.firebase.model.UserModelFirebase
import com.capstone.nutrise.firebase.model.UserUpdateModelFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun login(email : String, password : String) : Result<UserModelFirebase> {
        return try {
            val response = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = response.user
            if (firebaseUser != null) {
                val user = UserModelFirebase(
                    email = firebaseUser.email ?: "",
                    password = ""
                )
                Result.Success(user)
            } else {
                Result.Error("Login failed: User not found")
            }
        } catch (e : Exception) {
            Result.Error(e.message ?: "Login error")
        }
    }

    suspend fun register(user : UserModelFirebase) : Result<UserModelFirebase> {
        return try {
            val response = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val firebaseUser = response.user
            if (firebaseUser != null) {
                val registeredUser = UserModelFirebase(
                    email = firebaseUser.email ?: "",
                    password = user.password
                )
                Result.Success(registeredUser)
            } else {
                Result.Error("Registration failed: User not created")
            }
        } catch (e : Exception) {
            Result.Error(e.message ?: "Registration error")
        }
    }

    suspend fun updateUser(user : UserUpdateModelFirebase) : Result<UserUpdateModelFirebase> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(user.name)
                    .build()

                currentUser.updateProfile(profileUpdates).await()

                if (!user.password.isNullOrEmpty()) {
                    currentUser.updatePassword(user.password!!).await()
                }

                Result.Success(user)
            } else {
                Result.Error("User is not authenticated")
            }
        } catch (e : Exception) {
            Result.Error(e.message ?: "Update user error")
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}