package com.ghanshyam.snapsaga.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {
    val storageReference =
        FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())

    storageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
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

fun uploadVideo(
    uri: Uri,
    folderName: String,
    progressDialog: ProgressDialog,
    callback: (String?) -> Unit
) {
    val storageReference =
        FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())

    progressDialog.setTitle("Uploading...")
    progressDialog.show()

    storageReference.putFile(uri).addOnSuccessListener { taskSnapshot ->
        taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
            progressDialog.dismiss()
            callback(downloadUri.toString())
        }.addOnFailureListener { exception ->
            Log.e("Utils", "Failed to get download URL: ${exception.message}")
            callback(null)
        }
    }.addOnFailureListener { exception ->
        Log.e("Utils", "Video upload failed: ${exception.message}")
        callback(null)
    }.addOnProgressListener {
        val uploadedValue: Long = (it.bytesTransferred / it.totalByteCount)*100
        progressDialog.setMessage("Uploaded $uploadedValue%")
    }
}