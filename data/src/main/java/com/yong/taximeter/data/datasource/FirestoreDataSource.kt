package com.yong.taximeter.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Firestore Data Source
 * - Get document from firestore, and convert it as object
 */
class FirestoreDataSource @Inject constructor(
    // Inject Firebase Firestore Instance
    private val firestore: FirebaseFirestore,
) {
    /**
     * Get a single Firestore Document, and convert as object [T]
     */
    suspend fun <T> getDocumentObject(
        collection: String,
        document: String,
        clazz: Class<T>
    ): T? {
        return firestore.collection(collection)
            .document(document)
            .get()
            .await()
            .toObject(clazz)
    }
}