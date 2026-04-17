package com.guardianwear.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.guardianwear.app.data.model.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuardianRepository @Inject constructor(
    private val auth      : FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val rtdb      : FirebaseDatabase
) {
    private val uid get() = auth.currentUser?.uid ?: ""

    fun observeVitals(): Flow<VitalData> = callbackFlow {
        val ref = rtdb.getReference("devices/$uid/vitals")
        val listener = object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                snap.getValue<VitalData>()?.let { trySend(it) }
            }
            override fun onCancelled(e: DatabaseError) {}
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun observeGait(): Flow<GaitData> = callbackFlow {
        val ref = rtdb.getReference("devices/$uid/gait")
        val listener = object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                snap.getValue<GaitData>()?.let { trySend(it) }
            }
            override fun onCancelled(e: DatabaseError) {}
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun observeAlerts(): Flow<List<AlertEvent>> = callbackFlow {
        val ref = firestore.collection("users/$uid/alerts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(30)
        val sub = ref.addSnapshotListener { snap, _ ->
            val list = snap?.documents?.mapNotNull {
                it.toObject(AlertEvent::class.java)?.copy(id = it.id)
            } ?: emptyList()
            trySend(list)
        }
        awaitClose { sub.remove() }
    }

    suspend fun resolveAlert(alertId: String) {
        firestore.document("users/$uid/alerts/$alertId")
            .update("resolved", true).await()
    }

    suspend fun getProfile(): UserProfile? =
        firestore.document("users/$uid/profile/setup")
            .get().await().toObject(UserProfile::class.java)

    suspend fun saveProfile(profile: UserProfile) {
        firestore.document("users/$uid/profile/setup").set(profile).await()
    }

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun logout() = auth.signOut()
    fun isLoggedIn() = auth.currentUser != null
    fun currentUserEmail() = auth.currentUser?.email ?: ""
}
