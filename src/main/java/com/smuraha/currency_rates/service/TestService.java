package com.smuraha.currency_rates.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.smuraha.currency_rates.firebase.entity.RawData;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class TestService {

    public String saveTest(RawData test) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentReference> future = firestore.collection("test").add(test);
        return future.get().getId();
    }

//    public List<Test> getTestList(int page, int pageSize) throws ExecutionException, InterruptedException {
//        Firestore firestore = FirestoreClient.getFirestore();
//        ApiFuture<QuerySnapshot> snapshotApiFuture = firestore.collection("test").offset(page * pageSize).limit(pageSize).get();
//        QuerySnapshot snapshot = snapshotApiFuture.get();
  //      List<Test> tests = snapshot.toObjects(Test.class);
//        return tests;
//    }
}
