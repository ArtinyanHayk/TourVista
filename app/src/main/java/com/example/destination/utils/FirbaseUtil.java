package com.example.destination.utils;



import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.FirebaseStorageKtxRegistrar;
import com.google.firebase.storage.StorageReference;

public class FirbaseUtil {
    public static String currentUsersId() {
        return FirebaseAuth.getInstance().getUid();
    }
    //
    public static String currentPostsId() {
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference currentPostsDetails() {
        return FirebaseFirestore.getInstance().collection("Posts").document(currentPostsId());
    }
    public static CollectionReference allPostsCollectionReference() {
        return FirebaseFirestore.getInstance().collection("Posts");
    }
    public  static StorageReference getCurrentPostPicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("Post_pic").
                child(FirbaseUtil.currentPostsId());
    }
    //

    public static DocumentReference currentUsersDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUsersId());
    }

    public static boolean isLoggedIn() {
        if (currentUsersId() != null) {
            return true;

        }
        return false;
    }

    public static CollectionReference allUsersCollectionReference() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatroomReherence(String ChatroomId) {

        return FirebaseFirestore.getInstance().collection("chatrooms").document(ChatroomId);
    }
    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }
    public  static StorageReference getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic").
                child(FirbaseUtil.currentUsersId());
    }
    public static DocumentReference getChatReference(String chatId){
        return FirebaseFirestore.getInstance().collection("chats").document(chatId);
    }
    public static  String chatId(String userId1,String userId2){
        if(userId1.hashCode() < userId2.hashCode()){
            return userId1 + "_" + userId2;
        }else
            return userId2 + "_" + userId1;
    }
    public static CollectionReference getChatMessageReference(String chatId){
        return getChatReference(chatId).collection("messages");
    }



}
