package com.ghanshyam.snapsaga.Utils

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {
    val storageReference = FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())

    storageReference.putFile(uri)
        .addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                callback(downloadUri.toString())
            }.addOnFailureListener { exception ->
                Log.e("Utils", "Failed to get download URL: ${exception.message}")
                callback(null)
            }
        }.addOnFailureListener { exception ->
            Log.e("Utils", "Image upload failed: ${exception.message}")
            callback(null)
        }
}
