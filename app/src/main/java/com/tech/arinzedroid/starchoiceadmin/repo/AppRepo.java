package com.tech.arinzedroid.starchoiceadmin.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tech.arinzedroid.starchoiceadmin.model.AdminModel;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.model.CompletedProducts;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.model.TransactionsModel;
import com.tech.arinzedroid.starchoiceadmin.model.UserProductsModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppRepo {

    private FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();
    private String tag = this.getClass().getSimpleName();

    private static final String AGENT_TABLE_NAME = "AGENTS";
    private static final String CLIENT_TABLE_NAME = "CLIENTS";
    private static final String TRANS_HISTORY_TABLE = "TRANSACTION_HISTORY";
    private static final String PRODUCTS_TABLE_NAME = "PRODUCTS";
    private static final String USER_PRODUCTS = "USER_PRODUCTS";
    private static final String ADMIN_TABLE_NAME = "ADMINS";
    private static final String DATE_CREATED = "dateCreated";

    public LiveData<AdminModel> loginAdmin(String username, String password){
        final MutableLiveData<AdminModel> adminModelMutableLiveData = new MutableLiveData<>();
        firestoreDb.collection(ADMIN_TABLE_NAME)
                .whereEqualTo("username",username).whereEqualTo("password",password)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.isEmpty()){
                            adminModelMutableLiveData.postValue(querySnapshot.getDocuments().get(0)
                                    .toObject(AdminModel.class));
                        }else
                            adminModelMutableLiveData.postValue(null);
                    }else{
                        Log.e(tag,"Error executing task >>>", task.getException());
                        adminModelMutableLiveData.postValue(null);
                    }
                });
        return adminModelMutableLiveData;
    }

    public LiveData<List<AgentsModel>> getAllAgents(){
        final MutableLiveData<List<AgentsModel>> agentMutableLiveData = new MutableLiveData<>();
        List<AgentsModel> agentsModelsList = new ArrayList<>();
        firestoreDb.collection(AGENT_TABLE_NAME)
                .orderBy(DATE_CREATED, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.isEmpty()){
                            for (QueryDocumentSnapshot data : querySnapshot) {
                                agentsModelsList.add(data.toObject(AgentsModel.class));
                            }
                            agentMutableLiveData.postValue(agentsModelsList);
                        }else
                            agentMutableLiveData.postValue(null);

                    }else{
                        agentMutableLiveData.postValue(null);
                        Log.e(tag,"Error executing task >>>", task.getException());
                    }

                });
        return agentMutableLiveData;
    }

    public LiveData<List<ProductsModel>> getAllProducts(){
        final MutableLiveData<List<ProductsModel>> productsModelMutableLiveData = new MutableLiveData<>();
        List<ProductsModel> productsModelList = new ArrayList<>();
        firestoreDb.collection(PRODUCTS_TABLE_NAME)
                .orderBy(DATE_CREATED, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       QuerySnapshot querySnapshot = task.getResult();
                       if(!querySnapshot.isEmpty()){
                           for (QueryDocumentSnapshot data : querySnapshot) {
                               productsModelList.add(data.toObject(ProductsModel.class));
                           }
                           productsModelMutableLiveData.postValue(productsModelList);
                       }else
                           productsModelMutableLiveData.postValue(null);
                   }else{
                       productsModelMutableLiveData.postValue(null);
                       Log.e(tag,"Error executing task >>>", task.getException());
                   }
                });
        return productsModelMutableLiveData;
    }

    public LiveData<Boolean> addAgent(AgentsModel agentsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        firestoreDb.collection(AGENT_TABLE_NAME).document(agentsModel.getId()).set(agentsModel)
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<Boolean> deleteAgent(AgentsModel agentsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        firestoreDb.collection(AGENT_TABLE_NAME).document(agentsModel.getId())
                .delete()
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<Boolean> updateAgent(AgentsModel agentsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();

        Map<String,Object> data = new HashMap<>();
        data.put("username",agentsModel.getUsername());
        data.put("password",agentsModel.getPassword());
        data.put("firstname",agentsModel.getFirstname());
        data.put("lastname",agentsModel.getLastname());
        data.put("address",agentsModel.getAddress());
        data.put("age",agentsModel.getAge());
        data.put("phone",agentsModel.getPhone());
        data.put("kinFulname",agentsModel.getKinFulname());
        data.put("kinAddress",agentsModel.getKinAddress());
        data.put("kinPhone",agentsModel.getKinPhone());

        firestoreDb.collection(AGENT_TABLE_NAME).document(agentsModel.getId())
                .update(data)
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<Boolean> addProducts(ProductsModel productsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        firestoreDb.collection(PRODUCTS_TABLE_NAME).document(productsModel.getId())
                .set(productsModel)
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<Boolean> deleteProduct(ProductsModel productsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        firestoreDb.collection(PRODUCTS_TABLE_NAME).document(productsModel.getId())
                .delete()
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<Boolean> deleteUserProduct(UserProductsModel userProductsModel){
        MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        firestoreDb.collection(USER_PRODUCTS).document(userProductsModel.getId())
                .delete()
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<Boolean> updateProduct(ProductsModel productsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        Log.e(tag,productsModel.getDesc());
        Map<String,Object> data = new HashMap<>();
        data.put("productName",productsModel.getProductName());
        data.put("desc",productsModel.getDesc());
        data.put("isActive",productsModel.isActive());
        data.put("price",productsModel.getPrice());

        firestoreDb.collection(PRODUCTS_TABLE_NAME).document(productsModel.getId())
                .update(data)
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> {
                    isSuccessful.postValue(false);
                    Log.e(tag,"Error >> ",task.getCause());
                });
        return isSuccessful;

    }

    public LiveData<Boolean> updateTransactionItem(TransactionsModel transactionsModel,
                                                   UserProductsModel userProductsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        Map<String,Object> data = new HashMap<>();
        data.put("amount",transactionsModel.getAmount());
        firestoreDb.collection(TRANS_HISTORY_TABLE).document(transactionsModel.getId())
                .update(data)
                .addOnSuccessListener(task ->{
                    isSuccessful.postValue(true);
                    Map<String,Object> data_ = new HashMap<>();

                    data_.put("paidFully",userProductsModel.isPaidFully());
                    data_.put("amtPaid",userProductsModel.getAmtPaid());
                    data_.put("dateUpdated",new Date());

                    firestoreDb.collection(USER_PRODUCTS).document(userProductsModel.getId())
                            .update(data_);
                })
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<Boolean> deleteTransactionItem(TransactionsModel transactionsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        firestoreDb.collection(TRANS_HISTORY_TABLE).document(transactionsModel.getId())
                .delete()
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<List<TransactionsModel>> getAllTransactions(){
        final MutableLiveData<List<TransactionsModel>> listMutableLiveData = new MutableLiveData<>();
        final List<TransactionsModel> transactionsModelsList = new ArrayList<>();
        firestoreDb.collection(TRANS_HISTORY_TABLE)
                .orderBy(DATE_CREATED, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.isEmpty()){
                            for(QueryDocumentSnapshot data : querySnapshot){
                                transactionsModelsList.add(data.toObject(TransactionsModel.class));
                            }
                            listMutableLiveData.postValue(transactionsModelsList);
                        }else
                            listMutableLiveData.postValue(null);
                    }else{
                        listMutableLiveData.postValue(null);
                        Log.e(tag,"Error executing task >>>", task.getException());
                    }
                });
        return listMutableLiveData;
    }

    public LiveData<List<TransactionsModel>> getAgentTransactions(String agentId){
        final MutableLiveData<List<TransactionsModel>> listMutableLiveData = new MutableLiveData<>();
        final List<TransactionsModel> transactionsModelsList = new ArrayList<>();
        firestoreDb.collection(TRANS_HISTORY_TABLE)
                .whereEqualTo("agentId",agentId)
                .orderBy(DATE_CREATED, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.isEmpty()){
                            for(QueryDocumentSnapshot data : querySnapshot){
                                transactionsModelsList.add(data.toObject(TransactionsModel.class));
                            }
                            listMutableLiveData.postValue(transactionsModelsList);
                        }else
                            listMutableLiveData.postValue(null);
                    }else{
                        listMutableLiveData.postValue(null);
                        Log.e(tag,"Error executing task >>>", task.getException());
                    }
                });
        return listMutableLiveData;
    }

    public LiveData<List<ClientsModel>> getAllClients(){
        final MutableLiveData<List<ClientsModel>> clientMutableLivedata = new MutableLiveData<>();
        final List<ClientsModel> clientsModelList = new ArrayList<>();
        firestoreDb.collection(CLIENT_TABLE_NAME)
                .orderBy(DATE_CREATED, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.isEmpty()){
                            for (QueryDocumentSnapshot data: querySnapshot) {
                                clientsModelList.add(data.toObject(ClientsModel.class));
                            }
                            clientMutableLivedata.postValue(clientsModelList);
                        }else clientMutableLivedata.postValue(null);
                    }else{
                        clientMutableLivedata.postValue(null);
                        Log.e(tag,"Error >>> ", task.getException());
                    }
                });
        return clientMutableLivedata;
    }

    public LiveData<List<ClientsModel>> getAgentClients(String agentId){
        final MutableLiveData<List<ClientsModel>> clientMutableLivedata = new MutableLiveData<>();
        final List<ClientsModel> clientsModelList = new ArrayList<>();
        firestoreDb.collection(CLIENT_TABLE_NAME)
                .orderBy(DATE_CREATED, Query.Direction.DESCENDING)
                .whereEqualTo("agentId",agentId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.isEmpty()){
                            for (QueryDocumentSnapshot data: querySnapshot) {
                                clientsModelList.add(data.toObject(ClientsModel.class));
                            }
                            clientMutableLivedata.postValue(clientsModelList);
                        }else clientMutableLivedata.postValue(null);
                    }else{
                        clientMutableLivedata.postValue(null);
                        Log.e(tag,"Error >>> ", task.getException());
                    }
                });
        return clientMutableLivedata;
    }

    public LiveData<Boolean> deleteClient(ClientsModel clientsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();
        firestoreDb.collection(CLIENT_TABLE_NAME).document(clientsModel.getId())
                .delete()
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));
        return isSuccessful;
    }

    public LiveData<List<CompletedProducts>> getCompletedProducts(){
        final MutableLiveData<List<CompletedProducts>> mutableLiveData = new MutableLiveData<>();
        final  List<CompletedProducts> completedProductsList = new ArrayList<>();

        firestoreDb.collection(USER_PRODUCTS).whereEqualTo("paidFully",true).get()
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.isEmpty()){
                            for (QueryDocumentSnapshot doc : querySnapshot) {
                                completedProductsList.add(doc.toObject(CompletedProducts.class));
                            }
                            mutableLiveData.postValue(completedProductsList);
                        }else mutableLiveData.postValue(null);
                    }else{
                        mutableLiveData.postValue(null);
                        Log.e(tag,"Error >>> ", task.getException());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<List<UserProductsModel>> getUserProducts(String userId){
        final MutableLiveData<List<UserProductsModel>> userProductsModelMutableLiveData = new MutableLiveData<>();
        List<UserProductsModel> userProductsModels = new ArrayList<>();
        firestoreDb.collection(USER_PRODUCTS)
                .whereEqualTo("userId",userId)
                .orderBy(DATE_CREATED,Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            userProductsModels.add(doc.toObject(UserProductsModel.class));
                        }
                        userProductsModelMutableLiveData.postValue(userProductsModels);
                    }else{
                        userProductsModelMutableLiveData.postValue(null);
                        Log.e(this.getClass().getSimpleName(),"Error on getUserProducts", task.getException());
                    }
                });
        return userProductsModelMutableLiveData;
    }

    public LiveData<Boolean> addUserProducts(List<UserProductsModel> userProductsModelList){
        final MutableLiveData<Boolean> success = new MutableLiveData<>();
        for(UserProductsModel data : userProductsModelList){
            firestoreDb.collection(USER_PRODUCTS).document(data.getId())
                    .set(data)
                    .addOnSuccessListener(docRef -> success.postValue(true))
                    .addOnFailureListener(ex -> success.postValue(false));
        }
        return success;
    }

    public LiveData<List<TransactionsModel>> getUserTransactionsByProduct(String productId, String userId){
        final MutableLiveData<List<TransactionsModel>> mutableLiveData = new MutableLiveData<>();
        List<TransactionsModel> transactionsModels = new ArrayList<>();
        firestoreDb.collection(TRANS_HISTORY_TABLE)
                .whereEqualTo("productId",productId)
                .whereEqualTo("userId",userId)
                .orderBy(DATE_CREATED,Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            transactionsModels.add(doc.toObject(TransactionsModel.class));
                        }
                        mutableLiveData.postValue(transactionsModels);
                    }else{
                        mutableLiveData.postValue(null);
                        Log.e(this.getClass().getSimpleName(),"Error >> " + task.getException());
                    }
                });
        return mutableLiveData;
    }

    public LiveData<Boolean> updateClient(ClientsModel clientsModel){
        final MutableLiveData<Boolean> isSuccessful = new MutableLiveData<>();

        Map<String,Object> data = new HashMap<>();
        data.put("fullname",clientsModel.getFullname());
        data.put("address",clientsModel.getAddress());
        data.put("picUrl", clientsModel.getPicUrl());
        data.put("phone",clientsModel.getPhone());
        data.put("dateUpdated",clientsModel.getDateUpdated());
        data.put("kinName",clientsModel.getKinName());
        data.put("kinAddress",clientsModel.getKinAddress());
        data.put("kinPhone",clientsModel.getKinPhone());

        firestoreDb.collection(CLIENT_TABLE_NAME).document(clientsModel.getId())
                .update(data)
                .addOnSuccessListener(task -> isSuccessful.postValue(true))
                .addOnFailureListener(task -> isSuccessful.postValue(false));

        return isSuccessful;
    }

}
